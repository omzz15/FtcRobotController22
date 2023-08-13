package om.self.ezftc.utils;

/**
 * An extension of the {@link Range} class that uses integers and supports {@link #convertTo(Integer)} and {@link #convertFrom(Integer)}
 */
public class RangeInt extends Range<Integer>{

    public RangeInt(Integer min, Integer max) {
        super(min, max);
    }

    /**
     * converts a value to unit value
     * @param val a value in range min - max
     * @return a value in range 0 - 1
     * @throws ArithmeticException if the min and max are the same value
     */
    @Override
    public Integer convertTo(Integer val){
        return (val - min)/(max - min);
    }

    /**
     * converts a unit value to value
     * @param val a value in range 0 - 1. This will work with values outside of this range but the result will be outside of the range min - max
     * @return a value in range min - max
     */
    @Override
    public Integer convertFrom(Integer val){
        return val * (max - min) + min;
    }
}
