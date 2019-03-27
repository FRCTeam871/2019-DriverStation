package com.team871.modules.camera.processing.detection;

import com.team871.config.Style.ColorMode;
import com.team871.config.network.tables.TargetNetTable;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.camera.CameraSelector;
import com.team871.util.data.NetBinaryDataValue;
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

    private HBox indicatorsPanel;
    private BinaryIndicator targetFoundIndicator;


    public VisionProcessConfigurator(){
        super();
        title = new Label("Not initialized");

        runningIndicator = new BinaryIndicator();
        runningIndicator.setAlignment(Pos.CENTER);

        targetFoundIndicator = new BinaryIndicator();
        targetFoundIndicator.setAlignment(Pos.CENTER);

        indicatorsPanel = new HBox(runningIndicator);

        this.getChildren().addAll(title, indicatorsPanel);
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
     * @param colorMode what to set the /
     * @param dataOutputTable
     */
    public void initialize(NetworkTable camerasTable, VisionProcessor visionProcessor, String processName, ColorMode colorMode, TargetNetTable dataOutputTable) {
        initialize(camerasTable, visionProcessor, processName, colorMode);

        targetFoundIndicator.initialize(colorMode, "Found Target", new NetBinaryDataValue(dataOutputTable.getHasTargetEntry()));

        indicatorsPanel.getChildren().add(targetFoundIndicator);
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
        runningIndicator.initialize(colorMode, "Running", visionProcessor.isWorking());

        updateColor(colorMode);
        cameraSelector.setOnAction(e -> {
            if(visionProcessor.isWorking().getValue())
                visionProcessor.stop();

            CvSink newSink = cameraSelector.getSelectedSink();
            if(newSink != null && newSink.isValid()){
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
