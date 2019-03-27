package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

public class NumberDisplay extends VBox {

    private ColorMode colorMode;
    private IData<Double> data;
    private Label textArea;
    private Label nameDisplay;
    private DecimalFormat df;


    public NumberDisplay(ColorMode colorMode, IData<Double> data, String name) {
        this();
        initialize(colorMode, data, name);
    }

    public NumberDisplay() {
        nameDisplay = new Label("Not initialized: ");
        textArea = new Label("0/0");

        this.getChildren().addAll(nameDisplay, textArea);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    public void initialize(ColorMode colorMode, IData<Double> data, String name) {
        this.colorMode = colorMode;
        this.data = data;

        nameDisplay.setText(name + ": ");
        textArea.setText(Double.toString(round(data.getValue(), 2)));

        textArea.setTextFill(colorMode.getSecondaryColor());
        nameDisplay.setTextFill(colorMode.getSecondaryColor());

        //Updates:
        colorMode.addListener(observable -> {
            textArea.setTextFill(colorMode.getPrimaryColor());
            nameDisplay.setTextFill(colorMode.getSecondaryColor());
        });
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
