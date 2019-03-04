package com.team871.modules.cameras;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class CameraServerSource {

    private static Integer cameraQuantity;
    private int cameraIndex;

    private Image display;
    private VideoCapture vc;
    private Mat captureImg;
    private MatOfByte byteMat;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public CameraServerSource(String sourceURL){

        if(cameraQuantity == null){
            cameraQuantity = -1;
        }
        cameraIndex = ++cameraQuantity;


        vc = new VideoCapture();
        vc.open(sourceURL);
        captureImg = new Mat();
        byteMat   = new MatOfByte();

        update();
        display = new Image("noCam.png");

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

//    public void changeSource(ICamera newCamera){
//        vc.release();
//        vc.open(newCamera.getLocation().toString());
//    }

    public void changeSource(String newSource){
        vc.release();
        vc.open(newSource);
    }

    public void changeSource(int index){
        vc.release();
        vc.open(index);
    }

    public Image grabHerByThePu(){
        return display;
    }

    public boolean isConnected(){
        return vc.isOpened();
    }

    /**
     *
     * @return true if a connection exists and it can update from it
     */
    public boolean update(){
        if(vc.isOpened() && vc.grab()) {
            vc.retrieve(captureImg);
            Imgcodecs.imencode(".bmp", captureImg, byteMat);
            display = new Image(new ByteArrayInputStream(byteMat.toArray()));
            return true;
        }else{
            return false;
        }
    }

    public void close(){
        vc.release();
        System.out.println(" \tCamera-" + cameraIndex + " shut-down");
    }
}