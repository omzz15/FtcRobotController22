package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.parts.apriltags.Tag;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterControl;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positionsolver.Solver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Constants;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.Group;
import om.self.task.core.TaskEx;
import om.self.task.other.TimedTask;

@Autonomous(name="Test Autonomous", group="Test")
public class TestAutonomousREAL extends LinearOpMode{
    Lifter l;
    PositionSolver positionSolver;
    Tag aprilTag;



    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
        PositionTracker pt = new PositionTracker(r);
        Slamra s = new Slamra(pt);
        l = new Lifter(r);
        positionSolver = new PositionSolver(d);
        aprilTag = new Tag(r);

        DecimalFormat df = new DecimalFormat("#0.0");
        r.init();
        s.updateSlamraPosition();
        Vector3 startupPose = s.slamraRawPose;
        Vector3 pushSignal = new Vector3(-1.5, 0, 90);
        while (!isStarted()) {
            s.updateSlamraPosition();
            aprilTag.onRun();
            telemetry.addData("raw position", s.slamraRawPose);
            if(!startupPose.equals(s.slamraRawPose)) telemetry.addLine("***** SLAMRA READY *****");
            telemetry.addLine(String.format("\nDetected tag ID=%s", aprilTag.detectedID));
            telemetry.addLine(String.format("\nPark ID=%s", aprilTag.parkID));
            r.opMode.telemetry.update();
            sleep(50);
        }
        r.start();

        Group container = new Group("container");
        TimedTask autoTask = new TimedTask("auto task", container);
        TimedTask killer = new TimedTask("killer", container);
        positionSolver.setNewTarget(pt.getCurrentPosition(), true);

//        // AFTER 27 SECONDS, PARK!
//        killer.addDelay(27000);
//        killer.addStep(() -> autoTask.runCommand(Group.Command.PAUSE));
//        park(killer);


        /******** Autonomous Setup ****************
         start position (-1.5,2.68,90)
         Various autonomous paths setup at bottom
        ******************************************/
        //IMPORTANT!! Need to go to parki3ng safe position at the end of all tasks
l.setCone(4);
        autoTask.addStep(() -> {
            LifterControl.flipOpen = 0;
            l.setGrabberClosed();
        });
        positionSolver.addMoveToTaskEx(Constants.tileToInch(pushSignal), autoTask);
        sideDangerousCycle(autoTask);

        while (opModeIsActive()) {
            r.run();
            container.run();
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("Ultra [Left : Mid : Right]", "["
                    + df.format(l.getLeftUltra()) + " : "
                    + df.format(l.getMidUltra()) + " : "
                    + df.format(l.getRightUltra()) +"]");
            if(gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if(gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());
            telemetry.update();
        }
        r.stop();
    }

    private void park(TaskEx autoTask){
        //Parking locations (for bottom left start):
        Vector3 locOne = new Vector3(-0.5, .5, 90);
        Vector3 locTwo = new Vector3(-1.5, .5, 90);
        Vector3 locThree = new Vector3(-2.5, .5, 90);
        int parkId = aprilTag.parkID;
        //Parking task
        positionSolver.addMoveToTaskEx(Constants.tileToInch(new Vector3(-1.5, .5, 90)), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(parkId == 1 ? locOne : parkId == 2 ? locTwo : locThree), autoTask);
    }

    private void sideTallCycle(TimedTask autoTask){
        // alliance side tall pole
        Vector3 start = new Vector3(-1.5,2.68,90);
        Vector3 through1T = new Vector3(-0.5,2.5,90);
        Vector3 through2T = new Vector3(-0.5,2,90);
        Vector3 preloadDepositT = new Vector3(-0.55,1.25,135);
        Vector3 through3T = new Vector3(-0.5,1.5,135);
        Vector3 poleStopT = new Vector3(-0.5,0.5,180);
        Vector3 wallStop = new Vector3(-1.5,0.5,180);
        Vector3 wall = new Vector3(-2.68,0.5,180);
        Vector3 poleT = new Vector3(-0.55,0.75,225);

        // alliance side tall pole
        autoTask.addStep(()->l.setGrabberClosed());
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through1T), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through2T), autoTask);
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(preloadDepositT), autoTask);
        l.addAutoDropToTask(autoTask);
        //dock while moving
        positionSolver.addMoveToTaskEx(Constants.tileToInch(through3T), autoTask);
        l.addAutoDockToTask(autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wallStop), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wall), autoTask);
        //grab
        l.addAutoGrabToTask(autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleT), autoTask);
        l.addAutoDropToTask(autoTask);
        l.addAutoDockToTask(autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wallStop), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(wall), autoTask);
        //grab
        l.addAutoGrabToTask(autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleStopT), autoTask);
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(poleT), autoTask);
        l.addAutoDropToTask(autoTask);
        //dock while moving
        //repeat
    }

    private void setupSideMid(TimedTask autoTask){
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
    }

    private void  sideDangerousCycle(TimedTask autoTask){
        setupSideDangerous(autoTask);
        setupSideDangerous(autoTask);
        setupSideDangerous(autoTask);
        setupSideDangerous(autoTask);
        setupSideDangerous(autoTask);
    }

    private void setupSideDangerous(TimedTask autoTask){
        // dangerous tall pole
        Vector3 rightLoadedPrep = new Vector3(-1.5,.5,90);
        Vector3 rightTallPrep = new Vector3(-1.5,0.5,180);
        Vector3 rightTall = new Vector3(-1.21, 0.21, 135);
        Vector3 rightStack = new Vector3(-2.55,.5,180);

        //  dangerous tall pole path
        l.addAutoPreDropToTask(autoTask, 3);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightLoadedPrep), autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightTall), autoTask);
        autoTask.addDelay(2000);
//        positionSolver.triggerEvent(Robot.Events.STOP);
        l.addAutoDropToTask(autoTask);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightTallPrep), autoTask);
        l.addAutoDockToTask(autoTask);
        autoTask.addDelay(500);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightStack), autoTask);
        l.addAutoGrabToTask(autoTask);
        autoTask.addDelay(1000);
        positionSolver.addMoveToTaskEx(Constants.tileToInch(rightLoadedPrep),autoTask);
        //pull away from stack after lifiting!!!
    }
}


/***************************************************************************************************
 * Old Telemetry from active OPMODE
 *             //telemetry.addData("raw position", s.slamraRawPose);
 *             //telemetry.addData("tilt position", l.getCurrentTurnPosition());
 *             //telemetry.addData("is closed", l.isGrabberClosed());
 *             //telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
 *             //telemetry.addData("lift position:",df.format(l.getLiftPosition()));
 *
 *                         //condition ? run if true : condition ? run if true : condition ? run if true : throw new RuntimeError("im dead :(");
 *
 * //            if(gamepad2.dpad_left){
 * //                l.getSettings().rightTurnServoOffset -= 0.0001;
 * //            }
 * //            if(gamepad2.dpad_right){
 * //                l.getSettings().rightTurnServoOffset += 0.0001;
 * //            }
 */