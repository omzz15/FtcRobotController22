package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.PositionSolverSettings;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.TaskEx;

public class PositionSolver extends Part<Drive, PositionSolverSettings, ObjectUtils.Null> {
//    public static final class Events{
//        public static final String done = "DONE";
//    } //TODO add done event

    protected PositionTracker positionTracker;

    public final ChannelSolver xChannel = new ChannelSolver(this, "x channel") {
        @Override
        public double getError(double target) {
            return target - positionTracker.getCurrentPosition().X;
        }

        @Override
        public void move(DriveControl base) {
            double r = Math.toRadians(positionTracker.getCurrentPosition().Z);
            base.power = base.power.addX(pid.returnValue() * Math.cos(r)).addY(-pid.returnValue() * Math.sin(r));

            parent.parent.parent.opMode.telemetry.addData("x pid", pid.returnValue());
            parent.parent.parent.opMode.telemetry.addData("pid coeff", pid.PIDs);
        }
    };

    public final ChannelSolver yChannel = new ChannelSolver(this, "y channel") {
        @Override
        public double getError(double target) {
            return target - positionTracker.getCurrentPosition().Y;
        }

        @Override
        public void move(DriveControl base) {
            double r = Math.toRadians(positionTracker.getCurrentPosition().Z);
            base.power = base.power.addY(pid.returnValue() * Math.cos(r)).addX(-pid.returnValue() * Math.sin(r));
        }
    };

    public final ChannelSolver rChannel = new ChannelSolver(this, "r channel") {
        @Override
        public double getError(double target) {
            return AngleMath.findAngleError(positionTracker.getCurrentPosition().Z, target);
        }

        @Override
        public void move(DriveControl base) {
            base.power = base.power.withZ(pid.returnValue());
        }
    };

    public PositionSolver(Drive parent) {
        super(parent, "position solver");
        setSettings(PositionSolverSettings.makeDefault());
    }

    public PositionSolver(Drive parent, PositionSolverSettings settings){
        super(parent, "position solver");
        setSettings(settings);
    }

    public void setNewTarget(Vector3 target, boolean resetPID){
        xChannel.setNewTarget(target.X, resetPID);
        yChannel.setNewTarget(target.Y, resetPID);
        rChannel.setNewTarget(target.Z, resetPID);

        triggerEvent(Robot.Events.START);//TODO make this better
    }

    public boolean isDone(){
        return xChannel.isDone() && yChannel.isDone() && rChannel.isDone();
    }

    public void addMoveToTaskEx(Vector3 target, TaskEx task){
        task.addStep(() -> setNewTarget(target, true));
        task.addStep(this::isDone);
    }

    @Override
    public void onBeanLoad() {
        positionTracker = getBeanManager().getBestMatch(PositionTracker.class, false, false);
    }

    @Override
    public void onSettingsUpdate(PositionSolverSettings settings) {
        xChannel.setSettings(settings.xChannelSettings);
        yChannel.setSettings(settings.yChannelSettings);
        rChannel.setSettings(settings.rChannelSettings);
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
