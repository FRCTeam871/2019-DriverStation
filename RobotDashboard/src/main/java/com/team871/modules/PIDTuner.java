package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.NetNumericalDataValue;
import com.team871.util.data.NumericalDataValue;
import edu.wpi.first.networktables.NetworkTable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PIDTuner extends VBox {

    //TODO: get actual key values from PID class on robot
    private final String P_Key = "P_Val";
    private final String I_Key = "I_Val";
    private final String D_Key = "D_Val";
    private final String SETPOINT_Key = "SetPoint_Val";
    private final String ERROR_Key = "Error_Val";

    private Label mainTitle;

    private Label pTitle;
    private TextField pControl;

    private Label iTitle;
    private TextField iControl;

    private Label dTitle;
    private TextField dControl;

    private Label setPoint;
    private TextField setPointControl;

    private Label error;
    private TextField errorControl;

    private Button sendButton;

    private NumericalDataValue pVal;
    private NumericalDataValue iVal;
    private NumericalDataValue dVal;
    private NumericalDataValue setPointVal;
    private NumericalDataValue errorVal;

    public PIDTuner() {
        mainTitle = new Label("Un initialized: ");

        pTitle = new Label("P: ");
        pControl = new TextField("0.00");
        pControl.setMaxWidth(50);

        iTitle = new Label("I: ");
        iControl = new TextField("0.00");
        iControl.setMaxWidth(50);

        dTitle = new Label("D: ");
        dControl = new TextField("0.00");
        dControl.setMaxWidth(50);

        setPoint = new Label("SetPoint: ");
        setPointControl = new TextField("0.00");
        setPointControl.setMaxWidth(50);
        setPointControl.setEditable(false);

        error = new Label("Error: ");
        errorControl = new TextField("0.00");
        errorControl.setMaxWidth(50);
        errorControl.setEditable(false);

        sendButton = new Button("Send");

        GridPane dataGrid = new GridPane();
        dataGrid.addRow(0, pTitle, pControl);
        dataGrid.addRow(1, iTitle, iControl);
        dataGrid.addRow(2, dTitle, dControl);
        dataGrid.addRow(3, setPoint, setPointControl);
        dataGrid.addRow(4, error, errorControl);
        dataGrid.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(mainTitle, dataGrid, sendButton);
        this.setAlignment(Pos.CENTER);
        sendButton.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(5, 5, 5, 5));


    }

    public void initialize(NetworkTable pidObject, ColorMode colorMode) {

        mainTitle.setText(pidObject.toString());
        pVal = new NetNumericalDataValue(pidObject.getEntry(P_Key));
        iVal = new NetNumericalDataValue(pidObject.getEntry(I_Key));
        dVal = new NetNumericalDataValue(pidObject.getEntry(D_Key));
        setPointVal = new NetNumericalDataValue(pidObject.getEntry(SETPOINT_Key));
        errorVal = new NetNumericalDataValue(pidObject.getEntry(ERROR_Key));

        pControl.setText("" + pVal.getValue());
        iControl.setText("" + iVal.getValue());
        dControl.setText("" + dVal.getValue());
        setPointControl.setText("" + dVal.getValue());
        errorControl.setText(""    + dVal.getValue());

        //Updates:

        iVal.addListener((observable, old, newValue) -> iControl.setText("" + newValue.doubleValue()));
        pVal.addListener((observable, old, newValue) -> pControl.setText("" + newValue.doubleValue()));
        dVal.addListener((observable, old, newValue) -> dControl.setText("" + newValue.doubleValue()));
        setPointVal.addListener(((observable, oldValue, newValue) -> setPoint.setText("" + newValue)));
        errorVal.addListener(((observable, oldValue, newValue) -> error.setText("" + newValue)));
        //error val only updates from the network and is not mutable from driverStation


        sendButton.setOnAction(event -> {
            pidObject.getEntry(P_Key).setNumber(Double.parseDouble(pControl.getText()));
            pidObject.getEntry(I_Key).setNumber(Double.parseDouble(iControl.getText()));
            pidObject.getEntry(D_Key).setNumber(Double.parseDouble(dControl.getText()));
            pidObject.getEntry(SETPOINT_Key).setNumber(Double.parseDouble(setPointControl.getText()));
        });

        colorMode.addListener(observable -> {
                mainTitle.setTextFill(colorMode.getSecondaryColor());
                pTitle.setTextFill(colorMode.getSecondaryColor());
                iTitle.setTextFill(colorMode.getSecondaryColor());
                dTitle.setTextFill(colorMode.getSecondaryColor());
        });

    }

}