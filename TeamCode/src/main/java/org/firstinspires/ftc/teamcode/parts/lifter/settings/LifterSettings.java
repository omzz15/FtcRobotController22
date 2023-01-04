package org.firstinspires.ftc.teamcode.parts.lifter.settings;

public class LifterSettings {
    //turn servos
    public final double turnServoMinPosition;
    public final double turnServoMaxPosition;
    public final double turnServoStartPosition;
    public double rightTurnServoOffset; //TODO return to final
    //grabber servo
    public final double grabberServoOpenPos;
    public final double grabberServoWideOpenPos;
    public final double grabberServoClosePos;
    //motor
    public final double minRegisterVal;
    public final int maxDownLiftSpeed;
    public final int maxUpLiftSpeed;
    public final int minLiftPosition;
    public final int maxLiftPosition;
    public final int tolerance;

    public final boolean useOldGrabber;

    public LifterSettings(double turnServoMinPosition, double turnServoMaxPosition, double turnServoStartPosition, double grabberServoOpenPos, double grabberServoWideOpenPos, double grabberServoClosePos, double minRegisterVal, int maxDownLiftSpeed, int maxUpLiftSpeed, int minLiftPosition, int maxLiftPosition, int tolerance, boolean useOldGrabber) {
        this.turnServoMinPosition = turnServoMinPosition;
        this.turnServoMaxPosition = turnServoMaxPosition;
        this.turnServoStartPosition = turnServoStartPosition;
        this.grabberServoOpenPos = grabberServoOpenPos;
        this.grabberServoWideOpenPos = grabberServoWideOpenPos;
        this.grabberServoClosePos = grabberServoClosePos;
        this.minRegisterVal = minRegisterVal;
        this.maxDownLiftSpeed = maxDownLiftSpeed;
        this.maxUpLiftSpeed = maxUpLiftSpeed;
        this.minLiftPosition = minLiftPosition;
        this.maxLiftPosition = maxLiftPosition;
        this.tolerance = tolerance;
        this.useOldGrabber = useOldGrabber;
    }

    public static LifterSettings makeDefault(){
        return new LifterSettings(
                0,
                1,
                0.75,
                .9,
                0.834,
                1,
                0.05,
                150,
                150,
                0,
                3200,
                20,
                true
        );
    }
}
