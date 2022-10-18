package om.self.ezftc.parts.drive;

import java.util.function.Function;

import om.self.ezftc.core.Part;
import om.self.ezftc.core.RobotPart;
import om.self.supplier.modifiers.SimpleRampedModifier;

@Part("drive")
public class Drive extends RobotPart<DriveSettings, DriveHardware> {
    SimpleRampedModifier xRamp = new SimpleRampedModifier();
    SimpleRampedModifier yRamp = new SimpleRampedModifier();
    SimpleRampedModifier rRamp = new SimpleRampedModifier();

    @Override
    public void onInit() {
        if(getSettings().useSmoothing)
    }
}
