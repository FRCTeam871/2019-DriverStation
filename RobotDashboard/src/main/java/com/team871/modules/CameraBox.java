package com.team871.modules;

import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;

public class CameraBox extends HBox {
	
	private double camHeight;
	private double camWidth;
	private CameraServerSource source;
	private ImageView display;

    private MediaView mediaView;

	public CameraBox(){
        Image img = new Image("noCam.png");
        source = new CameraServerSource("noCam.png");
	    camWidth  = 720;
        camHeight = 480;

        display = new ImageView(img);
        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);
        getChildren().add(display);

        display.setImage(source.grabHerByThePu());


    }

    /**
     * @param cameraHeight in pixels
     * @param cameraWidth in pixels
     */
    public void initialize(NetworkTable cameraTable, int cameraHeight, int cameraWidth){
        camWidth  = cameraWidth;
        camHeight = cameraHeight;

       //source.changeSource("http://10.8.71.2:1181/?action=stream"); //TODO: Grab source URL from netTables instead of hardcode
       source.changeSource(0);

        display.setFitHeight(camHeight);
        display.setFitWidth(camWidth);

        int FPS = 75;

        Runnable videoUpdateTask = () -> {
            long startT;

            while (true) {
                startT = System.currentTimeMillis();
                source.update();

//                Platform.runLater(Runnable = () -> {
                    display.setImage(source.grabHerByThePu());
//                });

                try {
                    final long sleepMillis = (long) ((1000.0/FPS)-(System.currentTimeMillis()-startT));
                    Thread.sleep(Math.max(sleepMillis,0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread videoUpdateThread = new Thread(videoUpdateTask);
        videoUpdateThread.setDaemon(true);
        videoUpdateThread.start();

    }

}
