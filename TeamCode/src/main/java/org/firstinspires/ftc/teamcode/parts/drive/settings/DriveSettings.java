package org.firstinspires.ftc.teamcode.parts.drive.settings;

import java.util.function.Supplier;
import om.self.ezftc.utils.Vector3;

public class DriveSettings {
    //input
    public final Supplier<Boolean> stopSupplier;

    //other
    public final DriveMode driveMode;
    public final Vector3 smoothingValues;
    public final double slowModeSpeed;



    public DriveSettings(Supplier<Boolean> stopSupplier, DriveMode driveMode, Vector3 smoothingValues, double slowModeSpeed) {
        this.stopSupplier = stopSupplier;
        this.driveMode = driveMode;
        this.smoothingValues = smoothingValues;
        this.slowModeSpeed = slowModeSpeed;
    }

    public enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
