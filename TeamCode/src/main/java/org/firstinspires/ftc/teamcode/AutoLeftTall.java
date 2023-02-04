package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto-LEFT-Tall", group="Test")
public class AutoLeftTall extends AutoRightDangerousAndAll {
    @Override
    public void initAuto(){
        isRight = false;
        transformFunc = (v) -> v.withX(-v.X).withZ(-180 - v.Z);
        targetPole = 2;
    }

}
