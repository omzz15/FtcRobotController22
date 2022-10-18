package om.self.ezftc.parts.drive;

import om.self.beans.Bean;
import om.self.ezftc.core.Part;
import om.self.ezftc.other.hardware.motor.MotorContainer;
import om.self.ezftc.other.hardware.motor.MotorGenerator;
import om.self.ezftc.other.hardware.motor.MotorSettings;

@Bean(alwaysLoad = true)
@Part("drive")
public class DriveHardware {
    MotorContainer FLMotor = MotorGenerator.make(MotorSettings.Number.ONE);
    MotorContainer FRMotor = MotorGenerator.make(MotorSettings.Number.ONE);
    MotorContainer BLMotor = MotorGenerator.make(MotorSettings.Number.ONE);
    MotorContainer BRMotor = MotorGenerator.make(MotorSettings.Number.ONE);
}
