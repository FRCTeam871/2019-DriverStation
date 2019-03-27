package com.team871.modules.camera.processing.detection;

import com.team871.modules.camera.processing.cscore.CvSinkStreamWrapper;
import com.team871.util.TimedLoopRunnable;
import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.IData;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.VideoException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class VisionProcessor {

    static final private int FPS = 30;
    static final private int MIN_FPS = 15;

    private final Thread visionProcessThread;
    private CvSinkStreamWrapper cvSinkStream;
    private CvSink imageIn;
    private boolean doRun;
    private BinaryDataValue isWorking;


    public VisionProcessor(IVisionProcess process, ITargetPipeline pipeline){
        this(null, process, pipeline);
    }

    public VisionProcessor(CvSink imageInput, IVisionProcess process, ITargetPipeline pipeline){
        this.imageIn = imageInput;

        doRun = false;
        isWorking = new BinaryDataValue(false);
        Mat outputImage = new Mat();

        Runnable vissionProcces = () -> {
            if(doRun) {
                if (imageIn != null && imageIn.isValid() && imageIn.getSource().isValid()) {
                    imageIn.grabFrame(outputImage, 1000.0 / MIN_FPS);
                    pipeline.process(outputImage);
                    process.publish(pipeline);


                    if (cvSinkStream!= null && cvSinkStream.isValid()) {
                        try {

                            overLayTarget(outputImage, pipeline.outputTargets());
                            cvSinkStream.putFrame(outputImage);
                        } catch (VideoException e){
                            //nothing
                        }
                    }

                    if (!isWorking.getValue())
                        isWorking.invert();
                } else {
                    if (isWorking.getValue())
                        isWorking.invert();
                }
            }
        };
        visionProcessThread = new Thread(new TimedLoopRunnable(vissionProcces,FPS));
        visionProcessThread.setDaemon(true);
        visionProcessThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public IData<Boolean> isWorking(){
        return isWorking;
    }

    public void changeSink(CvSink newSink){
        this.imageIn = newSink;
    }

    public void start(){
        if(cvSinkStream == null){
            cvSinkStream = new CvSinkStreamWrapper("VisionProcessorOutput", 1280, 720, FPS);
        }

        doRun = true;
    }

    public void stop() {
        doRun = false;
    }

    public void overLayTarget(Mat source, List<MatOfPoint> targets) {
        Imgproc.drawContours(source, targets, -1, new Scalar(255,0,0));

    }

    public void close(){
        doRun = false;

        if(visionProcessThread.isAlive())
            visionProcessThread.interrupt();

        if(cvSinkStream!=null)
            cvSinkStream.close();
    }
}
