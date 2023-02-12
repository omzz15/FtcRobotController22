package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Hashtable;

import om.self.ezftc.utils.hardware.motor.MotorSettings;

public class OdometryHardware {
    public final DcMotor leftYWheel;
    public final DcMotor rightYWheel;
    public final DcMotor XWheel;

    public OdometryHardware(DcMotor leftYWheel, DcMotor rightYWheel, DcMotor XWheel) {
        this.leftYWheel = leftYWheel;
        this.rightYWheel = rightYWheel;
        this.XWheel = XWheel;
    }

    public static OdometryHardware makeForOdoBot(HardwareMap map){
        return new OdometryHardware(
                new MotorSettings(MotorSettings.Number.TWO_B).makeMotor(map),
                new MotorSettings(MotorSettings.Number.ONE_B).makeMotor(map),
                new MotorSettings(MotorSettings.Number.ZERO_B).makeMotor(map)
        );
    }
}
