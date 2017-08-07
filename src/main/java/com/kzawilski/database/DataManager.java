package com.kzawilski.database;

import com.kzawilski.database.domain.ExchangeRate;
import com.kzawilski.database.domain.FileName;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class DataManager {

    private EntityManager em;

    private static EntityManagerFactory emf;

    public DataManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("NBP");
        }
        em = emf.createEntityManager();
    }

    public void saveExchangeRate(ExchangeRate exchangeRate) {
        em.getTransaction().begin();
        em.persist(exchangeRate);
        em.getTransaction().commit();
    }

    public void saveFileName(String name) {
        FileName fileName = new FileName();
        fileName.setName(name);
        saveFileName(fileName);
    }

    public void saveFileName(FileName fileName) {
        em.getTransaction().begin();
        em.persist(fileName);
        em.getTransaction().commit();
    }

    public List<ExchangeRate> getExchangeRateByDateAndCode(Date fromDate, Date toDate, String code) {
        em.getTransaction().begin();
        List<ExchangeRate> r = em.createNamedQuery("getExchangeRateByDateAndCode", ExchangeRate.class)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("code", code)
                .getResultList();
        em.getTransaction().commit();
        return r;
    }

    public List<ExchangeRate> getAllExchangeRate() {
        em.getTransaction().begin();
        List<ExchangeRate> r = em.createNamedQuery("getAllExchange", ExchangeRate.class)
                .getResultList();
        em.getTransaction().commit();
        return r;
    }

    public List<FileName> getAllFileNames() {
        em.getTransaction().begin();
        List<FileName> r = em.createNamedQuery("getAllNames", FileName.class)
                .getResultList();
        em.getTransaction().commit();
        return r;
    }

    public List<String> getAllCodes() {
        em.getTransaction().begin();
        List<String> r = em.createNamedQuery("getAllCurrencyCodes", String.class)
                .getResultList();
        em.getTransaction().commit();
        return r;
    }

    public static void stopEntityManagerFactory(){
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public void stopEntityManager() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
