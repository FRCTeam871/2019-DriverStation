package com.team871.modules.camera;

import com.team871.modules.camera.processing.detection.CScoreInterface;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.control.ComboBox;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class CameraSelector extends ComboBox<VideoCamera> {

    private final CScoreInterface cScoreInterface;

    public CameraSelector(NetworkTable camerasTable){
        super();

        this.cScoreInterface = new CScoreInterface(camerasTable);
        this.setPromptText("Select Camera Source");

        //Updates:
        cScoreInterface.addListener(observable -> updateSelection());
        updateSelection();
    }

    public CvSink getSelectedSink(){
        return cScoreInterface.createNewSink(this.getValue());
    }

    private void updateSelection(){
        this.getItems().retainAll(cScoreInterface.getFoundCameras());
        //removes old cameras
//        System.out.println(this.getItems().toString());

        for(VideoCamera camera: cScoreInterface.getFoundCameras()){
            if(!this.getItems().contains(camera)){
                this.getItems().add(camera);
                //so only new camera's get added.
            }
        }
    }

}
