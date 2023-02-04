
package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class PositionSolverSettings {
    public final SolverSettings xChannelSettings;
    public final SolverSettings yChannelSettings;
    public final SolverSettings rChannelSettings;

    public PositionSolverSettings(SolverSettings xChannelSettings, SolverSettings yChannelSettings, SolverSettings rChannelSettings) {
        this.xChannelSettings = xChannelSettings;
        this.yChannelSettings = yChannelSettings;
        this.rChannelSettings = rChannelSettings;
    }

    public static PositionSolverSettings makeDefault(){
        return new PositionSolverSettings(
                new SolverSettings(2, 10, true, 10000, new PIDCoefficients(0.05, 0, 0), 1),
                new SolverSettings(2, 10, true, 10000, new PIDCoefficients(0.05, 0, 0), 1),
                new SolverSettings(5, 10, true, 10000, new PIDCoefficients(0.025, 0, 0), 1)
        );
    }

}
