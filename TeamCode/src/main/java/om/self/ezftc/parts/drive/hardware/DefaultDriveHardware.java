package om.self.ezftc.parts.drive.hardware;

import om.self.beans.core.Profile;
import om.self.ezftc.other.hardware.motor.MotorGenerator;
import om.self.ezftc.other.hardware.motor.MotorSettings;

@Profile
public class DefaultDriveHardware extends DriveHardware{
    public DefaultDriveHardware() {
        super(
                MotorGenerator.make(MotorSettings.Number.ONE),
                MotorGenerator.make(MotorSettings.Number.ONE),
                MotorGenerator.make(MotorSettings.Number.ONE),
                MotorGenerator.make(MotorSettings.Number.ONE)
        );
    }
}
