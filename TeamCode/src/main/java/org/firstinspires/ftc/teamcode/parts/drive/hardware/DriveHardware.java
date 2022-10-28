package org.firstinspires.ftc.teamcode.parts.drive.hardware;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.hardware.motor.MotorSettings;

public class DriveHardware {
    ///////////
    //objects//
    ///////////
    public final DcMotor topLeftMotor;
    public final DcMotor topRightMotor;
    public final DcMotor bottomLeftMotor;
    public final DcMotor bottomRightMotor;

    public DriveHardware(Robot robot, MotorSettings topLeftMotorSettings, MotorSettings topRightMotorSettings, MotorSettings bottomLeftMotorSettings, MotorSettings bottomRightMotorSettings) {
        topLeftMotor = topLeftMotorSettings.makeMotor(robot.opMode.hardwareMap);
        topRightMotor = topRightMotorSettings.makeMotor(robot.opMode.hardwareMap);
        bottomLeftMotor = bottomLeftMotorSettings.makeMotor(robot.opMode.hardwareMap);
        bottomRightMotor = bottomRightMotorSettings.makeMotor(robot.opMode.hardwareMap);
    }

    public DriveHardware(DcMotor topLeftMotor, DcMotor topRightMotor, DcMotor bottomLeftMotor, DcMotor bottomRightMotor){
        this.topLeftMotor = topLeftMotor;
        this.topRightMotor = topRightMotor;
        this.bottomLeftMotor = bottomLeftMotor;
        this.bottomRightMotor = bottomRightMotor;
    }
}
