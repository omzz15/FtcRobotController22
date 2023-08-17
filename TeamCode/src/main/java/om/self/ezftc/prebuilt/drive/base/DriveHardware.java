package om.self.ezftc.prebuilt.drive.base;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import om.self.ezftc.utils.hardware.motor.MotorSettings;

/**
 * A class that holds the hardware for the drive. This contains the motors and is used by the {@link Drive} class.
 */
public class DriveHardware {
    /**
     * The top left motor (motor for the top left wheel).
     */
    public final DcMotor topLeftMotor;
    /**
     * The top right motor (motor for the top right wheel).
     */
    public final DcMotor topRightMotor;
    /**
     * The bottom left motor (motor for the bottom left wheel).
     */
    public final DcMotor bottomLeftMotor;
    /**
     * The bottom right motor (motor for the bottom right wheel).
     */
    public final DcMotor bottomRightMotor;

    /**
     * Creates a drive hardware configuration by using the givin motor settings to create motors.
     * @param hardwareMap the hardware map to get the motors from
     * @param topLeftMotorSettings the settings for the top left motor (will create {@link #topLeftMotor})
     * @param topRightMotorSettings the settings for the top right motor (will create {@link #topRightMotor})
     * @param bottomLeftMotorSettings the settings for the bottom left motor (will create {@link #bottomLeftMotor})
     * @param bottomRightMotorSettings the settings for the bottom right motor (will create {@link #bottomRightMotor})
     */
    public DriveHardware(HardwareMap hardwareMap, MotorSettings topLeftMotorSettings, MotorSettings topRightMotorSettings, MotorSettings bottomLeftMotorSettings, MotorSettings bottomRightMotorSettings) {
        topLeftMotor = topLeftMotorSettings.makeMotor(hardwareMap);
        topRightMotor = topRightMotorSettings.makeMotor(hardwareMap);
        bottomLeftMotor = bottomLeftMotorSettings.makeMotor(hardwareMap);
        bottomRightMotor = bottomRightMotorSettings.makeMotor(hardwareMap);
    }

    /**
     * Creates a drive hardware configuration with the given motors.
     * @param topLeftMotor the top left motor (motor for the top left wheel)
     * @param topRightMotor the top right motor (motor for the top right wheel)
     * @param bottomLeftMotor the bottom left motor (motor for the bottom left wheel)
     * @param bottomRightMotor the bottom right motor (motor for the bottom right wheel)
     */
    public DriveHardware(DcMotor topLeftMotor, DcMotor topRightMotor, DcMotor bottomLeftMotor, DcMotor bottomRightMotor){
        this.topLeftMotor = topLeftMotor;
        this.topRightMotor = topRightMotor;
        this.bottomLeftMotor = bottomLeftMotor;
        this.bottomRightMotor = bottomRightMotor;
    }

    /**
     * Creates the default drive hardware configuration (you should change this to work with your main robot).
     * @param hardwareMap the hardware map to get the motors from
     * @return the default drive hardware configuration
     */
    public static DriveHardware makeDefault(HardwareMap hardwareMap){
        return new DriveHardware(
            hardwareMap,
            new MotorSettings(MotorSettings.Number.ZERO, DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_USING_ENCODER, 0),
            new MotorSettings(MotorSettings.Number.ONE, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_USING_ENCODER, 0),
            new MotorSettings(MotorSettings.Number.TWO, DcMotorSimple.Direction.REVERSE, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_USING_ENCODER, 0),
            new MotorSettings(MotorSettings.Number.THREE, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.BRAKE, DcMotor.RunMode.RUN_USING_ENCODER, 0)
        );
    }
}
