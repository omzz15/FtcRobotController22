package org.firstinspires.ftc.teamcode.parts.drive.teleop;

import java.util.function.Supplier;

public class DriveTeleopSettings {
    public final Supplier<Double> xSupplier;
    public final Supplier<Double> ySupplier;
    public final Supplier<Double> rSupplier;

    public final Supplier<Boolean> slowModeSupplier;

    public DriveTeleopSettings(Supplier<Double> xSupplier, Supplier<Double> ySupplier, Supplier<Double> rSupplier, Supplier<Boolean> slowModeSupplier) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.rSupplier = rSupplier;
        this.slowModeSupplier = slowModeSupplier;
    }
}
