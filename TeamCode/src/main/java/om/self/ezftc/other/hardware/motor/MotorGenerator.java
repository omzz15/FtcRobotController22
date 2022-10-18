package om.self.ezftc.other.hardware.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import om.self.beans.core.Autowired;
import om.self.ezftc.other.Generator;
import om.self.task.core.EventManager;

public class MotorGenerator {
    private static final Generator<HardwareMap, MotorContainer> generator = new Generator<>((hardwareMap, link) -> {
        link.generateMotor(hardwareMap);
        link.updateMotor(true);
    });

    @Autowired
    public void construct(HardwareMap hardwareMap, EventManager eventManager){
        generator.init(hardwareMap,eventManager);
    }


    public static MotorContainer make(MotorSettings settings){
        MotorContainer mc = new MotorContainer();
        mc.setSettings(settings);
        generator.add(mc);
        return mc;
    }

    public static MotorContainer make(MotorSettings.Number number){
        return make(new MotorSettings(number));
    }
}
