package com.example.test;

import com.example.test.centrobank_service.CentralBankCurrencyResponse;
import com.example.test.centrobank_service.CurrencyData;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                Date d = new Date(dateFormat.parse(currencyResponse.getDate()).getTime() + 1000 * 60 * 60 * 12);
                return Optional.of(currencyResponseFromCurrencyData(data.get(), d));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private CurrencyResponse currencyResponseFromCurrencyData(CurrencyData data, Date date) {
        return new CurrencyResponse(data.getCode(), date, data.getValue() / data.getNominal());
    }


    @RequestMapping(value = "/rate/{code}/{date}", method = RequestMethod.GET)
    public CurrencyResponse getCurrencyRate(@PathVariable("code") String code,
                                            @PathVariable("date")
                                            @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)Optional<Date> date) {
        Optional<CurrencyResponse> response = queryCentralBankForCurrencyRate(date.orElse(new Date(new Date().getTime() + (1000 * 60 * 60 * 24))), code);
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