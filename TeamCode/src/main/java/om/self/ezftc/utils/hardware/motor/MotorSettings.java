package om.self.ezftc.utils.hardware.motor;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorSettings {
    public Number number;
    public DcMotorSimple.Direction direction;
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    public DcMotor.RunMode runMode;
    public double power;
    public int targetPos;

    public MotorSettings(Number number){
        construct(number, DcMotorSimple.Direction.FORWARD, DcMotor.ZeroPowerBehavior.FLOAT, DcMotor.RunMode.RUN_WITHOUT_ENCODER, 0,0);
    }

    public MotorSettings(Number number, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        construct(number,direction,zeroPowerBehavior, DcMotor.RunMode.RUN_WITHOUT_ENCODER, 0,0);
    }

    public MotorSettings(Number number, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode, double power){
        construct(number,direction,zeroPowerBehavior,runMode,power,0);
    }

    public MotorSettings(Number number, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode, double power, int targetPos){
        construct(number,direction,zeroPowerBehavior,runMode,power,targetPos);
    }

    void construct(Number number, DcMotorSimple.Direction direction, DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode, double power, int targetPos){
        this.number = number;
        this.direction = direction;
        this.zeroPowerBehavior = zeroPowerBehavior;
        this.runMode = runMode;
        this.power = power;
        this.targetPos = targetPos;
    }

    public DcMotor makeMotor(@NonNull HardwareMap hardwareMap){
        DcMotor motor = hardwareMap.get(DcMotor.class, number.value);
        updateMotor(motor, true);
        return motor;
    }

    public DcMotorEx makeExMotor(@NonNull HardwareMap hardwareMap){
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, number.value);
        updateMotor(motor, true);
        return motor;
    }

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

    public enum Number {
        ZERO("motor0"),
        ONE("motor1"),
        TWO("motor2"),
        THREE("motor3"),
        ZERO_B("motor0B"),
        ONE_B("motor1B"),
        TWO_B("motor2B"),
        THREE_B("motor3B");

        public String value;

        Number(String value){
            this.value = value;
        }
    }
}
