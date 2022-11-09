package org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking;

public class EncoderTrackerSettings {
    public final int ticksPerInchSideways;
    public final int ticksPerInchForward;

    public EncoderTrackerSettings(int ticksPerInchSideways, int ticksPerInchForward) {
        this.ticksPerInchSideways = ticksPerInchSideways;
        this.ticksPerInchForward = ticksPerInchForward;
    }

    public static EncoderTrackerSettings makeDefault(){
        return new EncoderTrackerSettings(
                55,
                48
        );
    }
}
