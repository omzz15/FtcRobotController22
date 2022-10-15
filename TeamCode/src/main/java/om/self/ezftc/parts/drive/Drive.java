package om.self.ezftc.parts.drive;

import om.self.beans.Bean;
import om.self.ezftc.core.RobotPart;
import om.self.supplier.modifiers.SimpleRampedModifier;

import java.util.function.Function;

@Bean(alwaysLoad = true)
public class Drive extends RobotPart<DriveSettings, DriveHardware> {
    //Function<Float, Float> xRamp = new SimpleRampedModifier<>();
}
