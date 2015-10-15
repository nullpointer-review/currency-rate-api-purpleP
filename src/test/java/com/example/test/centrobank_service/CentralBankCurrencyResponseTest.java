package com.example.test.centrobank_service;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mihaildoronin on 14.10.15.
 */
public class CentralBankCurrencyResponseTest {

    @Test
    public void testResponseDeserialization() throws JAXBException {

        File inputFile = new File(Thread.currentThread().getContextClassLoader().getResource("test_response.xml").getFile());
        JAXBContext jaxbContext = JAXBContext.newInstance(CentralBankCurrencyResponse.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        CentralBankCurrencyResponse response = (CentralBankCurrencyResponse) jaxbUnmarshaller.unmarshal(inputFile);
        List<CurrencyData> data = new ArrayList<>(2);
        data.add(new CurrencyData("AUD", 1, 16.0102));
        data.add(new CurrencyData("GBP", 1, 43.8254));
        CentralBankCurrencyResponse correctResponse = new CentralBankCurrencyResponse("02/03/2002", data);
        assertEquals(response, correctResponse);
    }

    @Test
    public void testCurrencyDataDeserialization() throws JAXBException {

        String input = "<Valute ID=\"R01035\">\n" +
                "        <NumCode>826</NumCode>\n" +
                "        <CharCode>GBP</CharCode>\n" +
                "        <Nominal>1</Nominal>\n" +
                "        <Name>Фунт стерлингов Соединенного королевства</Name>\n" +
                "        <Value>43,8254</Value>\n" +
                "    </Valute>";

        JAXBContext jaxbContext = JAXBContext.newInstance(CurrencyData.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        CurrencyData data = (CurrencyData) jaxbUnmarshaller.unmarshal(new StringReader(input));
        assertEquals(data, new CurrencyData("GBP", 1, 43.8254));

    }
}