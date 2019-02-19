package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.IData;
import com.team871.util.data.NumericalDataValue;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.ScaleDirection;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Team871-TPfaffe
 * Is a VBOX containing a one dimensional graph
 * Contains various graph templates.
 */
public class CircleGraph extends VBox {


    private Gauge gauge;
    private ColorMode colorMode;
    private IData<Double> data;
    private boolean isNormalized;

    public CircleGraph() {
        colorMode = new ColorMode(false);

        this.gauge = GaugeBuilder
                .create()
                .skinType(SkinType.BAR)
                .title("Null")
                .unit("Null")
                .maxSize(200, 200)
                .minSize(40, 40)
                .scaleDirection(ScaleDirection.CLOCKWISE)
                .thresholdVisible(true)
                .thresholdColor(Color.YELLOW)
                .threshold(0)
                .checkThreshold(true)
                .minValue(0)
                .maxValue(0)
                .barColor(colorMode.getPrimaryColor())
                .unitColor(colorMode.getSecondaryColor())
                .titleColor(colorMode.getSecondaryColor())
                .valueColor(colorMode.getPrimaryColor())
                .value(0.0)
                .build();

        this.getChildren().addAll(gauge);
        this.setSpacing(.5);
        this.setAlignment(Pos.CENTER);
    }


    /**
     * @param colorMode     the color mode settings to be used in this Graph
     * @param data          the data this graph will read from.
     */
    public void initialize(ColorMode colorMode, IData<Double> data) {
        this.colorMode = colorMode;
        this.data = data;

        this.isNormalized = false;

        this.gauge = GaugeBuilder
                .create()
                .skinType(SkinType.BAR)
                .title("Null")
                .unit("Null")
                .scaleDirection(ScaleDirection.CLOCKWISE)
                .thresholdVisible(true)
                .thresholdColor(Color.YELLOW)
                .threshold(0)
                .checkThreshold(true)
                .minValue(0)
                .maxValue(0)
                .barColor(colorMode.getPrimaryColor())
                .unitColor(colorMode.getSecondaryColor())
                .titleColor(colorMode.getSecondaryColor())
                .valueColor(colorMode.getPrimaryColor())
                .value(data.getValue())
                .build();

        this.getChildren().remove(0);
        this.getChildren().addAll(gauge);
        this.setSpacing(.5);
        this.setAlignment(Pos.CENTER);
        this.setPrefHeight(100);
        this.setPrefWidth(100);

        //Updates:
        colorMode.addListener(observable -> {
            gauge.setBarColor(colorMode.getPrimaryColor());
            gauge.setTitleColor(colorMode.getSecondaryColor());
            gauge.setValueColor(colorMode.getSecondaryColor());
            gauge.setUnitColor(colorMode.getSecondaryColor());
        });


        data.addListener((observable, old, newValue) -> {
            if (isNormalized && data instanceof NumericalDataValue) {//don't worry this cast is 'safe'
                gauge.setValue(newValue);
                //gauge.setValue(((NumericalDataValue) data).getValue(gauge.getMaxValue(), gauge.getMinValue()) );
            }
            else {
                gauge.setValue(newValue);
            }
        });

        gauge.setValue(data.getValue());

    }


    /**
     * For Creating a battery gauge/graphBox (0-100%).
     */
    public void createBatteryRadialGraphBox() {

        gauge.setTitle("Battery");
        gauge.setUnit("%");
        gauge.setScaleDirection(ScaleDirection.CLOCKWISE);
        gauge.setThresholdVisible(true);
        gauge.setThresholdColor(Color.RED);
        gauge.setThreshold(15);
        gauge.setCheckThreshold(true);
        gauge.setMinValue(0);
        gauge.setMaxValue(100);
        gauge.setValue(data.getValue());

    }

    public void createRadialHeadingGraph(){
        gauge.setTitle("Heading");
        gauge.setUnit("deg");
        gauge.setScaleDirection(ScaleDirection.CLOCKWISE);
        gauge.setThresholdVisible(true);
        gauge.setMinValue(0);
        gauge.setMaxValue(360);
        isNormalized = true;
    }

    /**
     * For Creating custom radial graph.
     *
     * @param title Name Of Gauge
     * @param unit  Unit of measurement
     */
    public void createCustomRadialGraphBox(String title, String unit) {
        Double max = Double.MAX_VALUE;
        Double min = Double.MIN_VALUE;

        if (data instanceof NumericalDataValue) { //don't worry this cast is 'safe'
            max = ((NumericalDataValue) data).getMaxValue();
            min = ((NumericalDataValue) data).getMinValue();
        }

        gauge.setValue(data.getValue());

        createCustomRadialGraphBox(title, unit, max, min);
    }

    /**
     * For Creating custom radial graph.
     * @param title Name Of Gauge
     * @param unit	Unit of measurement
     * @param maxVal Maximum Value Of Gauge
     * @param minVal Minimum Value Of Gauge
     */
    public void createCustomRadialGraphBox(String title, String unit, double maxVal, double minVal) {

        gauge.setTitle(title);
        gauge.setUnit(unit);
        gauge.setScaleDirection(ScaleDirection.CLOCKWISE);
        gauge.setThresholdVisible(true);
        gauge.setThresholdColor(Color.YELLOW);
        gauge.setThreshold(15);
        gauge.setCheckThreshold(true);
        gauge.setMinValue(minVal);
        gauge.setMaxValue(maxVal);

        gauge.setValue(data.getValue());

    }

}
