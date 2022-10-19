package om.self.ezftc.parts.drive.hardware;

import om.self.ezftc.other.hardware.motor.MotorGenerator;
import om.self.ezftc.other.hardware.motor.MotorSettings;

public class DefaultDriveHardware extends DriveHardware{
    public DefaultDriveHardware() {
        super(
                MotorGenerator.make(MotorSettings.Number.ONE),
                MotorGenerator.make(MotorSettings.Number.TWO),
                MotorGenerator.make(MotorSettings.Number.THREE),
                MotorGenerator.make(MotorSettings.Number.FOUR)
        );
    }
}
