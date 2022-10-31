package org.firstinspires.ftc.teamcode.parts.drive;

import org.firstinspires.ftc.teamcode.parts.drive.hardware.DefaultDriveHardware;
import org.firstinspires.ftc.teamcode.parts.drive.hardware.DriveHardware;
import org.firstinspires.ftc.teamcode.parts.drive.settings.DefaultDriveSettings;
import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveSettings;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.RobotPart;

public class Drive extends RobotPart<DriveSettings, DriveHardware> {



    public Drive(Robot robot){
        super(robot, "drive", DefaultDriveSettings.make(robot), DefaultDriveHardware.make(robot));
    }

    public Drive(Robot robot, DriveSettings driveSettings, DriveHardware driveHardware) {
        super(robot, "drive", driveSettings, driveHardware);
    }


}
