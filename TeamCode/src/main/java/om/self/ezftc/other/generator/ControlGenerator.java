package om.self.ezftc.other.generator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import om.self.beans.Bean;
import om.self.beans.core.Autowired;
import om.self.supplier.Utils;
import om.self.supplier.modifiers.DeadZoneModifier;
import om.self.supplier.modifiers.SimpleRampedModifier;
import om.self.supplier.suppliers.LinkedSupplier;
import om.self.task.core.EventManager;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Bean(alwaysLoad = true)
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

    public static Supplier<Double> makeEx(boolean useSecondGamepad, Function<Gamepad, Double> conversion, double deadZoneMin, double deadZoneMax, double ramp, double currVal){
        return () -> new DeadZoneModifier<>(deadZoneMin, deadZoneMax)
                        .toSupplier(new SimpleRampedModifier(ramp,currVal)
                            .toSupplier(make(useSecondGamepad, conversion))).get();
    }

    public static Supplier<Double> makeEx(boolean useSecondGamepad, Function<Gamepad, Double> conversion, double deadZone, double ramp){
        return makeEx(useSecondGamepad, conversion, -deadZone, deadZone, ramp, 0f);
    }

    @Autowired
    public void construct(OpMode opMode, EventManager eventManager){
        generator.init(opMode,eventManager);
    }
}
