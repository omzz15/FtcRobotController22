
package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

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
                null, null, null
        );
    }

}
