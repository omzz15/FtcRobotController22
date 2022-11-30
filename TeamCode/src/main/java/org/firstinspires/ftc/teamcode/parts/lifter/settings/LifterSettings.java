package org.firstinspires.ftc.teamcode.parts.lifter.settings;

public class LifterSettings {
    //turn servos
    public final double turnServoMinPosition;
    public final double turnServoMaxPosition;
    public double rightTurnServoOffset; //TODO return to final
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
                .2,
                1,
                0,
                .90,
                0.88,
                0.05,
                64,
                64,
                0,
                3200
        );
    }
}
