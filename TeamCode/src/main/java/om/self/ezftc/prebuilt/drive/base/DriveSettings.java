package om.self.ezftc.prebuilt.drive.base;

import om.self.ezftc.utils.Vector3;

/**
 * A class that holds the settings for the drive. This contains the drive mode, smoothing values, and weather to use smoothing and is used by the {@link Drive} class.
 */
public class DriveSettings {
    /**
     * The {@link DriveMode} to use. (This is based on your drive system, Ex: Mechanum, Tank, Omni, etc.)
     */
    public final DriveMode driveMode;
    /**
     * The smoothing values to use for the drive. This is how much each movement direction (X, Y, and R) can change each loop so a smaller number will mean less change and acceleration. (This is used to smooth the drive power to prevent jerking and wheel slip)
     */
    public final Vector3 smoothingValues;
    /**
     * Weather to use smoothing. (This is used to smooth the drive power to prevent jerking and wheel slip)
     * @see #smoothingValues
     */
    public final boolean useSmoothing;

    /**
     * Creates a drive settings with the given values.
     * @param driveMode the drive mode to use {@link #driveMode}
     * @param smoothingValues the smoothing values to use {@link #smoothingValues}
     * @param useSmoothing weather to use smoothing {@link #useSmoothing}
     */
    public DriveSettings(DriveMode driveMode, Vector3 smoothingValues, boolean useSmoothing) {
        this.driveMode = driveMode;
        this.smoothingValues = smoothingValues;
        this.useSmoothing = useSmoothing;
    }

    /**
     * Creates a default drive settings. (Configure this to work with your main robot)
     * @return the default drive settings
     */
    public static DriveSettings makeDefault(){
        return new DriveSettings(
                DriveSettings.DriveMode.MECANUM,
                new Vector3(0.075,0.075,0.075),
                true
        );
    }

    /**
     * The drive modes that can be used.
     * <br>
     * Feel free to add your own, just make sure to create a {@link om.self.ezftc.prebuilt.drive.base.Drive.DrivePowerConverter DrivePowerConverter} for it in {@link Drive#onSettingsUpdate(DriveSettings)}.
     */
    public enum DriveMode{
        TANK,
        MECANUM,
        OMNI
    }
}
