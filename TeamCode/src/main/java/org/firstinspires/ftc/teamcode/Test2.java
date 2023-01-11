package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import org.firstinspires.ftc.teamcode.parts.apriltags.Tag;
import org.firstinspires.ftc.teamcode.parts.bulkread.BulkRead;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveTeleop;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterTeleop;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.slamra.Slamra;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.task.other.TimedTask;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="lifter Test", group="Linear Opmode")
public class Test2 extends LinearOpMode {
    double tileSide = 23.5;
    public Vector3 tiletoField(Vector3 p){
        return new Vector3(p.X * tileSide, p.Y * tileSide, p.Z);
    }

    public Vector3 fieldToTile(Vector3 p){
        return new Vector3(p.X / tileSide, p.Y / tileSide, p.Z);
    }

    Vector3 fieldStartPos = new Vector3(-36,63,90);

    @Override
    public void runOpMode() {
        long start;

        Robot r = new Robot(this);
        new BulkRead(r);
        Drive d = new Drive(r);
        new DriveTeleop(d);
        //new HeaderKeeper(d);

        PositionTracker pt = new PositionTracker(r);
        Slamra s = new Slamra(pt);
        //new EncoderTracker(pt);

        Lifter l = new Lifter(r);
        new LifterTeleop(l);

        //Tag t = new Tag(r);

        DecimalFormat df = new DecimalFormat("#0.0");
        TimedTask autoTask = new TimedTask("auto task");
        TimedTask getConeTask = new TimedTask("get cone");
        TimedTask deliverConeTask = new TimedTask("deliver cone");

        r.init();
        s.onStart();
        // inject initial autonomous field start position
        s.slamraFieldStart = fieldStartPos;

        while (!isStarted()) {
            s.updateSlamraPosition();
            telemetry.addData("pt position", pt.getCurrentPosition());
            telemetry.addData("raw position", s.slamraRawPose);
            telemetry.addData("final position", s.slamraFinal);
            r.opMode.telemetry.update();
            sleep(50);
        }

        r.start();
        s.setupFieldOffset();

//        l.addAutoDropPre(deliverConeTask,2060);
//        l.addAutoDrop(deliverConeTask);
//
//        l.addAutoGrabPre(getConeTask,0);
//        l.addAutoGrabToTask(getConeTask,0);

        while (opModeIsActive()) {
            start = System.currentTimeMillis();
            r.run();

            if(gamepad1.y) getConeTask.run();
            if(gamepad1.b) deliverConeTask.run();
            telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("tile position", fieldToTile(pt.getCurrentPosition()));
            telemetry.addData("tilt position", l.getCurrentTurnPosition());
//            //telemetry.addData("is closed", l.isClosed());
            telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
//            //telemetry.addData("rightRange", df.format(l.getRightRange()));
//            //telemetry.addData("leftRange", df.format(l.getLeftRange()));
//            //telemetry.addData("leftDistance", df.format(l.getLeftDistance()));
//            //telemetry.addData("rightDistance", df.format(l.getRightDistance()));
            telemetry.addData("lift position:",df.format(l.getLiftPosition()));
//            telemetry.addData("Ultra [Left : Mid : Right]", "["
//                    + df.format(l.getLeftUltra()) + " : "
//                    + df.format(l.getMidUltra()) + " : "
//                    + df.format(l.getRightUltra()) +"]");

//            if(gamepad2.dpad_left){
//                l.getSettings().rightTurnServoOffset -= 0.0001;
//            }
//            if(gamepad2.dpad_right){
//                l.getSettings().rightTurnServoOffset += 0.0001;
//            }

            if(gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if(gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());
            //telemetry.addLine(String.format("\nDetected tag ID=%d", t.detectedID));
            r.opMode.telemetry.addData("time", System.currentTimeMillis() - start);

            telemetry.update();
        }
        r.stop();
    }
}
