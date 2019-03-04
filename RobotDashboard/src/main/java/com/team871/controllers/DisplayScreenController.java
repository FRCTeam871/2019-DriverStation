package com.team871.controllers;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.config.network.DeepSpaceNetworkVariables;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 */
public class DisplayScreenController {


    @FXML
    public Parent drive;
    @FXML
    public DriveScreenController driveController;
    @FXML
    public DebugScreenController debugController;

    @FXML
    BorderPane background;
    @FXML
    AnchorPane menuBackground;
    @FXML
    Button debugBtn;
    @FXML
    Button driveBtn;

    private IDashboardConfig config;

    public DisplayScreenController(){
        this(new DefaultDashboardConfig());
    }

    public DisplayScreenController(IDashboardConfig config){
        this.config = config;
    }

    @FXML
    void initialize(){
        DeepSpaceNetworkVariables networkVariables = new DeepSpaceNetworkVariables(true, config.getNetworkTableInstance(), "0.00");
//        background.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        background    .setBackground(new Background(new BackgroundFill(config.getColorMode().getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        menuBackground.setBackground(new Background(new BackgroundFill(config.getColorMode().getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        debugController.initialize(config, networkVariables);
        driveController.initialize(config, networkVariables);
    }

    @FXML
    void driveBtnAction() {
    }

    @FXML
    void debugBtnAction() {
        System.out.println("hah, you thought something would happen....");
    }
}
