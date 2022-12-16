package org.firstinspires.ftc.teamcode.parts.positiontracker;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.parts.positiontracker.hardware.PositionTrackerHardware;
import org.firstinspires.ftc.teamcode.parts.positiontracker.settings.PositionTrackerSettings;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.Vector3;


public class PositionTracker extends LoopedPartImpl<Robot, PositionTrackerSettings, PositionTrackerHardware> {
    private Vector3 currentPosition = new Vector3();
    private double offset;
    private long lastUpdateTime = System.currentTimeMillis();

    public PositionTracker(Robot robot) {
        super(robot, "position tracker", robot.startTaskManager);
        setConfig(PositionTrackerSettings.makeDefault(), PositionTrackerHardware.makeDefault(robot));
    }

    public PositionTracker(Robot robot, PositionTrackerSettings positionTrackerSettings, PositionTrackerHardware positionTrackerHardware) {
        super(robot, "position tracker", robot.startTaskManager);
        setConfig(positionTrackerSettings, positionTrackerHardware);
    }

    public void setAngle(double angle){
        updateAngle();
        offset += currentPosition.Z - angle;
        currentPosition = currentPosition.withZ(angle);
    }

    public Vector3 getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Vector3 currentPosition) {
        this.currentPosition = currentPosition;
        lastUpdateTime = System.currentTimeMillis();
    }

    public boolean isPositionStale(){
        return System.currentTimeMillis() - lastUpdateTime > getSettings().stalePosTime;
    }

    private void updateAngle() {
        double angle = getHardware().imu.getAngularOrientation(AxesReference.EXTRINSIC, getSettings().axesOrder, AngleUnit.DEGREES).thirdAngle;
        if (getSettings().flipAngle)
            angle *= -1;
        angle -= offset;
        setCurrentPosition(currentPosition.withZ(AngleMath.scaleAngle(angle)));
    }


    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {
        setAngle(getSettings().startAngle);
    }

    @Override
    public void onStart() {}

    @Override
    public void onSettingsUpdate(PositionTrackerSettings positionTrackerSettings) {

    }

    @Override
    public void onHardwareUpdate(PositionTrackerHardware hardware) {
        hardware.imu.initialize(hardware.parameters);

        while (!hardware.imu.isGyroCalibrated())
        {
            parent.opMode.telemetry.addData("gyro status", "calibrating");
            parent.opMode.telemetry.update();
        }

        parent.opMode.telemetry.addData("gyro status", "calibrated :)");
        parent.opMode.telemetry.update();
    }

    @Override
    public void onRun() {
        updateAngle();
    }

    @Override
    public void onStop() {

    }
}