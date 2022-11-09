package org.firstinspires.ftc.teamcode.parts.drive.headerkeeper;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class HeaderKeeperSettings {
    public final double minRegisterVal;
    public final PIDCoefficients pidCoefficients;

    public HeaderKeeperSettings(double minRegisterVal, PIDCoefficients pidCoefficients) {
        this.minRegisterVal = minRegisterVal;
        this.pidCoefficients = pidCoefficients;
    }

    public static HeaderKeeperSettings makeDefault(){
        return new HeaderKeeperSettings(
                0.1,
                new PIDCoefficients(
                        -0.02,0,0
                )
        );
    }
}
