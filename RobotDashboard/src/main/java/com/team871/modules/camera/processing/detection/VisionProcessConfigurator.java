package com.team871.modules.camera.processing.detection;

import com.team871.config.Style.ColorMode;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.camera.CameraSelector;
import com.team871.util.data.BinaryDataValue;
import edu.wpi.cscore.CvSink;
import edu.wpi.first.networktables.NetworkTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 * Allows for the interpretation of images
 *      from through a chosen source through
 *      a vision processor.
 */
public class VisionProcessConfigurator extends VBox {

    private Label title;
    private CameraSelector cameraSelector;
    private BinaryIndicator runningIndicator;
    private BinaryIndicator targetFoundIndicator;


    public VisionProcessConfigurator(){
        super();
        runningIndicator = new BinaryIndicator();
        title = new Label("Not initialized");
        targetFoundIndicator = new BinaryIndicator();
        targetFoundIndicator.setMaxSize(15, 15);
        targetFoundIndicator.setAlignment(Pos.CENTER);
        runningIndicator.setMaxSize(15, 15);
        runningIndicator.setAlignment(Pos.CENTER);
        this.getChildren().addAll(title, new HBox(runningIndicator, targetFoundIndicator));
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

        title.setText(processName);
        title.setTextFill(colorMode.getSecondaryColor());
        runningIndicator.initialize(colorMode, "Is On", visionProcessor.isWorking());
        targetFoundIndicator.initialize(colorMode, "Found Target", new BinaryDataValue());

        updateColor(colorMode);
        cameraSelector.setOnAction(e -> {
            if(visionProcessor.isWorking().getValue())
                visionProcessor.stop();

            CvSink newSink = cameraSelector.getSelectedSink();
            if(newSink.isValid()){
                visionProcessor.changeSink(newSink);
                visionProcessor.start();
            }
        });
        colorMode.addListener(e -> updateColor(colorMode));
        this.setPadding(new Insets(5));

    }

    private void updateColor(ColorMode colorMode){
        title.setTextFill(colorMode.getSecondaryColor());
    }
}
