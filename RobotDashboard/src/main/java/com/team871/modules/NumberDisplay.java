package com.team871.modules;

import com.team871.config.Style.ColorMode;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NumberDisplay extends HBox {

    private Label textArea;
    private Label nameDisplay;


    public NumberDisplay(ColorMode colorMode, ObservableValue<Double> data, String name, char dataPrefix) {
        this();
        initialize(colorMode, data, name, dataPrefix);
    }

    public NumberDisplay() {
        nameDisplay = new Label("Not initialized: ");
        textArea = new Label("0/0");

        this.getChildren().addAll(nameDisplay, textArea);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    public void initialize(ColorMode colorMode, ObservableValue<Double> data, String name, char dataPrefix) {
        nameDisplay.setText(name + dataPrefix + " ");
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
