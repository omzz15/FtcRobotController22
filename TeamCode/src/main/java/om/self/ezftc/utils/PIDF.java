package om.self.ezftc.utils;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/**
 * A simple PIDF controller. (mainly used for motors)
 * <br>
 * Note: all ranges are disabled by default and can be enabled by setting the {@link OptionalRange#enable} flag to true. The ranges can be set with {@link OptionalRange#setRange(Comparable, Comparable)}.
 */
public class PIDF
{
	/**
	 * The PIDF coefficients
	 */
	private PIDFCoefficients PIDFs = new PIDFCoefficients(0,0,0,0);
	/**
	 * The range of the error vales. (Any value outside of this range will be clamped to the min or max value)
	 */
	public final OptionalRange<Double> errorRange = new OptionalRange<>(-1., 1., false);
	/**
	 * The range of the output values. (Any value outside of this range will be clamped to the min or max value)
	 * <br>
	 * Note: This will not affect the integral term. use {@link #integralRange} for preventing integral windup.
	 */
	public final OptionalRange<Double> outputRange = new OptionalRange<>(-1., 1., false);
	/**
	 * What is the maximum value the total error term can be. (this is to prevent integral windup)
	 * <br>
	 * Note: To calculate the total influence of the integral term on the output, multiply this range by the integral coefficient.
	 */
	public final OptionalRange<Double> integralRange = new OptionalRange<>(-1., 1., false);
	/**
	 * This will reset the integral term (set total error to 0) when the sign of the error changes.
	 */
	public boolean resetIntegralOnSignChange = false;

	/**
	 * What is the target value of the PIDF controller
	 */
	private double setPoint = 0;
	/**
	 * The error last time {@link #calculate(double)} was called (raw value)
	 * <br>
	 * Note: This value will be NaN if {@link #calculate(double)} has not been called yet.
	 */
	private double error = Double.NaN;
	/**
	 * The error velocity (change in error over time) last time {@link #calculate(double)} was called
	 */
	private double errorVel = 0;
	/**
	 * The total error (sum of all errors) last time {@link #calculate(double)} was called
	 */
	private double totalError = 0;
	/**
	 * The last error, used to calculate the error velocity and total error.
	 * <br>
	 * Note: This value will be NaN if {@link #calculate(double)} has not been called yet.
	 */
	private double lastError = Double.NaN;
	/**
	 * The last time {@link #calculate(double)} was called
	 * <br>
	 * Note: This value will be NaN if {@link #calculate(double)} has not been called yet.
	 */
	private double lastTime = Double.NaN;
	/**
	 * The time between the last two calls to {@link #calculate(double)}
	 */
	private double period = 0;
	/**
	 * The output of the PIDF controller (raw value)
	 */
	private double output = 0;

	/**
	 * Creates a new PIDF controller with the default values
	 */
	public PIDF() {
	}

	/**
	 * Creates a new PIDF controller with the given PIDF coefficients
	 * @param PIDFs the PIDF coefficients
	 */
	public PIDF(PIDFCoefficients PIDFs) {
		setPIDFs(PIDFs);
	}

	/**
	 * Creates a new PIDF controller with the given PIDF coefficients and set point
	 * @param PIDFs the PIDF coefficients
	 * @param setPoint the set point
	 */
	public PIDF(PIDFCoefficients PIDFs, double setPoint) {
		this(PIDFs);
		setSetPoint(setPoint);
	}

	/**
	 * Creates a new PIDF controller with the given PIDF coefficients, set point, and error range
	 * @param PIDFs the PIDF coefficients
	 * @param setPoint the set point
	 * @param errorRange the error range
	 */
	public PIDF(PIDFCoefficients PIDFs, double setPoint, Range<Double> errorRange) {
		this(PIDFs);
		setSetPoint(setPoint);
		this.errorRange.setRange(errorRange.getMin(), errorRange.getMax(), true);
	}

	/**
	 * Creates a new PIDF controller with the given PIDF coefficients, set point, error range, output range, and integral range
	 * @param PIDFs the PIDF coefficients
	 * @param setPoint the set point
	 * @param errorRange the error range
	 * @param outputRange the output range
	 * @param integralRange the integral range
	 */
	public PIDF(PIDFCoefficients PIDFs, double setPoint, Range<Double> errorRange, Range<Double> outputRange, Range<Double> integralRange) {
		this(PIDFs, setPoint, errorRange);
		this.outputRange.setRange(outputRange.getMin(), outputRange.getMax(), true);
		this.integralRange.setRange(integralRange.getMin(), integralRange.getMax(), true);
	}

