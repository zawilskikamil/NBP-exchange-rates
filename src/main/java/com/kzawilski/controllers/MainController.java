package com.kzawilski.controllers;

import com.kzawilski.common.DownloadFileServices;
import com.kzawilski.common.Prediction;
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
        chart.setAnimated(false);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<ExchangeRate> rates = this.rates.stream().filter(rate ->
                rate.getCurrencyCode().equals(selectedCurrency)
                        && (toDate == null || rate.getDate().before(toDate))
                        && (fromDate == null || rate.getDate().after(fromDate))
        ).collect(Collectors.toList());
        Prediction prediction = new Prediction(rates);

        Date[] dates = prediction.getDateSeries();
        Double[] series = prediction.getRateSeries();
        Double[] trend = prediction.getTrend();

        Series series0 = new Series();
        series0.setName(selectedCurrency);
        Series series1 = new Series();
        series1.setName("Trend");

        List<Data> data0 = new ArrayList<>();
        List<Data> data1 = new ArrayList<>();

        for (int i = 0; i < dates.length; i++) {
            data0.add(new Data(dateFormat.format(dates[i]),series[i]));
            if (trend[i] != null) {
                data1.add(new Data(dateFormat.format(dates[i]), trend[i]));
            }
        }

        series0.getData().addAll(data0);
        series1.getData().addAll(data1);
        chart.getData().addAll(series0,series1);
        chart.setPrefWidth(dates.length*40.0);
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
