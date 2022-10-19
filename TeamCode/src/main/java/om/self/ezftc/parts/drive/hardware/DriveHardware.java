package om.self.ezftc.parts.drive.hardware;

import om.self.beans.Bean;
import om.self.ezftc.core.Part;
import om.self.ezftc.other.hardware.motor.MotorContainer;

@Bean
public class DriveHardware {
    public final MotorContainer FLMotor;
    public final MotorContainer FRMotor;
    public final MotorContainer BLMotor;
    public final MotorContainer BRMotor;

    public DriveHardware(MotorContainer FLMotor, MotorContainer FRMotor, MotorContainer BLMotor, MotorContainer BRMotor) {
        this.FLMotor = FLMotor;
        this.FRMotor = FRMotor;
        this.BLMotor = BLMotor;
        this.BRMotor = BRMotor;
    }
}
