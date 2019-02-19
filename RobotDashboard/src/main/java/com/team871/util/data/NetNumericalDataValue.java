package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetNumericalDataValue extends NumericalDataValue implements IData<Double> {

    /**
     * @param tableEntry the networked value that this dataValue will read from.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry, double maxValue, double minValue) {
        super(maxValue, minValue);

        tableEntry.addListener(event -> {
            try {
                super.set(event.value.getDouble());
            } catch (ClassCastException e) {
                System.out.println("Table Entry(" + tableEntry.getInfo() + "): " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        set(-1.);
    }

    /**
     *  @param tableEntry the networked value that this dataValue will read from.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry) {
        super();

        super.set(tableEntry.getDouble(-1.0));

        tableEntry.addListener(event -> {
            try {
                super.set(event.value.getDouble());
            } catch (ClassCastException e) {
                System.out.println("On Table Entry(" + tableEntry.getInfo() + "): " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }
}
