package com.team871.util.data;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TPfaffe-871
 * Contains a double value constrained by a max and min that is readOnly.
 */
public class NumericalDataValue implements IData<Double> {

    private Double value;
    private double maxValue;
    private double minValue;

    private List<ChangeListener<? super Double>> changeListeners;
    private List<InvalidationListener> invalidationListeners;

    /**
     * Value of the NumericalDataValue is set to 0 by default
     */
    protected NumericalDataValue() {
        this(0.0, Double.MAX_VALUE - 1, Double.MIN_VALUE + 1);
    }

    /**
     * @param value of the NumericalDataValue
     */
    public NumericalDataValue(Double value) {
        this(value, Double.MAX_VALUE - 1, Double.MIN_VALUE + 1);
    }

    /**
     * Value of NumericalDataValue is set to 0 by default.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NumericalDataValue(double maxValue, double minValue) {
        this(0.0, maxValue, minValue);
    }

    /**
     * Value of NumericalDataValue is set to 0 by default.
     * @param value    of the NumericalDataValue.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NumericalDataValue(Double value, double maxValue, double minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;

        changeListeners = new ArrayList<>();
        invalidationListeners = new ArrayList<>();

        set(value);
    }

    /**
     *
     * @returns the formatted data stored within this dataValue
     */
    public Double getValue() {
        return this.value;
    }

    public Double getValue(double normalMin, double normalMax){
        double normVal = this.value;
        normVal = normVal % normalMax;

        if (normVal < normalMin)
            normVal += normalMax;

        return normVal;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    /**
     *
     * @return string representation of this dataValue
     */
    public String toString() {
        return "" + this.value;
    }

    /**
     * This is only for testing purposes and breaks encapsulation
     * so should never be utilized in real builds
     *
     * @param delta the amount added to the preexisting value
     */
    @Deprecated
    public void changeBy(double delta) { //TODO: should probably remove but eh?
        set(value + delta);
    }

    protected void set(Double value) {
        double oldVal = value;
        Double newVal = Math.min(maxValue, value);
        newVal = Math.max(minValue, newVal);
        this.value = newVal;
        notifyChangeListeners(oldVal);
        notifyInvalidationListeners();

    }


    //Listeners:
    private void notifyChangeListeners(double oldValue) {
        for (ChangeListener<? super Double> changeListener : changeListeners) {
            changeListener.changed(this, oldValue, this.value);
        }
    }

    private void notifyInvalidationListeners() {
        for (InvalidationListener changeListener : invalidationListeners) {
            changeListener.invalidated(this);
        }
    }

    @Override
    public void addListener(ChangeListener<? super Double> listener) {
        changeListeners.add(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Double> listener) {
        removeListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }
}
