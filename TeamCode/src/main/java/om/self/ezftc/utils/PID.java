package om.self.ezftc.utils;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class PID
{
	PIDCoefficients PIDs;
	double maxClamp;
	double minClamp;
	double value;

	double totalError;
	double lastError;
	double currentError;
	long lastTime;

	public PID(){}
	public PID(PIDCoefficients PIDs, double minClamp, double maxClamp)
	{
		this.PIDs = PIDs;
		this.minClamp = minClamp;
		this.maxClamp = maxClamp;
	}

	public void updatePID(double error)
	{
		lastError = currentError;
		currentError = error;
		totalError += error;

		double calculatedI = (totalError * PIDs.i);
		if(calculatedI > maxClamp) totalError = maxClamp/PIDs.i;
		else if(calculatedI < minClamp) totalError = minClamp/ PIDs.i;

		double calculatedD = (((currentError - lastError) * PIDs.d) / ((double)(System.nanoTime() - lastTime) / (double) 1000000000));

		value = (error * PIDs.p) + calculatedI - calculatedD;

		lastTime = System.nanoTime();
	}

	public void resetErrors()
	{
		totalError = 0;
		lastError = 0;
		currentError = 0;
	}

	public double updatePIDAndReturnValue(double error)
	{
		updatePID(error);
		return returnValue();
	}

	public double returnValue()
	{
		return Math.min(Math.max(value, minClamp), maxClamp);
	}

	public double returnUncappedValue()
	{
		return value;
	}
}