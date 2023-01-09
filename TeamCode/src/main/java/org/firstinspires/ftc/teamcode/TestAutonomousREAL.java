package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking.EncoderTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Constants;
import om.self.ezftc.utils.Vector;
import om.self.ezftc.utils.Vector3;
import om.self.task.other.TimedTask;

@Autonomous(name="Test Autonomous", group="Test")
public class TestAutonomousREAL extends LinearOpMode{

    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
        //new HeaderKeeper(d);
        PositionTracker pt = new PositionTracker(r);
        Slamra s = new Slamra(pt);
        //new EncoderTracker(pt);
        Lifter l = new Lifter(r);
        PositionSolver positionSolver = new PositionSolver(d);

        DecimalFormat df = new DecimalFormat("#0.0");
        r.init();

        while (!isStarted()) {
            s.updateSlamraPosition();
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("raw position", s.slamraRawPose);
            telemetry.addData("final position", s.slamraFinal);
            r.opMode.telemetry.update();
            sleep(50);
        }
        r.start();

        TimedTask container = new TimedTask("container");
        TimedTask autoTask = new TimedTask("auto task");

        container.addTimedStep(autoTask::run, 25000);
        container.addStep(() -> System.out.println("done!!"));

        positionSolver.setNewTarget(pt.getCurrentPosition(), true);
//        l.addAutoGrabPre(autoTask, 0);
//        l.addAutoOpenGrabber(autoTask);
        l.addAutoGrabToTask(autoTask, 0);
//         positionSolver.addMoveToTaskEx(blueLoadedPrep, autoTask);
//         positionSolver.addMoveToTaskEx(blueTall, autoTask);
//         positionSolver.addMoveToTaskEx(blueTallPrep, autoTask);
        // l.addAutoDropPre(autoTask, 2060);
        // l.addAutoDrop(autoTask);
//         positionSolver.addMoveToTaskEx(blueStack, autoTask);

        //**************** Movement *************
        // position x and y based on whole tiles
        // start position (-1.5,2.68,90)
        //***************************************
        /*
        Vector3 blueLoadedPrep = new Vector3(-1.5,.5,90);
        Vector3 blueTallPrep = new Vector3(-1.5,0.5,180);
        Vector3 blueTall = new Vector3(-1.25, 0.25, 135);
        Vector3 blueStack = new Vector3(-2.68,.5,180);

        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueLoadedPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTall), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTallPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueStack), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTallPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTall), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTallPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueStack), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTallPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(blueTall), autoTask);
*/
        while (opModeIsActive()) {
            r.run();
            //container.run();
            autoTask.run();

            //if(gamepad1.y) pt.setAngle(0);
            telemetry.addData("position", pt.getCurrentPosition());
            //telemetry.addData("raw position", s.slamraRawPose);
            //telemetry.addData("tilt position", l.getCurrentTurnPosition());
            //telemetry.addData("is closed", l.isClosed());
            //telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
            //telemetry.addData("lift position:",df.format(l.getLiftPosition()));
            //telemetry.addData("Ultra [Left : Mid : Right]", "["
            //        + df.format(l.getLeftUltra()) + " : "
            //        + df.format(l.getMidUltra()) + " : "
            //        + df.format(l.getRightUltra()) +"]");

//            if(gamepad2.dpad_left){
//                l.getSettings().rightTurnServoOffset -= 0.0001;
//            }
//            if(gamepad2.dpad_right){
//                l.getSettings().rightTurnServoOffset += 0.0001;
//            }

            if(gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if(gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());

            telemetry.update();
        }
        r.stop();
    }
}
