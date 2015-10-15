package com.example.test.centrobank_service;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by mihaildoronin on 14.10.15.
 */
public class DoubleAdapter extends XmlAdapter<String, Double> {
    @Override
    public Double unmarshal(String v) throws Exception {
        return Double.parseDouble(v.replace(',', '.'));
    }

    @Override
    public String marshal(Double v) throws Exception {
        return null;
    }
}
