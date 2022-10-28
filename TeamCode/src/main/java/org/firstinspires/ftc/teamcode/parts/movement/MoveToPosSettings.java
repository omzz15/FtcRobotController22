package org.firstinspires.ftc.teamcode.parts.movement;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class MoveToPosSettings
{
	public double[] tol;
	public int timesInTol;
	public int maxRuntime;
	public double maxPower;
	public PIDCoefficients turnPID = null;
	public PIDCoefficients xPID = null;
	public PIDCoefficients yPID = null;

	public MoveToPosSettings(){}
	public MoveToPosSettings(double[] tol, int timesInTol, int maxRuntime, double maxPower)
	{
		this.tol = tol;
		this.timesInTol = timesInTol;
		this.maxRuntime = maxRuntime;
		this.maxPower = maxPower;
	}

	public MoveToPosSettings(double[] tol, int timesInTol, int maxRuntime, double maxPower, PIDCoefficients xPID, PIDCoefficients yPID, PIDCoefficients turnPID)
	{
		this.tol = tol;
		this.timesInTol = timesInTol;
		this.maxRuntime = maxRuntime;
		this.maxPower = maxPower;
		this.xPID = xPID;
		this.yPID = yPID;
		this.turnPID = turnPID;
	}

	public boolean isPIDValid()
	{
		return turnPID != null && xPID != null && yPID != null;
	}


	TurnToAngSettings toRotAngleSettings()
	{
		if(turnPID != null) return new TurnToAngSettings(tol[2], timesInTol, maxRuntime, maxPower, turnPID);
		return new TurnToAngSettings(tol[2], timesInTol, maxRuntime, maxPower);
	}

	public MoveToPosSettings withPower(double power){
		return new MoveToPosSettings(tol, timesInTol, maxRuntime, power, xPID, yPID, turnPID);
	}

	public MoveToPosSettings withTime(int time){
		return new MoveToPosSettings(tol, timesInTol, time, maxPower, xPID, yPID, turnPID);
	}
}