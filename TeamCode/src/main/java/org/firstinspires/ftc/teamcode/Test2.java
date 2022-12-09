package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveTeleop;
import org.firstinspires.ftc.teamcode.parts.drive.headerkeeper.HeaderKeeper;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterTeleop;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking.EncoderTracker;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;

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
    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
            new DriveTeleop(d);
            new HeaderKeeper(d);
        PositionTracker pt = new PositionTracker(r);
        //    new EncoderTracker(pt);
        Lifter l = new Lifter(r);
        new LifterTeleop(l);
        DecimalFormat df = new DecimalFormat("#0.0");

        r.init();

        waitForStart();

        r.start();

        while (opModeIsActive()) {
            r.run();
            //if(gamepad1.y) pt.setAngle(0);
            //telemetry.addData("position", pt.getCurrentPosition());
            telemetry.addData("tilt position", l.getCurrentTurnPosition());
            telemetry.addData("is closed", l.isClosed());
            telemetry.addData("right servo offset", l.getSettings().rightTurnServoOffset);
            telemetry.addData("rightRange", df.format(l.getRightRange()));
            telemetry.addData("leftRange", df.format(l.getLeftRange()));
            telemetry.addData("leftDistance", df.format(l.getLeftDistance()));
            telemetry.addData("rightDistance", df.format(l.getRightDistance()));
            if(gamepad2.dpad_left){
                l.getSettings().rightTurnServoOffset -= 0.0001;
            }
            if(gamepad2.dpad_right){
                l.getSettings().rightTurnServoOffset += 0.0001;
            }

            if(gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if(gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());
            telemetry.update();
        }

        r.stop();
    }
}
