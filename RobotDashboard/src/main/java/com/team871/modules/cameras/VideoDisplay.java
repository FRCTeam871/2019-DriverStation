package com.team871.modules.cameras;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private Label currentCameraName;

    private double camHeight;
    private double camWidth;
    private CvSink cvsink;

    private NetworkTable camerasTable;
    private  List<VideoCamera> cameraList;
    private int cameraListIndex;
    private boolean sinkSourceChange;
    private double FPS;

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public VideoDisplay(){
        Image img = new Image("noCam.png");
	    camWidth  = 720;
        camHeight = 480;

        currentCameraName = new Label("Camera Source: not found");
        currentCameraName.setTextFill(Color.WHITE);

        display = new ImageView(img);
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);
        getChildren().addAll(display, currentCameraName);


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

//        WindowsUsbCameraWrapper usbCameraWrapper = new WindowsUsbCameraWrapper(0);

        findCameras();

        Runnable videoUpdateTask = () -> {
            long startT;
            long intervalCount;
            Mat captureImg = new Mat();
            MatOfByte byteMat = new MatOfByte();

            changeSinkSource(cameraListIndex);

            while (true) {
                startT = System.currentTimeMillis();

                if(sinkSourceChange){
                    changeSinkSource(cameraListIndex);
                    sinkSourceChange = false;
                }

                if(!cameraList.isEmpty()) {
                    if (cvsink.getSource().isValid()) {
                        cvsink.grabFrame(captureImg);
                        Imgcodecs.imencode(".bmp", captureImg, byteMat);
                        //grabbing from video source

                        display.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
                        currentCameraName.setText(cameraList.get(0).getName());
                        //setting the source to JavaFX display
                    }
                }
                else {
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

        camerasTable.addSubTableListener((event, parentName, table ) -> findCameras(), false);
    }


    private void changeSinkSource(int newIndex){
        cameraListIndex = newIndex;
        if(!cameraList.isEmpty()) {
            cvsink.setSource(cameraList.get(cameraListIndex));
            if (cvsink.getSource().isValid())
                System.out.println(cvsink.getSource().getVideoMode().fps);
//                FPS = cvsink.getSource().getVideoMode().fps;
        }
    }

    private void findCameras(){
        Set<String> camerasFound = camerasTable.getSubTables();
        for(String tableKey: camerasFound){
            String location = camerasTable.getSubTable(tableKey).getEntry("streams").getStringArray(new String[]{"notFound"})[1];
            location = location.substring(location.indexOf(':')+1);
            System.out.println(location);
            HttpCamera camera = new HttpCamera(("camera " +System.currentTimeMillis()), location);
            cameraList.add(camera);
        }
    }

    private void close(){
        cvsink.close();
        System.out.println("Closing Video Display");
    }
}
