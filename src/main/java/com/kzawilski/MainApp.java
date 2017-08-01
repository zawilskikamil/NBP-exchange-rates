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
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root, 300, 300);
        stage.setTitle("NBP exchange rates");
        stage.setScene(scene);
        stage.show();
        startWacher();
    }

    private void startWacher(){
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                FolderWatcher watcher = new FolderWatcher();
                File theDir = new File("nbp");
                System.out.println(theDir.getAbsolutePath());
                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    System.out.println("creating directory: " + theDir.getName());
                    boolean result = false;

                    try{
                        theDir.mkdir();
                        result = true;
                    }
                    catch(SecurityException se){
                        //handle it
                    }
                    if(result) {
                        System.out.println("DIR created");
                    }else {
                        System.out.println("DIR exist ");
                        System.out.println(theDir.getAbsolutePath());
                    }
                }
                Path path = FileSystems.getDefault().getPath(theDir.getAbsolutePath());
                return watcher.watch(path);
            }
        };
        new Thread(task).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
