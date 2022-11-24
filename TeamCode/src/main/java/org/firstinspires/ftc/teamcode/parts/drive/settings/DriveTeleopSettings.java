package org.firstinspires.ftc.teamcode.parts.drive.settings;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Vector;
import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.LatchedModifier;

public class DriveTeleopSettings {
    public final Supplier<Vector3> powerSupplier;

    public final Supplier<Boolean> stopSupplier;

    public final Supplier<Boolean> slowModeSupplier;
    public final double slowModeSpeed;

    public DriveTeleopSettings(Supplier<Vector3> powerSupplier, Supplier<Boolean> stopSupplier, Supplier<Boolean> slowModeSupplier, double slowModeSpeed) {
        this.powerSupplier = powerSupplier;
        this.stopSupplier = stopSupplier;
        this.slowModeSupplier = slowModeSupplier;
        this.slowModeSpeed = slowModeSpeed;
    }

    public static DriveTeleopSettings makeDefault(Robot robot){
        Gamepad gamepad = robot.opMode.gamepad1;

        return new DriveTeleopSettings(
                () -> new Vector3(
                        gamepad.left_stick_x,
                        -gamepad.left_stick_y,
                        gamepad.right_stick_x
                ),
                () -> gamepad.x,
                new LatchedModifier().toSupplier(() -> gamepad.a),
                0.6
        );
    }
}
