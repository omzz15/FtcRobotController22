package org.firstinspires.ftc.teamcode.parts.lifter2.settings;

public class LifterSettings {
    //turn servos
    public final double turnAngleMin;
    public final double turnServoMin;
    public final double turnPotentiometerMin;
    public final double turnAngleMax;
    public final double turnServoMax;
    public final double turnPotentiometerMax;

    public final double rightTurnServoOffset;

    //grabber servo
    public final double grabberServoClosePos;
    public final double grabberServoOpenPos;
    public final double grabberServoWideOpenPos;
    public final int servoCloseToOpenTime; //time in ms
    public final int servoOpenToWideOpenTime; //time in ms

    //motor
    public final int minLiftPosition;
    public final int maxLiftPosition;

    public final double liftHoldPower;
    public final int maxDownSpeed;
    public final int maxUpSpeed;
    public final int tolerance;

    //changed grabberservo open pos from .9 to .86 (wouldn't open all the way during autonomous test)


    public LifterSettings(double turnAngleMin, double turnServoMin, double turnPotentiometerMin, double turnAngleMax, double turnServoMax, double turnPotentiometerMax, double rightTurnServoOffset, double grabberServoClosePos, double grabberServoOpenPos, double grabberServoWideOpenPos, int servoCloseToOpenTime, int servoOpenToWideOpenTime, int minLiftPosition, int maxLiftPosition, double liftHoldPower, int maxDownSpeed, int maxUpSpeed, int tolerance) {
        this.turnAngleMin = turnAngleMin;
        this.turnServoMin = turnServoMin;
        this.turnPotentiometerMin = turnPotentiometerMin;
        this.turnAngleMax = turnAngleMax;
        this.turnServoMax = turnServoMax;
        this.turnPotentiometerMax = turnPotentiometerMax;
        this.rightTurnServoOffset = rightTurnServoOffset;
        this.grabberServoClosePos = grabberServoClosePos;
        this.grabberServoOpenPos = grabberServoOpenPos;
        this.grabberServoWideOpenPos = grabberServoWideOpenPos;
        this.servoCloseToOpenTime = servoCloseToOpenTime;
        this.servoOpenToWideOpenTime = servoOpenToWideOpenTime;
        this.minLiftPosition = minLiftPosition;
        this.maxLiftPosition = maxLiftPosition;
        this.liftHoldPower = liftHoldPower;
        this.maxDownSpeed = maxDownSpeed;
        this.maxUpSpeed = maxUpSpeed;
        this.tolerance = tolerance;
    }

    public static LifterSettings makeDefault(){
        return new LifterSettings(
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
    }
}
