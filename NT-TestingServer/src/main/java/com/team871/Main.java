package com.team871;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    static final String FXML_FILENAME = "gui.fxml";
    static final int GUI_WIDTH  = 720/2;
    static final int GUI_HEIGHT = 480/2;

    @Override
    public void start(Stage primaryStage) throws Exception{



        Parent root = null;

        try {
            URL location = getClass().getClassLoader().getResource(FXML_FILENAME);
            FXMLLoader loader = new FXMLLoader(location);
            loader.setController(new GUIController());
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load the FXML file!");
            e.printStackTrace();
            stop();
            return;
        }

        primaryStage.setTitle("Network Tables Testing Server");
        primaryStage.setScene(new Scene(root, GUI_WIDTH, GUI_HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
