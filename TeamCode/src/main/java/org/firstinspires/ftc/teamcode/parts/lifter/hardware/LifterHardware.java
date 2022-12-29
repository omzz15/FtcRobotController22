package org.firstinspires.ftc.teamcode.parts.lifter.hardware;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.lib.DFR304Range;

import om.self.ezftc.utils.hardware.motor.MotorFunction;
import om.self.ezftc.utils.hardware.motor.MotorSettings;
import om.self.ezftc.utils.hardware.servo.ServoSettings;

public class LifterHardware {
    public final DcMotor leftLiftMotor;
    public final DcMotor rightLiftMotor;
    public final Servo leftTurnServo;
    public final Servo rightTurnServo;
    public final Servo grabServo;
    //public final ColorRangeSensor leftRange;
    //public final ColorRangeSensor rightRange;
    //public final DistanceSensor leftDistance;
    //public final DistanceSensor rightDistance;
    public final DFR304Range leftUltrasonic;
    public final DFR304Range rightUltrasonic;
    public final DFR304Range midUltrasonic;
    public final RevBlinkinLedDriver blinkin;

    public LifterHardware(DcMotor leftLiftMotor, DcMotor rightLiftMotor, Servo leftTurnServo,
                          Servo rightTurnServo, Servo grabServo, ColorRangeSensor leftRange,
                          ColorRangeSensor rightRange, DistanceSensor leftDistance,
                          DistanceSensor rightDistance, DFR304Range leftUltrasonic,
                          DFR304Range rightUltrasonic, DFR304Range midUltrasonic,
                          RevBlinkinLedDriver blinkin) {
        this.leftLiftMotor = leftLiftMotor;
        this.rightLiftMotor = rightLiftMotor;
        this.leftTurnServo = leftTurnServo;
        this.rightTurnServo = rightTurnServo;
        this.grabServo = grabServo;
        //this.leftRange = leftRange;
        //this.rightRange = rightRange;
        //this.leftDistance = leftDistance;
        //this.rightDistance = rightDistance;
        this.leftUltrasonic = leftUltrasonic;
        this.rightUltrasonic = rightUltrasonic;
        this.midUltrasonic = midUltrasonic;
        this.blinkin = blinkin;
    }

    public static LifterHardware makeDefault(HardwareMap hardwareMap){
        final double liftHoldPower = 0.7;

        MotorSettings leftMotorSettings = new MotorSettings(MotorSettings.Number.ZERO_B, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);
        MotorSettings rightMotorSettings = new MotorSettings(MotorSettings.Number.ONE_B, DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_TO_POSITION, liftHoldPower);

        ServoSettings leftServoSettings = new ServoSettings(ServoSettings.Number.ZERO_B, Servo.Direction.FORWARD);
        ServoSettings rightServoSettings = new ServoSettings(ServoSettings.Number.TWO_B, Servo.Direction.REVERSE);

        ServoSettings grabServoSettings = new ServoSettings(ServoSettings.Number.FOUR_B, Servo.Direction.FORWARD);
        //ColorRangeSensor leftRange = hardwareMap.get(ColorRangeSensor.class, "range2");
        //ColorRangeSensor rightRange = hardwareMap.get(ColorRangeSensor.class, "range1");
        //DistanceSensor leftDistance = hardwareMap.get(DistanceSensor.class, "blueWallSensor");
        //DistanceSensor rightDistance = hardwareMap.get(DistanceSensor.class, "redWallSensor");
        DFR304Range rightUltraDist = hardwareMap.get(DFR304Range.class, "rightUltraSensor");
        DFR304Range leftUltraDist = hardwareMap.get(DFR304Range.class, "leftUltraSensor");
        DFR304Range midUltraDist = hardwareMap.get(DFR304Range.class, "middleUltraSensor");
        RevBlinkinLedDriver blinkin = hardwareMap.get(RevBlinkinLedDriver.class,"blinkin");
        DFR304Range.Parameters parameters = new DFR304Range.Parameters();
        parameters.maxRange = DFR304Range.MaxRange.CM150;
        parameters.measureMode = DFR304Range.MeasureMode.PASSIVE;
        leftUltraDist.initialize(parameters);
        rightUltraDist.initialize(parameters);
        midUltraDist.initialize(parameters);

        return new LifterHardware(
                leftMotorSettings.makeMotor(hardwareMap),
                rightMotorSettings.makeMotor(hardwareMap),
                leftServoSettings.makeServo(hardwareMap),
                rightServoSettings.makeServo(hardwareMap),
                grabServoSettings.makeServo(hardwareMap),
                //leftGrabServoSettings.makeCRServo(hardwareMap),
                //rightGrabServoSettings.makeCRServo(hardwareMap),
                null,//leftRange,
                null,//rightRange,
                null,//leftDistance,
                null,//rightDistance,
                leftUltraDist,//leftUltraDist
                rightUltraDist, //rightUltraDist
                midUltraDist,
                blinkin
        );
    }
}
