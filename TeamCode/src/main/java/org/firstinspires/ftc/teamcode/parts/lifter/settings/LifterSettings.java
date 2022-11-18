package org.firstinspires.ftc.teamcode.parts.lifter.settings;

public class LifterSettings {
    //turn servos
    public final double turnServoMinPosition;
    public final double turnServoMaxPosition;
    public final double rightTurnServoOffset;
    //grabber servo
    public final double grabberServoOpenPos;
    public final double grabberServoClosePos;
    //motor
    public final double minRegisterVal;
    public final int maxDownLiftSpeed;
    public final int maxUpLiftSpeed;
    public final int minLiftPosition;
    public final int maxLiftPosition;

    public LifterSettings(double turnServoMinPosition, double turnServoMaxPosition, double rightTurnServoOffset, double grabberServoOpenPos, double grabberServoClosePos, double minRegisterVal, int maxDownLiftSpeed, int maxUpLiftSpeed, int minLiftPosition, int maxLiftPosition) {
        this.turnServoMinPosition = turnServoMinPosition;
        this.turnServoMaxPosition = turnServoMaxPosition;
        this.rightTurnServoOffset = rightTurnServoOffset;
        this.grabberServoOpenPos = grabberServoOpenPos;
        this.grabberServoClosePos = grabberServoClosePos;
        this.minRegisterVal = minRegisterVal;
        this.maxDownLiftSpeed = maxDownLiftSpeed;
        this.maxUpLiftSpeed = maxUpLiftSpeed;
        this.minLiftPosition = minLiftPosition;
        this.maxLiftPosition = maxLiftPosition;
    }

    public static LifterSettings makeDefault(){
        return new LifterSettings(
                0,
                1,
                0,
                1,
                0.88,
                0.05,
                4,
                8,
                0,
                1000
        );
    }
}
