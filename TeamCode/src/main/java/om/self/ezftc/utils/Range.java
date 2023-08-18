package om.self.ezftc.utils;

/**
 * Simple class that stores a range and provides some utilities like limiting a value to the range or converting a value to a unit value and vice versa.
 * <br>
 * Note: This class does not support some actions such as {@link #convertTo(Comparable)} or {@link #convertFrom(Comparable)} so you may need to use an implementation such as {@link RangeDouble} or {@link RangeInt}
 * @param <T> the type of the range
 */
public class Range<T extends Comparable<T>>{
	T min;
	T max;

	/**
	 * creates a new range with the given min and max values (inclusive)
	 * @param min the minimum value of the range
	 * @param max the maximum value of the range
	 */
	public Range(T min, T max){
		setRange(min, max);
	}

	/**
	 * returns the minimum value of the range
	 * @return the minimum value of the range
	 */
	public T getMin() {
		return min;
	}

	/**
	 * sets the minimum value of the range
	 * @param min the minimum value of the range (inclusive)
	 */
	public void setMin(T min) {
		if (min == null)
			throw new IllegalArgumentException("The argument min can not be null!");
		this.min = min;
	}

	/**
	 * returns the maximum value of the range
	 * @return the maximum value of the range
	 */
	public T getMax() {
		return max;
	}

	/**
	 * sets the maximum value of the range
	 * @param max the maximum value of the range (inclusive)
	 */
	public void setMax(T max) {
		if (max == null)
			throw new IllegalArgumentException("The argument max can not be null!");
		this.max = max;
	}

	/**
	 * sets the range of the range
	 * @param min the minimum value of the range (inclusive)
	 * @param max the maximum value of the range (inclusive)
	 */
	public void setRange(T min, T max){
		setMin(min);
		setMax(max);
	}

	/**
	 * takes the val and limits it to be within the min and max values of the range
	 * @param val the value to limit
	 * @return the constrained value
	 */
	public T limit(T val){
		if(val.compareTo(min) < 0)
			return min;
		if(val.compareTo(max) > 0)
			return max;
		return val;
	}

	/**
	 * converts a value to unit value
	 * @param val a value in range min - max
	 * @return a value in range 0 - 1
	 * @throws UnsupportedOperationException if the type does not support this operation
	 */
	public T convertTo(T val){
		throw new UnsupportedOperationException("convertTo is not supported for this type");
	}

	/**
	 * converts a unit value to value
	 * @param val a value in range 0 - 1
	 * @return a value in range min - max
	 * @throws UnsupportedOperationException if the type does not support this operation
	 */
	public T convertFrom(T val){
		throw new UnsupportedOperationException("convertFrom is not supported for this type");
	}

	/**
	 * converts a value from the second Range range to an equivalent value in the first Range range
	 * @param val the value to convert in range min - max of second Range
	 * @param range2 the second Range for converting
	 * @return the value converted to the first end point space in range min - max
	 */
	public T doubleConvert(T val, Range<T> range2){
		return convertFrom(range2.convertTo(val));
	}

	/**
	 * checks if the passed in value is within the range stored (inclusive)
	 * @param val the value to check
	 * @return whether the value is in range (inclusive of end points)
	 */
	public boolean isInLimit(T val){
		return val.compareTo(min) >= 0 && val.compareTo(max) <= 0;
	}
}
