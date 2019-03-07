package com.team871.modules.camera.process;

import com.team871.util.data.TimmedLoopThread;
import edu.wpi.cscore.CvSink;
import org.opencv.core.Mat;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class VisionProccessor {

    static final private int FPS = 30;
    static final private int MIN_FPS = 15;

    final private Thread visionProcessThread;

    public VisionProccessor(CvSink imageIn, IVisionProcess coProcess, ITargetPipeline pipeline){
        CvSink imageIn1 = imageIn;
        IVisionProcess coProcess1 = coProcess;
        ITargetPipeline pipeline1 = pipeline;

        Mat outputImage = new Mat();

        Runnable vissionProcces = () -> {
            if (imageIn.isValid() && imageIn.getSource().isValid()) {
                imageIn.grabFrame(outputImage, 1000.0 / MIN_FPS);
                pipeline.process(outputImage);
                coProcess.publish(pipeline);
            }
        };
        visionProcessThread = new Thread(new TimmedLoopThread(vissionProcces,1000/FPS));
        visionProcessThread.setDaemon(true);

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public void start(){
        visionProcessThread.start();
    }

    public void close(){
        if(visionProcessThread.isAlive())
            visionProcessThread.interrupt();
    }

}
