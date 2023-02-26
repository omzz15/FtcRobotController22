package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import om.self.ezftc.utils.Vector3;

public class OdometrySettings {
    public final double ticksPerInch;
    public final double ticksPerRotation;
    public final Vector3 robotOffset;

    public OdometrySettings(double ticksPerInch, double ticksPerRotation, Vector3 robotOffset) {
        this.ticksPerInch = ticksPerInch;
        this.ticksPerRotation = ticksPerRotation;
        this.robotOffset = robotOffset;
    }

    public static OdometrySettings makeForOdoBot(){
        return new OdometrySettings(
                (222965+222523+222522)/(117.5*3), //82300 / 48.0,
                (449412+464214+458163+451268)/16, //169619,
                new Vector3(2.25, 0,0)
        );
    }
}
