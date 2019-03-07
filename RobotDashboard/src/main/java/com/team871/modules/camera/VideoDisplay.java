package com.team871.modules.camera;

import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VideoDisplay extends VBox {

    private ImageView display;
    private HBox currentCameraInfoBox;
    private Label currentCameraName;
    private Label currentCameraResolution;
    private Label currentCameraDataRate;

    private double camHeight;
    private double camWidth;
    private CvSink cvsink;

    private NetworkTable camerasTable;
    private  List<VideoCamera> cameraList;
    private int cameraListIndex;
    private double FPS;

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public VideoDisplay(){
        Image img = new Image("noCam.png");
	    camWidth  = 720;
        camHeight = 480;

        currentCameraName = new Label("Camera Source: not found");
        currentCameraDataRate = new Label();
        currentCameraResolution = new Label();
        currentCameraInfoBox = new HBox(currentCameraName, currentCameraDataRate, currentCameraResolution);

        display = new ImageView(img);
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);
        getChildren().addAll(display, currentCameraInfoBox);


        display.setImage(img);
        cvsink = new CvSink("CameraDisplay");
        cameraList = new ArrayList<>();
        cameraListIndex = 0;
        FPS = 60;

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * @param cameraHeight in pixels
     * @param cameraWidth in pixels
     */
    public void initialize(NetworkTable camerasTable, int cameraHeight, int cameraWidth){
        this.camerasTable = camerasTable;
        camWidth  = cameraWidth;
        camHeight = cameraHeight;

        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);

        CameraServerJNI.setTelemetryPeriod(1);

        Runnable videoUpdateTask = () -> {
            long startT;
            Mat captureImg = new Mat();
            MatOfByte byteMat = new MatOfByte();

            changeSinkSource(cameraListIndex);

            while (true) {
                startT = System.currentTimeMillis();

                changeSinkSource(cameraListIndex);

                if(!cameraList.isEmpty()) {
                    if (cvsink.getSource().isValid()) {
                        cvsink.grabFrame(captureImg);
                        Imgcodecs.imencode(".bmp", captureImg, byteMat);
                        //grabbing from video source

                        display.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
                        //setting the source to JavaFX display
                    } else {
                        System.out.println("non-valid camera!" + cameraListIndex);
//                        findCameras();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("No camera's found");
                    findCameras();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    final long sleepMillis = (long) ((1000.0 / FPS) - (System.currentTimeMillis() - startT));
                    Thread.sleep(Math.max(sleepMillis, 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread displayUpdateThread = new Thread(videoUpdateTask);
        displayUpdateThread.setDaemon(true);
        displayUpdateThread.start();

        camerasTable.addSubTableListener((event, parentName, table ) -> findCameras(), true);
    }


    private void changeSinkSource(int newIndex){
        cameraListIndex = newIndex;
        if(!cameraList.isEmpty()) {
            cvsink.setSource(cameraList.get(cameraListIndex));
            if (cvsink.getSource().isValid()) {
//                FPS = cvsink.getSource().getVideoMode().fps;
                // for some reason this is only returning 0. will use default

                Platform.runLater(() -> {
                    updateCameraInfo();
                });

            }
        }
    }

    private void updateCameraInfo(){
        VideoCamera current = cameraList.get(cameraListIndex);
        currentCameraName.setText(current.getName() + ": " +  current.getDescription());
//        currentCameraDataRate.setText("" + current.getActualDataRate());
//        currentCameraResolution.setText("" + current.getVideoMode().width + " x " + current.getVideoMode().height);
    }

    private void findCameras(){
        if (camerasTable.getSubTables().isEmpty())
            return;

        System.out.println("Commencing Search for cameras: ");

        Set<String> camerasFound = camerasTable.getSubTables();
        for(String tableKey: camerasFound){
            String[] foundLocations = camerasTable.getSubTable(tableKey).getEntry("streams").getStringArray(new String[]{"notFound"});
            String location = foundLocations[0];;
            if (foundLocations.length == 2){
                location = foundLocations[1];
            }
            //do this to avoid it trying to resolve RobotRio ip from dns
            location = location.substring(location.indexOf(':')+1);
            System.out.println("\t Found camera at: " + location);
            HttpCamera camera = new HttpCamera(("camera " +System.currentTimeMillis()), location);
            cameraList.add(camera);
        }
        System.out.println( );
    }

    private void close(){
        cvsink.close();
        System.out.println("Closing Video Display");
    }
}
