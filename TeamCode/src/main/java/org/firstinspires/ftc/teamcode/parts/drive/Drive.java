package org.firstinspires.ftc.teamcode.parts.drive;

import org.firstinspires.ftc.teamcode.parts.drive.hardware.DriveHardware;
import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveSettings;

import java.util.function.Function;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.SimpleRampedModifier;

public class Drive extends ControllablePart<Robot, DriveSettings, DriveHardware, DriveControl> {
    private double xTarget = 0;
    private double yTarget = 0;
    private double rTarget = 0;

    private final SimpleRampedModifier xRamp = new SimpleRampedModifier();
    private final SimpleRampedModifier yRamp = new SimpleRampedModifier();
    private final SimpleRampedModifier rRamp = new SimpleRampedModifier();

    private Function<Double, Double> xPowerFilter = pow -> pow;
    private Function<Double, Double> yPowerFilter = pow -> pow;
    private Function<Double, Double> rPowerFilter = pow -> pow;

    private DrivePowerConverter dpc;

    private boolean smoothing = false;

    public Drive(Robot robot){
        super(robot, "drive");
        setConfig(
                DriveSettings.makeDefault(),
                DriveHardware.makeDefault(robot.opMode.hardwareMap)
        );
    }

    public Drive(Robot robot, DriveSettings driveSettings, DriveHardware driveHardware) {
        super(robot, "drive");
        setConfig(driveSettings, driveHardware);
    }

    public boolean isSmoothing() {
        return smoothing;
    }

    public void setSmoothing(boolean smoothing) {
        this.smoothing = smoothing;
        if(!smoothing){
            xPowerFilter = pow -> pow;
            yPowerFilter = pow -> pow;
            rPowerFilter = pow -> pow;
            return;
        }

        xPowerFilter = xRamp;
        yPowerFilter = yRamp;
        rPowerFilter = rRamp;
    }

    public Vector3 getTargetPower(){
        return new Vector3(xTarget, yTarget, rTarget);
    }

    public int[] getMotorPositions(){
        return new int[]{
            getHardware().topLeftMotor.getCurrentPosition(),
                getHardware().topRightMotor.getCurrentPosition(),
                getHardware().bottomLeftMotor.getCurrentPosition(),
                getHardware().bottomRightMotor.getCurrentPosition()
        };
    }

    public void setTargetPower(double x, double y, double r){
        xTarget = x;
        yTarget = y;
        rTarget = r;
    }

    public void setTargetPower(Vector3 powers){
        setTargetPower(powers.X, powers.Y, powers.Z);
    }

    /**
     * this method is a direct connection to the drive and bypasses things like motion smoothing. Use {@link Drive#setTargetPower(double, double, double)}
     */
    public void moveRobot(double x, double y, double r){
        double[] pows = dpc.convert(x,y,r);
        getHardware().topLeftMotor.setPower(pows[0]);
        getHardware().topRightMotor.setPower(pows[1]);
        getHardware().bottomLeftMotor.setPower(pows[2]);
        getHardware().bottomRightMotor.setPower(pows[3]);
    }

    /**
     * just like the original moveRobot with different parameter type
     * @param powers the power that you want to move the robot at
     * @see Drive#moveRobot(double, double, double)
     */
    public void moveRobot(Vector3 powers){
        moveRobot(powers.X, powers.Y, powers.Z);
    }


    public void stopRobot(){
        xRamp.setCurrentVal(0);
        yRamp.setCurrentVal(0);
        rRamp.setCurrentVal(0);
        moveRobot(0,0,0);
    }

    @Override
    public void onSettingsUpdate(DriveSettings driveSettings) {
        //set smoothing values
        xRamp.setRamp(driveSettings.smoothingValues.X);
        yRamp.setRamp(driveSettings.smoothingValues.Y);
        rRamp.setRamp(driveSettings.smoothingValues.Z);

        //set the conversion function
        switch (driveSettings.driveMode){
            case TANK:
                dpc = (x, y, r) -> (
                        new double[]{
                                x + r,
                                x - r,
                                x + r,
                                x - r
                        }
                );
                break;
            case MECANUM:
                dpc = (x, y, r) -> (
                        new double[]{
                                x + y + r,
                                -x + y - r,
                                -x + y + r,
                                x + y - r
                        }
                );
                break;
            case OMNI:
                //experimental
                dpc = (x, y, r) -> (
                        new double[]{
                                y + r,
                                x + r,
                                y + r,
                                x + r
                        }
                );
                break;
        }
    }

    @Override
    public void onRun() {
        moveRobot(xPowerFilter.apply(xTarget), yPowerFilter.apply(yTarget), rPowerFilter.apply(rTarget));
    }

    @Override
    public void onRun(DriveControl control) {
        if(control.stop) stopRobot();
        else setTargetPower(control.power);
    }

    interface DrivePowerConverter{
        double[] convert(double x, double y, double r);
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
