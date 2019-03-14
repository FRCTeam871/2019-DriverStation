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
    public final String TABLE_KEY = "ROBOT";

    public final String TIME_ELAPSED_KEY = "TimeElapsed";

    //Default camera table
    public final String CAMERAS_TABLE_KEY = "CameraPublisher";

    //Robot location information
    public final String HEADING_KEY = "headingAngle";
    public final String LOCATION_X_KEY = "locationX";
    public final String LOCATION_Y_KEY = "locationY";

    //Arm position information
    public final String UPPER_ARM_ANGLE_KEY = "upperArmAngle";
    public final String LOWER_ARM_ANGLE_KEY = "lowerArmAngle";
    public final String WRIST_ANGLE_KEY = "wristAngle";

    //Networked PID tableKeys
    public final String UPPER_ARM_PID_KEY = "upperArmPID";
    public final String LOWER_ARM_PID_KEY = "lowerArmPID";
    public final String WRIST_PID_KEY = "wristPID";

    //Networked Vision Processing tableKeys
    public final String SENSOR_DATA_TABLE_KEY    = "GRIP";
    public final String VISUAL_TARGET_SENSOR_KEY = "visualTargetSensor";
    public final String LINE_SENSOR_KEY          = "lineSensor";

    //General Target Information subKeys
    public final String HAS_TARGET_KEY = "hasTarget";
    public final String ANGLE_KEY      = "angle";
    public final String CENTER_X_KEY   = "centerX";
    public final String CENTER_Y_KEY   = "centerY";
    public final String LENGTH_X_KEY   = "lengthX";
    public final String LENGTH_Y_KEY   = "lengthY";


    public final String IS_GRABBING_KEY = "isGrabbing";
    public final String IS_VACUUM_ON_KEY = "isVacuumOn";
    public final String IS_VACUUM_INNER_KEY = "isVacuumInner";

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
