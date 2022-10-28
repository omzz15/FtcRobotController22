package org.firstinspires.ftc.teamcode.parts.drive.settings;

import java.util.function.Supplier;
import om.self.ezftc.utils.Vector3;

public class DriveSettings {
    //input
    public final Supplier<Double> xSupplier;
    public final Supplier<Double> ySupplier;
    public final Supplier<Double> rSupplier;
    public final Supplier<Boolean> stopSupplier;

    public final Supplier<Boolean> slowModeSupplier;

    //other
    public final DriveMode driveMode;
    public final Vector3 smoothingValues;
    public final double slowModeSpeed;



    public DriveSettings(Supplier<Double> xSupplier, Supplier<Double> ySupplier, Supplier<Double> rSupplier, Supplier<Boolean> stopSupplier, Supplier<Boolean> slowModeSupplier, DriveMode driveMode, Vector3 smoothingValues, double slowModeSpeed) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.rSupplier = rSupplier;
        this.stopSupplier = stopSupplier;
        this.slowModeSupplier = slowModeSupplier;
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
