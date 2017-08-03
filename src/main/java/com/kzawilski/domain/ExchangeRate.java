package com.kzawilski.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "EXCHANGE_RATE")
@NamedNativeQueries({
        @NamedNativeQuery(name = "getByDateAndCode", query = "select e from EXCHANGE_RATE where (e.date between :fromDate and :toDate) and e.code like :code")
})
@Access(AccessType.FIELD)
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

    @Column(name = "CONVERTER")
    private int converter;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "date")
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
