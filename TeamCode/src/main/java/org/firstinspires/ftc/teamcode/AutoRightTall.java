package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="3 Auto-RIGHT-Tall", group="Test")
public class AutoRightTall extends AutoRightDangerousAndAll {
    @Override
    public void initAuto(){
        isRight = true;
        transformFunc = (v) -> v;
        targetPole = 2;
    }

}
