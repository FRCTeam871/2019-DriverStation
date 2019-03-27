package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetStringDataValue extends StringDataValue implements IData<String> {

  public NetStringDataValue(NetworkTableEntry tableEntry) {
    super();

      //Updates:
    tableEntry.addListener(event -> {
      try {
        setValue(event.value.getString());
      } catch (ClassCastException e) {
        System.out.println("TableEntry(" + tableEntry.getInfo() + ") ERROR: " + e.toString());
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    setValue("notFound");
  }
}
