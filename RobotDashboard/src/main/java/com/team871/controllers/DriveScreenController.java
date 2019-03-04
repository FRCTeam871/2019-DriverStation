package com.team871.controllers;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.network.DeepSpaceNetworkVariables;
import com.team871.modules.ArmDisplay;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.modules.cameras.VideoDisplay;
import javafx.fxml.FXML;

/**
 * @author T3Pfaffe on 2/8/2019.
 * @project 2019-DriverStation
 */
public class DriveScreenController {

    @FXML
    VideoDisplay videoDisplay;
    @FXML
    ArmDisplay armDisplay;
    @FXML
    CircleGraph headingDisplay;
    @FXML
    BinaryIndicator grabInSense;
    @FXML
    BinaryIndicator grabOutSense;

    private DeepSpaceNetworkVariables netConfig;
    private ColorMode colorMode;

    public DriveScreenController(){
    }

    @FXML
    void initialize(IDashboardConfig config, DeepSpaceNetworkVariables netConfig){
        this.netConfig = netConfig;
        colorMode = config.getColorMode();

        videoDisplay.initialize(netConfig.camerasTable,720, 1280);

        armDisplay.initialize(netConfig.upperArmAngle, netConfig.lowerArmAngle, netConfig.wristAngle);

        headingDisplay.initialize(colorMode, netConfig.heading);
        headingDisplay.createRadialHeadingGraph();

        grabInSense.initialize (colorMode, "Inner Succ", netConfig.isVacuumInner);
        grabOutSense.initialize(colorMode, "Outer Succ", netConfig.isVacuumInner, true);
    }
}
