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

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mihaildoronin on 14.10.15.
 */
@RestController()
public class CurrencyController {

    private String baseUrl = "http://cbr.ru/scripts/XML_daily.asp";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;

    private Optional<CurrencyResponse> queryCentralBankForCurrencyRate(Date date, String code) {
        try {
            String response = Unirest.get(baseUrl).queryString("date_req", dateFormat.format(date)).asString().getBody();
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(CentralBankCurrencyResponse.class);
            }
            if (jaxbUnmarshaller == null) {
               jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            }
            CentralBankCurrencyResponse currencyResponse = (CentralBankCurrencyResponse) jaxbUnmarshaller.unmarshal(new StringReader(response));
            Optional<CurrencyData> data = currencyResponse.getData().stream().filter(currencyData -> currencyData.getCode().equals(code)).findFirst();
            if (data.isPresent()) {
                return Optional.of(currencyResponseFromCurrencyData(data.get(), date));
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




    @RequestMapping(value = "/rate/{code}/**", method = RequestMethod.GET)
    public CurrencyResponse getCurrencyRate(HttpServletRequest servletRequest) {

        Map pathVariables =
                (Map) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String code = (String) pathVariables.get("code");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String [] uriParts = servletRequest.getRequestURI().split("/");
        String dateAsString = null;
        if (uriParts.length > 3) {
            dateAsString = uriParts[3];
        }

        Date date = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        if (dateAsString != null) {
            try {
                date = format.parse(dateAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Optional<CurrencyResponse> response = queryCentralBankForCurrencyRate(date, code);
        if (response.isPresent()) {
            return response.get();
        }
        else {
            throw new ResourceNotFoundException();
        }

    }
}
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}