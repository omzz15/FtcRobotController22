package om.self.ezftc.other.hardware.motor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import om.self.ezftc.other.Generator;


public class MotorGenerator {
    private static final Generator<HardwareMap, MotorContainer> generator = new Generator<>((hardwareMap, link) -> {
        link.generateMotor(hardwareMap);
        link.updateMotor(true);
    });


    public static void generate(HardwareMap hardwareMap){
        generator.load(hardwareMap);
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
