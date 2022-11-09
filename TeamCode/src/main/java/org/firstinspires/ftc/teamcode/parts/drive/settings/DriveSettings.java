package org.firstinspires.ftc.teamcode.parts.drive.settings;

import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;

public class DriveSettings {
    //other
    public final DriveMode driveMode;
    public final Vector3 smoothingValues;


    public DriveSettings(DriveMode driveMode, Vector3 smoothingValues) {
        this.driveMode = driveMode;
        this.smoothingValues = smoothingValues;
    }

    public static DriveSettings makeDefault(){
        return new DriveSettings(
                DriveSettings.DriveMode.MECANUM,
                new Vector3(0.1,0.1,0.1)//smoothing
        );
    }

    public enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
