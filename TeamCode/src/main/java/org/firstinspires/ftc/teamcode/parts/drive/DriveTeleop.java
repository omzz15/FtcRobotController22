package org.firstinspires.ftc.teamcode.parts.drive;

import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveTeleopSettings;

import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.VectorMath;

public class DriveTeleop extends Part<Drive> {
    private DriveTeleopSettings settings;

    public DriveTeleop(Drive parent) {
        super(parent, "drive teleop");
        settings = DriveTeleopSettings.makeDefault(parent.parent);
    }

    public DriveTeleop(Drive parent, DriveTeleopSettings settings) {
        super(parent, "drive teleop");
        this.settings = settings;
    }

    public DriveTeleopSettings getSettings() {
        return settings;
    }

    public void setSettings(DriveTeleopSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {
        parent.setBaseController(() -> new DriveControl(
                settings.slowModeSupplier.get() ? VectorMath.multiply(settings.powerSupplier.get(), settings.slowModeSpeed) : settings.powerSupplier.get(),
                settings.stopSupplier.get()
        ), true);
    }

    @Override
    public void onStop() {
    }
}
