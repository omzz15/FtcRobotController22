package org.firstinspires.ftc.teamcode.parts.lifter.settings;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Supplier;
import om.self.ezftc.core.Robot;

public class LifterTeleopSettings {
    public final Supplier<Float> heightSpeedSupplier;
    public final Supplier<Float> turnSpeedSupplier;
    public final double turnSpeedMultiplier;
    public final Supplier<Boolean> grabberCloseSupplier;
    public final Supplier<Double> grabberMoveSupplier;
    public final Supplier<Boolean> goToBottomSupplier;
    public final Supplier<Boolean> autoGrabSupplier;
    public final Supplier<Boolean> autoDockSupplier;
    public final Supplier<Boolean> autoDropSupplier;
    
    public LifterTeleopSettings(Supplier<Float> heightSpeedSupplier, 
                                Supplier<Float> turnSpeedSupplier, 
                                double turnSpeedMultiplier, 
                                Supplier<Boolean> grabberCloseSupplier, 
                                Supplier<Double> grabberMoveSupplier, 
                                Supplier<Boolean> goToBottomSupplier, 
                                Supplier<Boolean> autoGrabSupplier, 
                                Supplier<Boolean> autoDockSupplier,
                                Supplier<Boolean> autoDropSupplier
                                ) {
        this.heightSpeedSupplier = heightSpeedSupplier;
        this.turnSpeedSupplier = turnSpeedSupplier;
        this.turnSpeedMultiplier = turnSpeedMultiplier;
        this.grabberCloseSupplier = grabberCloseSupplier;
        this.grabberMoveSupplier = grabberMoveSupplier;
        this.goToBottomSupplier = goToBottomSupplier;
        this.autoGrabSupplier = autoGrabSupplier;
        this.autoDockSupplier = autoDockSupplier;
        this.autoDropSupplier = autoDropSupplier;
    }

    public static LifterTeleopSettings makeDefault(Robot robot){
        Gamepad gamepad2 = robot.opMode.gamepad2;

        return new LifterTeleopSettings(
                () -> gamepad2.right_trigger - gamepad2.left_trigger, // height Speed
                () -> -gamepad2.left_stick_y, // turnSpeed
                0.005, // turnSpeedMultiplieer
                () -> !gamepad2.a, // grabber close/open
                () -> gamepad2.a ? 0.5 : gamepad2.x ? -0.5 : 0, //grabberMove
                () -> gamepad2.b, // autoToBottom
                () -> gamepad2.y, // autoGrab
                () -> gamepad2.right_bumper, // autoDock
                () -> gamepad2.dpad_up // autoDrop
        );
    }
}
