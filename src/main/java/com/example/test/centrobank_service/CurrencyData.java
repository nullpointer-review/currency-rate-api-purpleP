package com.example.test.centrobank_service;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by mihaildoronin on 14.10.15.
 */

@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyData {
    @XmlElement(name = "CharCode")
    private String code;
    @XmlElement(name = "Nominal")
    private int nominal;

    @Override
    public String toString() {
        return "CurrencyData{" +
                "code='" + code + '\'' +
                ", nominal=" + nominal +
                ", value=" + value +
                '}';
    }


    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(value = DoubleAdapter.class)
    private Double value;

    public CurrencyData() {
    }

    public CurrencyData(String code, int nominal, double value) {

        this.code = code;
        this.nominal = nominal;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyData that = (CurrencyData) o;

        if (nominal != that.nominal) return false;
        if (Double.compare(that.value, value) != 0) return false;
        return !(code != null ? !code.equals(that.code) : that.code != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = code != null ? code.hashCode() : 0;
        result = 31 * result + nominal;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
