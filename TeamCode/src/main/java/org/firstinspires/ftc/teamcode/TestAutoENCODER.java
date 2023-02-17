package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.parts.apriltags.Tag;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking.EncoderTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.hardware.PositionTrackerHardware;
import org.firstinspires.ftc.teamcode.parts.positiontracker.settings.PositionTrackerSettings;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;

import java.text.DecimalFormat;
import java.util.function.Function;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Constants;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.Group;
import om.self.task.core.TaskEx;
import om.self.task.other.TimedTask;

@Autonomous(name="autoEncoderTest", group="Test")
public class TestAutoENCODER extends LinearOpMode {
    public Function<Vector3, Vector3> transformFunc;
    public boolean isRight;
    public Vector3 customStartPos;

    EncoderTracker encoderTracker;
    //    Lifter l;
    PositionSolver positionSolver;
    Tag aprilTag;
//    Vector3 pushSignal = new Vector3(-1.5, 0.3, 90);

    public void initAuto() {
        transformFunc = (v) -> v;
        isRight = true;
    }

    private Vector3 tileToInchAuto(Vector3 tiles) {
        return Constants.tileToInch(transformFunc.apply(tiles));
    }

    @Override
    public void runOpMode() {
        initAuto();
        Robot r = new Robot(this);
        Drive d = new Drive(r);

        PositionTrackerSettings pts = PositionTrackerSettings.makeDefault();
//        pts.withPosition(transformFunc.apply(pts.startPosition));
        pts = pts.withPosition(customStartPos != null ? customStartPos : transformFunc.apply(pts.startPosition));
        PositionTracker pt = new PositionTracker(r, pts, PositionTrackerHardware.makeDefault(r));

        encoderTracker = new EncoderTracker(pt);
//        Slamra s = new Slamra(pt);
//        l = new Lifter(r);
        positionSolver = new PositionSolver(d);
        aprilTag = new Tag(r);

        r.init();
//        s.updateSlamraPosition();
//        Vector3 startupPose = s.slamraRawPose;
        while (!isStarted()) {
//            s.updateSlamraPosition();
            aprilTag.onRun();
//            telemetry.addData("raw position", s.slamraRawPose);
//            if(!startupPose.equals(s.slamraRawPose)) telemetry.addLine("***** SLAMRA READY *****");
            telemetry.addLine(String.format("\nDetected tag ID=%s", aprilTag.detectedID));
            telemetry.addLine(String.format("\nPark ID=%s", aprilTag.parkID));
            r.opMode.telemetry.update();
            sleep(50);
        }
        r.start();

        Group container = new Group("container", r.taskManager);
        TimedTask autoTask = new TimedTask("auto task", container);
        TimedTask killer = new TimedTask("killer", container);
        positionSolver.setNewTarget(pt.getCurrentPosition(), true);

////        // AFTER 27 SECONDS, PARK!
//        killer.addDelay(25000);
//        killer.addStep(() -> autoTask.runCommand(Group.Command.PAUSE));
//        park(killer);


        /******** Autonomous Setup ****************
         start position (-1.5,2.68,90)
         Various autonomous paths setup at bottom
         ******************************************/
        //IMPORTANT!! Need to go to parking safe position at the end of all tasks

        parkOnly(autoTask);

        while (opModeIsActive()) {
            r.run();
            //container.run();
            telemetry.addData("position", pt.getCurrentPosition());
//            telemetry.addData("Ultra [Left : Mid : Right]", "["
//                    + df.format(l.getLeftUltra()) + " : "
//                    + df.format(l.getMidUltra()) + " : "
//                    + df.format(l.getRightUltra()) +"]");
            if (gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if (gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());
            telemetry.update();
        }
        r.stop();
    }

    private void parkOnly(TimedTask autoTask) {

        Vector3 locOne = isRight ? new Vector3(-0.5, 1.5, 90) : new Vector3(-2.5, 1.5, 90);
        Vector3 locTwo = new Vector3(-1.5, 1.5, 90);
        Vector3 locThree = isRight ? new Vector3(-2.5, 1.5, 90) : new Vector3(-0.5, 1.5, 90);
        int parkId = aprilTag.parkID;
        //Parking task
//        autoTask.addDelay(15000);
        positionSolver.addMoveToTaskEx(tileToInchAuto(new Vector3(-1.5, 1.5, 90)), autoTask);
        if (!(parkId == 2))
            positionSolver.addMoveToTaskEx(tileToInchAuto(parkId == 1 ? locOne : locThree), autoTask);
        autoTask.addStep(() -> positionSolver.triggerEvent(Robot.Events.STOP));
    }

}
