package com.team871.config.network;

import com.team871.util.data.IData;
import com.team871.util.data.NetBinaryDataValue;
import com.team871.util.data.NetNumericalDataValue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author T3Pfaffe on 2/15/2019.
 * @project 2019-Robot
 */
public class DeepSpaceNetworkVariables extends DeepSpaceNetConfig{
    private static DeepSpaceNetworkVariables deepSpaceNetworkVariables;

    public IData<Double> heading;
    public IData<Double> locationX;
    public IData<Double> locationY;

    public IData<Double> upperArmAngle;
    public IData<Double> lowerArmAngle;
    public IData<Double> wristAngle;

    public NetworkTable upperArmPID;
    public NetworkTable lowerArmPID;
    public NetworkTable wristPID;

    public IData<Boolean> isGrabbing;
    public IData<Boolean> isVacuumOn;
    public IData<Boolean> isVacuumInner;

    public NetworkTable camerasTable;

    public DeepSpaceNetworkVariables(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        super(isClient, instance, VERSION_VAL);


        heading   = new NetNumericalDataValue(getDefaultTable().getEntry(HEADING_KEY),361, -361);
        locationX = new NetNumericalDataValue(getDefaultTable().getEntry(LOCATION_X_KEY));
        locationY = new NetNumericalDataValue(getDefaultTable().getEntry(LOCATION_Y_KEY));

        upperArmAngle = new NetNumericalDataValue(getDefaultTable().getEntry(UPPER_ARM_ANGLE_KEY));
        lowerArmAngle = new NetNumericalDataValue(getDefaultTable().getEntry(LOWER_ARM_ANGLE_KEY));
        wristAngle    = new NetNumericalDataValue(getDefaultTable().getEntry(WRIST_ANGLE_KEY));

        upperArmPID = getDefaultTable().getSubTable(UPPER_ARM_PID_KEY);
        lowerArmPID = getDefaultTable().getSubTable(LOWER_ARM_PID_KEY);
        wristPID = getDefaultTable().getSubTable(WRIST_PID_KEY);

        isGrabbing = new NetBinaryDataValue(getDefaultTable().getEntry(IS_GRABBING_KEY));
        isVacuumOn = new NetBinaryDataValue(getDefaultTable().getEntry(IS_VACUUM_ON_KEY));
        isVacuumInner = new NetBinaryDataValue(getDefaultTable().getEntry(IS_VACUUM_INNER_KEY));

        camerasTable = instance.getTable(CAMERAS_TABLE_KEY);
    }

}
