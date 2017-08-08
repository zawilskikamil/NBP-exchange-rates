package com.kzawilski.common;

import com.kzawilski.database.domain.ExchangeRate;

import java.util.Date;
import java.util.List;

public class Prediction {
    Date[] dateSeries;
    Double[] rateSeries;
    Double[] trend;
    Double mean;
    int size;

    public Prediction(List<ExchangeRate> rates) {
        size = rates.size();
        trend = new Double[size];
        dateSeries = new Date[size];
        rateSeries = new Double[size];
        for (int i = 0; i < size; i++) {
            dateSeries[i] = rates.get(i).getDate();
            rateSeries[i] = rates.get(i).getRate();
        }
        prepare();
    }

    public Prediction(Date[] dateSeries, Double[] rateSeries) throws Exception {
        if (dateSeries.length != rateSeries.length){
            throw new Exception("invalid lengths");
        }
        this.dateSeries = dateSeries;
        this.rateSeries = rateSeries;
        size = dateSeries.length;
        prepare();
    }

    private void prepare(){
        Double sum = 0.0;
        for (Double d : rateSeries){
            sum += d;
        }
        mean = sum/size;

        for (int i = 2; i < size - 2; i++) {
            Double subSum = 0.0;
            for (int j = i-2; j < i + 3; j++){
                subSum += rateSeries[j];
            }
            trend[i] = subSum/5;
        }
        // TODO prediction
    }

    public Date[] getDateSeries() {
        return dateSeries;
    }

    public Double[] getRateSeries() {
        return rateSeries;
    }

    public Double[] getTrend() {
        return trend;
    }
}
