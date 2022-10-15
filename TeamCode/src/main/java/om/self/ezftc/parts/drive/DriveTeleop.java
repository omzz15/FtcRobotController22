package om.self.ezftc.parts.drive;

import om.self.beans.Bean;
import om.self.ezftc.core.TeleopPart;
import om.self.ezftc.other.generator.ControlGenerator;

import java.util.function.Supplier;

@Bean(alwaysLoad = true, tags = {"teleop"})
public class DriveTeleop extends TeleopPart<Drive> {
    private boolean useSecondGamepad = false;
    //private Supplier<Float> driveXPower = ControlGenerator.makeEx(useSecondGamepad, gamepad -> gamepad.left_stick_x, 0.07f, 0.03f);
    //private Supplier<Float> driveYPower = ControlGenerator.makeEx(useSecondGamepad, gamepad -> gamepad.left_stick_y, 0.07f, 0.03f);
    //private Supplier<Float> driveRPower = ControlGenerator.makeEx(useSecondGamepad, gamepad -> gamepad.right_stick_x, 0.07f, 0.03f);


    @Override
    public void teleopCode() {}
}
