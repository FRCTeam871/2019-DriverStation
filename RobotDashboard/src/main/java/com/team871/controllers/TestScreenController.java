package com.team871.controllers;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.Style.ColorModeController;
import com.team871.config.network.AbstractNetConfig;
import com.team871.config.network.ArmstrongNetConfig;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.modules.PIDTuner;
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

    private IDashboardConfig config;
    private NetworkTableInstance netTable;
    private AbstractNetConfig netConfig;
    private ColorMode colorMode;
    ColorModeController colorModeController;

    public TestScreenController() {
        config = new DefaultDashboardConfig();
        netTable = config.getNetworkTableInstance();
        netConfig = new ArmstrongNetConfig(netTable, "0.0");
        colorMode = config.getColorMode();
        colorModeController = new ColorModeController(colorMode);


    }


    @FXML
    private void initialize() {
        binaryIndicator1.initialize(colorMode, "Test", new BinaryDataValue(true));
        circleGraph1.initialize(colorMode, new NumericalDataValue(22.));
        circleGraph1.createBatteryRadialGraphBox();
        pid1.initialize(netTable.getTable("LOL_IDK"), colorMode);

        colorModeController.update();

    }
}
