package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.ChannelSolverSettings;


import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Vector3;

public abstract class ChannelSolver<PARENT extends ControllablePart<PARENT,?,?,CONTROL>, CONTROL> extends Part<PARENT, ChannelSolverSettings, ObjectUtils.Null> {
    private double targetPosition;

    private int timesInTolerance;
    private double startTime;

    private boolean isRunning;

    private PID pid = new PID();

    public ChannelSolver(PARENT parent, String name) {
        super(parent, name);
    }

    @Override
    public void onSettingsUpdate(ChannelSolverSettings settings) {
        pid.PIDs = settings.PIDCoefficients;
        if(settings.alwaysRun){
            parent.addController(getName() + " controller", (base) -> {
                pid.updatePID(targetPosition - getCurrentValue());
                move(base);
            });
        }
    }

    public void start(){
        parent.parent.addController();
    }

    public abstract double getCurrentValue();

    public abstract Vector3 move(CONTROL base);

    public boolean isInTolerance(){
        return timesInTolerance >= getSettings().timesInTolerance;
    }
}
