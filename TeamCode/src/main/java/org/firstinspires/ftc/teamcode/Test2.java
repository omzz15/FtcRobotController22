//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.parts.drive.Drive;
//import org.firstinspires.ftc.teamcode.parts.drive.DriveTeleop;
//import org.firstinspires.ftc.teamcode.parts.drive.headerkeeper.HeaderKeeper;
//import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
//import org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking.EncoderTracker;
//
//import om.self.beans.core.BeanManager;
//import om.self.ezftc.core.Robot;
//
///**
// * This file contains an example of an iterative (Non-Linear) "OpMode".
// * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
// * The names of OpModes appear on the menu of the FTC Driver Station.
// * When a selection is made from the menu, the corresponding OpMode
// * class is instantiated on the Robot Controller and executed.
// *
// * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
// * It includes all the skeletal structure that all iterative OpModes contain.
// *
// * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
// * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
// */
//
//@TeleOp(name="Misc Test", group="Linear Opmode")
//public class Test2 extends LinearOpMode {
//    @Override
//    public void runOpMode() {
//        waitForStart();
//        BeanManager bm = new BeanManager();
//        bm.addBean(new t1(), false, true);
//        bm.addBean(new t2(), false, true);
//        bm.addBean(new t3(), false, true);
//        bm.addBean(new t4(), false, true);
//        bm.addBean(new t5(), false, true);
//
//        while (opModeIsActive()){
//            telemetry.addData("t2", bm.getBestMatch(t2.class, false, false));
//            telemetry.update();
//        }
//    }
//}
//class par{}
//
//class t1 extends par{}
//class t2 extends par{}
//class t3 extends par{}
//class t4 extends par{}
//class t5 extends par{}
