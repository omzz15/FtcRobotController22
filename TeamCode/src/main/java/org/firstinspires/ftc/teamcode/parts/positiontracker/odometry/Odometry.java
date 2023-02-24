package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTicket;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.VectorMath;

public class Odometry extends LoopedPartImpl<PositionTracker, OdometrySettings, OdometryHardware> {
    int lastLeftYPos, lastRightYPos, lastXPos;
    double cumulativeDistance = 0;

    public Odometry(PositionTracker parent, OdometrySettings settings, OdometryHardware hardware) {
        super(parent, "odometry");
        setConfig(settings, hardware);
    }

    public Odometry(PositionTracker parent) {
        super(parent, "odometry");
        //TODO make default
        setConfig(OdometrySettings.makeForOdoBot(), OdometryHardware.makeForOdoBot(parent.parent.opMode.hardwareMap));
    }

    private double getAngleFromDiff(int leftYDiff, int rightYDiff){
        return (leftYDiff - rightYDiff) / getSettings().ticksPerRotation;
    }

    @Override
    public void onRun() {
        parent.parent.opMode.telemetry.addData("y odo dist", (getHardware().leftYWheel.getCurrentPosition() + getHardware().rightYWheel.getCurrentPosition()) / 2.0);
        parent.parent.opMode.telemetry.addData("cumulativeDistance", cumulativeDistance);

        int currLeftY = getHardware().leftYWheel.getCurrentPosition();
        int currRightY = getHardware().leftYWheel.getCurrentPosition();
        int currX = getHardware().XWheel.getCurrentPosition();

        int leftYDiff = currLeftY - lastLeftYPos;
        int rightYDiff = currRightY - lastRightYPos;
        int XDiff = currX - lastXPos;

        parent.parent.opMode.telemetry.addData("left y diff", leftYDiff);
        parent.parent.opMode.telemetry.addData("right y diff", rightYDiff);
        parent.parent.opMode.telemetry.addData("x", XDiff);

        Vector3 pos = parent.getCurrentPosition();

        double angle = AngleMath.scaleAngle(pos.Z + getAngleFromDiff(leftYDiff, rightYDiff));

        double XMove = XDiff / getSettings().ticksPerInch;
        double YMove = (leftYDiff + rightYDiff) / (2 * getSettings().ticksPerInch);

        cumulativeDistance += YMove;

        parent.addPositionTicket(Odometry.class, new PositionTicket(VectorMath.translateAsVector2(pos.withZ(angle), XMove, YMove)));

        lastLeftYPos = currLeftY;
        lastRightYPos = currRightY;
        lastXPos = currX;
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {}

    @Override
    public void onStart() {
        lastXPos = getHardware().XWheel.getCurrentPosition();
        lastLeftYPos = getHardware().leftYWheel.getCurrentPosition();
        lastRightYPos = getHardware().rightYWheel.getCurrentPosition();
    }

    @Override
    public void onStop() {

    }
}
