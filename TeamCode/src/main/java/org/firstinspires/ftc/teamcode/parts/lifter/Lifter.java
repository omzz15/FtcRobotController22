package org.firstinspires.ftc.teamcode.parts.lifter;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.hardware.LifterHardware;
import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterSettings;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.ControllablePart;
import om.self.task.other.TimedTask;

public class Lifter extends ControllablePart<Robot, LifterSettings, LifterHardware, LifterControl>{
    public static final class TaskNames{
        public final static String autoGrab = "auto grab";
        public static String autoDrop;
        public final static String coneMeasureRanges = "measure cone range";
    }

    private Drive drive;

    //private boolean closed = false;
    private int liftTargetPosition = 0;

    private final TimedTask autoGrabTask = new TimedTask(TaskNames.autoGrab, getTaskManager());
    private final TimedTask coneRangeingTask = new TimedTask(TaskNames.coneMeasureRanges, getTaskManager());
    private int conePos = 0;//how high the cone is based on lifter position
    private int ultraRangeModule = 0; // keeps track of measuring ranges

    public Lifter(Robot parent) {
        super(parent, "lifter", () -> new LifterControl(0,0,true));
        setConfig(
                LifterSettings.makeDefault(),
                LifterHardware.makeDefault(parent.opMode.hardwareMap)
        );
    }

    public Lifter(Robot parent, LifterSettings settings, LifterHardware hardware){
        super(parent, "lifter", () -> new LifterControl(0,0,true));
        setConfig(settings, hardware);
    }

    public void liftWithPower(double power){
        if(Math.abs(power) < getSettings().minRegisterVal) return;

        if(power < 0)
            power *= getSettings().maxDownLiftSpeed;
        else
            power *= getSettings().maxUpLiftSpeed;

        setLiftPosition(getHardware().leftLiftMotor.getCurrentPosition() + (int)power);
    }

    public void setLiftPosition(int position){
        position = Math.min(getSettings().maxLiftPosition, Math.max(getSettings().minLiftPosition, position));
        int diff = getHardware().rightLiftMotor.getCurrentPosition() - getHardware().leftLiftMotor.getCurrentPosition();

        liftTargetPosition = position;

        getHardware().leftLiftMotor.setTargetPosition(position);
        getHardware().rightLiftMotor.setTargetPosition(position + diff);
    }

    public boolean isLiftInTolerance(){
        return Math.abs(liftTargetPosition - getLiftPosition()) <= getSettings().tolerance;
    }

    public void setLiftToTop(){
        setLiftPosition(getSettings().maxLiftPosition);
    }

    public void setLiftToBottom(){
        setLiftPosition(getSettings().minLiftPosition);
    }

    public int getLiftPosition(){
        return getHardware().leftLiftMotor.getCurrentPosition();
    }

    //public double getLeftRange(){return getHardware().leftRange.getDistance(DistanceUnit.CM);}
    //public double getRightRange(){return getHardware().rightRange.getDistance(DistanceUnit.CM);}
    //public double getLeftDistance(){return getHardware().leftDistance.getDistance(DistanceUnit.CM);}
    //public double getRightDistance(){return getHardware().rightDistance.getDistance(DistanceUnit.CM);}
    public double getRightUltra(){return getHardware().rightUltrasonic.getDistanceCm();}
    public double getLeftUltra(){return getHardware().leftUltrasonic.getDistanceCm();}
    public double getMidUltra(){return getHardware().midUltrasonic.getDistanceCm();}


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

    public void setGrabberPower(double power){
        getHardware().leftGrabServo.setPower(power);
        getHardware().rightGrabServo.setPower(power);
    }

    public void setGrabberClosed(boolean closed){
        getHardware().grabServo.setPosition(closed ? getSettings().grabberServoClosePos : getSettings().grabberServoOpenPos);
    }

    //public boolean isClosed(){
    //    return closed;
    //}

    public void constructAutoGrab(){
        autoGrabTask.addStep(() -> setGrabberClosed(true));
        autoGrabTask.addStep(() -> setLiftPosition(conePos + 400));
        autoGrabTask.addStep(() -> setTurnPosition(0.95));
        autoGrabTask.addDelay(500);
        autoGrabTask.addStep(() -> setGrabberClosed(false));
        autoGrabTask.addStep(this::isLiftInTolerance);
        autoGrabTask.addStep(() -> setLiftPosition(conePos));
        autoGrabTask.addStep(() -> setGrabberClosed(true));
        autoGrabTask.addDelay(100);
        autoGrabTask.addStep(() -> setLiftPosition(conePos + 400));
    }

    public void constructConeRanging(){
        coneRangeingTask.addStep(() -> getHardware().leftUltrasonic.measureRange());
        coneRangeingTask.addDelay(30);
        coneRangeingTask.addStep(() -> getHardware().rightUltrasonic.measureRange());
        coneRangeingTask.addDelay(30);
        coneRangeingTask.addStep(() -> getHardware().midUltrasonic.measureRange());
        coneRangeingTask.addDelay(30);
        coneRangeingTask.autoReset = true;
        coneRangeingTask.autoStart = true;
    }

    public void doConeRange() {
        if (Math.abs(getRightUltra() - getLeftUltra()) < 2 && getLeftUltra() < 20) {
            getHardware().blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
//        } else if(getRightUltra() + getLeftUltra() < 40){
//            getHardware().blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        } else if (getRightUltra() < getLeftUltra() && getRightUltra() <= 10){  //closer to right
            getHardware().blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        } else if (getLeftUltra() < getRightUltra() && getLeftUltra() <= 10){  //closer to left
            getHardware().blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.ORANGE);
        } else {
            getHardware().blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        }
    }

    @Override
    public void onRun(LifterControl control) { //TODO separate keeping lifter motor position from onRun
        liftWithPower(control.lifterPower);
        turnWithPower(control.turningPower);
        setGrabberPower(control.closePower);
        setGrabberClosed(control.close);
        doConeRange();
    }

    @Override
    public void onBeanLoad() {
        drive = getBeanManager().getBestMatch(Drive.class, false);
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
        constructAutoGrab();
        constructConeRanging();
    }

    @Override
    public void onStart() {
        setTurnPosition(getSettings().turnServoStartPosition);
    }

    @Override
    public void onStop() {

    }
}
