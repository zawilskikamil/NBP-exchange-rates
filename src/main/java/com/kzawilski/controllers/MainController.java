package com.kzawilski.controllers;

import com.kzawilski.common.DownloadFileServices;
import com.kzawilski.database.DataManager;
import com.kzawilski.database.domain.ExchangeRate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public LineChart chart;

    @FXML
    public ListView codesSelect;

    public DatePicker fromDatePicker;

    public DatePicker toDatePicker;

    private Date fromDate;

    private Date toDate;

    private DownloadFileServices downloadFileServices;

    private List<ExchangeRate> rates;

    private Set<String> codes;

    private List<String> selectedCurrency;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        downloadFileServices = new DownloadFileServices();
        codes = new TreeSet<>();
        selectedCurrency = new ArrayList<>();

        fromDatePicker.setOnAction(t -> {
            LocalDate date = fromDatePicker.getValue();
            System.err.println("Selected date: " + date);
            fromDate = java.sql.Date.valueOf(date);
            DrawChart();
        });
        toDatePicker.setOnAction(t -> {
            LocalDate date = toDatePicker.getValue();
            System.err.println("Selected date: " + date);
            toDate = java.sql.Date.valueOf(date);
            DrawChart();
        });
        refresh();
    }

    public void refresh() {
        DataManager dm = new DataManager();
        rates = dm.getAllExchangeRate();
        codes.addAll(dm.getAllCodes());
        dm.stopEntityManager();
        DrawChart();
        prepareSelectCodesList();
    }

    private void prepareSelectCodesList() {
        ObservableList<String> items = FXCollections.observableArrayList(codes);
        codesSelect.getItems().clear();
        codesSelect.getItems().addAll(items);
        codesSelect.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        codesSelect.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            selectedCurrency.clear();
            selectedCurrency.addAll(codesSelect.getSelectionModel().getSelectedItems());
            DrawChart();
        });
    }

    public void DrawChart() {
        chart.getData().clear();
        for (String currency : selectedCurrency) {
            XYChart.Series series = new XYChart.Series();
            series.setName(currency);
            for (ExchangeRate rate : rates) {
                if (rate.getCurrencyCode().equals(currency)
                        && (toDate == null || rate.getDate().before(toDate))
                        && (fromDate == null || rate.getDate().after(fromDate))
                        ) {
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
