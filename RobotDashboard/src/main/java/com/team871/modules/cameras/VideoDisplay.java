package com.team871.modules.cameras;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VideoDisplay extends HBox {
	
	private double camHeight;
	private double camWidth;
	private ImageView display;
    private CvSink cvsink;

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public VideoDisplay(){
        Image img = new Image("noCam.png");
	    camWidth  = 720;
        camHeight = 480;

        display = new ImageView(img);
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);
        getChildren().add(display);

        display.setImage(img);
        cvsink = new CvSink("CameraDisplay");

//        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * @param cameraHeight in pixels
     * @param cameraWidth in pixels
     */
    public void initialize(NetworkTable camerasTable, int cameraHeight, int cameraWidth){
        camWidth  = cameraWidth;
        camHeight = cameraHeight;


        List<VideoCamera> cameraList = new ArrayList<>();
        Set<String> camerasFound = camerasTable.getSubTables();
        for(String tableKey: camerasFound){
            String location = camerasTable.getSubTable(tableKey).getEntry("streams").getStringArray(new String[]{"notFound"})[0];
            location = location.substring(location.indexOf(':'));
            HttpCamera camera = new HttpCamera(("camera " +System.currentTimeMillis()), location);
            cameraList.add(camera);
        }

//        UsbCamera usbCamera = new UsbCamera("Webcam");
//        if(usbCamera.isValid()){
//            cameraList.add(usbCamera);
//        }
        WindowsUsbCameraWrapper usbCameraWrapper = new WindowsUsbCameraWrapper(0);



        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);



        Runnable videoUpdateTask = () -> {
            long startT;


            Mat captureImg = new Mat();
            MatOfByte byteMat = new MatOfByte();
            int FPS = 60;
            if(!cameraList.isEmpty()) {
                cvsink.setSource(cameraList.get(0));
                if(cvsink.getSource().isValid())
                    FPS =  cvsink.getSource().getVideoMode().fps;
            }else {
                System.out.println("No camera's found");
            }

            while (true) {
                startT = System.currentTimeMillis();
                if(!cameraList.isEmpty()){
                    if (true) {
                        Imgcodecs.imencode(".bmp", captureImg, byteMat);
                        //grabbing from video source

                        display.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
                        //setting the source to JavaFX display
                }else {
                    System.out.println("No camera's found");
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


    }

    private void close(){
        cvsink.close();
        System.out.println("Closing VideoDisplay");
    }
}
