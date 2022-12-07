
package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

import om.self.ezftc.utils.Vector3;

public class PositionSolverSettings {
    public final Vector3 tolerance;
    public final int timesInTolerance;
    public final boolean alwaysRun;
    public final int maxRuntime;
    public final PIDCoefficients xPIDCoefficients;
    public final PIDCoefficients yPIDCoefficients;
    public final PIDCoefficients rPIDCoefficients;

    public PositionSolverSettings(Vector3 tolerance, int timesInTolerance, boolean alwaysRun, int maxRuntime, PIDCoefficients xPIDCoefficients, PIDCoefficients yPIDCoefficients, PIDCoefficients rPIDCoefficients) {
        this.tolerance = tolerance;
        this.timesInTolerance = timesInTolerance;
        this.alwaysRun = alwaysRun;
        this.maxRuntime = maxRuntime;
        this.xPIDCoefficients = xPIDCoefficients;
        this.yPIDCoefficients = yPIDCoefficients;
        this.rPIDCoefficients = rPIDCoefficients;
    }

    public static PositionSolverSettings makeDefault(){
        return new PositionSolverSettings(
                new Vector3(1,1,10),
                10,
                true,
                10000,
                new PIDCoefficients(0,0,0),
                new PIDCoefficients(0,0,0),
                new PIDCoefficients(0,0,0)
        );
    }
}
