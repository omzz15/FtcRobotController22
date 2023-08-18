package om.self.ezftc.utils;

/**
 * Like {@link Range} but has an {@link #enable} flag to determine if the range is enabled or not. (if {@link #limit(Comparable)} should limit the value or not)
 * @param <T>
 */
public class OptionalRange<T extends Comparable<T>> extends Range<T>{
    /**
     * This is a flag that will be used to determine if the range is enabled or not. If enabled, the {@link #limit(Comparable)} method will limit the value else it will just return it.
     */
    public boolean enable;

    public OptionalRange(T min, T max) {
        super(min, max);
    }

    public OptionalRange(T min, T max, boolean enable) {
        super(min, max);
        this.enable = enable;
    }

    /**
     * Sets the range and enables it.
     * @param min the minimum value of the range (inclusive)
     * @param max the maximum value of the range (inclusive)
     * @param enable whether the range should be enabled or not
     */
    public void setRange(T min, T max, boolean enable){
        setRange(min, max);
        this.enable = enable;
    }

    /**
     * Takes the val and limits it to be within the min and max values of the range if the {@link #enable} flag is true. Else it will just return the input value.
     *
     * @param val the value to limit
     * @return the constrained value if {@link #enable} is true else it will just return the input value.
     */
    @Override
    public T limit(T val) {
        if(enable)
            return super.limit(val);
        return val;
    }

    /**
     * Checks if the passed in value is within the range stored (inclusive).
     * <br>
     * Note: This will always return true if {@link #enable} is false, even if the value is not in range.
     *
     * @param val the value to check
     * @return whether the value is in range (inclusive of end points)
     */
    @Override
    public boolean isInLimit(T val) {
        return !enable || super.isInLimit(val);
    }
}
