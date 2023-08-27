package om.self.ezftc.prebuilt.positiontracker;


import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import om.self.ezftc.core.part.Part;

import java.util.Hashtable;

import om.self.ezftc.core.Robot;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.Vector2;
import om.self.ezftc.utils.Vector3;
import om.self.ezftc.utils.VectorMath;


public class PositionTracker extends Part<Robot, PositionTrackerSettings, ObjectUtils.Null> {
    private Vector3 position;
    private Vector3 confidence;
    private Vector3 accuracy;
    private Vector2 relativePosition;
    private Vector2 relativeConfidence;

    private Hashtable<String, PositionTicket> currentTickets = new Hashtable<>();
    private Hashtable<String, Supplier<PositionTicket>> ticketSuppliers = new Hashtable<>();
    private long lastUpdateTime = 0;

    public PositionTracker(Robot robot) {
        super(robot, "position tracker", robot.startTaskManager);
        setConfig(PositionTrackerSettings.makeDefault(), null);
    }

    public PositionTracker(Robot robot, PositionTrackerSettings positionTrackerSettings) {
        super(robot, "position tracker", robot.startTaskManager);
        setConfig(positionTrackerSettings, null);
    }

    public Vector3 getCurrentPosition() {
        return position;
    }

    public void setCurrentPosition(Vector3 currentPosition, Vector3 confidence, Vector3 accuracy) {
        this.currentPosition = currentPosition;
        lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Sets the current position of the robot
     * @param currentPosition the current position
     * @deprecated you should add a position ticket so it integrates with other position sources!!
     */
    @Deprecated
    public void setCurrentPosition(Vector3 currentPosition) {
        this.currentPosition = currentPosition;
        lastUpdateTime = System.currentTimeMillis();
    }

    public Vector2 getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector2 relativePosition) {
        this.relativePosition = relativePosition;
    }

    public boolean addPositionTicket(String key, PositionTicket pt){
        if(ticketSuppliers.containsKey(key))
            return false;
        return currentTickets.putIfAbsent(key, pt) == null;
    }

    public boolean isPositionStale(){
        return System.currentTimeMillis() - lastUpdateTime > getSettings().stalePosTime;
    }

    @Override
    public void onBeanLoad() {
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onInitialStart() {
    }

    @Override
    public void onStart() {
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void onSettingsUpdate(PositionTrackerSettings positionTrackerSettings) {
        setAngle(positionTrackerSettings.startPosition.Z);
        currentPosition = positionTrackerSettings.startPosition;
    }

    @Override
    public void onHardwareUpdate(PositionTrackerHardware hardware) {

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

        if(positionSourceId != null && tickets.containsKey(positionSourceId)){
            lastUpdateTime = System.currentTimeMillis();
            PositionTicket ticket = tickets.get(positionSourceId);
            currentPosition = ticket.position; //todo add something better
            relativePosition = VectorMath.add(relativePosition, ticket.robotRelative);
            tickets.clear();
        }
    }

    @Override
    public void onStop() {

    }

}