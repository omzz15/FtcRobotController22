package org.firstinspires.ftc.teamcode.parts.positiontracker.odometry;

import com.qualcomm.robotcore.hardware.DcMotor;

public class OdometryHardware {
    public final DcMotor YWheel1;
    public final DcMotor YWheel2;
    public final DcMotor XWheel;

    public OdometryHardware(DcMotor YWheel1, DcMotor YWheel2, DcMotor XWheel) {
        this.YWheel1 = YWheel1;
        this.YWheel2 = YWheel2;
        this.XWheel = XWheel;
    }
}
