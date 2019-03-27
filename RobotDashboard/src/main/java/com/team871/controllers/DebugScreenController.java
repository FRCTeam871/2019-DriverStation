package com.team871.controllers;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.network.DeepSpaceNetConfig;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.modules.PIDTuner;
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
    void initialize(IDashboardConfig config, DeepSpaceNetConfig netConfig) {
        ColorMode colorMode = config.getColorMode();

//        binaryIndicator1.initialize(colorMode, "isGrabbing", );
        circleGraph1.initialize(colorMode, netConfig.robotLocalizationTable.getHeading());
        circleGraph1.createRadialHeadingGraph();
//        pid1.initialize(netConfig.upperArmPID, colorMode);

        //TODO: make netTable objects for Grabber and PID's
    }
}
