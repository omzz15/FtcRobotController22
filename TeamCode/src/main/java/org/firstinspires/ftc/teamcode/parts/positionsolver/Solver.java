package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.ChannelSolverSettings;


import java.util.function.Consumer;

import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.EventManager;

public abstract class Solver<PARENT extends Part<?,?,?>, CONTROL> extends Part<PARENT, ChannelSolverSettings, ObjectUtils.Null> {
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
    private long startTime;

    private boolean successful;

    public final PID pid = new PID();

    public Solver(PARENT parent, String name) {
        super(parent, name);
        events = new Events();
        controllers = new Controllers();

        //add event things
        getEventManager().attachToEvent(events.complete, "set vars", () -> {
            successful = true;
        });

        getEventManager().attachToEvent(events.timedOut, "set vars and stop part", () -> {
            successful = false;
            triggerEvent(EventManager.CommonEvent.STOP);
        });
    }

    @Override
    public void onSettingsUpdate(ChannelSolverSettings settings) {
        pid.PIDs = settings.PIDCoefficients;
        if(getSettings().alwaysRun)
            getEventManager().detachFromEvent(events.complete, "stop part");
        else
            getEventManager().attachToEvent(events.complete, "stop part", () -> getEventManager().triggerEvent(EventManager.CommonEvent.STOP));
    }

    public abstract double getError(double target);

    public abstract void move(CONTROL base);

    public abstract ControllablePart<?,?,?, CONTROL> getControlled();

    public boolean isSuccessful(){
        return successful;
    }

    public void setNewTarget(double target){
        this.target = target;
        reset();
    }

    public void reset(boolean resetPID){
        if(resetPID) pid.resetErrors();
        timesInTolerance = 0;
        startTime = System.currentTimeMillis();
        successful = false;
    }

    public void reset(){
        reset(true);
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {
        reset();
        getControlled().addController(controllers.driveController, getRun());
    }

    private Consumer<CONTROL> getRun(){
        if(getSettings().maxRuntime < 0 || getSettings().alwaysRun)
            return (base) -> {
                double error = getError(target);
                pid.updatePID(error);

                if(error <= getSettings().tolerance) {
                    timesInTolerance ++;
                    if(timesInTolerance == getSettings().reqTimesInTolerance)
                        triggerEvent(events.complete);
                }
                else
                    timesInTolerance = 0;

                move(base);
            };

        return (base) -> {
            double error = getError(target);
            pid.updatePID(error);

            if(error <= getSettings().tolerance) {
                timesInTolerance ++;
                if(timesInTolerance == getSettings().reqTimesInTolerance)
                    triggerEvent(events.complete);
            }
            else
                timesInTolerance = 0;

            if(System.currentTimeMillis() - startTime >= getSettings().maxRuntime)
                triggerEvent(events.timedOut);

            move(base);
        };
    }

    @Override
    public void onStop() {
        getControlled().removeController(controllers.driveController);
    }
}
