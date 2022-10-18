package om.self.ezftc.parts.drive.settings;

import om.self.beans.Bean;
import om.self.ezftc.core.Part;

@Bean(alwaysLoad = true)
@Part("drive")
public class DriveSettings {
    public final DriveMode driveMode;

    public final boolean useSmoothing;
    public final double[] smoothingValues;

    public final double slowModeSpeed;

    public DriveSettings(DriveMode driveMode, boolean useSmoothing, double[] smoothingValues, double slowModeSpeed) {
        this.driveMode = driveMode;
        this.useSmoothing = useSmoothing;
        this.smoothingValues = smoothingValues;
        this.slowModeSpeed = slowModeSpeed;
    }

    public enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
