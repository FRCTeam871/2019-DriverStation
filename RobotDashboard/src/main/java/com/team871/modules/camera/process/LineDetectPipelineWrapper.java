package com.team871.modules.camera.process;

import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class LineDetectPipelineWrapper extends LineDetectPipeline implements ITargetPipeline{


    @Override
    public List<MatOfPoint> outputTargets() {
        return super.convexHullsOutput();
        //convexHullsOutput is the particular dataset for this pipeline
    }
}
