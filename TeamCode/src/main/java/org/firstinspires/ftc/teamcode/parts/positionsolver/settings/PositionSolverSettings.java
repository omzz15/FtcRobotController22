
package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class PositionSolverSettings {
    public final ChannelSolverSettings xChannelSettings;
    public final ChannelSolverSettings yChannelSettings;
    public final ChannelSolverSettings rChannelSettings;

    public PositionSolverSettings(ChannelSolverSettings xChannelSettings, ChannelSolverSettings yChannelSettings, ChannelSolverSettings rChannelSettings) {
        this.xChannelSettings = xChannelSettings;
        this.yChannelSettings = yChannelSettings;
        this.rChannelSettings = rChannelSettings;
    }

    public static PositionSolverSettings makeDefault(){
        return new PositionSolverSettings(
                new ChannelSolverSettings(0.5, 10, false, 10000, new PIDCoefficients(0.1, 0, 0)),
                new ChannelSolverSettings(0.5, 10, false, 10000, new PIDCoefficients(0.1, 0, 0)),
                new ChannelSolverSettings(5, 10, false, 10000, new PIDCoefficients(0.05, 0, 0))
        );
    }

}
