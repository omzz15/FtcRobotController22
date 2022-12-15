package org.firstinspires.ftc.teamcode.parts.drive.settings;

import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;

public class DriveSettings {
    //other
    public final DriveMode driveMode;
    public final Vector3 smoothingValues;
    public final boolean useSmoothing;

    public DriveSettings(DriveMode driveMode, Vector3 smoothingValues, boolean useSmoothing) {
        this.driveMode = driveMode;
        this.smoothingValues = smoothingValues;
        this.useSmoothing = useSmoothing;
    }

    public static DriveSettings makeDefault(){
        return new DriveSettings(
                DriveSettings.DriveMode.MECANUM,
                new Vector3(0.1,0.1,0.1),
                true
        );
    }

    public enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
