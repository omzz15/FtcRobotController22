package org.firstinspires.ftc.teamcode.parts.drive;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveTeleopSettings;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.VectorMath;

public class DriveTeleop extends LoopedPartImpl<Drive, DriveTeleopSettings, ObjectUtils.Null> {

    public DriveTeleop(Drive parent) {
        super(parent, "drive teleop");
        setSettings(DriveTeleopSettings.makeDefault(parent.parent));
    }

    public DriveTeleop(Drive parent, DriveTeleopSettings settings) {
        super(parent, "drive teleop");
        setSettings(settings);
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onRun() {
        parent.parent.opMode.telemetry.addData("Drive Speed", getSettings().midModeSupplier.get() ? "mid" : getSettings().slowModeSupplier.get() ? "slow" : "fast");
    }

    @Override
    public void onStart() {
        parent.setBaseController(() -> new DriveControl(
                getSettings().midModeSupplier.get() ? VectorMath.multiply(getSettings().powerSupplier.get(), getSettings().midModeSpeed) :
                getSettings().slowModeSupplier.get() ? VectorMath.multiply(getSettings().powerSupplier.get(), getSettings().slowModeSpeed) :
                getSettings().powerSupplier.get(),

                getSettings().stopSupplier.get()
        ), true);
    }

    @Override
    public void onStop() {
    }
}
