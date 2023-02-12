package org.firstinspires.ftc.teamcode.parts.positiontracker;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterSettings;
import org.firstinspires.ftc.teamcode.parts.positiontracker.hardware.PositionTrackerHardware;
import org.firstinspires.ftc.teamcode.parts.positiontracker.settings.PositionTrackerSettings;

import java.util.Hashtable;
import java.util.LinkedList;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.Vector3;


public class PositionTracker extends LoopedPartImpl<Robot, PositionTrackerSettings, PositionTrackerHardware> {
    private Vector3 currentPosition = new Vector3();
    private double offset;
    private long lastUpdateTime = System.currentTimeMillis();
    private LinkedList<PositionTicket> tickets = new LinkedList<>();

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

    /**
     * @param currentPosition the current position
     * @deprecated you should add a position ticket so it integrates!!
     */
    @Deprecated
    public void setCurrentPosition(Vector3 currentPosition) {
        this.currentPosition = currentPosition;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void addPositionTicket(PositionTicket pt){
        tickets.add(pt);
    }

    public boolean isPositionStale(){
        return System.currentTimeMillis() - lastUpdateTime > getSettings().stalePosTime;
    }

    private void updateAngle() {
        if(getHardware() != null) {
            double angle = getHardware().imu.getAngularOrientation(AxesReference.EXTRINSIC, getSettings().axesOrder, AngleUnit.DEGREES).thirdAngle;
            if (getSettings().flipAngle)
                angle *= -1;
            angle -= offset;
            setCurrentPosition(currentPosition.withZ(AngleMath.scaleAngle(angle)));
        }
    }


    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSettingsUpdate(PositionTrackerSettings positionTrackerSettings) {
        setAngle(positionTrackerSettings.startPosition.Z);
        currentPosition = positionTrackerSettings.startPosition;
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

        if(!tickets.isEmpty()) {

            lastUpdateTime = System.currentTimeMillis();

            currentPosition = tickets.getFirst().position; //todo add something better

            tickets.clear();
        }
        updateAngle();
    }

    @Override
    public void onStop() {

    }
}