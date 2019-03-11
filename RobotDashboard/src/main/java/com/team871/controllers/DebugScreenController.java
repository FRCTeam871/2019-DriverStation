package com.team871.controllers;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.Style.ColorModeController;
import com.team871.config.network.DeepSpaceNetworkVariables;
import com.team871.modules.*;
import com.team871.util.data.NumericalDataValue;
import javafx.fxml.FXML;

/**
 * @author T3Pfaffe on 1/25/2019.
 * @project DriverStation
 */
public class DebugScreenController {

    @FXML
    private BinaryIndicator binaryIndicator1;
    @FXML
    private CircleGraph circleGraph1;
    @FXML
    private PIDTuner pid1;


    public DebugScreenController() {

    }


    @FXML
    void initialize(IDashboardConfig config, DeepSpaceNetworkVariables netConfig) {
        ColorMode colorMode = config.getColorMode();

        binaryIndicator1.initialize(colorMode, "isGrabbing", netConfig.isGrabbing);
        circleGraph1.initialize(colorMode, netConfig.heading);
        circleGraph1.createRadialHeadingGraph();
        pid1.initialize(netConfig.upperArmPID, colorMode);

    }
}
