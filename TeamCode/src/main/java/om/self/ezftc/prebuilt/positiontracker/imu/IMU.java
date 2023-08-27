package om.self.ezftc.prebuilt.positiontracker.imu;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import om.self.ezftc.core.part.Part;
import om.self.ezftc.prebuilt.positiontracker.PositionTracker;
import om.self.ezftc.utils.AngleMath;

public class IMU extends Part<PositionTracker, IMUSettings, IMUHardware>{
    private IMUState state = IMUState.NOT_CALIBRATED;
    private double offset;
    private double angle;

    public IMU(PositionTracker parent) {
        this(parent, IMUSettings.makeDefault(), IMUHardware.makeDefault(parent.parent.opMode.hardwareMap));
    }

    public IMU(PositionTracker parent, IMUSettings settings, IMUHardware hardware) {
        super(parent, "IMU");
        setConfig(settings, hardware);
    }

    public void setAngle(double angle){
        updateAngle();
        offset += this.angle - angle;
        this.angle = angle;
    }

    private void updateAngle() {
            double angle = getHardware().imu.getAngularOrientation(AxesReference.EXTRINSIC, getSettings().axesOrder, AngleUnit.DEGREES).thirdAngle;
            if (getSettings().flipAngle)
                angle *= -1;
            angle -= offset;
            this.angle = AngleMath.scaleAngle(angle);
    }

    /**
     * Called when the settings of this part are updated.
     *
     * @param imuSettings the new settings
     */
    @Override
    public void onSettingsUpdate(IMUSettings imuSettings) {

    }

    /**
     * Called when the hardware of this part is updated.
     *
     * @param imuHardware the new hardware
     */
    @Override
    public void onHardwareUpdate(IMUHardware imuHardware) {
        imuHardware.imu.initialize(imuHardware.parameters);

    }

    /**
     * Method that gets called by the bean manager to load this part
     */
    @Override
    public void onBeanLoad() {

    }

    /**
     * Method that gets called when {@link Robot.Names.Events#INIT} is triggered to initialize the part. <br>
     * WARNING: beans may not be loaded onInit, so please use the {@link #onBeanLoad()} or {@link #onStart()} methods to access beans.
     */
    @Override
    public void onInit() {

    }

    /**
     * Method that gets called when {@link Robot.Names.Events#INITIAL_START} is triggered to run code on specifically the first start. <br>
     * Note: This method is run before {@link Robot.Names.Events#START} is triggered and {@link #onStart()} runs.
     */
    @Override
    public void onInitialStart() {

    }

    /**
     * Method that gets called when {@link Robot.Names.Events#START} is triggered to start the part.
     */
    @Override
    public void onStart() {

    }

    /**
     * Method that gets called when {@link Robot.Names.Events#STOP} is triggered to stop the part.
     */
    @Override
    public void onStop() {

    }

    public enum IMUState{
        CALIBRATING,
        CALIBRATED,
        NOT_CALIBRATED
    }
}
