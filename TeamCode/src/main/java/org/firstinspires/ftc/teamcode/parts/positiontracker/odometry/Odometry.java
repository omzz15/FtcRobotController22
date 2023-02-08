package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import java.util.function.Supplier;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.task.core.Group;

public class Odometry extends LoopedPartImpl<PositionTracker, OdometrySettings, OdometryHardware> {
    int lastY1Pos, lastY2Pos, lastXPos, last;

    public Odometry(PositionTracker parent, OdometrySettings settings, OdometryHardware hardware) {
        super(parent, "odometry");
        setConfig(settings, hardware);
    }

    public Odometry(PositionTracker parent) {
        super(parent, "odometry");
        //TODO make default
    }

    private double getHeadingFromOdometry(){
        double angle = ;
        return ;
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {}

    @Override
    public void onStart() {
        lastXPos = getHardware().XWheel.getCurrentPosition();
        lastY1Pos = getHardware().YWheel1.getCurrentPosition();
        lastY2Pos = getHardware().YWheel2.getCurrentPosition();
    }

    @Override
    public void onStop() {

    }
}
