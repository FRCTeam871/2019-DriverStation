package com.team871.modules.camera.processing.detection;

import com.team871.config.Style.ColorMode;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.camera.CameraSelector;
import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.IData;
import edu.wpi.first.networktables.NetworkTable;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 * Allows for the interpretation of images
 *      from through a chosen source through
 *      a vision processor.
 */
public class VisionProcessConfigurator extends VBox {

    private CameraSelector cameraSelector;
    private BinaryIndicator binaryIndicator;


    public VisionProcessConfigurator(){
        super();
        binaryIndicator = new BinaryIndicator();
        binaryIndicator.setPrefSize(25, 25);
        binaryIndicator.setAlignment(Pos.CENTER);
        this.getChildren().addAll(binaryIndicator);
        this.setAlignment(Pos.CENTER);
    }

    /**
     *
     * @param camerasTable will tell the configurator
     *                     where to look for sources.
     * @param visionProcessor will tell the configurator
     *                        what to do with the source.
     * @param processName what the process is called for
     *                    identification.
     */
    public void initialize(NetworkTable camerasTable, VisionProcessor visionProcessor, String processName, ColorMode colorMode){
        cameraSelector = new CameraSelector(camerasTable);
        this.getChildren().add(cameraSelector);
        binaryIndicator.initialize(colorMode, processName, visionProcessor.isWorking());
        visionProcessor.start();
        cameraSelector.setOnAction(e -> visionProcessor.changeSink(cameraSelector.getSelectedSink()));

    }
}
