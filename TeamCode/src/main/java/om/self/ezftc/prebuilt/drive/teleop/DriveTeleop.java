package om.self.ezftc.prebuilt.drive.teleop;

import org.apache.commons.lang3.ObjectUtils;

import om.self.ezftc.core.part.Part;
import om.self.ezftc.prebuilt.drive.base.Drive;
import om.self.ezftc.prebuilt.drive.base.DriveControl;

import om.self.ezftc.utils.VectorMath;

/**
 * A class that sets the base controller in the default control environment to controller the drive with a controller.
 */
public class DriveTeleop extends Part<Drive, DriveTeleopSettings, ObjectUtils.Null>{

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
    public void onInitialStart() {

    }

    /**
     * Sets the base controller in the default control environment to controller the drive with a controller.
     */
    @Override
    public void onStart() {
        parent.getDefualtControlEnvironment().setBaseController(() -> new DriveControl(
                getSettings().slowModeSupplier.get() ? VectorMath.scale(getSettings().powerSupplier.get(), getSettings().slowModeSpeed) :
                getSettings().midModeSupplier.get() ? VectorMath.scale(getSettings().powerSupplier.get(), getSettings().midModeSpeed) :
                getSettings().invertSupplier.get() ? VectorMath.scaleAsVector2(getSettings().powerSupplier.get(), -1) : getSettings().powerSupplier.get(),

                getSettings().stopSupplier.get()
        ));
    }

    @Override
    public void onStop() {
    }
}
