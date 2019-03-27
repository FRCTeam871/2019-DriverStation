package com.team871.modules.camera.processing.detection.line;

import com.team871.config.network.tables.TargetNetTable;
import com.team871.modules.camera.processing.detection.ITargetPipeline;
import com.team871.modules.camera.processing.detection.IVisionProcess;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class FindLineVisionProcess implements IVisionProcess {

    private final TargetNetTable targetNetworkTable;


    private final double WIDTH = 720;

    private boolean hasLine = false;
    private double angle    = 0;
    private double centerX  = 0;
    private double centerY  = 0;
    private double lengthX  = 0;
    private double lengthY  = 0;

    public FindLineVisionProcess(TargetNetTable targetNetworkTable){
        this.targetNetworkTable = targetNetworkTable;
    }

    @Override
    public void publish(ITargetPipeline pipeline) {
        //Publishing the data to places goes here.

        if(!pipeline.outputTargets().isEmpty()){
            hasLine = true;
            RotatedRect r = Imgproc.minAreaRect(new MatOfPoint2f(pipeline.outputTargets().stream().sorted((m1, m2) -> {
                return Imgproc.contourArea(m1) < Imgproc.contourArea(m2) ? -1 : 1;
            }).findFirst().get().toArray()));

            angle = r.angle + ((r.size.width > r.size.height) ? 90 : 0);
            DecimalFormat d = new DecimalFormat("0.0");
            centerX = r.center.x - ((double) WIDTH / 2);
            centerY = r.center.y;
            if(r.size.width > r.size.height) {
                lengthX = r.size.height;
                lengthY = r.size.width;
            }else {
                lengthX = r.size.width;
                lengthY = r.size.height;
            }

        } else {
            hasLine = false;
        }

        NtPublish();
    }

    private void NtPublish(){

        targetNetworkTable.getHasTargetEntry().setBoolean(hasLine);
        targetNetworkTable.getAngleEntry().setDouble(angle);
        targetNetworkTable.getCenterXEntry().setDouble(centerX);
        targetNetworkTable.getCenterYEntry().setDouble(centerY);
        targetNetworkTable.getLengthXEntry().setDouble(lengthX);
        targetNetworkTable.getLengthYEntry().setDouble(lengthY);
        targetNetworkTable.getDistanceKey().setDouble(0.0);

    }

}
