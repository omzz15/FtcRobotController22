package om.self.ezftc.utils.hardware.motor;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A class to store motor settings and make motors from them. It can also update motors with settings.
 */
public class MotorSettings {
    /**
     * The motor name. This is how each motor is identified and is set in the hardware map. This can be any string but it is recommended to follow the constants in {@link Number}
     */
    public String name;
    /**
     * The direction of the motor. It can either be {@link DcMotorSimple.Direction#FORWARD FORWARD} or {@link DcMotorSimple.Direction#REVERSE REVERSE}. This is relative and has no absolute direction such as CW or CCW.
     */
    public DcMotorSimple.Direction direction;
    /**
     * What the motor should do when the power is set to 0. This can be {@link DcMotor.ZeroPowerBehavior#FLOAT FLOAT} or {@link DcMotor.ZeroPowerBehavior#BRAKE BRAKE} but NOT {@link DcMotor.ZeroPowerBehavior#UNKNOWN UNKNOWN}
     */
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    /**
     * How the motor should run. This can be {@link DcMotor.RunMode#RUN_WITHOUT_ENCODER RUN_WITHOUT_ENCODER}, {@link DcMotor.RunMode#RUN_USING_ENCODER RUN_USING_ENCODER}, or {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION} but probably not {@link DcMotor.RunMode#STOP_AND_RESET_ENCODER STOP_AND_RESET_ENCODER} unless you want the motor to do nothing
     */
    public DcMotor.RunMode runMode;
    /**
     * The power the motor should run at. This is only used if the run mode is {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION} else you should use  {@link DcMotor#setPower(double)} on the motor
     */
    public double power;
    /**
     * The target position of the motor. This is only used if the run mode is {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION}
     */
    public int targetPos;

    /**
     * Creates Motor Settings with default values and the given name (Direction: forward, ZeroPowerBehavior: float, RunMode: run without encoder, Power: 0, TargetPos: 0)
     * @param name the name of the motor (how it is identified in the hardware map)
     */
    public MotorSettings(String name){
        this(name, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER, 0,0);
    }

    /**
     * Creates Motor Settings with default values and the given name and direction (ZeroPowerBehavior: float, RunMode: run without encoder, Power: 0, TargetPos: 0)
     * @param name the name of the motor (how it is identified in the hardware map)
     * @param direction the direction of the motor
     */
    public MotorSettings(String name, DcMotorSimple.Direction direction){
        this(name, direction, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER, 0,0);
    }

    /**
     * Creates Motor Settings with default values and the given name, direction, and zero power behavior (RunMode: run without encoder, Power: 0, TargetPos: 0)
     * @param name the name of the motor (how it is identified in the hardware map)
     * @param direction the direction of the motor
     * @param zeroPowerBehavior what the motor should do when the power is set to 0
     */
    public MotorSettings(String name, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        this(name,direction,zeroPowerBehavior, DcMotor.RunMode.RUN_WITHOUT_ENCODER, 0,0);
    }

    /**
     * Creates Motor Settings with the given values and a target position of 0. This should really only be used if the run mode is {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION} because that is the only one that uses power
     * @param name the name of the motor (how it is identified in the hardware map)
     * @param direction the direction of the motor
     * @param zeroPowerBehavior what the motor should do when the power is set to 0
     * @param runMode how the motor should run
     */
    public MotorSettings(String name, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode, double power){
        this(name,direction,zeroPowerBehavior,runMode,power,0);
    }

    /**
     * Creates Motor Settings with the given values. This should really only be used if the run mode is {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION} because that is the only one that uses power and target position
     * @param name the name of the motor (how it is identified in the hardware map)
     * @param direction the direction of the motor
     * @param zeroPowerBehavior what the motor should do when the power is set to 0
     * @param runMode how the motor should run
     * @param power the power the motor should run at
     * @param targetPos the target position of the motor
     */
    public MotorSettings(String name, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode, double power, int targetPos){
        this.name = name;
        this.direction = direction;
        this.zeroPowerBehavior = zeroPowerBehavior;
        this.runMode = runMode;
        this.power = power;
        this.targetPos = targetPos;
    }

    /**
     * Creates a {@link DcMotor}, then calls {@link #updateMotor(DcMotor, boolean)} to put all the settings on it.
     * @param hardwareMap the hardware map to get the motor from
     * @return the configured motor
     */
    public DcMotor makeMotor(@NonNull HardwareMap hardwareMap){
        DcMotor motor = hardwareMap.get(DcMotor.class, name);
        updateMotor(motor, true);
        return motor;
    }

    /**
     * Creates a {@link DcMotorEx}, then calls {@link #updateMotor(DcMotor, boolean)} to put all the settings on it.
     * @param hardwareMap the hardware map to get the motor from
     * @return the configured motor
     */
    public DcMotorEx makeExMotor(@NonNull HardwareMap hardwareMap){
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, name);
        updateMotor(motor, true);
        return motor;
    }

    /**
     * Creates a {@link CRServo}, then calls {@link #updateSimpleMotor(DcMotorSimple)} to put the direction and power on it.
     * @param hardwareMap the hardware map to get the motor from
     * @return the configured motor
     */
    public CRServo makeCRServo(@NonNull HardwareMap hardwareMap){
        CRServo motor = hardwareMap.get(CRServo.class, name);
        updateSimpleMotor(motor);
        return motor;
    }

    /**
     * Updates a motor with the settings in this class. This will set the direction, zero power behavior, run mode, power, and target position.
     * <br>
     * Note: power and target position are only set if the run mode is {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION}
     * @param motor the motor to update
     * @param resetPos whether or not to reset the position of the motor. This is only used if the run mode is {@link DcMotor.RunMode#RUN_USING_ENCODER RUN_USING_ENCODER} or {@link DcMotor.RunMode#RUN_TO_POSITION RUN_TO_POSITION}
     */
    public void updateMotor(@NonNull DcMotor motor, boolean resetPos){
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        if(runMode == DcMotor.RunMode.RUN_USING_ENCODER && resetPos)
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        else if(runMode == DcMotor.RunMode.RUN_TO_POSITION){
            if(resetPos)
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setPower(power);
            motor.setTargetPosition(targetPos);
        }
        motor.setMode(runMode);
    }

    /**
     * Updates a simple motor with the settings in this class. This will set the direction and power.
     * @param motor the motor to update
     */
    public void updateSimpleMotor(@NonNull DcMotorSimple motor){
        motor.setDirection(direction);
        motor.setPower(power);
    }

    /**
     * Interface that stores a set of default motor names in the format "motor#" where # is the number of the motor. There are also names with a B appended for motors on the expansion hub.
     */
    public interface Number {
        String ZERO = "motor0";
        String ONE = "motor1";
        String TWO = "motor2";
        String THREE = "motor3";
        String ZERO_B = "motor0B";
        String ONE_B = "motor1B";
        String TWO_B = "motor2B";
        String THREE_B = "motor3B";
    }
}
