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
import om.self.ezftc.utils.Vector3;
import om.self.task.core.TaskEx;

@Autonomous(name="Test Autonomous", group="Test")
public class TestAutonomousREAL extends LinearOpMode{

    double tileSide = 23.5;
    public Vector3 tileToInch(Vector3 p){
        return new Vector3(p.X * tileSide, p.Y * tileSide, p.Z);
    }

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
        s.onStart();

        // inject initial autonomous field start position
        //Vector3 fieldStartPos = new Vector3(-36,63,-90);
        s.slamraFieldStart = tileToInch(new Vector3(-1.5,2.68,90));

        while (!isStarted()) {
            s.updateSlamraPosition();
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("raw position", s.slamraRawPose);
            telemetry.addData("final position", s.slamraFinal);
            r.opMode.telemetry.update();
            sleep(50);
        }

        r.start();
        s.setupFieldOffset();

        TaskEx moveToPositionTask = new TaskEx("auto task");

        // start position (-1.5,2.68,-90)
        // cone pos is (-1.5, 0.5, -90)
        // pole position (not the game) (position to capture) is roughly or -1.26 , 0.16
        Vector3 blueLoadedPrep = new Vector3(-1.5,.5,90);
        Vector3 blueTallPrep = new Vector3(-1.5,0.5,180);
        Vector3 blueTall = new Vector3(-1.25, 0.15, 135);
        Vector3 blueStack = new Vector3(-2.68,.5,180);
        //*************************************************
        // position x and y based on whole tiles
        // start position (-1.5,2.68,-90)
        Vector3[] position = {
                blueLoadedPrep,
                blueTall,
                blueTallPrep,
                blueStack,
                blueTallPrep,
                blueTall,
                blueTallPrep,
                blueStack,
                blueTallPrep,
                blueTall
        };
        for (Vector3 p : position)
            positionSolver.addMoveToTaskEx(tileToInch(p), moveToPositionTask);

        while (opModeIsActive()) {
            r.run();
            moveToPositionTask.run();
            //if(gamepad1.y) pt.setAngle(0);
            telemetry.addData("position", pt.getCurrentPosition());
            //telemetry.addData("raw position", s.slamraRawPose);
            //telemetry.addData("tilt position", l.getCurrentTurnPosition());
            //telemetry.addData("is closed", l.isClosed());
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
