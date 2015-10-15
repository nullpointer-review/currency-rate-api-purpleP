package com.example.test.centrobank_service;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by mihaildoronin on 14.10.15.
 */
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CentralBankCurrencyResponse {
    @XmlAttribute(name = "Date", required = true)
    private String date;

    public List<CurrencyData> getData() {
        return data;
    }

    public void setData(List<CurrencyData> data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement(name = "Valute")
    List<CurrencyData> data;

    public CentralBankCurrencyResponse() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CentralBankCurrencyResponse response = (CentralBankCurrencyResponse) o;

        if (date != null ? !date.equals(response.date) : response.date != null) return false;
        return !(data != null ? !data.equals(response.data) : response.data != null);

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public CentralBankCurrencyResponse(String date, List<CurrencyData> data) {
        this.date = date;
        this.data = data;
    }

    @Override
    public String toString() {
        return "CentralBankCurrencyResponse{" +
                "date=" + date +
                ", data=" + data +
                '}';
    }
}
