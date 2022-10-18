package om.self.ezftc.parts.drive;

import java.util.function.Supplier;

import om.self.ezftc.core.Part;
import om.self.ezftc.core.TeleopPart;
import om.self.ezftc.other.control.ControlGenerator;
import om.self.supplier.modifiers.DeadZoneModifier;
import om.self.supplier.modifiers.NumberConverter;
import om.self.task.core.EventManager;

/**
 * Provides teleop for the drive of the robot. In order to use this, ensure the 'teleop' tag is present in the BeanManager and 'drive' is present in Robot parts
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT_DRIVE --> INIT_DRIVE_TELEOP</li>
 *     <li>START_DRIVE --> START_DRIVE_TELEOP</li>
 *     <li>STOP_DRIVE --> STOP_DRIVE_TELEOP</li>
 * </ul>
 * USED BEANS:
 * <ul>
 *     <li>{@link Drive}</li>
 *     <li>{@link EventManager}</li>
 * </ul>
 * ADDED BEANS:
 * <ul>
 *   <li>{@link DriveTeleop}</li>
 */
@Part("drive")
public class DriveTeleop extends TeleopPart<Drive> {
    private boolean useSecondGamepad = false;
    private Supplier<Float> driveXPower = new DeadZoneModifier<>(input -> 0f, -0.07f,0.07f)
            .toSupplier(ControlGenerator.make(useSecondGamepad, gamepad -> gamepad.left_stick_x));
    private Supplier<Float> driveYPower = new DeadZoneModifier<>(input -> 0f, -0.07f,0.07f)
            .toSupplier(ControlGenerator.make(useSecondGamepad, gamepad -> gamepad.left_stick_y));
    private Supplier<Float> driveRPower = new DeadZoneModifier<>(input -> 0f, -0.07f,0.07f)
            .toSupplier(ControlGenerator.make(useSecondGamepad, gamepad -> gamepad.right_stick_x));
    private Supplier<Boolean> stopButton = ControlGenerator.make(useSecondGamepad, gamepad -> gamepad.x);

    @Override
    public void onRun() {
        if(stopButton.get()) getPart().stopRobot();
        else getPart().setPower(driveXPower.get(),driveYPower.get(),driveRPower.get());
    }
}
