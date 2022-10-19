package om.self.ezftc.parts.drive;

import java.util.function.Function;

import om.self.ezftc.core.Part;
import om.self.ezftc.core.RobotPart;
import om.self.ezftc.parts.drive.hardware.DriveHardware;
import om.self.ezftc.parts.drive.settings.DriveSettings;
import om.self.supplier.modifiers.SimpleRampedModifier;

@Part("drive")
public class Drive extends RobotPart<DriveSettings, DriveHardware> {
    private double xTarget = 0;
    private double yTarget = 0;
    private double rTarget = 0;

    private final SimpleRampedModifier xRamp = new SimpleRampedModifier();
    private final SimpleRampedModifier yRamp = new SimpleRampedModifier();
    private final SimpleRampedModifier rRamp = new SimpleRampedModifier();

    private Function<Double, Double> xPowerFilter;
    private Function<Double, Double> yPowerFilter;
    private Function<Double, Double> rPowerFilter;

    private DrivePowerConverter dpc;


    public void setPower(double x, double y, double r){
        xTarget = x;
        yTarget = y;
        rTarget = r;
    }

    /**
     * this method is a direct connection to the drive and bypasses things like motion smoothing
     */
    public void moveRobot(double x, double y, double r){
        double[] pows = dpc.convert(x,y,r);
        getHardware().FLMotor.getMotor().setPower(pows[0]);
        getHardware().FRMotor.getMotor().setPower(pows[1]);
        getHardware().BLMotor.getMotor().setPower(pows[2]);
        getHardware().BRMotor.getMotor().setPower(pows[3]);
    }

//    void headlessMoveRobot(double offset)
//    {
//        double curAngle = -robot.positionTracker.currentPosition.R + offset;
//        curAngle = robot.scaleAngle(curAngle);
//        double gamepadAngle = robot.getAngleFromXY(-gamepad1.left_stick_x, -gamepad1.left_stick_y);
//        double error = -robot.findAngleError(curAngle,gamepadAngle);
//        double power = Math.max(Math.abs(gamepad1.left_stick_x), Math.abs(gamepad1.left_stick_y));
//        double[] XY = robot.getXYFromAngle(error);
//        XY[0] *= power;
//        XY[1] *= power;
//        robot.robotHardware.setMotorsToSeparatePowersArrayList(robot.robotHardware.driveMotors, moveRobotPowers(XY[0],XY[1],gamepad1.right_stick_x, true, true));
//    }

    public void stopRobot(){
        xRamp.setCurrentVal(0);
        yRamp.setCurrentVal(0);
        rRamp.setCurrentVal(0);
        moveRobot(0,0,0);
    }


    @Override
    public void onSettingsUpdate(DriveSettings driveSettings) {
        if(driveSettings.useSmoothing){
            xRamp.setRamp(driveSettings.smoothingValues[0]);
            yRamp.setRamp(driveSettings.smoothingValues[1]);
            rRamp.setRamp(driveSettings.smoothingValues[2]);
            xPowerFilter = xRamp;
            yPowerFilter = yRamp;
            rPowerFilter = rRamp;
        } else {
            xPowerFilter = pow -> pow;
            yPowerFilter = pow -> pow;
            rPowerFilter = pow -> pow;
        }

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

    interface DrivePowerConverter{
        double[] convert(double x, double y, double r);
    }
}
