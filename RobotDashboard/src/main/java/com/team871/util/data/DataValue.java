package com.team871.util.data;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T3Pfaffe on 3/26/2019.
 * @project DriverStation
 */
public class DataValue<e> implements IData<e> {

    private List<ChangeListener<? super e>> changeListeners;
    private List<InvalidationListener> invalidationListeners;

    private e value;


    /**
     *
     * @param value of this DataValue Object
     */
    DataValue(e value){
        this.value = value;

        changeListeners = new ArrayList<>();
        invalidationListeners = new ArrayList<>();
    }

    /**
     *
     * @param newValue the new value to set this DataValue to.
     * @return whether or not this DataValue has changed as a result.
     */
    protected boolean setValue(e newValue){
        if(newValue.equals(getValue()))
            return false;
            //ignore duplicates

        noitfyChangeListeners(newValue, this.value, changeListeners);
        this.value = newValue;
        notifyInvalidationListeners(invalidationListeners);

        return true;
    }


    //Observable Implementation:
    @Override
    public void addListener(ChangeListener<? super e> listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super e> listener) {
        changeListeners.remove(listener);
    }

    @Override
    public e getValue() {
        return value;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

    private void noitfyChangeListeners(e newValue, e oldvalue, List<ChangeListener<? super e>> changeListeners){
        for (ChangeListener<? super e> changeListener: changeListeners){
            changeListener.changed(this, oldvalue, newValue);
        }
    }

    private void notifyInvalidationListeners(List<InvalidationListener> listeners){
        for (InvalidationListener listener: listeners){
            listener.notify();
        }
    }

    //Object Overrides:
    @Override
    public String toString(){
        return getValue().toString();
    }

}
