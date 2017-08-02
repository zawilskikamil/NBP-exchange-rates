package com.kzawilski;

import com.kzawilski.common.FolderWatcher;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.spi.DirectoryManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MainApp extends Application {

    Task<Void> task;

    @Override
    public void start(Stage stage) throws IOException {
        startWacher();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root, 300, 300);
        stage.setTitle("NBP exchange rates");
        stage.setScene(scene);
        stage.show();
    }

    private void startWacher() {
        task = new FolderWatcher();
        new Thread(task).start();
    }

    @Override
    public void stop() throws Exception {
        task.cancel();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
