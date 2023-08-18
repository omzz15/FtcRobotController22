package om.self.ezftc.prebuilt.drive.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.LatchedModifier;

/**
 * settings for {@link DriveTeleop}
 */
public class DriveTeleopSettings {
    /**
     * A supplier for the power vector (this is how you convert the controller input to the power vector)
     */
    public final Supplier<Vector3> powerSupplier;

    /**
     * A supplier for the stop boolean (this is how you convert the controller input to the stop boolean)
     */
    public final Supplier<Boolean> stopSupplier;

    /**
     * A supplier for the mid mode boolean. This will slow down the drive by the given amount.
     */
    public final Supplier<Boolean> midModeSupplier;
    /**
     * The amount to slow down the drive by when mid mode is enabled. It should be a value between 0 and 1.
     */
    public final double midModeSpeed;

    /**
     * A supplier for the slow mode boolean. This will slow down the drive by the given amount.
     */
    public final Supplier<Boolean> slowModeSupplier;
    /**
     * The amount to slow down the drive by when slow mode is enabled. It should be a value between 0 and 1.
     */
    public final double slowModeSpeed;

    /**
     * A supplier for the invert boolean. This will invert the x and y drive.
     */
    public final Supplier<Boolean> invertSupplier;

    /**
     * Creates a drive teleop settings with the given values
     * @param powerSupplier the power supplier {@link #powerSupplier}
     * @param stopSupplier the stop supplier {@link #stopSupplier}
     * @param midModeSupplier the mid mode supplier {@link #midModeSupplier}
     * @param midModeSpeed the mid mode speed {@link #midModeSpeed}
     * @param slowModeSupplier the slow mode supplier {@link #slowModeSupplier}
     * @param slowModeSpeed the slow mode speed {@link #slowModeSpeed}
     * @param invertSupplier the invert supplier {@link #invertSupplier}
     */
    public DriveTeleopSettings(Supplier<Vector3> powerSupplier, Supplier<Boolean> stopSupplier, Supplier<Boolean> midModeSupplier, double midModeSpeed, Supplier<Boolean> slowModeSupplier, double slowModeSpeed, Supplier<Boolean> invertSupplier) {
        this.powerSupplier = powerSupplier;
        this.stopSupplier = stopSupplier;
        this.midModeSupplier = midModeSupplier;
        this.midModeSpeed = midModeSpeed;
        this.slowModeSupplier = slowModeSupplier;
        this.slowModeSpeed = slowModeSpeed;
        this.invertSupplier = invertSupplier;
    }

    /**
     * Creates a default drive teleop settings. (Configure this to work with your main robot)
     * @param robot the robot
     * @return the default drive teleop settings
     */
    public static DriveTeleopSettings makeDefault(Robot robot){
        Gamepad gamepad = robot.opMode.gamepad1;

        return new DriveTeleopSettings(
                () -> new Vector3(
                        gamepad.left_stick_x,
                        -gamepad.left_stick_y,
                        gamepad.right_stick_x
                ),
                () -> gamepad.x,
                new LatchedModifier().toSupplier(() -> gamepad.right_bumper),
                0.7,
                //new LatchedModifier().toSupplier(() -> gamepad.b),
                () -> gamepad.right_trigger > 0.5,
                0.5,
                new LatchedModifier(false).toSupplier(() -> gamepad.left_bumper)
        );
    }

    /**
     * Creates a forza drive teleop settings. This is just a random thing
     * @param robot the robot
     * @return the forza drive teleop settings
     */
    public static DriveTeleopSettings makeForza(Robot robot){
        Gamepad gamepad = robot.opMode.gamepad1;

        return new DriveTeleopSettings(
                () -> new Vector3(
                        -gamepad.right_stick_x,
                        gamepad.left_trigger - gamepad.right_trigger,
                        gamepad.left_stick_x
                ),
                () -> gamepad.x,
                new LatchedModifier().toSupplier(() -> gamepad.right_bumper),
                0.7,
                //new LatchedModifier().toSupplier(() -> gamepad.b),
                () -> false,
                0.5,
                new LatchedModifier(false).toSupplier(() -> gamepad.left_bumper)
        );
    }
}
