package org.firstinspires.ftc.teamcode.parts.lifter.hardware;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import om.self.ezftc.utils.hardware.motor.MotorFunction;
import om.self.ezftc.utils.hardware.motor.MotorSettings;
import om.self.ezftc.utils.hardware.servo.ServoSettings;

public class LifterHardware {
    public final DcMotor leftLiftMotor;
    public final DcMotor rightLiftMotor;

    public final Servo leftTurnServo;
    public final Servo rightTurnServo;
    //public final Servo grabServo;
    public final ColorRangeSensor leftRange;
    public final ColorRangeSensor rightRange;
    public final DistanceSensor leftDistance;
    public final DistanceSensor rightDistance;

    public final CRServo leftGrabServo;
    public final CRServo rightGrabServo;



    public static LifterHardware makeDefault(HardwareMap hardwareMap){
        final double liftHoldPower = 0.7;

        MotorSettings leftMotorSettings = new MotorSettings(MotorSettings.Number.ZERO_B, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);
        MotorSettings rightMotorSettings = new MotorSettings(MotorSettings.Number.ONE_B, DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);
        ServoSettings leftServoSettings = new ServoSettings(ServoSettings.Number.ZERO, Servo.Direction.FORWARD);
        ServoSettings rightServoSettings = new ServoSettings(ServoSettings.Number.TWO, Servo.Direction.REVERSE);
        ServoSettings grabServoSettings = new ServoSettings(ServoSettings.Number.FOUR, Servo.Direction.FORWARD);
        ColorRangeSensor leftRange = hardwareMap.get(ColorRangeSensor.class, "range2");
        ColorRangeSensor rightRange = hardwareMap.get(ColorRangeSensor.class, "range1");
        DistanceSensor leftDistance = hardwareMap.get(DistanceSensor.class, "blueWallSensor");
        DistanceSensor rightDistance = hardwareMap.get(DistanceSensor.class, "redWallSensor");

        MotorSettings leftGrabServoSettings = new MotorSettings(ServoSettings.Number.ZERO_B, DcMotorSimple.Direction.REVERSE);
        MotorSettings rightGrabServoSettings = new MotorSettings(ServoSettings.Number.TWO_B, DcMotorSimple.Direction.FORWARD);

        //ServoSettings grabServoSettings = new ServoSettings(ServoSettings.Number.FOUR, Servo.Direction.FORWARD);

        return new LifterHardware(
                leftMotorSettings.makeMotor(hardwareMap),
                rightMotorSettings.makeMotor(hardwareMap),
                leftServoSettings.makeServo(hardwareMap),
                rightServoSettings.makeServo(hardwareMap),
                grabServoSettings.makeServo(hardwareMap),
                leftRange,
                rightRange,
                leftDistance,
                rightDistance
                //grabServoSettings.makeServo(hardwareMap)

                leftGrabServoSettings.makeCRServo(hardwareMap),
                rightGrabServoSettings.makeCRServo(hardwareMap)
        );
    }
}
