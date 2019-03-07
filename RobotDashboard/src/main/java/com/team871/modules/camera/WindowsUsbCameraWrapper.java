package com.team871.modules.camera;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * @author T3Pfaffe on 3/1/2019.
 * @project DriverStation
 */
public class WindowsUsbCameraWrapper {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private Integer instanceQuantity;
    private int instanceIndex;

    private final double FPS;
    private VideoCapture videoCapture;
    private CvSource cvSource;


    public WindowsUsbCameraWrapper(int usbIndex) {
        if(instanceQuantity == null)
            instanceQuantity = 0;
        else
            instanceQuantity++;
        instanceIndex = instanceQuantity;

        //Statics
        FPS = 60;

        //CV objects
        CameraServer cameraServer = CameraServer.getInstance();
        videoCapture = new VideoCapture(usbIndex);
        cvSource = new CvSource(("WinUSBCam" + instanceQuantity), new VideoMode(VideoMode.PixelFormat.kMJPEG, 1280, 720, (int) FPS));
        cameraServer.startAutomaticCapture(cvSource);

        Runnable videoUpdateTask = () -> {
            long startT;
            Mat captureImg = new Mat();
            while (true) {
                startT = System.currentTimeMillis();
                videoCapture.read(captureImg);
                cvSource.putFrame(captureImg);
                try {
                    final long sleepMillis = (long) ((1000.0 / FPS) - (System.currentTimeMillis() - startT));
                    Thread.sleep(Math.max(sleepMillis, 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread videoUpdateThread = new Thread(videoUpdateTask);
        videoUpdateThread.setDaemon(true);
        videoUpdateThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public boolean isValid(){
        return cvSource.isValid();
    }

    public void close(){
        videoCapture.release();
        System.out.println(" \tWindowsUsbCameraWrapper-" + instanceIndex + " shut-down");
    }

}
