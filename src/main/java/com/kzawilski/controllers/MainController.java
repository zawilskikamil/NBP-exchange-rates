package com.kzawilski.controllers;

import com.kzawilski.common.DownloadFileServices;
import com.kzawilski.database.DataManager;
import com.kzawilski.database.domain.ExchangeRate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceDialog;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public LineChart chart;

    DownloadFileServices downloadFileServices;

    List<ExchangeRate> rates;

    Set<String> codes;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        downloadFileServices = new DownloadFileServices();
        codes = new TreeSet<>();
        refresh();
        DrawChart();
    }

    public void refresh(){
        rates = DataManager.getInstance().getAllExchangeRate();
        codes.addAll(DataManager.getInstance().getAllCodes());
    }

    public void DrawChart(){
        String[] selectedCurrency = new String[] {"USD","THB"};
        for (String currency : selectedCurrency) {
            XYChart.Series series = new XYChart.Series();
            series.setName(currency);
            for (ExchangeRate rate : rates) {
                if (rate.getCurrencyCode().equals(currency)) {
                    series.getData().add(new XYChart.Data(rate.getDate().toString(), rate.getRate()));
                }
            }
            chart.getData().add(series);
        }
    }



    public void openDownloadDialog(ActionEvent actionEvent) {
        List<String> choices = downloadFileServices.getFileList();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Choice file");
        dialog.setHeaderText("Choice file to download");
        dialog.setContentText("Choose file:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            System.out.println("Your choice: " + choice);
            downloadFileServices.downloadFile(choice);
        });
    }
}
