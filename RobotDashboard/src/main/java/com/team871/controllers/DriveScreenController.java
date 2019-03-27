package com.team871.controllers;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.network.DeepSpaceNetworkVariables;
import com.team871.modules.ArmDisplay;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.modules.GameInformationDisplay;
import com.team871.modules.camera.VideoDisplay;
import com.team871.modules.camera.processing.detection.VisionProcessConfigurator;
import com.team871.modules.camera.processing.detection.VisionProcessor;
import com.team871.modules.camera.processing.detection.dockingTarget.DockingTargetDetectPipelineWrapper;
import com.team871.modules.camera.processing.detection.line.FindLineVisionProcess;
import com.team871.modules.camera.processing.detection.line.LineDetectPipelineWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

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
    HBox grabSenseBox;
    @FXML
    BinaryIndicator grabInSense;
    @FXML
    BinaryIndicator grabOutSense;
    @FXML
    VisionProcessConfigurator lineDetectConfigurator;
    @FXML
    VisionProcessConfigurator dockingTargetDetectConfigurator;
    @FXML
    public GameInformationDisplay gameInformationDisplay;

    private DeepSpaceNetworkVariables netConfig;
    private ColorMode colorMode;

    public DriveScreenController(){
    }

    @FXML
    void initialize(IDashboardConfig config, DeepSpaceNetworkVariables netConfig){
        this.netConfig = netConfig;
        colorMode = config.getColorMode();

        gameInformationDisplay.initialize(netConfig, colorMode);

        armDisplay.initialize(netConfig.upperArmAngle, netConfig.lowerArmAngle, netConfig.wristAngle);

        headingDisplay.initialize(colorMode, netConfig.heading);
        headingDisplay.createRadialHeadingGraph();
        headingDisplay.setPrefHeight(150);
        headingDisplay.setPrefWidth(150);

        grabSenseBox.setAlignment(Pos.CENTER);
        grabInSense.initialize (colorMode, "Inner Succ", netConfig.isVacuumInner);
        grabOutSense.initialize(colorMode, "Outer Succ", netConfig.isVacuumInner, true);

        videoDisplay.initialize(netConfig.camerasTable,480, 720, colorMode);

        VisionProcessor lineDetectProcessor = new VisionProcessor(new FindLineVisionProcess(netConfig.lineSensor), new LineDetectPipelineWrapper());
        lineDetectConfigurator.initialize(netConfig.camerasTable, lineDetectProcessor, "Line Detection", colorMode, netConfig.lineSensor);

        VisionProcessor targetDetectProcessor = new VisionProcessor(new FindLineVisionProcess(netConfig.visualTargetSensor), new DockingTargetDetectPipelineWrapper());
        dockingTargetDetectConfigurator.initialize(netConfig.camerasTable, targetDetectProcessor, "Docking Target Detection", colorMode, netConfig.visualTargetSensor);
    }
}
