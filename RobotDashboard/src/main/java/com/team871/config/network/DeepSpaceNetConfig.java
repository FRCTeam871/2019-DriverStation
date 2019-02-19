package com.team871.config.network;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 * Is a collection of
 */
public class DeepSpaceNetConfig extends AbstractNetConfig {
    private DeepSpaceNetConfig armstrongNetConfig;


    public final String TABLE_KEY;

    public final String CAMERAS_TABLE_KEY;
    public final String CAMERA_1_TABLE;
    public final String CAMERA_2_TABLE;

    public final String HEADING_KEY;
    public final String LOCATION_X_KEY;
    public final String LOCATION_Y_KEY;

    public final String UPPER_ARM_ANGLE_KEY;
    public final String LOWER_ARM_ANGLE_KEY;
    public final String WRIST_ANGLE_KEY;

    public final String UPPER_ARM_PID_KEY;
    public final String LOWER_ARM_PID_KEY;
    public final String WRIST_PID_KEY;

    public final String FOUND_TARGETS_LIST_KEY;

    public final String IS_GRABBING_KEY;
    public final String IS_VACUUM_ON_KEY;
    public final String IS_VACUUM_INNER_KEY;

    public DeepSpaceNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
        super(isClient, instance, VERSION_VAL);
        if(armstrongNetConfig == null)
            armstrongNetConfig = this;


        TABLE_KEY = "ROBOT";

        CAMERAS_TABLE_KEY = "Cameras";
        CAMERA_1_TABLE = "camera1";
        CAMERA_2_TABLE = "camera2";

        HEADING_KEY  = "headingAngle";
        LOCATION_X_KEY = "locationX";
        LOCATION_Y_KEY = "locationY";

        UPPER_ARM_ANGLE_KEY = "upperArmAngle";
        LOWER_ARM_ANGLE_KEY = "lowerArmAngle";
        WRIST_ANGLE_KEY     = "wristAngle";
        WRIST_PID_KEY       = "wristPID";
        LOWER_ARM_PID_KEY   = "lowerArmPID";
        UPPER_ARM_PID_KEY   = "upperArmPID";

        FOUND_TARGETS_LIST_KEY = "foundTargetsList";

        IS_GRABBING_KEY     = "isGrabbing";
        IS_VACUUM_ON_KEY    = "isVacuumOn";
        IS_VACUUM_INNER_KEY = "isVacuumInner";
    }

    @Override
    public NetworkTable getTable(){
        return super.getInstance().getTable(TABLE_KEY);
    }

}
