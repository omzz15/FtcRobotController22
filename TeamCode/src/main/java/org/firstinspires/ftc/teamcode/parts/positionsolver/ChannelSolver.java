package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.ChannelSolverSettings;


import java.util.function.Consumer;

import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.EventManager;

public abstract class ChannelSolver<PARENT extends ControllablePart<PARENT,?,?,CONTROL>, CONTROL> extends Part<PARENT, ChannelSolverSettings, ObjectUtils.Null> {
    public final class Events{
        public final String complete = getName() + "_COMPLETE";
        public final String timedOut = getName() + "_TIMEOUT";
    }

    public final class Controllers{
        public final String driveController = getName() + " controller";
    }

    public final Events events;
    public final Controllers controllers;

    private double target;

    private int timesInTolerance;
    private double startTime;

    private boolean isRunning;

    private PID pid = new PID();

    public ChannelSolver(PARENT parent, String name) {
        super(parent, name);
        events = new Events();
        controllers = new Controllers();
    }

    @Override
    public void onSettingsUpdate(ChannelSolverSettings settings) {
        pid.PIDs = settings.PIDCoefficients;
        if(settings.alwaysRun){
            parent.addController(controllers.driveController, getAlwaysRun());
        }
    }

    public abstract double getError(double target);

    public abstract Vector3 move(CONTROL base);

//    public boolean isInTolerance(){
//        return timesInTolerance >= getSettings().timesInTolerance;
//    }

//    public boolean isInTime(){
//        return getSettings().maxRuntime < 0 || System.currentTimeMillis() - startTime < getSettings().maxRuntime;
//    }

//    public boolean isDone(){
//        return isInTolerance() || !isInTime();
//    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {
        if(getSettings().alwaysRun){
            parent.addController(controllers.driveController, getAlwaysRun());
        }
    }

    private Consumer<CONTROL> getRun(){
        return (base) -> {
            double error = getError(target);
            pid.updatePID(error);

            if(error <= getSettings().tolerance) {
                timesInTolerance ++;
                if(timesInTolerance == getSettings().timesInTolerance)
                    triggerEvent(events.complete);
            }
            else {
                timesInTolerance = 0;
            }

            if(System.currentTimeMillis() - startTime >= getSettings().maxRuntime)
                triggerEvent(events.timedOut);

            move(base);
        };
    }

    private Consumer<CONTROL> getAlwaysRun(){
        return (base) -> {
            double error = getError(target);
            pid.updatePID(error);

            if(error <= getSettings().tolerance) {
                timesInTolerance ++;
                if(timesInTolerance == getSettings().timesInTolerance)
                    triggerEvent(events.complete);
            }
            else {
                timesInTolerance = 0;
            }

            move(base);
        };
    }

    @Override
    public void onStop() {
        parent.queRemoveController(controllers.driveController);
    }
}
