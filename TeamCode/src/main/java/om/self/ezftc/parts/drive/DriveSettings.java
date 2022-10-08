package om.self.ezftc.parts.drive;

import om.self.beans.Bean;

@Bean
public class DriveSettings {
    public DriveMode driveMode = DriveMode.MECANUM;

    public boolean useSmoothing = true;
    double[] smoothingValues = new double[]{.1,.1,.1};

    public double slowModeSpeed = 0.6;

    enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
