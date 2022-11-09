package org.firstinspires.ftc.teamcode.parts.drive.headerkeeper;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.PID;

public class HeaderKeeper extends Part<Drive> {
    private PositionTracker pt;
    private HeaderKeeperSettings settings;

    private double lastHeading;
    private PID pid;


    public HeaderKeeper(Drive parent, HeaderKeeperSettings settings) {
        super(parent, "header keeper");
        parent.addDependency(PositionTracker.class);
        this.settings = settings;
    }

    public HeaderKeeper(Drive parent) {
        super(parent, "header keeper");
        parent.addDependency(PositionTracker.class);
        this.settings = HeaderKeeperSettings.makeDefault();
    }

    @Override
    public void onInit() {
        pid = new PID(settings.pidCoefficients, -0.3, 0.3);
    }

    @Override
    public void onStart() {
        pt = getParent().getDependency(PositionTracker.class);
        getParent().addController("header keeper", (control) -> {
            if(control.power.Z > settings.minRegisterVal){
                lastHeading = pt.getCurrentPosition().Z;
                pid.resetErrors();
                return control;
            }

            pid.updatePID(AngleMath.findAngleError(pt.getCurrentPosition().Z, lastHeading));
            return new DriveControl(control.power.withZ(pid.returnValue()), control.stop);
        });
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onStop() {

    }
}
