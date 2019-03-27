package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetBinaryDataValue extends BinaryDataValue implements IData<Boolean> {



  /**
   * Will update to this network entry
   *
   * @param entry the value that this data is representing.
   */
  public NetBinaryDataValue(NetworkTableEntry entry) {
    super();

    //Updates:
    entry.addListener(event -> {
      try {
        setValue(event.value.getBoolean());
      } catch (ClassCastException e) {
        System.out.println("Table Entry(" + entry.getInfo() + "): " + e.toString());
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    setValue(false);
  }

}
