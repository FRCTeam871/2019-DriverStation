package com.team871.controllers;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.Style.ColorModeController;
import com.team871.config.network.ArmstrongNetConfig;
import com.team871.modules.*;
import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.NumericalDataValue;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * @author T3Pfaffe on 1/25/2019.
 * @project DriverStation
 */
public class TestScreenController {

    @FXML
    private AnchorPane background;

    @FXML
    private BinaryIndicator binaryIndicator1;
    @FXML
    private CircleGraph circleGraph1;
    @FXML
    private PIDTuner pid1;
    @FXML
    private NumberGraph pid1Graph;
    @FXML
    private ArmDisplay armDisplay;


    private IDashboardConfig config;
    private NetworkTableInstance netTable;
    private ArmstrongNetConfig netConfig;
    private ColorMode colorMode;
    ColorModeController colorModeController;

    public TestScreenController() {
        config = new DefaultDashboardConfig();
        netTable = config.getNetworkTableInstance();
        netConfig = new ArmstrongNetConfig(true, netTable, "0.0");
        colorMode = config.getColorMode();
        colorModeController = new ColorModeController(colorMode);
    }


    @FXML
    private void initialize() {
        binaryIndicator1.initialize(colorMode, "Test", new BinaryDataValue(true));
        circleGraph1.initialize(colorMode, new NumericalDataValue(22.));
        circleGraph1.createBatteryRadialGraphBox();
        pid1.initialize(netTable.getTable("LOL_IDK"), colorMode);
        pid1Graph.initialize(new NumericalDataValue(25.));
        armDisplay.initialize(new NumericalDataValue(90.), new NumericalDataValue(90.), new NumericalDataValue(90.));

        colorModeController.update();

    }
}
