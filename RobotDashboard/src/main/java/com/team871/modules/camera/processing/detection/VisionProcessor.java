package com.team871.modules.camera.processing.detection;

import com.team871.util.data.*;
import edu.wpi.cscore.CvSink;
import org.opencv.core.Mat;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class VisionProcessor {

    static final private int FPS = 30;
    static final private int MIN_FPS = 15;

    final private Thread visionProcessThread;

    private CvSink imageIn;
    private BinaryDataValue isWorking;

    public VisionProcessor(IVisionProcess process, ITargetPipeline pipeline){
        this(null, process, pipeline);
    }

    public VisionProcessor(CvSink imageInput, IVisionProcess process, ITargetPipeline pipeline){
        this.imageIn = imageInput;

        isWorking = new BinaryDataValue(false);
        Mat outputImage = new Mat();

        Runnable vissionProcces = () -> {
            if (imageIn!=null && imageIn.isValid() && imageIn.getSource().isValid()) {
                imageIn.grabFrame(outputImage, 1000.0 / MIN_FPS);
                pipeline.process(outputImage);
                process.publish(pipeline);
                if(!isWorking.getValue())
                    isWorking.invert();
            }else {
                if(isWorking.getValue())
                    isWorking.invert();
            }
        };
        visionProcessThread = new Thread(new TimmedLoopThread(vissionProcces,FPS));
        visionProcessThread.setDaemon(true);

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public IData<Boolean> isWorking(){
        return isWorking;
    }

    public void changeSink(CvSink newSink){
        this.imageIn = newSink;
    }

    public void start(){
        visionProcessThread.start();
    }

    public void close(){
        if(visionProcessThread.isAlive())
            visionProcessThread.interrupt();
    }

}
