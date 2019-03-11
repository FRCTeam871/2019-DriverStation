package com.team871.modules.camera.processing.detection.dockingTarget;

import com.team871.modules.camera.processing.detection.ITargetPipeline;
import org.opencv.core.MatOfPoint;
import java.util.List;

public class DockingTargetDetectPipelineWrapper extends DockingTargetDetectPipeline implements ITargetPipeline {

    @Override
    public List<MatOfPoint> outputTargets() {
        return super.convexHullsOutput();
        //convexHullsOutput is the particular dataset for this pipeline
    }
}
