package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Constants;
import om.self.ezftc.utils.Vector;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.Group;
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

        s.updateSlamraPosition();
        Vector3 startupPose = s.slamraRawPose;

        while (!isStarted()) {
            s.updateSlamraPosition();
            //telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("raw position", s.slamraRawPose);
            if(!startupPose.equals(s.slamraRawPose)) telemetry.addLine("***** SLAMRA READY *****");
            //telemetry.addData("final position", s.slamraFinal);
            r.opMode.telemetry.update();
            sleep(50);
        }
        r.start();

        Group container = new Group("container");
        TimedTask autoTask = new TimedTask("auto task", container);
// Running container in background, at end of 25 sec go to park no matter what maybe
//        container.addTimedStep(autoTask::run, 25000);
//        container.addTimedStepep(() -> System.out.println("done!!"));

        positionSolver.setNewTarget(pt.getCurrentPosition(), true);

      /* TESTS:
      l.addAutoDockToTask(autoTask, 0);
        autoTask.addDelay(2000);
        l.addAutoGrabToTask(autoTask);
        autoTask.addDelay(2000);
        l.addAutoPreDropToTask(autoTask, 2);
        autoTask.addDelay(2000);
        l.addAutoDropToTask(autoTask);
      */

        //**************** Movement *************
        // position x and y based on whole tiles
        // start position (-1.5,2.68,90)
        //***************************************

        // dangerous tall pole
        Vector3 rightLoadedPrep = new Vector3(-1.5,.5,90);
        Vector3 rightTallPrep = new Vector3(-1.5,0.5,180);
        Vector3 rightTall = new Vector3(-1.25, 0.25, 135);
        Vector3 rightStack = new Vector3(-2.68,.5,180);

        // alliance side tall pole
        Vector3 start = new Vector3(-1.5,2.68,90);
        Vector3 through1T = new Vector3(-0.5,2.5,90);
        Vector3 through2T = new Vector3(-0.5,2,90);
        Vector3 preloadDepositT = new Vector3(-0.55,1.25,135);
        Vector3 through3T = new Vector3(-0.5,1.5,135);
        Vector3 poleStopT = new Vector3(-0.5,0.5,180);
        Vector3 wallStop = new Vector3(-1.5,0.5,180);
        Vector3 wall = new Vector3(2.68,0.5,180);
        Vector3 poleT = new Vector3(-0.55,0.75,225);


        // alliance side mid pole
        //start position
        Vector3 through1M = new Vector3(-1.5,1.5,90);
        Vector3 through2M = new Vector3(-1.5,1.5,135);
        Vector3 preloadDepositM = new Vector3(-1.23,1.25,135);
        //through2M
        Vector3 through3M = new Vector3(-1.5,1.5,180);
        //wallStop
        //wall
        Vector3 poleM = new Vector3(-1.23,0.75,225);


        // alliance side tall pole
        autoTask.addStep(()->l.setGrabberClosed());
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through1T), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through2T), autoTask);
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(preloadDepositT), autoTask);
        l.addAutoDropToTask(autoTask);
        //dock while moving
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through3T), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wallStop), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wall), autoTask);
        //grab
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleT), autoTask);
        l.addAutoDropToTask(autoTask);
        //dock while moving
        //repeat

        //  dangerous tall pole path
        autoTask.addStep(()->l.setGrabberClosed());
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightLoadedPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightTall), autoTask);
        l.addAutoDropToTask(autoTask);
     //   positionSolver.addMoveToTaskEx(Constants.tileToInch(rightTallPrep), autoTask);
     //   l.addAutoDockToTask(autoTask, 4);
     //   positionSolver.addMoveToTaskEx(Constants.tileToInch(rightStack), autoTask);

        while (opModeIsActive()) {
            r.run();

            container.run();

            //if(gamepad1.y) pt.setAngle(0);
            telemetry.addData("position", pt.getCurrentPosition());
            //telemetry.addData("raw position", s.slamraRawPose);
            //telemetry.addData("tilt position", l.getCurrentTurnPosition());
            //telemetry.addData("is closed", l.isGrabberClosed());
            //telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
            //telemetry.addData("lift position:",df.format(l.getLiftPosition()));
            telemetry.addData("Ultra [Left : Mid : Right]", "["
                    + df.format(l.getLeftUltra()) + " : "
                    + df.format(l.getMidUltra()) + " : "
                    + df.format(l.getRightUltra()) +"]");

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
