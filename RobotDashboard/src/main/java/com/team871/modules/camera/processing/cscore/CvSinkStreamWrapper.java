package com.team871.modules.camera.processing.cscore;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.UUID;

/**
 * @author T3Pfaffe on 3/14/2019.
 * @project DriverStation
 */
public class CvSinkStreamWrapper {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private CvSource cvSource;


    /**
     *
     * @param streamName
     * @param width
     * @param height
     * @param FPS
     */
    public CvSinkStreamWrapper(String streamName, int width, int height, int FPS) {
        UUID uuid = UUID.randomUUID();
        String UUID_Value = uuid.toString();

        streamName = streamName + "_[UUID:" + UUID_Value + "]";

        //CV objects
        CameraServer cameraServer = CameraServer.getInstance();

        cvSource = new CvSource(streamName, new VideoMode(VideoMode.PixelFormat.kMJPEG, width, height, FPS));

        cameraServer.startAutomaticCapture(cvSource);

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Typically called in a update loop to display a moving image.
     * @param newFrame puts a new frame to be published
     */
    public void putFrame(Mat newFrame){
        if(cvSource.isValid())
            cvSource.putFrame(newFrame);
    }

    public boolean isValid(){
        return cvSource.isValid();
    }

    public void close(){

        if(cvSource!=null){
            cvSource.close();
        }
    }

}