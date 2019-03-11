package com.team871.modules.camera.processing.detection;

import edu.wpi.first.vision.VisionPipeline;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public interface ITargetPipeline extends VisionPipeline {


    List<MatOfPoint> outputTargets();

}
