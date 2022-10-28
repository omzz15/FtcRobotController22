package org.firstinspires.ftc.teamcode.parts.movement;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class TurnToAngSettings {
	double tol;
	int timesInTol;
	int maxRuntime;
	double maxPower;
	PIDCoefficients turnPID;

	TurnToAngSettings() {
	}

	TurnToAngSettings(double tol, int timesInTol, int maxRuntime, double maxPower) {
		this.tol = tol;
		this.timesInTol = timesInTol;
		this.maxRuntime = maxRuntime;
		this.maxPower = maxPower;
	}

	TurnToAngSettings(double tol, int timesInTol, int maxRuntime, double maxPower, PIDCoefficients turnPID) {
		this.tol = tol;
		this.timesInTol = timesInTol;
		this.maxRuntime = maxRuntime;
		this.maxPower = maxPower;
		this.turnPID = turnPID;
	}

	boolean isPIDValid() {
		return turnPID != null;
	}
}