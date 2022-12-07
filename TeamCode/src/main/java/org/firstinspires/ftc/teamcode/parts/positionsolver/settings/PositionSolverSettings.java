
package org.firstinspires.ftc.teamcode.parts.positionsolver.settings;

public class PositionSolverSettings {
    private final ChannelSolverSettings xChannelSettings;
    private final ChannelSolverSettings yChannelSettings;
    private final ChannelSolverSettings rChannelSettings;

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
