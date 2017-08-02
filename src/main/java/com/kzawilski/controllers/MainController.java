package com.kzawilski.controllers;

import com.kzawilski.download.DownloadFlieServices;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    DownloadFlieServices downloadFlieServices;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        downloadFlieServices = new DownloadFlieServices();
    }

    public void openDownloadDialog(ActionEvent actionEvent) {
        List<String> choices = downloadFlieServices.getFileList();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Choice file");
        dialog.setHeaderText("Choice file to download");
        dialog.setContentText("Choose file:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            System.out.println("Your choice: " + choice);
            downloadFlieServices.downloadFile(choice);
        });
    }
}
