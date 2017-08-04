package com.kzawilski.database;

import com.kzawilski.database.domain.ExchangeRate;
import com.kzawilski.database.domain.FileName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class DataManager {

    private EntityManager em;
    private EntityManagerFactory emf;

    private static DataManager instance;

    private DataManager() {
        emf = Persistence.createEntityManagerFactory("NBP");
        em = emf.createEntityManager();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void saveExchangeRate(ExchangeRate exchangeRate) {
        em.getTransaction().begin();
        ExchangeRate e = em.merge(exchangeRate);
        System.out.println(e);
        em.getTransaction().commit();
    }

    public void saveFileName(String name) {
        FileName fileName = new FileName();
        fileName.setName(name);
        saveFileName(fileName);
    }

    public void saveFileName(FileName fileName) {
        em.getTransaction().begin();
        em.merge(fileName);
        em.getTransaction().commit();
    }

    public List<ExchangeRate> getExchangeRateByDateAndCode(Date fromDate, Date toDate, String code) {
        return em.createNamedQuery("getExchangeRateByDateAndCode", ExchangeRate.class)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("code", code)
                .getResultList();
    }

    public List<ExchangeRate> getAllExchangeRate() {
        return em.createNamedQuery("getAllExchange", ExchangeRate.class)
                .getResultList();
    }

    public List<FileName> getAllFileNames() {
        return em.createNamedQuery("getAllNames", FileName.class)
                .getResultList();
    }

    public void stop() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public List<String> getAllCodes() {
        return em.createNamedQuery("getAllCurrencyCodes", String.class)
                .getResultList();    }
}
