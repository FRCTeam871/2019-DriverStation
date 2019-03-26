package com.team871;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.controllers.DisplayScreenController;
import com.team871.modules.camera.processing.cscore.WindowsUsbCameraWrapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * @project Robotics Dashboard
 * @author Team871-TPfaffe
 *
 */

public class FXMLGraphicalMain extends Application {

    static final String FXML_FILENAME = "FXML/Display.fxml";
    static final int TEAM_NUMBER = 871;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;

        IDashboardConfig config = new DefaultDashboardConfig(TEAM_NUMBER);
        WindowsUsbCameraWrapper usbCameraWrapper = new WindowsUsbCameraWrapper(0);

        try {
            URL location = getClass().getClassLoader().getResource(FXML_FILENAME);
            FXMLLoader loader = new FXMLLoader(location);
            loader.setController(new DisplayScreenController(config));
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load the FXML file!");
            e.printStackTrace();
            stop();
            return;

        }

        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(new Scene(root, config.getInitialWidth(), config.getInitialHeight()));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void stop() {
        System.out.println("GraphicalMain shutdown initiated");
        Platform.exit();
    }
}
