package org.firstinspires.ftc.teamcode.parts.lifter;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.parts.lifter.hardware.LifterHardware;
import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterSettings;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.ConfigurablePart;
import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.RobotPart;

public class Lifter extends RobotPart implements ControllablePart<Robot, LifterControl>, ConfigurablePart<Robot, LifterSettings, LifterHardware> {
    //private EdgeExModifier powerEdgeDetector = new EdgeExModifier();
    private boolean closed = false;

    public Lifter(Robot parent) {
        super(parent, "lifter");
        setConfig(
                LifterSettings.makeDefault(),
                LifterHardware.makeDefault(parent.opMode.hardwareMap)
        );
        constructThings();
    }

    public Lifter(Robot parent, LifterSettings settings, LifterHardware hardware){
        super(parent, "lifter");
        setConfig(settings, hardware);
        constructThings();
    }

    private void constructThings(){
        constructControllable();
    }

    public void liftWithPower(double power){
        if(Math.abs(power) < getSettings().minRegisterVal) return;

        if(power < 0)
            power *= getSettings().maxDownLiftSpeed;
        else
            power *= getSettings().maxUpLiftSpeed;

//        if(power > getSettings().minRegisterVal){
//            if(getHardware().leftLiftMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
//                getHardware().leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//                getHardware().rightLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            }
//            getHardware().leftLiftMotor.setPower(power);
//            getHardware().rightLiftMotor.setPower(power);
//        } else if(getHardware().leftLiftMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER){

            setLiftPosition(getHardware().leftLiftMotor.getCurrentPosition() + (int)power);

//            getHardware().leftLiftMotor.setPower(getSettings().liftHoldPower);
//            getHardware().rightLiftMotor.setPower(getSettings().liftHoldPower);
//
//            getHardware().leftLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            getHardware().rightLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
    }

    public void setLiftPosition(int position){
        position = Math.min(getSettings().maxLiftPosition, Math.max(getSettings().minLiftPosition, position));
        int diff = getHardware().rightLiftMotor.getCurrentPosition() - getHardware().leftLiftMotor.getCurrentPosition();

        getHardware().leftLiftMotor.setTargetPosition(position);
        getHardware().rightLiftMotor.setTargetPosition(position + diff);

//        if(getHardware().leftLiftMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
//            getHardware().leftLiftMotor.setPower(getSettings().liftHoldPower);
//            getHardware().rightLiftMotor.setPower(getSettings().liftHoldPower);
//
//            getHardware().leftLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            getHardware().rightLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
    }

    public int getLiftPosition(){
        return getHardware().leftLiftMotor.getCurrentPosition();
    }
    public double getLeftRange(){return getHardware().leftRange.getDistance(DistanceUnit.INCH);}
    public double getRightRange(){return getHardware().rightRange.getDistance(DistanceUnit.INCH);}

    public void turnWithPower(double power){
        setTurnPosition(getCurrentTurnPosition() + power);
    }

    public void setTurnPosition(double position){
        position = Math.min(getSettings().turnServoMaxPosition, Math.max(getSettings().turnServoMinPosition, position));

        getHardware().leftTurnServo.setPosition(position);
        getHardware().rightTurnServo.setPosition(position + getSettings().rightTurnServoOffset);
    }

    public double getCurrentTurnPosition(){
        return getHardware().leftTurnServo.getPosition();
    }

    public void setGrabberClosed(boolean isClosed){
        this.closed = isClosed;
        getHardware().grabServo.setPosition(isClosed ? getSettings().grabberServoClosePos : getSettings().grabberServoOpenPos);
    }

    public boolean isClosed(){
        return closed;
    }

    @Override
    public void onRun(LifterControl control) { //TODO separate keeping lifter motor position from onRun
        liftWithPower(control.lifterPower);
        turnWithPower(control.turningPower);
        setGrabberClosed(control.close);
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onSettingsUpdate(LifterSettings lifterSettings) {

    }

    @Override
    public void onHardwareUpdate(LifterHardware lifterHardware) {

    }

    @Override
    public void onInit() {
        //powerEdgeDetector.setOnFall(() -> se);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
