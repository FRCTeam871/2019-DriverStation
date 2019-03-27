package com.team871.util.data;

/**
 * @author TPfaffe-871
 * Contains a double value constrained by a max and min that is readOnly.
 */
public class NumericalDataValue extends DataValue<Double> {

    private double maxValue;
    private double minValue;

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
        super(value);
        this.maxValue = maxValue;
        this.minValue = minValue;

    }


    /**
     *
     * @param normalMin the minimum to normalize this value to.
     * @param normalMax the maximum to normalize this value to.
     * @return the normalized value
     */
    public Double getNormalizedValue(double normalMin, double normalMax){
        double normVal = getValue();
        normVal = normVal % normalMax;

        if (normVal < normalMin)
            normVal += normalMax;

        return normVal;
    }

    /**
     *
     * @return the maximum value this data is allowed to be.
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     *
     * @return the smallest value this data is allowed to be.
     */
    public double getMinValue() {
        return minValue;
    }

    @Override
    protected boolean setValue(Double value) {
        double newVal = Math.min(maxValue, value);
        newVal = Math.max(minValue, newVal);
        return super.setValue(newVal);
    }
}
