package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTicket;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import java.util.function.Supplier;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.Group;

public class Odometry extends LoopedPartImpl<PositionTracker, OdometrySettings, OdometryHardware> {
    int lastLeftYPos, lastRightYPos, lastXPos;

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
        return leftYDiff - rightYDiff / getSettings().ticksPerRotation;
    }

    @Override
    public void onRun() {
        int leftYDiff = getHardware().leftYWheel.getCurrentPosition() - lastLeftYPos;
        int rightYDiff = getHardware().rightYWheel.getCurrentPosition() - lastRightYPos;
        int XDiff = getHardware().XWheel.getCurrentPosition() - lastXPos;

        parent.parent.opMode.telemetry.addData("left y diff", leftYDiff);
        parent.parent.opMode.telemetry.addData("right y diff", rightYDiff);
        parent.parent.opMode.telemetry.addData("x", XDiff);

        Vector3 pos = parent.getCurrentPosition();

        double angle = AngleMath.scaleAngle(pos.Z) + getAngleFromDiff(leftYDiff, rightYDiff);

        double robotXMovement = XDiff / getSettings().ticksPerInch;
        double robotYMovement = (leftYDiff + rightYDiff) / (2 * getSettings().ticksPerInch);

        double radAngle = Math.toRadians(angle);

        double fieldXMovement = Math.cos(radAngle) * robotXMovement + Math.sin(radAngle) * robotYMovement;
        double fieldYMovement = Math.cos(radAngle) * robotYMovement + Math.sin(radAngle) * robotXMovement;

        parent.addPositionTicket(new PositionTicket(pos.addX(fieldXMovement).addY(fieldYMovement).withZ(angle)));

        lastLeftYPos = getHardware().leftYWheel.getCurrentPosition();
        lastRightYPos = getHardware().rightYWheel.getCurrentPosition();
        lastXPos = getHardware().XWheel.getCurrentPosition();
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
