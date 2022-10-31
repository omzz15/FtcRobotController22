package org.firstinspires.ftc.teamcode.parts.drive.settings;

import com.qualcomm.robotcore.hardware.Gamepad;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.LatchedModifier;

public class DefaultDriveSettings{
    ////////////
    //settings//
    ////////////
    public static DriveSettings make(Robot robot){
        return new DriveSettings(
                () -> robot.opMode.gamepad1.x, //drive stop
                DriveSettings.DriveMode.MECANUM,
                new Vector3(0.1,0.1,0.1), //smoothing
                0.6 //slow mode speed
        );
    }
}
