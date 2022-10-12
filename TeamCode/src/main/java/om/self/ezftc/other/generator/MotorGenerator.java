package om.self.ezftc.other.generator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import om.self.beans.core.Autowired;
import om.self.supplier.modifiers.DeadZoneModifier;
import om.self.supplier.modifiers.SimpleRampedModifier;
import om.self.supplier.suppliers.LinkedSupplier;
import om.self.task.core.EventManager;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class MotorGenerator {
    private static HardwareMap hardwareMap;
    public static final List<Map.Entry<LinkedSupplier<Gamepad, ?>, Boolean>> unlinkedSuppliers = new LinkedList<>();

    public static Supplier<Gamepad> make(boolean useSecondGamepad){
        return make(useSecondGamepad, gamepad -> gamepad);
    }

    public static<T> LinkedSupplier<Gamepad, T> make(Boolean useSecondGamepad, Function<Gamepad, T> conversion){
        LinkedSupplier<Gamepad, T> supplier = new LinkedSupplier<>(conversion);
        unlinkedSuppliers.add(new AbstractMap.SimpleEntry<>(supplier, useSecondGamepad));
        return supplier;
    }

    public<T extends Number & Comparable<T>> Supplier<T> makeEx(boolean useSecondGamepad,Function<Gamepad, T> conversion, T deadZoneMin, T deadZoneMax, T ramp, T currVal){
        return new SimpleRampedModifier<T>(ramp,currVal)
                .toSupplier(new DeadZoneModifier<T>(deadZoneMin, deadZoneMax)
                        .toSupplier(make(useSecondGamepad, conversion)));
    }
}
