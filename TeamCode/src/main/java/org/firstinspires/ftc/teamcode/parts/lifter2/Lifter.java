package org.firstinspires.ftc.teamcode.parts.lifter2;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterControl;
import org.firstinspires.ftc.teamcode.parts.lifter.hardware.LifterHardware;
import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterSettings;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.StatefullPart;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Range;
import om.self.supplier.consumer.EdgeConsumer;
import om.self.task.core.Group;
import om.self.task.core.TaskEx;
import om.self.task.other.TimedTask;

public class Lifter extends StatefullPart<Robot, LifterSettings, LifterHardware, LifterState> {


    public Lifter(Robot parent) {
        super(parent, "Lifter");
    }

    public Lifter(Robot parent, Group taskManager) {
        super(parent, "Lifter", taskManager);
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

    @Override
    public void onStateUpdate(LifterState lifterState) {

    }

    @Override
    public LifterState sanitizeState(LifterState lifterState) {
        if(lifterState == null || lifterState.grabberPosition == null)
            throw new RuntimeException("lifter state or the values inside can not be null!");

        if()
    }

    public enum PoleHeight{
        LOW,
        MID,
        HIGH
    }

    public enum GrabberPosition{
        CLOSE,
        OPEN,
        WIDE_OPEN
    }
}
