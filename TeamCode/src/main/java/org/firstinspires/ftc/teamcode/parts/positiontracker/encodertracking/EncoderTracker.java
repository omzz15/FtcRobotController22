package org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.VectorMath;

public class EncoderTracker extends LoopedPartImpl<PositionTracker, EncoderTrackerSettings, ObjectUtils.Null> {
    private Drive drive;

    private int[] lastMotorPos = new int[4];

    public EncoderTracker(PositionTracker parent, EncoderTrackerSettings settings) {
        super(parent, "encoder tracker");
        setSettings(settings);
    }

    public EncoderTracker(PositionTracker parent) {
        super(parent, "encoder tracker");
        setSettings(EncoderTrackerSettings.makeDefault());
    }


    @Override
    public void onInit() {}

    @Override
    public void onBeanLoad() {
        drive = getBeanManager().getBestMatch(Drive.class, false, false);
    }

    @Override
    public void onStart() {
        lastMotorPos = drive.getMotorPositions();
    }

    @Override
    public void onRun() {
         Vector3 currentPos = parent.getCurrentPosition();
         double currentAngle = currentPos.Z;

         int[] currMotorPos = drive.getMotorPositions();
         int[] diff = new int[]{
                 currMotorPos[0] - lastMotorPos[0],
                 currMotorPos[1] - lastMotorPos[1],
                 currMotorPos[2] - lastMotorPos[2],
                 currMotorPos[3] - lastMotorPos[3],
         };
         lastMotorPos = currMotorPos;

        //get the X and Y movement of the robot
        double XMove = (.25 * (-diff[0] + diff[2] + diff[1] - diff[3])) / getSettings().ticksPerInchSideways;
        double YMove = (.25 * (diff[0] + diff[2] + diff[1] + diff[3])) / getSettings().ticksPerInchForward;

        //rotate movement and add to robot positionTracker
        Vector3 movement = new Vector3(
            YMove * java.lang.Math.sin(currentAngle * java.lang.Math.PI / 180) - XMove * java.lang.Math.cos(currentAngle * java.lang.Math.PI / 180),
            XMove * java.lang.Math.sin(currentAngle * java.lang.Math.PI / 180) + YMove * java.lang.Math.cos(currentAngle * java.lang.Math.PI / 180),
            0
        );

        parent.setCurrentPosition(VectorMath.add(currentPos, movement));
    }

    @Override
    public void onStop() {

    }
}
