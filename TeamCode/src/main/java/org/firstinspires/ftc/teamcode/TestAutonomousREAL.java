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

import java.text.DecimalFormat;
import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.hardware.motor.MotorSettings;
import om.self.task.core.TaskEx;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@Autonomous(name="Test Autonomous", group="Test")
public class TestAutonomousREAL extends LinearOpMode{
    @Override
    double tileSide = 23.5;

    public Vector3 tilePos(Vector3 p){
        return new Vector3(p.X * tileSide, p.Y * tileSide, p.Z);
    }

    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
        //new HeaderKeeper(d);
        PositionTracker pt = new PositionTracker(r);
        //new Slamra(pt);
        //new EncoderTracker(pt);
        Lifter l = new Lifter(r);
        new LifterTeleop(l);
        DecimalFormat df = new DecimalFormat("#0.0");
        r.init();
        //new Slamra(pt);

        waitForStart();
        r.start();

        PositionSolver positionSolver = new PositionSolver(d);

        TaskEx moveToPositionTask = new TaskEx("auto task");

        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);
        positionSolver.addMoveToTaskEx(tilePos(new Vector3(1.5,2.5,0)), moveToPositionTask);



        while (opModeIsActive()) {
            r.run();
            //if(gamepad1.y) pt.setAngle(0);
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("tilt position", l.getCurrentTurnPosition());
            //telemetry.addData("is closed", l.isClosed());
            telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
            //telemetry.addData("rightRange", df.format(l.getRightRange()));
            //telemetry.addData("leftRange", df.format(l.getLeftRange()));
            //telemetry.addData("leftDistance", df.format(l.getLeftDistance()));
            //telemetry.addData("rightDistance", df.format(l.getRightDistance()));
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
