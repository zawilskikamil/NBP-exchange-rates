package com.kzawilski.database.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "EXCHANGE_RATES")
@NamedQueries({
        @NamedQuery(name = "getExchangeRateByDateAndCode", query = "select e from ExchangeRate e where (e.date between :fromDate and :toDate) and e.currencyCode like :code order by e.date"),
        @NamedQuery(name = "getAllExchange", query = "select e from ExchangeRate e order by e.date"),
        @NamedQuery(name = "getAllCurrencyCodes", query = "select distinct e.currencyCode from ExchangeRate e")
})
@Access(AccessType.FIELD)
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

    @Column(name = "CONVERTER")
    private int converter;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "RATE")
    private Double rate;

    @Column(name = "DATE")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getConverter() {
        return converter;
    }

    public void setConverter(int converter) {
        this.converter = converter;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", currencyName='" + currencyName + '\'' +
                ", converter=" + converter +
                ", currencyCode='" + currencyCode + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
