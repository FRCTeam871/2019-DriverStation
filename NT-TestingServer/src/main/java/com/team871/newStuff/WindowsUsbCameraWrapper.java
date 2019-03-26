package com.team871.newStuff;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * @author T3Pfaffe on 3/19/2019.
 * @project DriverStation
 */
public class WindowsUsbCameraWrapper {


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private Integer instanceQuantity;
    private int instanceIndex;

    private final int FPS;
    private final int Width;
    private final int Height;

    private VideoCapture videoCapture;
    private CvSinkStreamWrapper cvSinkStream;
    private Thread videoUpdateThread;

    public WindowsUsbCameraWrapper(int usbIndex) {
        if(instanceQuantity == null)
            instanceQuantity = 0;
        else
            instanceQuantity++;
        instanceIndex = instanceQuantity;

        //Statics
        FPS = 30;
        Width = 1280;
        Height = 720;

        //CV objects
        videoCapture = new VideoCapture(usbIndex);
        cvSinkStream = new CvSinkStreamWrapper(("WindowsUSB-"+ instanceQuantity), Width, Height, FPS);

        Runnable videoUpdateTask = () -> {
            long startT;
            Mat captureImg = new Mat();

            startT = System.currentTimeMillis();
            videoCapture.read(captureImg);
            cvSinkStream.putFrame(captureImg);
        };

        videoUpdateThread = new Thread(new TimedLoopThread(videoUpdateTask, FPS));
        videoUpdateThread.setDaemon(true);
        videoUpdateThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public boolean isValid(){
        return cvSinkStream.isValid();
    }

    public void close(){
        videoUpdateThread.interrupt();
        videoCapture.release();
        cvSinkStream.close();
        System.out.println(" \tWindowsUsbCameraWrapper-" + instanceIndex + " shut-down");
    }

}
