package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author T3Pfaffe on 1/29/2019.
 * @project DriverStation
 */
public class BinaryIndicator extends VBox {

    private Circle circle;
    private Label title;
    private boolean invert;

    public BinaryIndicator() {

        circle = new Circle();
        circle.setRadius(25);
        circle.setFill(Color.BLUE);

        title = new Label("Title");
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(5));

        this.getChildren().addAll(title, circle);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5));

        setNeutral();
    }

    /**
     * Will display a true or false status.
     * @param colorMode changes the font colors to match colorMode.
     * @param title     the title/name of the indicator.
     * @param data      the value this indicator will update to.
     */
    public void initialize(ColorMode colorMode, String title, IData<Boolean> data) {
        initialize(colorMode, title, data, false);
    }

    /**
     * Will display a true or false status.
     * @param colorMode changes the font colors to match colorMode.
     * @param title     the title/name of the indicator.
     * @param data      the value this indicator will update to.
     * @param isInverted whether or not the data value will be inverted.
     */
    public void initialize(ColorMode colorMode, String title, IData<Boolean> data, boolean isInverted) {
        this.title.setText(title);
        this.title.setTextFill(colorMode.getSecondaryColor());
        this.invert = isInverted;

        //Updates:
        colorMode.addListener(observable -> this.title.setTextFill(colorMode.getSecondaryColor()));

        setState(data.getValue());

        data.addListener((observable, old, newValue) -> setState(newValue));

    }

    /**
     *
     * @param newTitle changes the title to new user provided one.
     */
    public void setTitle(String newTitle){
        title.setText(newTitle);
    }

    /**
     * Sets the Indicator to On (GREEN color) or Off position (RED color)
     */
    private void setState(boolean on) {
        if (invert)
                on = !on;

        if (on)
            circle.setFill(Color.GREEN);
        else
            circle.setFill(Color.RED);
    }

    /**
     * Sets the indicator to neutral (YELLOW color)
     */
    private void setNeutral() {
        circle.setFill(Color.YELLOW);
    }

}
