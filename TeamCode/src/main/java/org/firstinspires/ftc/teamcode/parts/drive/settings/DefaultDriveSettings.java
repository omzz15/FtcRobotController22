package org.firstinspires.ftc.teamcode.parts.drive.settings;

import com.qualcomm.robotcore.hardware.Gamepad;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.LatchedModifier;

public class DefaultDriveSettings{
    ////////////
    //settings//
    ////////////
    //input
    private static boolean useFirstGamepad = true;


    public static DriveSettings make(Robot robot){
        Gamepad selectedGamepad = useFirstGamepad ? robot.opMode.gamepad1 : robot.opMode.gamepad2;

        return new DriveSettings(
                () -> (double)selectedGamepad.left_stick_x, //drive x
                () -> (double)selectedGamepad.left_stick_y, //drive y
                () -> (double)selectedGamepad.right_stick_x, // drive r
                () -> selectedGamepad.x, //drive stop
                new LatchedModifier().toSupplier(() -> selectedGamepad.b), // slow mode
                DriveSettings.DriveMode.MECANUM,
                new Vector3(0.1,0.1,0.1),
                0.6
        );
    }
}
