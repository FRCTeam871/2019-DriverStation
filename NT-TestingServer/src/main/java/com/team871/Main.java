package com.team871;

import com.team871.config.network.DeepSpaceNetConfig;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    static final String FXML_FILENAME = "sample.fxml";
    static final int GUI_WIDTH  = 720/2;
    static final int GUI_HEIGHT = 480/2;

    static final String NET_IDENTITY = "NetworkTestingServer";
    static final int TEAM_NUMBER = 871;

    NetworkTableInstance networkTableInstance;

    @Override
    public void start(Stage primaryStage) throws Exception{


        networkTableInstance = NetworkTableInstance.getDefault();

        networkTableInstance.setNetworkIdentity(NET_IDENTITY);
        networkTableInstance.setServerTeam(TEAM_NUMBER);
        networkTableInstance.startServer();

        DeepSpaceNetConfig netConfig = new DeepSpaceNetConfig(false, networkTableInstance, "0.00");

        Parent root = null;

        try {
            URL location = getClass().getClassLoader().getResource(FXML_FILENAME);
            FXMLLoader loader = new FXMLLoader(location);
//            loader.setController(new DisplayScreenController(config));
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load the FXML file!");
            e.printStackTrace();
            stop();
            return;
        }

        primaryStage.setTitle("Robot Dashboard");
        primaryStage.setScene(new Scene(root, GUI_WIDTH, GUI_HEIGHT));
        primaryStage.show();

        WindowsUsbCameraWrapper usbCameraWrapper = new WindowsUsbCameraWrapper(0);
        System.out.println("Camera is valid: " + usbCameraWrapper.isValid());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
