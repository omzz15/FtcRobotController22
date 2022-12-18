package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.DriveTeleop;
import org.firstinspires.ftc.teamcode.parts.drive.headerkeeper.HeaderKeeper;
import org.firstinspires.ftc.teamcode.parts.positionsolver.PositionSolver;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
import org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking.EncoderTracker;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.Vector3;
import om.self.task.core.Task;
import om.self.task.core.TaskEx;

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

@TeleOp(name="move to position Test", group="Linear Opmode")
public class Test extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot r = new Robot(this);
        Drive d = new Drive(r);
        new DriveTeleop(d);
        PositionTracker pt = new PositionTracker(r);
        new EncoderTracker(pt);
        PositionSolver ps = new PositionSolver(d);

        TaskEx test = new TaskEx("auto test", r.taskManager);
        ps.addMoveToTaskEx(new Vector3(0,30,0), test);
        ps.addMoveToTaskEx(new Vector3(0,-30,0), test);

        r.init();

        waitForStart();

        r.start();

        while (opModeIsActive()) {
            r.run();
            r.opMode.telemetry.addData("position", pt.getCurrentPosition());
            r.opMode.telemetry.addData("solver", ps.isDone());

            for(String s: d.getControllerNameMapping().keySet())
                r.opMode.telemetry.addData("drive controller: " + s, "");

            if(r.opMode.gamepad1.a) r.opMode.telemetry.addData("task manager", r.getTaskManager());
            if(r.opMode.gamepad1.b) r.opMode.telemetry.addData("event manager", r.getEventManager());
            r.opMode.telemetry.update();
        }

        r.stop();
    }
}
