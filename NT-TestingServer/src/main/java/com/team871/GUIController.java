package com.team871;

import com.team871.config.network.DeepSpaceNetConfig;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GUIController {

    @FXML
    Button toggleServerButton;

    static final String NET_IDENTITY = "NetworkTestingServer";
    static final int TEAM_NUMBER = 871;

    private NetworkTableInstance networkTableInstance;
    private DeepSpaceNetConfig netConfig;
    private WindowsUsbCameraWrapper usbCameraWrapper;
    private boolean isServerOn;

    public GUIController(){
        isServerOn = false;

        networkTableInstance = NetworkTableInstance.getDefault();
        networkTableInstance.setNetworkIdentity(NET_IDENTITY);
        networkTableInstance.setServerTeam(TEAM_NUMBER);
    }



    @FXML
    void initialize(){
        toggleServerButton.setText("Start Server");
    }

    @FXML
    void toggleServerAction() {

        if(isServerOn) {
            usbCameraWrapper.close();
            networkTableInstance.stopServer();

            System.out.println("Closed. (sorta)");

            toggleServerButton.setText("Start Server");
            isServerOn = false;

        }else {
            networkTableInstance.startServer();
            usbCameraWrapper = new WindowsUsbCameraWrapper(0);
            netConfig = new DeepSpaceNetConfig(false, networkTableInstance, "0.00");

            toggleServerButton.setText("Stop Server");
            isServerOn = true;

            System.out.println("Camera is valid: " + usbCameraWrapper.isValid());
            System.out.println(netConfig.getInstance().getTable(netConfig.CAMERAS_TABLE_KEY).getSubTables().toString());
        }
    }
}
