package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto-no move", group="Test")
public class NoMoveAuto extends AutoRightDangerousAndAll {
    @Override
    public void initAuto(){
        isRight = true;
        parkOnly = true;
        transformFunc = (v) -> v;
        shutdownps = true;
    }

}
