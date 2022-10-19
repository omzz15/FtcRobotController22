package org.firstinspires.ftc.teamcode;

import android.app.ActivityManager;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import om.self.beans.PackageBeanManager;
import om.self.ezftc.core.Robot;
import om.self.ezftc.other.hardware.motor.MotorGenerator;


@TeleOp(name="drive test", group="test")
public class Test extends OpMode
{
    PackageBeanManager pbm = new PackageBeanManager("org.firstinspires.ftc.teamcode");

    //Robot robot = new Robot(this, "drive");

    @Override
    public void init() {
        //pbm.load();
        //robot.beanManager.addTag("teleop");
        //robot.init();
    }

    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        //robot.start();
    }

    @Override
    public void loop() {
        //telemetry.addData("test", pbm.getBean(BeanTest.class));
        telemetry.addData("test", "good to go");
        telemetry.update();
        //robot.run();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        //robot.stop();
    }

}
