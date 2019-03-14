package com.team871.modules.camera.processing.detection;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * @author T3Pfaffe on 3/14/2019.
 * @project DriverStation
 */
public class CvSinkStreamWrapper {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private CvSource cvSource;


    public CvSinkStreamWrapper(String streamName, int width, int height, int FPS) {

        //CV objects
        CameraServer cameraServer = CameraServer.getInstance();
        cvSource = new CvSource(streamName, new VideoMode(VideoMode.PixelFormat.kMJPEG, width, height, FPS));
        Mat captureImg = new Mat();

        cameraServer.startAutomaticCapture(cvSource);
        putFrame(captureImg);

//        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Typically called in a update loop to display a moving image.
     * @param newFrame puts a new frame to be published
     */
    public void putFrame(Mat newFrame){
        cvSource.putFrame(newFrame);
    }

    public boolean isValid(){
        return cvSource.isValid();
    }


}
