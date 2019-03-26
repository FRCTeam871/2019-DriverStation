package com.team871.modules.camera.processing.cscore;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoSource;

/**
 * @author T3Pfaffe on 3/21/2019.
 * @project DriverStation
 */
public class WrappedVideoCamera {

    private final VideoCamera containedVideoCamera;

    public WrappedVideoCamera(VideoCamera cameraToBeWrapped) {
        this.containedVideoCamera = cameraToBeWrapped;
//        uuid = new UUID()
    }

    public VideoCamera getContainedVideoCamera(){
        return this.containedVideoCamera;
    }

    public String toString(){
        return containedVideoCamera.getName();
    }


    @Override
    public boolean equals(Object other) {

        if(other instanceof VideoSource) {
            return this.containedVideoCamera.equals(other);
        }else if (other instanceof WrappedVideoCamera){
            return this.getContainedVideoCamera().getName().equals(((WrappedVideoCamera) other).getContainedVideoCamera().getName());
        }
        else {
            return (this == other);
        }
    }
}
