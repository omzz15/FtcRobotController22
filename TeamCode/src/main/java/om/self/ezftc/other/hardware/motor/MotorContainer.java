package om.self.ezftc.other.hardware.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorContainer {
    private MotorSettings settings;
    private DcMotor motor;

    public MotorContainer(MotorSettings settings, DcMotor motor) {
        this.settings = settings;
        this.motor = motor;
    }

    public MotorContainer() {
    }

    public MotorSettings getSettings() {
        return settings;
    }

    public void setSettings(MotorSettings settings) {
        this.settings = settings;
    }

    public void setMotor(DcMotor motor){
        this.motor = motor;
    }

    public DcMotor getMotor(){
        return motor;
    }

    /**
     * this will update the contained motor with the current settings. NOTE power and targetPos will only be set if the run mode is RUN_TO_POSITION
     * @param resetPos weather the motors position should be reset(only applies to run modes that use encoders)
     */
    public void updateMotor(boolean resetPos){
        motor.setDirection(settings.direction);
        motor.setZeroPowerBehavior(settings.zeroPowerBehavior);
        if(settings.runMode == DcMotor.RunMode.RUN_USING_ENCODER && resetPos)
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        else if(settings.runMode == DcMotor.RunMode.RUN_TO_POSITION){
            if(resetPos)
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setPower(settings.power);
            motor.setTargetPosition(settings.targetPos);
        }
        motor.setMode(settings.runMode);
    }

    public void generateMotor(HardwareMap hardwareMap){
        setMotor(hardwareMap.get(DcMotor.class, settings.number.value));
    }
}
