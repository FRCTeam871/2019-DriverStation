package com.team871.config.network;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 * Is a collection of Keys  corresponding to data points over Network Tables.
 */
public class DeepSpaceNetConfig extends AbstractNetConfig {

    //Default table key
    public static final String TABLE_KEY = "ROBOT";

    public static final String TIME_ELAPSED_KEY = "TimeElapsed";

    //Default camera table
    public static final String CAMERAS_TABLE_KEY = "CameraPublisher";

    //Robot location information
    public static final String HEADING_KEY = "headingAngle";
    public static final String LOCATION_X_KEY = "locationX";
    public static final String LOCATION_Y_KEY = "locationY";

    //Arm position information
    public static final String UPPER_ARM_ANGLE_KEY = "upperArmAngle";
    public static final String LOWER_ARM_ANGLE_KEY = "lowerArmAngle";
    public static final String WRIST_ANGLE_KEY = "wristAngle";

    //Networked PID tableKeys
    public static final String UPPER_ARM_PID_KEY = "upperArmPID";
    public static final String LOWER_ARM_PID_KEY = "lowerArmPID";
    public static final String WRIST_PID_KEY = "wristPID";

    //Networked Vision Processing tableKeys
    public static final String SENSOR_DATA_TABLE_KEY    = "GRIP";
    public static final String VISUAL_TARGET_SENSOR_KEY = "visualTargetSensor";
    public static final String LINE_SENSOR_KEY          = "lineSensor";

    //General Target Information subKeys
    public static final String HAS_TARGET_KEY = "hasTarget";
    public static final String ANGLE_KEY      = "angle";
    public static final String CENTER_X_KEY   = "centerX";
    public static final String CENTER_Y_KEY   = "centerY";
    public static final String LENGTH_X_KEY   = "lengthX";
    public static final String LENGTH_Y_KEY   = "lengthY";


    public static final String IS_GRABBING_KEY = "isGrabbing";
    public static final String IS_VACUUM_ON_KEY = "isVacuumOn";
    public static final String IS_VACUUM_INNER_KEY = "isVacuumInner";

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance) {
        super(isClient, instance);
    }

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        super(isClient, instance, VERSION_VAL);
    }

    @Override
    public NetworkTable getDefaultTable(){
        return super.getInstance().getTable(TABLE_KEY);
    }

}
