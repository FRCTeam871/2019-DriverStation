package com.team871.util.data;

/**
 * @author T3Pfaffe on 3/26/2019.
 * @project DriverStation
 */
public class MutableDataValue<e> extends DataValue<e> {

    private DataValue<e> wrappedMutableData;

    public MutableDataValue(DataValue<e> wrappedMutableData) {
        super(wrappedMutableData.getValue());
        this.wrappedMutableData = wrappedMutableData;

        wrappedMutableData.addListener(((observable, oldValue, newValue) -> super.setValue(newValue)));
    }

    @Override
    public boolean setValue(e newValue) {
        return wrappedMutableData.setValue(newValue);
    }


}
