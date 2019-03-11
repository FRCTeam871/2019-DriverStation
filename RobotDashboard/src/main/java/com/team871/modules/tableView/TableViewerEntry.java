package com.team871.modules.tableView;

import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;

public interface TableViewerEntry {

    String getKey();

    NetworkTableValue getValue();

    NetworkTableType getType();

}
