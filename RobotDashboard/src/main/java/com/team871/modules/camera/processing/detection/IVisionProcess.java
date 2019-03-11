package com.team871.modules.camera.processing.detection;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public interface IVisionProcess {

    /**
     * @param pipeline the pipeline that will provide preliminary
     *                 information form the image.
     */
    void publish(ITargetPipeline pipeline);



}
