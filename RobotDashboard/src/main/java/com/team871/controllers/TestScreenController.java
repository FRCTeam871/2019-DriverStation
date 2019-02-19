package com.team871.controllers;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.Style.ColorModeController;
import com.team871.config.network.DeepSpaceNetworkVariables;
import com.team871.modules.*;
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
    private DeepSpaceNetworkVariables netConfig;
    private ColorMode colorMode;
    ColorModeController colorModeController;

    public TestScreenController() {
        config = new DefaultDashboardConfig(871);
        netTable = config.getNetworkTableInstance();
        netConfig = new DeepSpaceNetworkVariables(true, netTable, "0.0");
        colorMode = config.getColorMode();
        colorModeController = new ColorModeController(colorMode);
    }


    @FXML
    private void initialize() {
        binaryIndicator1.initialize(colorMode, "isGrabbing", netConfig.isGrabbing);
        circleGraph1.initialize(colorMode, netConfig.heading);
        circleGraph1.createRadialHeadingGraph();

        pid1.initialize(netConfig.upperArmPID, colorMode);
        pid1Graph.initialize(new NumericalDataValue(25.));
        armDisplay.initialize(netConfig.upperArmAngle, netConfig.lowerArmAngle, netConfig.wristAngle);

        colorModeController.update();

    }
}
