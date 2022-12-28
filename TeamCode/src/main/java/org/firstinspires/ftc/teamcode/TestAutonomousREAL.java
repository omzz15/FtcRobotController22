package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterTeleop;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.SlamraSettings;

import java.text.DecimalFormat;
import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.hardware.motor.MotorSettings;
import om.self.task.core.TaskEx;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@Autonomous(name="Test Autonomous", group="Test")
public class TestAutonomousREAL extends LinearOpMode{

    double tileSide = 23.5;
    public Vector3 tilePos(Vector3 p){
        return new Vector3(p.X * tileSide, p.Y * tileSide, p.Z);
    }
    SlamraSettings slamrasettings =  new SlamraSettings(
            new Vector3(0,0,0),
            0.1
    );
    Vector3 fieldStartPos = new Vector3(-36,63,-90);

    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
        //new HeaderKeeper(d);
        PositionTracker pt = new PositionTracker(r);
        Slamra s = new Slamra(pt);
        //new EncoderTracker(pt);
        Lifter l = new Lifter(r);

        DecimalFormat df = new DecimalFormat("#0.0");
        r.init();
        s.onStart();

        // inject initial autonomous field start position
        s.slamraFieldStart = fieldStartPos;

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

        PositionSolver positionSolver = new PositionSolver(d);
        TaskEx moveToPositionTask = new TaskEx("auto task");

        positionSolver.addMoveToTaskEx(tilePos(new Vector3(0.5,0,90)), moveToPositionTask);
        /*
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        */

        //moveToPositionTask.run();


        while (opModeIsActive()) {
            r.run();
            //if(gamepad1.y) pt.setAngle(0);
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("raw position", s.slamraRawPose);
            telemetry.addData("tilt position", l.getCurrentTurnPosition());
            //telemetry.addData("is closed", l.isClosed());
            telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
            telemetry.addData("lift position:",df.format(l.getLiftPosition()));
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
