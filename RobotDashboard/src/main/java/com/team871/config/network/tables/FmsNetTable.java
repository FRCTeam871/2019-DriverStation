package com.team871.config.network.tables;

import com.team871.util.data.NetBinaryDataValue;
import com.team871.util.data.NetNumericalDataValue;
import com.team871.util.data.NetStringDataValue;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import javafx.beans.value.ObservableValue;

/**
 * @author T3Pfaffe on 3/27/2019.
 * @project DriverStation
 */
public final class FmsNetTable extends NetworkEntriesModule{

    private static final String FMS_TABLE_KEY              = "FMSInfo";
    private static final String EVENT_NAME_KEY             = "EventName";
    private static final String GAMES_SPECIFIC_MESSAGE_KEY = "GameSpecificMessage";
    private static final String IS_RED_TEAM_KEY            = "IsRedAlliance";
    private static final String MATCH_NUMBER_KEY           = "MatchNumber";
    private static final String MATCH_TYPE_KEY             = "MatchType";
    private static final String REPLAY_NUMBER_KEY          = "ReplayNumber";
    private static final String STATION_NUMBER_KEY         = "StationNumber";


    public FmsNetTable(NetworkTable parentTable) {
        super(parentTable, FMS_TABLE_KEY);
    }

    public NetworkTableEntry getEventNameEntry(){
        return getTable().getEntry(EVENT_NAME_KEY);
    }

    public NetworkTableEntry getGameSpecificMessageEntry(){
        return getTable().getEntry(GAMES_SPECIFIC_MESSAGE_KEY);
    }

    public NetworkTableEntry getIsRedTeamEntry(){
        return getTable().getEntry(IS_RED_TEAM_KEY);
    }

    public NetworkTableEntry getMatchNumberEntry(){
        return getTable().getEntry(MATCH_NUMBER_KEY);
    }

    public NetworkTableEntry getMatchTypeEntry(){
        return getTable().getEntry(MATCH_TYPE_KEY);
    }

    public NetworkTableEntry getReplayNumberEntry(){
        return getTable().getEntry(REPLAY_NUMBER_KEY);
    }

    public NetworkTableEntry getStationNumberEntry(){
        return getTable().getEntry(STATION_NUMBER_KEY);
    }



    public ObservableValue<String> getEventName(){
        return new NetStringDataValue(getEventNameEntry());
    }

    public ObservableValue<String> getGameSpecificMessage(){
        return new NetStringDataValue(getGameSpecificMessageEntry());
    }

    public ObservableValue<Boolean> getIsRedAlliance(){
        return new NetBinaryDataValue(getIsRedTeamEntry());
    }

    public ObservableValue<Double> getMatchNumber(){
        return new NetNumericalDataValue(getMatchNumberEntry());
    }

    public ObservableValue<String> getMatchType(){
        return new NetStringDataValue(getMatchTypeEntry());
    }

    public ObservableValue<Double> getReplayNumber(){
        return new NetNumericalDataValue(getReplayNumberEntry());
    }

    public ObservableValue<Double> getGamStationNumber(){
        return new NetNumericalDataValue(getStationNumberEntry());
    }
}
