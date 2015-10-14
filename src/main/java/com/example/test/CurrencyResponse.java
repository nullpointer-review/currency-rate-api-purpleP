package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by mihaildoronin on 14.10.15.
 */
public class CurrencyResponse {

    private String code;
    JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd")
    private Date date;

    public CurrencyResponse() {
    }

    public CurrencyResponse(String code, Date date, double rate) {
        this.code = code;
        this.date = date;
        this.rate = rate;
    }

    public double getRate() {

        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private double rate;
}
