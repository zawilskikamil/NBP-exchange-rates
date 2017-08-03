package com.kzawilski.database;

import com.kzawilski.domain.ExchangeRate;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class DataManager {

    private static EntityManager em;

    public DataManager(){
        em = Persistence.createEntityManagerFactory("NBP").createEntityManager();
    }

    public void saveOrUpdateExchangeRate(ExchangeRate exchangeRate) {
        em.getTransaction().begin();
        em.merge(exchangeRate);
        em.getTransaction().commit();
    }

    public List<ExchangeRate> getExchangeRateByDateAndCode(Date fromDate, Date toDate, String code) {
        return em.createNativeQuery("getByDateAndCode", ExchangeRate.class)
                .setParameter("fromDate",fromDate)
                .setParameter("toDate",toDate)
                .setParameter("code",code)
                .getResultList();
    }

    public static void dispose(){
        em.close();
    }

}
