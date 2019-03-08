package com.team871.modules.camera;

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

    private NetworkTable camerasTable;
    private List<InvalidationListener> invalidationListeners;

    public CScoreInterface(NetworkTable camerasTable){
        this.camerasTable = camerasTable;

        invalidationListeners = new ArrayList<>();
        foundCameras = new ArrayList<>();
        activeSinks = new ArrayList<>();

        camerasTable.addSubTableListener((event, parentName, table ) -> findCameras(), true);
        notifyInvalidationListeners();
    }


    public CvSink createNewSink(VideoCamera newCamera){

        String newCameraName = "[Sink@" + newCamera.getName()+"]";
        CvSink newSink = new CvSink(newCameraName);
        newSink.setSource(newCamera);
        if(!activeSinks.contains(newSink)) {
            int i = 0;
            for(CvSink cvSink : activeSinks){
                if (cvSink.getName().equals(newCameraName)){
                    System.out.println("" + newCameraName + " already exists. Returning the preexisting one");
                 return cvSink;
                }
            }
        }
            activeSinks.add(newSink);

        return newSink;
    }

    public List<CvSink> getOpenSinks(){
        return this.activeSinks;
    }

    public void removeSink(CvSink cvsink){
        activeSinks.remove(cvsink);
    }

    public List<VideoCamera> getFoundCameras(){
        return foundCameras;
    }


    public static Image grabImage(CvSink input){

        MatOfByte byteMat = new MatOfByte();
        Mat captureImg =  grabMat(input);

        Imgcodecs.imencode(".bmp", captureImg, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public static Mat grabMat(CvSink input){
        Mat captureImg = new Mat();
        input.grabFrame(captureImg);
        return captureImg;
    }

    private void findCameras(){
        if (camerasTable.getSubTables().isEmpty())
            return;

        System.out.println("Commencing Search for cameras: ");

        Set<String> streamsFound = camerasTable.getSubTables();
        boolean changed = false;
        for(String tableKey: streamsFound){
            String[] foundLocations = camerasTable.getSubTable(tableKey).getEntry("streams").getStringArray(new String[]{"notFound"});
            if(foundLocations.length == 0)
                continue;
            String location = foundLocations[0];;
            if (foundLocations.length == 2){
                location = foundLocations[1];
            }
            //do this to avoid it trying to resolve RobotRio ip from dns

            location = location.substring(location.indexOf(':')+1);
            String newCameraName = (location +":Type=HTTP/MJPG");
            HttpCamera camera = new HttpCamera(newCameraName, location);

            boolean isDuplicate = false;
            for(VideoCamera videoCamera: foundCameras){
                if(videoCamera.getName().equals(newCameraName)){
                    isDuplicate = true;
                }
            }

            if(!isDuplicate) {
                changed = true;
                foundCameras.add(camera);
                System.out.println("\t Found new camera at: " + location);
            }
        }
        System.out.println( );
        if(changed){
            notifyInvalidationListeners();
        }
        else notifyInvalidationListeners(); //TODO: find out why I have to do this no matter what.
    }


    //Observable Value stuff:
    private void notifyInvalidationListeners() {
        for (InvalidationListener changeListener : invalidationListeners) {
            changeListener.invalidated(this);
        }
    }

    @Override
    @Deprecated
    public void addListener(ChangeListener<? super List<VideoCamera>> listener) {
        throw new UnsupportedOperationException("Change Listener is  not supported for this object");
    }

    @Override
    @Deprecated
    public void removeListener(ChangeListener<? super List<VideoCamera>> listener) {
        throw new UnsupportedOperationException("Change Listener is  not supported for this object");
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
