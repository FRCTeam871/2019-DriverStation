package com.team871.modules.tableView;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TableViewer extends TreeTableView implements TableViewerEntry{

    private NetworkTable sourceTable;
    private List<TableViewerEntry> entryList;

    private TreeTableColumn keyColumn   = new TreeTableColumn();
    private TreeTableColumn valueColumn = new TreeTableColumn();
    private TreeTableColumn typeColumnn = new TreeTableColumn();


    /**
     *
     * @param sourceTable Table viewer will find and display
     *                 all entries inside this table.
     */
    public TableViewer(NetworkTable sourceTable){
        this.sourceTable = sourceTable;
        entryList = new ArrayList<>();

        List<NetworkTable>       subTables = getAllTables(sourceTable);
        List<NetworkTableEntry> subEntries = getAllEntries(sourceTable);

        for(NetworkTable table : subTables){
            entryList.add(new TableViewer(table));
        }
        for(NetworkTableEntry entry : subEntries){
            entryList.add(new TableViewerEntryFactory(entry));
        }


        getColumns().addAll(keyColumn, valueColumn, typeColumnn);

    }

    private List<NetworkTable> getAllTables(NetworkTable sourceTable){
        List<NetworkTable> subTables = new ArrayList<>();
        Set<String> subTablesKeys = sourceTable.getSubTables();
        for(String tableKey: subTablesKeys){
            subTables.add(sourceTable.getSubTable(tableKey));
        }
        return subTables;
    }

    private List<NetworkTableEntry> getAllEntries(NetworkTable sourceTable){
        List<NetworkTableEntry> subEntries = new ArrayList<>();
        Set<String> subEntryKeys  = sourceTable.getKeys();
        for(String entryKey : subEntryKeys) {
            subEntries.add(sourceTable.getEntry(entryKey));
        }
        return subEntries;
    }


    @Override
    public String getKey() {
        return sourceTable.getPath();
    }

    @Override
    public NetworkTableValue getValue() {
        return null;
    }

    @Override
    public NetworkTableType getType() {
        return null;
    }
}
