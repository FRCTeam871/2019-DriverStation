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
    private Label label;

    public BinaryIndicator() {

        circle = new Circle();
        circle.setRadius(25);
        circle.setFill(Color.BLUE);

        label = new Label("Title");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(5, 5, 5, 5));

        this.getChildren().addAll(label, circle);
        this.setAlignment(Pos.CENTER);

        setNeutral();
    }

    /**
     * Will display a true or false status
     *
     * @param colorMode changes the font colors to match colorMode
     * @param title     the title/name of the indicator
     * @param data      the value this indicator will update to.
     */
    public void initialize(ColorMode colorMode, String title, IData<Boolean> data) {


        label.setText(title);
        label.setTextFill(colorMode.getSecondaryColor());

        //Updates:
        colorMode.addListener(observable -> label.setTextFill(colorMode.getSecondaryColor()));

        setState(data.getValue());

        data.addListener((observable, old, newValue) -> setState(newValue));
    }


    /**
     * Sets the Indicator to On (GREEN color) or Off position (RED color)
     */
    private void setState(boolean on) {
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
