package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import om.self.ezftc.prebuilt.BulkRead;
import om.self.ezftc.prebuilt.drive.base.Drive;
import om.self.ezftc.prebuilt.drive.teleop.DriveTeleop;
import om.self.ezftc.prebuilt.drive.teleop.DriveTeleopSettings;
import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;
import org.firstinspires.ftc.teamcode.parts.lifter.LifterTeleop;
import om.self.ezftc.prebuilt.positionsolver.PositionSolver;
import om.self.ezftc.prebuilt.positionsolver.settings.PositionSolverSettings;
import om.self.ezftc.prebuilt.positiontracker.PositionTracker;
import om.self.ezftc.prebuilt.positiontracker.encodertracking.EncoderTracker;
import om.self.ezftc.prebuilt.positiontracker.odometry.Odometry;

import java.text.DecimalFormat;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;

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

@TeleOp(name="1 Teleop Forza", group="Linear Opmode")
public class TeleopForza extends LinearOpMode {
    double tileSide = 23.5;
    boolean slideDone = false;
    Vector3 fieldStartPos = new Vector3(-36,63,90);

    public Vector3 tiletoField(Vector3 p){
        return new Vector3(p.X * tileSide, p.Y * tileSide, p.Z);
    }

    public Vector3 fieldToTile(Vector3 p){
        return new Vector3(p.X / tileSide, p.Y / tileSide, p.Z);
    }

    @Override
    public void runOpMode() {
        long start;
        FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        Robot r = new Robot(this);
        new BulkRead(r);
        Drive d = new Drive(r);
        new DriveTeleop(d, DriveTeleopSettings.makeForza(r));
        //new HeaderKeeper(d);
        PositionTracker pt = new PositionTracker(r);
        PositionSolver ps = new PositionSolver(d, PositionSolverSettings.defaultNoAlwaysRunSettings);

        //Slamra s = new Slamra(pt);
        EncoderTracker et = new EncoderTracker(pt);
        Odometry odo = new Odometry(pt);
        odo.raise();

        Lifter l = new Lifter(r);
        new LifterTeleop(l);

        DecimalFormat df = new DecimalFormat("#0.0");

        r.init();

        while (!isStarted()) {}

        r.start();
        //l.startAutoHome();

        pt.positionSourceId = EncoderTracker.class;

        while (opModeIsActive()) {
            start = System.currentTimeMillis();
            r.run();

            //double x = s.slamraFinal.X;
            //double y = s.slamraFinal.Y;
            //double z = Math.toRadians(s.slamraFinal.Z);
            double x = pt.getCurrentPosition().X;
            double y = pt.getCurrentPosition().Y;
            double z = Math.toRadians(pt.getCurrentPosition().Z);

            double x1 = Math.cos(z)*8;
            double y1 = Math.sin(z)*8;
            packet.fieldOverlay().setFill("blue").fillCircle(x,y,6);
            packet.fieldOverlay().setStroke("red").strokeLine(x,y,x+x1,y+y1);

            // Debug lines
            telemetry.addData("opmode name",r.opMode.getClass().getName());
            telemetry.addData("opmode name",r.opMode.getClass().getAnnotation(TeleOp.class));

            telemetry.addData("limit hit", !l.getHardware().limitSwitch.getState());
            //telemetry.addData("position", pt.getCurrentPosition());
            //telemetry.addData("tile position", fieldToTile(pt.getCurrentPosition()));
            telemetry.addData("tilt position", l.getCurrentTurnPosition());
            telemetry.addData("is grabber closed", l.isGrabberClosed());
            telemetry.addData("right servo offset", df.format(l.getSettings().rightTurnServoOffset));
            telemetry.addData("lift position:",df.format(l.getLiftPosition()));
            telemetry.addData("Ultra [Left : Mid : Right]", "["
                    + df.format(l.getLeftUltra()) + " : "
                    + df.format(l.getMidUltra()) + " : "
                    + df.format(l.getRightUltra()) +"]");

            if(gamepad1.dpad_down) telemetry.addData("tasks", r.getTaskManager());
            if(gamepad1.dpad_down) telemetry.addData("events", r.getEventManager());
            r.opMode.telemetry.addData("time", System.currentTimeMillis() - start);

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();
        }
        r.stop();
    }
}
