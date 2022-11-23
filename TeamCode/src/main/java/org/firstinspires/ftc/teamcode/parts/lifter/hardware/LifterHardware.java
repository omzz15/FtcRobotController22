package org.firstinspires.ftc.teamcode.parts.lifter.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import om.self.ezftc.utils.hardware.motor.MotorSettings;
import om.self.ezftc.utils.hardware.servo.ServoSettings;

public class LifterHardware {
    public final DcMotor leftLiftMotor;
    public final DcMotor rightLiftMotor;

    public final Servo leftTurnServo;
    public final Servo rightTurnServo;
    public final Servo grabServo;

    public LifterHardware(DcMotor leftLiftMotor, DcMotor rightLiftMotor, Servo leftTurnServo, Servo rightTurnServo, Servo grabServo) {
        this.leftLiftMotor = leftLiftMotor;
        this.rightLiftMotor = rightLiftMotor;
        this.leftTurnServo = leftTurnServo;
        this.rightTurnServo = rightTurnServo;
        this.grabServo = grabServo;
    }

    public static LifterHardware makeDefault(HardwareMap hardwareMap){
        final double liftHoldPower = 0.5;

        MotorSettings leftMotorSettings = new MotorSettings(MotorSettings.Number.ZERO_B, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);
        MotorSettings rightMotorSettings = new MotorSettings(MotorSettings.Number.ONE_B, DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);
        ServoSettings leftServoSettings = new ServoSettings(ServoSettings.Number.ZERO, Servo.Direction.FORWARD);
        ServoSettings rightServoSettings = new ServoSettings(ServoSettings.Number.TWO, Servo.Direction.REVERSE);
        ServoSettings grabServoSettings = new ServoSettings(ServoSettings.Number.FOUR, Servo.Direction.FORWARD);

        return new LifterHardware(
                leftMotorSettings.makeMotor(hardwareMap),
                rightMotorSettings.makeMotor(hardwareMap),
                leftServoSettings.makeServo(hardwareMap),
                rightServoSettings.makeServo(hardwareMap),
                grabServoSettings.makeServo(hardwareMap)
        );
    }
}
