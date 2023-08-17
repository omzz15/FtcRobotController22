package om.self.ezftc.prebuilt.drive.teleop;

import org.apache.commons.lang3.ObjectUtils;

import om.self.ezftc.prebuilt.drive.base.Drive;
import om.self.ezftc.prebuilt.drive.base.DriveControl;
import om.self.ezftc.prebuilt.drive.teleop.DriveTeleopSettings;

import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.VectorMath;

public class DriveTeleop extends LoopedPartImpl<Drive, DriveTeleopSettings, ObjectUtils.Null> {
    Vector3 invertVector = new Vector3(-1,-1,1);

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
        parent.parent.opMode.telemetry.addData("Drive Speed", getSettings().slowModeSupplier.get() ? "slow" : getSettings().midModeSupplier.get() ? "mid" : "fast");
    }

    @Override
    public void onStart() {
        parent.setBaseController(() -> new DriveControl(
                getSettings().slowModeSupplier.get() ? VectorMath.multiply(getSettings().powerSupplier.get(), getSettings().slowModeSpeed) :
                getSettings().midModeSupplier.get() ? VectorMath.multiply(getSettings().powerSupplier.get(), getSettings().midModeSpeed) :
                getSettings().invertSupplier.get() ? VectorMath.multiply(getSettings().powerSupplier.get(), invertVector) : getSettings().powerSupplier.get(),

                getSettings().stopSupplier.get()
        ), true);
    }

    @Override
    public void onStop() {
    }
}
