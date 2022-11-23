package org.firstinspires.ftc.teamcode.parts.lifter.settings;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Supplier;

import om.self.ezftc.core.Robot;

public class LifterTeleopSettings {
    public final Supplier<Float> heightSpeedSupplier;

    public final Supplier<Float> turnSpeedSupplier;
    public final double turnSpeedMultiplier;

    public final Supplier<Boolean> grabberCloseSupplier;

    public LifterTeleopSettings(Supplier<Float> heightSpeedSupplier, Supplier<Float> turnSpeedSupplier, double turnSpeedMultiplier, Supplier<Boolean> grabberCloseSupplier) {
        this.heightSpeedSupplier = heightSpeedSupplier;
        this.turnSpeedSupplier = turnSpeedSupplier;
        this.turnSpeedMultiplier = turnSpeedMultiplier;
        this.grabberCloseSupplier = grabberCloseSupplier;
    }

    public static LifterTeleopSettings makeDefault(Robot robot){
        Gamepad gamepad = robot.opMode.gamepad2;

        return new LifterTeleopSettings(
                () -> gamepad.right_trigger - gamepad.left_trigger,
                () -> -gamepad.left_stick_y,
                0.003,
                () -> !gamepad.a
        );
    }
}
