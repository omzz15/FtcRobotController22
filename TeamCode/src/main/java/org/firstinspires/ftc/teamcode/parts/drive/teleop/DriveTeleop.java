package org.firstinspires.ftc.teamcode.parts.drive.teleop;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.TeleopPartWithSettings;

public class DriveTeleop extends TeleopPartWithSettings<Drive, DriveTeleopSettings> {
    public DriveTeleop(Drive drive, DriveTeleopSettings settings){
        super(drive, settings);
    }

    public DriveTeleop(Drive drive){
        super(drive, DefaultDriveTeleopSettings.make(drive.getParent()));
    }

    @Override
    public void onSettingsUpdate(DriveTeleopSettings settings) {

    }

    @Override
    public void onRun() {

    }
}
