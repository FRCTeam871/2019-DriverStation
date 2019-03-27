package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetNumericalDataValue extends NumericalDataValue implements IData<Double> {

    /**
     *  @param tableEntry the networked value that this dataValue will read from.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry) {
        super();
        initialize(tableEntry);
    }

    /**
     * @param tableEntry the networked value that this dataValue will read from.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry, double maxValue, double minValue) {
        super(maxValue, minValue);
        initialize(tableEntry);
    }

    private void initialize(NetworkTableEntry tableEntry){
        tableEntry.addListener(event -> {
            try {
                setValue(event.value.getDouble());
            } catch (ClassCastException e) {
                System.out.println("Table Entry(" + tableEntry.getInfo() + "): " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        setValue(-1.);
    }
}
