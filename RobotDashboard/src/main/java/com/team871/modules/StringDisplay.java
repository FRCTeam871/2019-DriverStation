package com.team871.modules;

import com.team871.config.Style.ColorMode;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class StringDisplay extends HBox {

    private ColorMode colorMode;
    private ObservableValue<String> data;
    private Label textArea;
    private Label nameDisplay;

    public StringDisplay(ColorMode colorMode, ObservableValue<String> data, String name) {
        this(colorMode, data, name, ':');
    }

    public StringDisplay(ColorMode colorMode, ObservableValue<String> data, String name, char dataPrefix) {
        this.colorMode = colorMode;
        this.data = data;



        nameDisplay = new Label(name + dataPrefix + " ");
        textArea = new Label(data.getValue());

        textArea.setTextFill(colorMode.getSecondaryColor());
        nameDisplay.setTextFill(colorMode.getSecondaryColor());

        this.getChildren().addAll(nameDisplay, textArea);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(.2);
        this.setPadding(new Insets(3, 3, 3, 3));

        //Updates:
        colorMode.addListener(observable -> {
            textArea.setTextFill(colorMode.getPrimaryColor());
            nameDisplay.setTextFill(colorMode.getSecondaryColor());
        });
    }
}
