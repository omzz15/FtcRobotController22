package org.firstinspires.ftc.teamcode.parts.lifter.settings;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Supplier;

import om.self.ezftc.core.Robot;

public class LifterTeleopSettings {
    public final Supplier<Float> heightSpeedSupplier;

    public final Supplier<Float> turnSpeedSupplier;
    public final double turnSpeedMultiplier;

    public final Supplier<Double> grabberMoveSupplier;
    public final Supplier<Boolean> goToBottomSupplier;
    public final Supplier<Boolean> goToTopSupplier;


    public LifterTeleopSettings(Supplier<Float> heightSpeedSupplier, Supplier<Float> turnSpeedSupplier, double turnSpeedMultiplier, Supplier<Double> grabberMoveSupplier, Supplier<Boolean> goToBottomSupplier, Supplier<Boolean> goToTopSupplier) {
        this.heightSpeedSupplier = heightSpeedSupplier;
        this.turnSpeedSupplier = turnSpeedSupplier;
        this.turnSpeedMultiplier = turnSpeedMultiplier;
        this.grabberMoveSupplier = grabberMoveSupplier;
        this.goToBottomSupplier = goToBottomSupplier;
        this.goToTopSupplier = goToTopSupplier;
    }

    public static LifterTeleopSettings makeDefault(Robot robot){
        Gamepad gamepad = robot.opMode.gamepad2;

        return new LifterTeleopSettings(
                () -> gamepad.right_trigger - gamepad.left_trigger, //heightSpeedSupplier
                () -> -gamepad.left_stick_y, //turnSpeedSupplier
                0.01,
                () -> gamepad.a ? 0.5 : gamepad.x ? -0.5 : 0, //grabberMoveSupplier
                () -> gamepad.b,
                () -> gamepad.y
        );
    }
}
