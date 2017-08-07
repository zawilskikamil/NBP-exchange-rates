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
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.scene.chart.XYChart.*;

public class MainController implements Initializable {

    @FXML
    public LineChart chart;

    @FXML
    public ListView codesSelect;

    @FXML
    public ScrollPane chartPane;

    @FXML
    public DatePicker fromDatePicker;

    @FXML
    public DatePicker toDatePicker;

    private Date fromDate;

    private Date toDate;

    private DownloadFileServices downloadFileServices;

    private List<ExchangeRate> rates;

    private Set<String> codes;

    private String selectedCurrency;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        downloadFileServices = new DownloadFileServices();
        codes = new TreeSet<>();
        chartPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        chartPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chart.setMaxSize(Double.MAX_VALUE, 500);
        fromDatePicker.setOnAction(t -> {
            LocalDate date = fromDatePicker.getValue();
            System.out.println("Selected date: " + date);
            fromDate = java.sql.Date.valueOf(date);
            DrawChart();
        });
        toDatePicker.setOnAction(t -> {
            LocalDate date = toDatePicker.getValue();
            System.out.println("Selected date: " + date);
            toDate = java.sql.Date.valueOf(date);
            DrawChart();
        });
        refresh();
    }

    public void refresh() {
        DataManager dm = new DataManager();
        rates = dm.getAllExchangeRate();
        codes.addAll(dm.getAllCodes());
        if (codes.size()>0){
            selectedCurrency = codes.iterator().next();
        }
        dm.stopEntityManager();
        DrawChart();
        prepareSelectCodesList();
    }

    private void prepareSelectCodesList() {
        ObservableList<String> items = FXCollections.observableArrayList(codes);
        codesSelect.getItems().clear();
        codesSelect.getItems().addAll(items);
        codesSelect.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        codesSelect.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            selectedCurrency = (String) codesSelect.getSelectionModel().getSelectedItem();
            DrawChart();
        });
    }

    public void DrawChart() {
        chart.getData().clear();
        Series series = new Series();
        series.setName(selectedCurrency);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<ExchangeRate> rates = this.rates.stream().filter(rate ->
                rate.getCurrencyCode().equals(selectedCurrency)
                        && (toDate == null || rate.getDate().before(toDate))
                        && (fromDate == null || rate.getDate().after(fromDate))
        ).collect(Collectors.toList());
        List<Data> data = new ArrayList<>();
        for (ExchangeRate rate : rates) {
            data.add(new Data(
                            dateFormat.format(rate.getDate()),
                            rate.getRate()
                    )
            );
        }
        series.getData().addAll(data);
        chart.getData().add(series);
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
