package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class ChannelSolverSettings {
    public final double tolerance;
    public final int timesInTolerance;
    public final boolean alwaysRun;
    public final int maxRuntime;
    public final PIDCoefficients PIDCoefficients;

    public ChannelSolverSettings(double tolerance, int timesInTolerance, boolean alwaysRun, int maxRuntime, com.qualcomm.robotcore.hardware.PIDCoefficients PIDCoefficients) {
        this.tolerance = tolerance;
        this.timesInTolerance = timesInTolerance;
        this.alwaysRun = alwaysRun;
        this.maxRuntime = maxRuntime;
        this.PIDCoefficients = PIDCoefficients;
    }
}
