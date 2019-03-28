package com.team871.modules.camera.processing.detection.dockingTarget;

import com.team871.config.network.tables.TargetNetTable;
import com.team871.modules.camera.processing.detection.ITargetPipeline;
import com.team871.modules.camera.processing.detection.IVisionProcess;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class FindDockingTargetVisionProcess implements IVisionProcess {

    private final TargetNetTable targetNetworkTable;

    private final double WIDTH = 480;

    private boolean hasTarget = false;
    private double angle    = 0;
    private double centerX  = 0;
    private double centerY  = 0;
    private double lengthX  = 0;
    private double lengthY  = 0;
    private double distance = 0;

    public FindDockingTargetVisionProcess(TargetNetTable targetNetworkTable){
        this.targetNetworkTable = targetNetworkTable;
    }

    @Override
    public void publish(ITargetPipeline pipeline) {
        //        System.out.println(pipeline.filterContoursOutput().size() + " " + pipeline.findContoursOutput().size());
        if(!pipeline.outputTargets().isEmpty()){
            List<RotatedRect> rects = pipeline.outputTargets().stream().sorted((m1, m2) -> {
                return Imgproc.contourArea(m1) < Imgproc.contourArea(m2) ? -1 : 1;
            }).map(m -> {
                MatOfPoint2f m2f = new MatOfPoint2f();
                m.convertTo(m2f, CvType.CV_32FC2);
                return Imgproc.minAreaRect(m2f);
            }).collect(Collectors.toList());

            for(int i = 0; i < rects.size(); i++) {
                RotatedRect r = rects.get(i);
                double angle = r.angle + ((r.size.width > r.size.height) ? 90 : 0);
                DecimalFormat d = new DecimalFormat("0.0");
                double centerX = r.center.x - (WIDTH / 2);
                double centerY = r.center.y;
                double lengthX, lengthY;
                if (r.size.width > r.size.height) {
                    lengthX = r.size.height;
                    lengthY = r.size.width;
                } else {
                    lengthX = r.size.width;
                    lengthY = r.size.height;
                }
            }

            if(rects.size() >= 2){
                hasTarget = true;
                select(rects.get(0), rects.get(1));
            }else{
                hasTarget = false;
            }
        } else {
            hasTarget = false;
        }
        NtPublish();
    }

    private void select(RotatedRect rect1, RotatedRect rect2){

        int width = (rect2.boundingRect().x + rect2.boundingRect().width) - rect1.boundingRect().x;
        int height = (rect2.boundingRect().y + rect2.boundingRect().height) - rect1.boundingRect().y;

        this.angle = (rect1.angle + rect2.angle) / 2;
        this.centerX = (rect1.center.x + rect2.center.x)/2 - (WIDTH / 2);
        this.centerY = (rect1.center.y + rect2.center.y)/2;
        this.lengthX = width;
        this.lengthY = height;
        this.distance = 0.0; //TODO: calculate estimated target distance

    }

    private void NtPublish(){
        targetNetworkTable.getHasTargetEntry().setBoolean(hasTarget);
        targetNetworkTable.getAngleEntry().setDouble(angle);
        targetNetworkTable.getCenterXEntry().setDouble(centerX);
        targetNetworkTable.getCenterYEntry().setDouble(centerY);
        targetNetworkTable.getLengthXEntry().setDouble(lengthX);
        targetNetworkTable.getLengthYEntry().setDouble(lengthY);
        targetNetworkTable.getDistanceKey().setDouble(0.0);
    }

}
