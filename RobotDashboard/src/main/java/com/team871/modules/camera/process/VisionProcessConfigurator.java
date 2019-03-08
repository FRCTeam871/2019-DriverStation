package com.team871.modules.camera.process;

import com.team871.modules.BinaryIndicator;
import com.team871.modules.camera.CameraSelector;
import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.layout.VBox;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class VisionProcessConfigurator extends VBox {

    private CameraSelector cameraSelector;
    private BinaryIndicator binaryIndicator;

    /**
     * Allows for the interpretation of images
     * from through a chosen source through
     *  a vision processors.
     */
    public VisionProcessConfigurator(){
        super();
        binaryIndicator = new BinaryIndicator();
        binaryIndicator.setPrefHeight(20);
        binaryIndicator.setPrefWidth(20);
        this.getChildren().addAll(binaryIndicator);

    }

    /**
     *
     * @param camerasTable will tell the configurator
     *                     where to look for sources.
     * @param visionProcessor will tell the configurator
     *                        what to do with the source.
     */
    public void initialize(NetworkTable camerasTable, VisionProcessor visionProcessor){
        cameraSelector = new CameraSelector(camerasTable);
        this.getChildren().add(cameraSelector);

        visionProcessor.start();
        cameraSelector.setOnAction(e -> visionProcessor.changeSink(cameraSelector.getSelectedSink()));
    }

}
