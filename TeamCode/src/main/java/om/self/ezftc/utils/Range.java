package om.self.ezftc.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import om.self.task.core.GroupEx;

public class Range {
	double min;
	double max;

	public Range(double min, double max){
		this.min = min;
		this.max = max;
	}

	public double cap(double val){
			return (val < min) ? min : (val > max) ? max : val;
	}

	/**
	 * converts a value to unit value
	 * @param val a value in range min - max
	 * @return a value in range 0 - 1
	 */
	public double convertTo(double val){
		return (val - min)/(max - min);
	}

	/**
	 * converts a unit value to value
	 * @param val a value in range 0 - 1
	 * @return a value in range min - max
	 */
	public double convertFrom(double val){
		return val * (max - min) + min;
	}

	/**
	 * converts a value from the second Range range to an equivalent value in the first Range range
	 * @param val the value to convert in range min - max of second Range
	 * @param range2 the second Range for converting
	 * @return the value converted to the first end point space in range min - max
	 */
	public double doubleConvert(double val, Range range2){
		return convertFrom(range2.convertTo(val));
	}

	public static boolean isInLimit(double val, double min, double max){
		return min < val && val < max;
	}

	public static boolean isInLimit(int val, int min, int max){
		return min < val && val < max;
	}

	public static double limit(double val, double min, double max){
		return Math.min(Math.max(val, min), max);
	}

	public static int limit(int val, int min, int max){
		return Math.min(Math.max(val, min), max);
	}

	public boolean isInLimit(double val){
		return isInLimit(val, min, max);
	}
}
