package com.team871.modules.camera;

import com.team871.config.Style.ColorMode;
import com.team871.modules.camera.processing.detection.CScoreInterface;
import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.VideoEvent;
import edu.wpi.first.networktables.NetworkTable;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.text.DecimalFormat;

public class VideoDisplay extends VBox {

    private ImageView display;
    private CameraSelector cameraSelector;
    private HBox currentCameraInfoBox;
    private Label currentCameraFPS;
    private Label currentCameraResolution;
    private Label currentCameraDataRate;

    private double camHeight;
    private double camWidth;
    private CvSink cvsink;

    private AnimationTimer displayUpdateThread2;

    private Image defaultImage;


    public VideoDisplay(){
        defaultImage = new Image("noCam.png");
        camWidth  = 720;
        camHeight = 480;

        currentCameraFPS = new Label(" [Null] FPS ");
        currentCameraDataRate = new Label(" [Null] Mbit/s ");
        currentCameraResolution = new Label(" [Null]x[Null]px ");
        currentCameraInfoBox = new HBox(currentCameraResolution, currentCameraDataRate, currentCameraFPS);

        display = new ImageView(defaultImage);
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);
        getChildren().addAll(display);


        display.setImage(defaultImage);
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * @param displayHeight in pixels
     * @param displayWidth in pixels
     */
    public void initialize(NetworkTable camerasTable, int displayHeight, int displayWidth, ColorMode colorMode){
        cameraSelector = new CameraSelector(camerasTable);
        this.getChildren().addAll( new HBox(cameraSelector, currentCameraInfoBox));
        updateColor(colorMode);

        camWidth  = displayWidth;
        camHeight = displayHeight;
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);


        Runnable videoUpdateTask = () -> {
            if (cvsink!=null && cvsink.isValid() && cvsink.getSource().isValid()) {

                display.setImage(CScoreInterface.grabImage(cvsink,(16)));
                //grabbing from CV source setting the source to JavaFX display
            }
            else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        displayUpdateThread2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (cvsink!=null && cvsink.isValid() && cvsink.getSource().isValid()) {

                    display.setImage(CScoreInterface.grabImage(cvsink));
                    //grabbing from CV source setting the source to JavaFX display
                }
                else {

                }
            }
        };

        displayUpdateThread2.start();

        //Updates:
        cameraSelector.setOnAction(event -> {
            setDefaultCameraInfo();
            cvsink = cameraSelector.getSelectedSink();
        });

        colorMode.addListener(observable -> updateColor(colorMode));

        CameraServerJNI.addListener(e -> updateCameraInfo(), VideoEvent.Kind.kTelemetryUpdated.getValue(), false);
        CameraServerJNI.setTelemetryPeriod(1.0);
    }

    private void updateCameraInfo(){
        if(cvsink == null) {
            setDefaultCameraInfo();
            return;
        }

        Platform.runLater(() -> {
            try {
                double dataRate = cvsink.getSource().getActualDataRate()*0.000008;
                DecimalFormat df = new DecimalFormat("#.##");
                df.setMinimumIntegerDigits(2);

                currentCameraDataRate.setText(" " + df.format(dataRate) + "Mbit/s ");
                currentCameraResolution.setText(" " + cvsink.getSource().getVideoMode().width + "x" + cvsink.getSource().getVideoMode().height + "px ");
                currentCameraFPS.setText(" @" + (int)cvsink.getSource().getActualFPS() + "FPS ");
            } catch(edu.wpi.cscore.VideoException e){
                System.out.println("Telemetry update failed as telemetry period is not set!");
            }
        });
    }

    private void setDefaultCameraInfo(){
        Platform.runLater(() -> {
            currentCameraFPS.setText(" [Null] FPS ");
            currentCameraDataRate.setText(" [Null] Mbit/s ");
            currentCameraResolution.setText(" [Null]x[Null]px ");
        });
    }

    private void updateColor(ColorMode colorMode){
        Paint newTextFill = colorMode.getSecondaryColor();
        currentCameraDataRate.setTextFill(newTextFill);
        currentCameraFPS.setTextFill(newTextFill);
        currentCameraResolution.setTextFill(newTextFill);
    }

    private void close(){
        System.out.println("Closing Video Display");
        displayUpdateThread2.stop();
        if(cvsink!= null)
            cvsink.close();
    }
}
