package com.team871.controllers;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.network.DeepSpaceNetConfig;
import com.team871.modules.ArmDisplay;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.modules.GameInformationDisplay;
import com.team871.modules.camera.VideoDisplay;
import com.team871.modules.camera.processing.detection.VisionProcessConfigurator;
import com.team871.modules.camera.processing.detection.VisionProcessor;
import com.team871.modules.camera.processing.detection.dockingTarget.DockingTargetDetectPipelineWrapper;
import com.team871.modules.camera.processing.detection.dockingTarget.FindDockingTargetVisionProcess;
import com.team871.modules.camera.processing.detection.line.FindLineVisionProcess;
import com.team871.modules.camera.processing.detection.line.LineDetectPipelineWrapper;
import com.team871.util.data.BinaryDataValue;
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

    private DeepSpaceNetConfig netConfig;
    private ColorMode colorMode;

    public DriveScreenController(){
    }

    @FXML
    void initialize(IDashboardConfig config, DeepSpaceNetConfig netConfig){
        this.netConfig = netConfig;
        colorMode = config.getColorMode();

        gameInformationDisplay.initialize(netConfig, colorMode);

        armDisplay.initialize(netConfig.armTable);

        headingDisplay.initialize(colorMode, netConfig.robotLocalizationTable.getHeading());
        headingDisplay.createRadialHeadingGraph();
        headingDisplay.setPrefHeight(150);
        headingDisplay.setPrefWidth(150);

        grabSenseBox.setAlignment(Pos.CENTER);
        grabInSense.initialize (colorMode, "Inner Succ", new BinaryDataValue());
        grabOutSense.initialize(colorMode, "Outer Succ", new BinaryDataValue(), true);

        videoDisplay.initialize(netConfig.camerasTable,480, 720, colorMode);

        VisionProcessor lineDetectProcessor = new VisionProcessor(new FindLineVisionProcess(netConfig.gripTable.getLineSensorTableTable()), new LineDetectPipelineWrapper());
        lineDetectConfigurator.initialize(netConfig.camerasTable, lineDetectProcessor, "Line Detection", colorMode, netConfig.gripTable.getLineSensorTableTable());

        VisionProcessor targetDetectProcessor = new VisionProcessor(new FindDockingTargetVisionProcess(netConfig.gripTable.getDockingTargetTable()), new DockingTargetDetectPipelineWrapper());
        dockingTargetDetectConfigurator.initialize(netConfig.camerasTable, targetDetectProcessor, "Docking Target Detection", colorMode, netConfig.gripTable.getDockingTargetTable());
    }
}
