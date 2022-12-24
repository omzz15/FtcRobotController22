package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterTeleop;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import java.text.DecimalFormat;
import om.self.ezftc.core.Robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@Autonomous(name="Test Autonomous", group="Test")
@Disabled
public class TestAutonomous extends LinearOpMode{
    @Override
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

        while (opModeIsActive()) {
            long start = System.currentTimeMillis();
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
        r.opMode.telemetry.addData("time", System.currentTimeMillis() - start);

        telemetry.update();
    }

        r.stop();
}
}
