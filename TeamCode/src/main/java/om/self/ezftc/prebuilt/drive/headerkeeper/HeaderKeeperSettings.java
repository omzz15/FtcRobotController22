package om.self.ezftc.prebuilt.drive.headerkeeper;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

/**
 * Settings for the header keeper
 */
public class HeaderKeeperSettings {
    /**
     * The minimum value required to register a turn input
     */
    public final double minRegisterVal;

    /**
     * The PID coefficients
     */
    public final PIDCoefficients pidCoefficients;
    /**
     * The maximum value the integral term can be. (this is to prevent integral windup)
     */
    public final double maxI;

    /**
     * Amount of time to wait before turning on the header keeper after a turn input is stopped
     */
    public final int headingSettleDelay;

    /**
     * Creates a header keeper settings with the given values
     * @param minRegisterVal the minimum value required to register a turn input
     * @param pidCoefficients the PID coefficients
     * @param maxI the maximum value the integral term can be. (this is to prevent integral windup)
     * @param headingSettleDelay amount of time to wait before turning on the header keeper after a turn input is stopped
     */
    public HeaderKeeperSettings(double minRegisterVal, PIDCoefficients pidCoefficients, double maxI, int headingSettleDelay) {
        this.minRegisterVal = minRegisterVal;
        this.pidCoefficients = pidCoefficients;
        this.maxI = maxI;
        this.headingSettleDelay = headingSettleDelay;
    }

    /**
     * Creates the default header keeper settings
     * @return the default header keeper settings
     */
    public static HeaderKeeperSettings makeDefault(){
        return new HeaderKeeperSettings(
                0.1,
                new PIDCoefficients(
                        -0.02,0,0
                ),
                0.3,
                700
        );
    }
}
