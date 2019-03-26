package com.team871.modules.camera;

import com.team871.modules.camera.processing.cscore.CScoreInterface;
import com.team871.modules.camera.processing.cscore.WrappedVideoCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class CameraSelector extends ComboBox<WrappedVideoCamera> {

    private final CScoreInterface cScoreInterface;

    public CameraSelector(NetworkTable camerasTable){
        super();

        this.cScoreInterface = new CScoreInterface(camerasTable);
        this.setPromptText("Select Camera Source");
        this.setMaxWidth(200.);

        //Updates:
        cScoreInterface.addListener((observable, oldValue, newValue) -> updateSelection(newValue));

        updateSelection(cScoreInterface.getFoundCameras());
    }

    /**
     *
     * @returns whatever CvSink is currently selected by the user.
     */
    public CvSink getSelectedSink(){
        if(this.getValue() == null)
            return null;
        else
            return cScoreInterface.createNewSink(this.getValue().getContainedVideoCamera());
    }

    private void updateSelection(List<VideoCamera> newValues){
        List<WrappedVideoCamera> pendingSelection = wrapVideoCameras(newValues);

        if(!pendingSelection.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<WrappedVideoCamera> items = this.getItems();

                retainAll(items, pendingSelection);
                //remove old nonexistent entries

                addAllUnique(items, pendingSelection);

            });
        }
    }

    /**
     *
     * @param videoCameras the list of videoCameras to be wrapped.
     * @return a list of all the wrapped cameras.
     */
    private List<WrappedVideoCamera> wrapVideoCameras(List<VideoCamera> videoCameras){
        List<WrappedVideoCamera> returnSet = new ArrayList<>();

        if(videoCameras != null && !videoCameras.isEmpty()) {
            for (VideoCamera videoCamera : videoCameras) {
                WrappedVideoCamera newWrappedCamera = new WrappedVideoCamera(videoCamera);
                addUnique(returnSet, newWrappedCamera);
            }
        }

        return returnSet;
    }

    /**
     * Will remove any items from oldList that do not exist in newList
     * @param oldList what will be modified if it is different that its comparator
     * @param newList what oldList will be compared against
     */
    private void retainAll(List<WrappedVideoCamera> oldList, List<WrappedVideoCamera> newList){
        for(WrappedVideoCamera oldCamera: oldList){
            boolean isDuplicate = false;
            for(WrappedVideoCamera newCamera: newList){
                if (oldCamera.equals(newCamera))
                    isDuplicate = true;
            }
            if(!isDuplicate)
                oldList.remove(oldCamera);
        }
    }

    /**
     *
     * @param cameraList list to be modified if newCamera is unique.
     * @param newCamera the new Camera attempting ot be added.
     * @return whether or not the list was modified.
     */
    private boolean addUnique(List<WrappedVideoCamera> cameraList, WrappedVideoCamera newCamera){
        for(WrappedVideoCamera oldVideoCamera : cameraList){
            if(oldVideoCamera.equals(newCamera))
                return false;
        }

        return cameraList.add(newCamera);
    }

    /**
     *
     * @param cameraList list to be modified if any form the newCameraList is unique.
     * @param newCameraList the new Camera's attempting to be added.
     * @return whether or not the list was modified.
     */
    private boolean addAllUnique(List<WrappedVideoCamera> cameraList, List<WrappedVideoCamera> newCameraList){
        boolean rVal = false;
        for(WrappedVideoCamera newCamera: newCameraList)
            rVal = addUnique(cameraList, newCamera);

        return rVal;
    }

}
