package com.example.test;

import com.example.test.centrobank_service.CentralBankCurrencyResponse;
import com.example.test.centrobank_service.CurrencyData;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import sun.util.calendar.Gregorian;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mihaildoronin on 14.10.15.
 */
@RestController()
public class CurrencyController {

    private String baseUrl = "http://cbr.ru/scripts/XML_daily.asp";
    private SimpleDateFormat[] dateFormats = {new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd.MM/yyyy")};

    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;


    /*
    *
    * This method could be refactored into CentralBankServiceGateWay class so that int could be mocked for testing.
    * Also I have suspicions that spring somehow serializes date slightly wrong because of timezones maybe.
    * */
    private Optional<CurrencyResponse> queryCentralBankForCurrencyRate(Date forDate, String code) {
        try {
            String response = Unirest.get(baseUrl).queryString("date_req", dateFormats[0].format(forDate)).asString().getBody();
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(CentralBankCurrencyResponse.class);
            }
            if (jaxbUnmarshaller == null) {
               jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            }
            CentralBankCurrencyResponse currencyResponse = (CentralBankCurrencyResponse) jaxbUnmarshaller.unmarshal(new StringReader(response));
            Optional<CurrencyData> data = currencyResponse.getData().stream().filter(currencyData -> currencyData.getCode().equals(code)).findFirst();
            Date validForDate = parseDate(currencyResponse.getDate(), dateFormats).orElseThrow(() -> new RuntimeException("Unrecognised date format in central bank response "));
            if (data.isPresent()) {
                return Optional.of(currencyResponseFromCurrencyData(data.get(), validForDate));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private CurrencyResponse currencyResponseFromCurrencyData(CurrencyData data, Date date) {
        Date d = new Date(date.getTime() + 1000 * 60 * 60 * 3);
        return new CurrencyResponse(data.getCode(), d, data.getValue() / data.getNominal());
    }

    private Optional<Date> parseDate(String date, SimpleDateFormat[] formats) {
        for (SimpleDateFormat format: formats) {
            try {
                return Optional.of(format.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /*
    * Spent a lot of time trying to make Optional<Date> param work. Seems to be impossible in spring.
    * Could have done custom path variable extractor, but it's not worth it.
    * */

    @RequestMapping(value = "/rate/{code}/**", method = RequestMethod.GET)
    public CurrencyResponse getCurrencyRate(HttpServletRequest servletRequest) {

        Map pathVariables =
                (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String code = (String) pathVariables.get("code");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String [] uriParts = servletRequest.getRequestURI().split("/");
        Calendar c = new GregorianCalendar(TimeZone.getDefault());
        c.add(Calendar.DATE, 1);
        Date forDate = c.getTime();
        if (uriParts.length > 3) {
            try {
                forDate = format.parse(uriParts[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Optional<CurrencyResponse> response = queryCentralBankForCurrencyRate(forDate, code);
        if (response.isPresent()) {
            return response.get();
        }
        else {
            throw new ResourceNotFoundException();
        }

    }
}

/*
* More specific exceptions should be created (for example for unreadable date format in central bank response
* Haven't got time to do that.
* */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}