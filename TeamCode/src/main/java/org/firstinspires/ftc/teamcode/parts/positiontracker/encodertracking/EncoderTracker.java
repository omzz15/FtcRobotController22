package org.firstinspires.ftc.teamcode.parts.positiontracker.encodertracking;

import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPart;
import om.self.ezftc.core.part.implementations.PartImpl;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.VectorMath;

public class EncoderTracker extends PartImpl<PositionTracker> implements LoopedPart<PositionTracker> {
    private Drive drive;

    private int[] lastMotorPos = new int[4];
    private EncoderTrackerSettings settings;

    public EncoderTracker(PositionTracker parent, EncoderTrackerSettings settings) {
        super(parent, "encoder tracker");
        make();
        setSettings(settings);
    }

    public EncoderTracker(PositionTracker parent) {
        super(parent, "encoder tracker");
        make();
        setSettings(EncoderTrackerSettings.makeDefault());
    }

    private void make(){
        constructLooped();
    }

    public void setSettings(EncoderTrackerSettings settings) {
        this.settings = settings;
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
        double XMove = (.25 * (-diff[0] + diff[2] + diff[1] - diff[3])) / settings.ticksPerInchSideways;
        double YMove = (.25 * (diff[0] + diff[2] + diff[1] + diff[3])) / settings.ticksPerInchForward;

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
