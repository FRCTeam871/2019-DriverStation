package com.team871.modules;

import com.team871.util.data.IData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

public class NumberGraph extends VBox {

    private static Integer graphUpdaterQuantity;

    private LineChart<Double, Double> chart;
    private Axis xAxis;
    private Axis yAxis;
    private IData<Double> data;

    public NumberGraph(){
        super();

        xAxis = new NumberAxis();
        xAxis.setTickMarkVisible(true);

        yAxis = new NumberAxis();
        yAxis.setTickMarkVisible(true);

        chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefHeight(100);
        chart.setPrefWidth(100);


        this.getChildren().addAll(chart);
    }


    public void initialize(IData<Double> data){
        this.data = data;



        if(graphUpdaterQuantity == null)
            graphUpdaterQuantity = 0;
        else
        graphUpdaterQuantity = ++graphUpdaterQuantity;

        XYChart.Series dataStream = new XYChart.Series();

        chart.getData().add(dataStream);

        Runnable dataPullUpdateTask = () -> {
            final long INITIAL_T = System.currentTimeMillis();
            long loopStartT;
            while (true) {
                loopStartT = System.currentTimeMillis();
                //Do Stuff between here:
                    Platform.runLater(() -> {
                        dataStream.getData().add(new XYChart.Data( (System.currentTimeMillis()- INITIAL_T)/1000, (data.getValue())) );

                    });
                //and here.
                try {
                    final long sleepMillis = ((500)-(System.currentTimeMillis()-loopStartT));
                    Thread.sleep(Math.max(sleepMillis,0));
                } catch (InterruptedException e) {
                    System.out.println("Graph update task[" + graphUpdaterQuantity + "] interrupted!!");
                    e.printStackTrace();
                }
            }
        };

        Thread graphUpdateThread = new Thread(dataPullUpdateTask);
        graphUpdateThread.setDaemon(true);
        graphUpdateThread.start();
    }
}
