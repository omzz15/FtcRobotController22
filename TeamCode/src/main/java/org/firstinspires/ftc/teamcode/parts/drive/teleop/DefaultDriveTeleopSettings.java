package org.firstinspires.ftc.teamcode.parts.drive.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import om.self.ezftc.core.Robot;
import om.self.supplier.modifiers.LatchedModifier;

public class DefaultDriveTeleopSettings {
    public static boolean useFirstGamepad = true;


    public static DriveTeleopSettings make(Robot robot){
        Gamepad selectedGamepad = useFirstGamepad ? robot.opMode.gamepad1 : robot.opMode.gamepad2;

        return new DriveTeleopSettings(
                () -> (double)selectedGamepad.left_stick_x, //drive x
                () -> (double)selectedGamepad.left_stick_y, //drive y
                () -> (double)selectedGamepad.right_stick_x,// drive r
                new LatchedModifier().toSupplier(() -> selectedGamepad.b) // slow mode
        );
    }
}
