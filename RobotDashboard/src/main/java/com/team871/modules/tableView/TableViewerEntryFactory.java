package com.team871.modules.tableView;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;

public class TableViewerEntryFactory implements TableViewerEntry{


    private final String key;
    private final NetworkTableValue value;
    private final NetworkTableType type;

    public TableViewerEntryFactory(NetworkTableEntry tableEntry){
        key = tableEntry.getName();
        value = tableEntry.getValue();
        type = tableEntry.getType();

    }

    public TableViewerEntryFactory(NetworkTable table){
        key = table.getPath();
        value = null;
        type  = null;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public NetworkTableValue getValue() {
        return value;
    }

    @Override
    public NetworkTableType getType() {
        return type;
    }
}
