package om.self.ezftc.other.control;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import om.self.ezftc.other.Generator;
import om.self.supplier.modifiers.DeadZoneModifier;
import om.self.supplier.modifiers.NumberConverter;
import om.self.supplier.modifiers.SimpleRampedModifier;
import om.self.supplier.suppliers.LinkedSupplier;
import om.self.task.core.EventManager;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControlGenerator{
    private static final Generator<OpMode, Map.Entry<LinkedSupplier<Gamepad, ?>, Boolean>> generator = new Generator<>((opMode, link) -> {
        if(link.getValue()) link.getKey().setInput(opMode.gamepad2);
        else link.getKey().setInput(opMode.gamepad1);
    });

    public static Supplier<Gamepad> make(boolean useSecondGamepad){
        return make(useSecondGamepad, gamepad -> gamepad);
    }

    public static<T> LinkedSupplier<Gamepad, T> make(Boolean useSecondGamepad, Function<Gamepad, T> conversion){
        LinkedSupplier<Gamepad, T> supplier = new LinkedSupplier<>(conversion);
        generator.add(new AbstractMap.SimpleEntry<>(supplier, useSecondGamepad));
        return supplier;
    }

    public static Supplier<Float> makeEx(boolean useSecondGamepad, Function<Gamepad, Float> conversion, double deadZoneMin, double deadZoneMax, double ramp, double currVal){
        return new NumberConverter<>(Double.class, Float.class)
                        .toSupplier(new DeadZoneModifier<>(deadZoneMin, deadZoneMax)
                            .toSupplier(new SimpleRampedModifier(ramp,currVal)
                                .toSupplier(new NumberConverter<>(Float.class, Double.class)
                                    .toSupplier(make(useSecondGamepad, conversion)))));
    }

    public static Supplier<Float> makeEx(boolean useSecondGamepad, Function<Gamepad, Float> conversion, double deadZone, double ramp){
        return makeEx(useSecondGamepad, conversion, -deadZone, deadZone, ramp, 0f);
    }

    public static void generate(OpMode opMode){
        generator.load(opMode);
    }
}
