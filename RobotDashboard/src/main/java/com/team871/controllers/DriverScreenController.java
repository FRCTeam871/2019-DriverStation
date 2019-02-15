package com.team871.controllers;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.network.AbstractNetConfig;
import com.team871.modules.BinaryIndicator;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.fxml.FXML;

/**
 * @author T3Pfaffe on 2/8/2019.
 * @project 2019-DriverStation
 */
public class DriverScreenController {

    @FXML
    BinaryIndicator grabIndicator;

    private IDashboardConfig config;
    private NetworkTableInstance netTable;
    private AbstractNetConfig netConfig;
    private ColorMode colorMode;

    public DriverScreenController(){
        config = new DefaultDashboardConfig();
        
    }

    @FXML
    public void initialize(){

    }

}
