package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.MutableDataValue;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author T3Pfaffe on 3/26/2019.
 * @project DriverStation
 */
public class ConnectionIndicator extends BinaryIndicator {

    public ConnectionIndicator(NetworkTableInstance tableInstance, ColorMode colorMode){
        this();
        initialize(tableInstance, colorMode);

    }

    public ConnectionIndicator(){
        super();
    }

    public void initialize(NetworkTableInstance tableInstance, ColorMode colorMode) {
        MutableDataValue<Boolean> connectedValue = new MutableDataValue<>(new BinaryDataValue());
        tableInstance.addConnectionListener(connectionNotification -> connectedValue.setValue(connectionNotification.connected),true);

        super.initialize(colorMode, "Connected", connectedValue);
    }
}
