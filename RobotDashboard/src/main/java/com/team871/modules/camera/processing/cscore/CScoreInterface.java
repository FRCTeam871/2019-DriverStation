package com.team871.modules.camera.processing.cscore;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class CScoreInterface implements ObservableValue<List<VideoCamera>> {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static List<CvSink> activeSinks;
    private static List<VideoCamera> foundCameras;

    private static final double DEFAULT_TIMEOUT = 100000;
    //100 seconds

    private NetworkTable camerasTable;
    private static List<InvalidationListener> invalidationListeners;
    private static List<ChangeListener<List<VideoCamera>>> changeListeners;

    public CScoreInterface(NetworkTable camerasTable){
        this.camerasTable = camerasTable;

        if(invalidationListeners == null) {
            invalidationListeners = new ArrayList<>();
            changeListeners = new ArrayList<>();

            foundCameras = new ArrayList<>();
            activeSinks = new ArrayList<>();
        }
        updateCameras();
        camerasTable.addSubTableListener((event, parentName, table ) -> {
            updateCameras();
        }, true);
    }



    //Public:

    public CvSink createNewSink(VideoCamera newCamera){
        if(newCamera == null)
            return null;

        String newCameraName = "[Sink@" + newCamera.getName() + "]";
        CvSink newSink = new CvSink(newCameraName);

        newSink.setSource(newCamera);

        for(CvSink cvSink : activeSinks){
            if (cvSink != null && cvSink.getSource().getName().equals(newCamera.getName())){
                System.out.println("" + newCameraName + " already exists. Returning the preexisting one");
                return cvSink;
            }
        }


        activeSinks.add(newSink);
        return newSink;
    }

    public List<CvSink> getOpenSinks(){
        return activeSinks;
    }

    public void removeSink(CvSink cvsink){
        activeSinks.remove(cvsink);
    }

    public List<VideoCamera> getFoundCameras(){
        return new ArrayList<>(foundCameras);
    }

    public static Image grabImage(CvSink input){
        return grabImage(input, DEFAULT_TIMEOUT);
    }

    public static Image grabImage(CvSink input, double timeout){
        MatOfByte byteMat = new MatOfByte();
        Mat captureImg =  grabMat(input, timeout);//~31ms
        Imgcodecs.imencode(".bmp", captureImg, byteMat); //~2ms

        return new Image(new ByteArrayInputStream(byteMat.toArray())); //~1ms
    }

    public static Mat grabMat(CvSink input){
        return grabMat(input, DEFAULT_TIMEOUT);
    }

    public static Mat grabMat(CvSink input, double timeout){

        Mat captureImg = new Mat();
        input.grabFrame(captureImg, timeout);
        return captureImg;
    }



    //Private:

    private List<HttpCamera> findCameras(){
        if (camerasTable==null || camerasTable.getSubTables().isEmpty()) {
            return null;
        }

        List<HttpCamera> foundCamerasReturn = new ArrayList<>();

        Set<String> streamsFound = camerasTable.getSubTables();


        for(String tableKey: streamsFound){
            NetworkTable camTable = camerasTable.getSubTable(tableKey);
            String[] foundLocations = camTable.getEntry("streams").getStringArray(new String[]{"notFound"});

            if(foundLocations.length == 0) {
                continue;
            }

            String location = foundLocations[0];

            if (foundLocations.length == 2)
                location = foundLocations[1];
                //do this to avoid it trying to resolve RoboRio ip from dns which will fail for some reason

            location = location.substring(location.indexOf(':')+1);

            if(location.equals("notFound")) {
                continue;
            }

            String defaultCamName = (location +":Type=HTTP/MJPG");
            String camName = camTable.getEntry("name").getString(defaultCamName); //TODO: find location of this
            //only use generated name if it cant find the actual name.

            HttpCamera cam = new HttpCamera(camName, location);
            foundCamerasReturn.add(cam);

        }
        return foundCamerasReturn;
    }

    private void updateCameras(){
        List<HttpCamera> newFoundCameras = findCameras();
        List<VideoCamera> oldFoundCameras = this.foundCameras;
        //dont look above. Everything is fine.

        if (newFoundCameras!= null && !newFoundCameras.equals(foundCameras)) {
            for (VideoCamera newVideoCamera: newFoundCameras) {
                boolean isDuplicate = false;
                for (VideoCamera oldVideoCamera: foundCameras){
                    if(newVideoCamera.getName().equals(oldVideoCamera.getName())){
                        isDuplicate = true;
                    }
                }

                if(!isDuplicate)
                    foundCameras.add(newVideoCamera);
            }

            notifyInvalidationListeners();
            notifyChangeListeners(oldFoundCameras, foundCameras);
        }
    }

    private List<WrappedVideoCamera> wrapVideoCameras(List<VideoCamera> videoCameras){
        List<WrappedVideoCamera> returnSet = new ArrayList<>();

        if(videoCameras != null && !videoCameras.isEmpty()) {
            for (VideoCamera videoCamera : videoCameras) {
                returnSet.add(new WrappedVideoCamera(videoCamera));
            }
        }
        return returnSet;
    }



    //Observable Value Implementation:

    private void notifyInvalidationListeners() {
        for (InvalidationListener changeListener : invalidationListeners) {
            changeListener.invalidated(this);
        }
    }

    private void notifyChangeListeners(List<VideoCamera> oldValue, List<VideoCamera> newValue){
        for(ChangeListener<List<VideoCamera>> changeListener :changeListeners){
            changeListener.changed(this, oldValue, newValue);
        }
    }

    @Override
    public void addListener(ChangeListener<? super List<VideoCamera>> listener) {
        changeListeners.add((ChangeListener<List<VideoCamera>>) listener);
    }

    @Override
    public void removeListener(ChangeListener<? super List<VideoCamera>> listener) {
        changeListeners.remove(listener);
    }

    @Override
    public List<VideoCamera> getValue() {
        return this.getFoundCameras();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);

    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }
}
