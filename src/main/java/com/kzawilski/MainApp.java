package com.kzawilski;

import com.kzawilski.common.FolderWatcher;
import com.kzawilski.database.DataManager;
import com.kzawilski.database.domain.ExchangeRate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class MainApp extends Application {

    Task<Void> task;

    @Override
    public void start(Stage stage) throws IOException {
        startWatcher();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root, 300, 300);
        stage.setTitle("NBP exchange rates");
        stage.setScene(scene);
        stage.show();
    }

    private void startWatcher() {
        task = new FolderWatcher();
        new Thread(task).start();
    }

    @Override
    public void stop() throws Exception {
        task.cancel();
        DataManager.getInstance().stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