	/**
	 * Returns the PIDF coefficients
	 * @return the PIDF coefficients
	 */
	public PIDFCoefficients getPIDFs() {
		return PIDFs;
	}

	/**
	 * Sets the PIDF coefficients
	 * @param PIDFs the PIDF coefficients
	 * @throws IllegalArgumentException if the argument PIDFs is null
	 */
	public void setPIDFs(PIDFCoefficients PIDFs) {
		if (PIDFs == null)
			throw new IllegalArgumentException("The argument PIDFs can not be null!");
		this.PIDFs = PIDFs;
	}

	/**
	 * Returns the error
	 * <br>
	 * Note: This will be NaN if {@link #calculate(double)} has not been called yet or {@link #reset()} was just called.
	 * @return the error (not limited by the error range)
	 */
	public double getRawError() {
		return error;
	}

	/**
	 * Returns the error
	 * <br>
	 * IMPORTANT: error will be NaN if {@link #calculate(double)} has not been called yet or {@link #reset()} was just called which will cause this method to throw an exception so call this only AFTER {@link #calculate(double)} has been called.
	 * @return the error (limited by the error range)
	 */
	public double getError() {
		return errorRange.limit(error);
	}

	/**
	 * Returns the error velocity
	 * @return the error velocity
	 */
	public double getErrorVel() {
		return errorVel;
	}

	/**
	 * Returns the total error
	 * @return the total error
	 */
	public double getTotalError() {
		return totalError;
	}

	/**
	 * Returns the last error
	 * <br>
	 * Note: This will be NaN if {@link #calculate(double)} has not been called yet or {@link #reset()} was just called.
	 * @return the last error (limited by the error range)
	 */
	public double getLastError() {
		return lastError;
	}

	/**
	 * Returns the last time {@link #calculate(double)} was called
	 * <br>
	 * Note: This will be NaN if {@link #calculate(double)} has not been called yet or {@link #reset()} was just called.
	 * @return the last time {@link #calculate(double)} was called (in seconds with nano second precision)
	 */
	public double getLastTime() {
		return lastTime;
	}

	/**
	 * Returns the time between the last two calls to {@link #calculate(double)}
	 * <br>
	 * Note: This will be 0 if {@link #calculate(double)} has not been called yet. or after {@link #reset()} and {@link #calculate(double)} are called.
	 * @return the time between the last two calls to {@link #calculate(double)} (in seconds with nano second precision)
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 * Returns the output of the PIDF controller
	 * @return the output of the PIDF controller (limited by the output range)
	 */
	public double getOutput() {
		return outputRange.limit(output);
	}

	/**
	 * Returns the raw output of the PIDF controller
	 * @return the raw output of the PIDF controller (not limited by the output range)
	 */
	public double getRawOutput() {
		return output;
	}

	/**
	 * Resets the PIDF controller to the default values (what they were when the object was created)
	 */
	public void reset(){
		lastError = Double.NaN;
		lastTime = Double.NaN;
		error = Double.NaN;
		errorVel = 0;
		totalError = 0;
	}

	/**
	 * Sets the set point of the PIDF controller and resets everything if reset is true.
	 * @param setPoint the set point
	 * @param reset whether to reset the PIDF controller or not
	 */
	public void setSetPoint(double setPoint, boolean reset){
		this.setPoint = setPoint;
		if(reset)
			reset();
	}

	/**
	 * Sets the set point of the PIDF controller without resetting everything.
	 * @param setPoint the set point
	 */
	public void setSetPoint(double setPoint){
		setSetPoint(setPoint, false);
	}

	/**
	 * Calculates all the parameters, including output, of the PIDF controller based on the input and other values.
	 * @param input the input value (the value trying to reach the set point)
	 */
	public void calculate(double input){
		error = setPoint - input;
		double error = getError();
		double time = System.nanoTime() * Constants.secondsPerNanoSecond;

		if(Double.isNaN(lastTime)){
			period = 0;
		} else {
			period = time - lastTime;
			errorVel = (error - lastError) / period;
			if (resetIntegralOnSignChange && Math.signum(error) != Math.signum(lastError))
				totalError = 0;
		}

		totalError = integralRange.limit(totalError + error * period);

		output = PIDFs.p * error + PIDFs.i * totalError + PIDFs.d * errorVel + PIDFs.f * setPoint;

		lastTime = time;
		lastError = error;
	}

	/**
	 * calls {@link #calculate(double)} and returns the output
	 * @param input the input value (the value trying to reach the set point)
	 * @return the output of the PIDF controller (limited by the output range)
	 */
	public double calculateAndReturn(double input){
		calculate(input);
		return getOutput();
	}
}