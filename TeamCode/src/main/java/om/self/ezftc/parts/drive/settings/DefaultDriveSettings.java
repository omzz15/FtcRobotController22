package om.self.ezftc.parts.drive.settings;

import om.self.beans.core.Profile;

@Profile
public class DefaultDriveSettings extends DriveSettings{
    public DefaultDriveSettings() {
        super(
                DriveMode.MECANUM,
                true,
                new double[]{0.1,0.1,0.1},
                0.5
        );
    }
}
