package om.self.ezftc.utils;

/**
 * An extension of the {@link Range} class that uses doubles and supports {@link #convertTo(Double)} and {@link #convertFrom(Double)
 */
public class RangeDouble extends Range<Double>{

    public RangeDouble(Double min, Double max) {
        super(min, max);
    }

    /**
     * creates a default range with a value of 0 to 1
     */
    public RangeDouble(){
        super(0.0,1.0);
    }

    /**
     * converts a value to unit value
     * @param val a value in range min - max
     * @return a value in range 0 - 1
     * @throws ArithmeticException if the min and max are the same value
     */
    @Override
    public Double convertTo(Double val){
        return (val - min)/(max - min);
    }

    /**
     * converts a unit value to value
     * @param val a value in range 0 - 1. This will work with values outside of this range but the result will be outside of the range min - max
     * @return a value in range min - max
     */
    @Override
    public Double convertFrom(Double val){
        return val * (max - min) + min;
    }
}
