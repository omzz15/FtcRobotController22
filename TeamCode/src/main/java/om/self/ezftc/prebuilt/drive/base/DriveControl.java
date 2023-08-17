package om.self.ezftc.prebuilt.drive.base;

import om.self.ezftc.utils.Vector3;

/**
 * A class that holds the data used to control the drive. This contains the target power and weather to emergency stop and is used by the {@link Drive} class because it is a {@link om.self.ezftc.core.part.ControllablePart ControllablePart}.
 */
public class DriveControl {
    /**
     * The target power of the drive.
     * <br>
     * Note: This will not be used if {@link #stop} is true.
     */
    public Vector3 power;
    /**
     * Weather to emergency stop the drive.
     * <br>
     * Note: This takes priority over all else (Ex: target power and smoothing) and will immediately set the motor power to 0 which could cause slippage.
     */
    public boolean stop;

    /**
     * Creates a default drive control with a target {@link #power} of all 0s and {@link #stop} set to false.
     */
    public DriveControl() {
        this.power = Vector3.V3_ZERO;
        this.stop = false;
    }

    /**
     * Creates a drive control with the given target {@link #power} and {@link #stop} values.
     * @param power the target power
     * @param stop weather to emergency stop
     */
    public DriveControl(Vector3 power, boolean stop) {
        this.power = power;
        this.stop = stop;
    }
}
