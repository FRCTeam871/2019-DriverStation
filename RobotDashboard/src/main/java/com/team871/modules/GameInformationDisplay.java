package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.config.network.AbstractNetConfig;
import com.team871.util.data.NetBinaryDataValue;
import com.team871.util.data.NetNumericalDataValue;
import com.team871.util.data.NetStringDataValue;
import edu.wpi.first.networktables.NetworkTable;
import javafx.scene.layout.VBox;

/**
 * @author T3Pfaffe on 3/26/2019.
 * @project DriverStation
 */
public class GameInformationDisplay extends VBox {

    private StringDisplay eventName;
    private StringDisplay gameSpecificMessage;
    private BinaryIndicator isRedTeam;
    private NumberDisplay matchNumber;
    private NumberDisplay replayNumber;
    private NumberDisplay stationNumber;

    public GameInformationDisplay(){
        super();
    }

    public void initialize(AbstractNetConfig netConfig, ColorMode colorMode){
        NetworkTable fmsData = netConfig.getDefaultTable().getSubTable(netConfig.FMS_TABLE_KEY);

        eventName = new StringDisplay(colorMode, new NetStringDataValue(fmsData.getEntry(netConfig.EVENT_NAME_KEY)), "Event");
        gameSpecificMessage = new StringDisplay(colorMode, new NetStringDataValue(fmsData.getEntry(netConfig.GAMES_SPECFIC_MESSAGE_KEY)), "Msg");
        isRedTeam = new BinaryIndicator(colorMode,"Is Red Team", new NetBinaryDataValue(fmsData.getEntry(netConfig.IS_RED_TEAM_KEY)));
        matchNumber = new NumberDisplay(colorMode,  new NetNumericalDataValue((fmsData.getEntry(netConfig.MATCH_NUMBER_KEY))), "Match #");
        replayNumber = new NumberDisplay(colorMode,  new NetNumericalDataValue((fmsData.getEntry(netConfig.REPLAY_NUMBER_KEY))), "Replay #");
        stationNumber = new NumberDisplay(colorMode,  new NetNumericalDataValue((fmsData.getEntry(netConfig.STATION_NUMBER_KEY))), "Station #");

        isRedTeam.setPrefSize(25, 15);

        this.getChildren().addAll(eventName, gameSpecificMessage, isRedTeam, matchNumber, replayNumber, stationNumber);
    }

}
