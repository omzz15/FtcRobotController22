package om.self.ezftc.other.hardware.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

    public enum Number {
        ONE("motor0"),
        TWO("motor1"),
        THREE("motor2"),
        FOUR("motor3"),
        ONE_B("motor0B"),
        TWO_B("motor1B"),
        THREE_B("motor2B"),
        FOUR_B("motor3B");

        public String value;

        Number(String value){
            this.value = value;
        }
    }
}