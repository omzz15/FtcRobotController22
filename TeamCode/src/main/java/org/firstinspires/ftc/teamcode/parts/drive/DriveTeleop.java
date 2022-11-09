package org.firstinspires.ftc.teamcode.parts.drive;

import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveTeleopSettings;

import om.self.ezftc.utils.VectorMath;

public class DriveTeleop {
    public DriveTeleop(Drive drive, DriveTeleopSettings settings){
        construct(drive, settings);
    }

    public DriveTeleop(Drive drive){
        construct(drive, DriveTeleopSettings.makeDefault(drive.getParent()));
    }

    private void construct(Drive drive, DriveTeleopSettings settings){
        drive.setBaseController(() -> new DriveControl(
                settings.slowModeSupplier.get() ? VectorMath.multiply(settings.powerSupplier.get(), settings.slowModeSpeed) : settings.powerSupplier.get(),
                settings.stopSupplier.get()
        ), true);
    }
}
