package om.self.ezftc.prebuilt.drive.base;

import com.qualcomm.robotcore.hardware.HardwareMap;

import om.self.ezftc.core.part.ControllablePart;
import om.self.ezftc.core.part.LoopedPart;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.PartParent;
import om.self.ezftc.utils.Vector3;
import om.self.supplier.modifiers.SimpleRampedModifier;

/**
 * A simple example of using a controllable and looped part to create a drive system for the robot
 */
public final class Drive extends ControllablePart<Robot, DriveSettings, DriveHardware, DriveControl> implements LoopedPart {
    /**
     * The power the drive system should end up at. (The actual power may be different if smoothing is enabled)
     */
    private Vector3 targetPower;

    /**
     * A modifier to ramp the x input if smoothing is enabled.
     */
    private final SimpleRampedModifier xRamp = new SimpleRampedModifier();
    /**
     * A modifier to ramp the y input if smoothing is enabled.
     */
    private final SimpleRampedModifier yRamp = new SimpleRampedModifier();
    /**
     * A modifier to ramp the r input if smoothing is enabled.
     */
    private final SimpleRampedModifier rRamp = new SimpleRampedModifier();

    /**
     * A function to apply smoothing to the input if enabled.
     */
    private Function<Vector3, Vector3> powerFilter = pow -> pow;

    /**
     * This converts the X, Y, R inputs to motor powers based on the {@link DriveSettings.DriveMode DriveMode}
     */
    private DrivePowerConverter dpc;

    /**
     * Creates the drive part by calling the {@link ControllablePart#ControllablePart(PartParent, String, Supplier, Map[]) super()} constructor and sets the config using {@link DriveSettings#makeDefault()} and {@link DriveHardware#makeDefault(HardwareMap)}
     * <br>
     * Note: This will set the base supplier to {@link DriveControl#DriveControl()}
     * @param robot The robot this part is driving. (The parent)
     */
    public  Drive(Robot robot){
        super(robot, "drive", DriveControl::new);
        setConfig(
                DriveSettings.makeDefault(),
                DriveHardware.makeDefault(robot.opMode.hardwareMap)
        );
    }

    /**
     * Creates the drive part by calling the {@link ControllablePart#ControllablePart(PartParent, String, Supplier, Map[]) super()} constructor and settings config using the passed in settings and hardware
     * <br>
     * Note: This will set the base supplier to {@link DriveControl#DriveControl()}
     * @param robot The robot this part is driving. (The parent)
     * @param driveSettings The settings for the drive system (see {@link DriveSettings})
     * @param driveHardware The hardware for the drive system (see {@link DriveHardware})
     */
    public Drive(Robot robot, DriveSettings driveSettings, DriveHardware driveHardware) {
        super(robot, "drive", DriveControl::new);
        setConfig(driveSettings, driveHardware);
    }

    /**
     * Returns the target power of the drive system. (this will be different from the actual power if smoothing is enabled)
     * @return the target power of the drive system
     */
    public Vector3 getTargetPower(){
        return targetPower;
    }

    /**
     * Returns the positions of the motors in an array in the order of topLeft, topRight, bottomLeft, bottomRight.
     * @return an int array with all motor positions
     */
    public int[] getMotorPositions(){
        return new int[]{
            getHardware().topLeftMotor.getCurrentPosition(),
            getHardware().topRightMotor.getCurrentPosition(),
            getHardware().bottomLeftMotor.getCurrentPosition(),
            getHardware().bottomRightMotor.getCurrentPosition()
        };
    }

    /**
     * Sets the target power of the drive system. (this will be different from the actual power if smoothing is enabled)
     * <br>
     * Note: This is also set by the control system so to use this manually you must disable the control system with the event {@link om.self.ezftc.core.part.ControllablePart.Names.Events#STOP_CONTROLLERS}
     * @param targetPower the target power to set
     */
    public void setTargetPower(Vector3 targetPower){
        this.targetPower = targetPower;
    }

    /**
     * This method will move the robot by converting the input powers (X, Y, and R) to motor powers and setting them.
     * <br>
     * Note: This method is a direct connection to the motors and bypasses things like motion smoothing. Use {@link Drive#setTargetPower(Vector3)} unless you know what you are doing (Just beware of wheel slip).
     * @param x the x power (-1 to 1) to move the robot at (side to side)
     * @param y the y power (-1 to 1) to move the robot at (forward and backward)
     * @param r the r power (-1 to 1) to move the robot at (rotation)
     */
    public void moveRobot(double x, double y, double r){
        double[] pows = dpc.convert(x,y,r);
        getHardware().topLeftMotor.setPower(pows[0]);
        getHardware().topRightMotor.setPower(pows[1]);
        getHardware().bottomLeftMotor.setPower(pows[2]);
        getHardware().bottomRightMotor.setPower(pows[3]);
    }

    /**
     * Just like {@link Drive#moveRobot(double, double, double)} but takes a {@link Vector3} instead of individual powers
     * @param powers the power that you want to move the robot at (Z is rotation)
     */
    public void moveRobot(Vector3 powers){
        moveRobot(powers.X, powers.Y, powers.Z);
    }

    /**
     * Sets all the ramp values to 0 (for smoothing) and sets the motor powers to 0. (This may cause slippage)
     */
    public void stopRobot(){
        xRamp.setCurrentVal(0);
        yRamp.setCurrentVal(0);
        rRamp.setCurrentVal(0);
        moveRobot(0,0,0);
    }

    /**
     * Updates this drive part with the new settings. It will set the ramps if smoothing is enabled then set the {@link #dpc}.
     * <br>
     * IMPORTANT: This should not be called by the user, use {@link #setSettings(Object)} or {@link #setConfig(Object, Object)} instead.
     * @param driveSettings the new settings
     */
    @Override
    public void onSettingsUpdate(DriveSettings driveSettings) {
        //set smoothing values
        xRamp.setRamp(driveSettings.smoothingValues.X);
        yRamp.setRamp(driveSettings.smoothingValues.Y);
        rRamp.setRamp(driveSettings.smoothingValues.Z);

        //set smoothing
        if(driveSettings.useSmoothing){
            powerFilter = (pow) -> new Vector3(
                    xRamp.apply(pow.X),
                    yRamp.apply(pow.Y),
                    rRamp.apply(pow.Z)
            );
        } else {
            powerFilter = (pow) -> pow;
        }

        //set the conversion function
        switch (driveSettings.driveMode){
            case TANK:
                dpc = (x, y, r) -> (
                        new double[]{
                                x + r,
                                x - r,
                                x + r,
                                x - r
                        }
                );
                break;
            case MECANUM:
                dpc = (x, y, r) -> (
                        new double[]{
                                x + y + r,
                                -x + y - r,
                                -x + y + r,
                                x + y - r
                        }
                );
                break;
            case OMNI:
                //experimental
                dpc = (x, y, r) -> (
                        new double[]{
                                y + r,
                                x + r,
                                y + r,
                                x + r
                        }
                );
                break;
        }
    }

    /**
     * This will update anything that needs to be updated when the hardware is updated but it is currently not doing anything as all configuration is handled in {@link DriveHardware}.
     * <br>
     * IMPORTANT: This should not be called by the user, use {@link #setHardware(Object)} or {@link #setConfig(Object, Object)} instead.
     * @param driveHardware the new hardware
     */
    @Override
    public void onHardwareUpdate(DriveHardware driveHardware) {

    }

    /**
     * This will be called every loop by the {@link om.self.ezftc.core.part.LoopedPart.Names.Tasks#MAIN_LOOP MAIN_LOOP} task. It will apply the smoothing to the target power and then move the robot.
     * <br>
     * IMPORTANT: This should not be called by the user!
     */
    @Override
    public void onRun() {
        moveRobot(powerFilter.apply(targetPower));
    }

    /**
     * This will be called every loop by the {@link om.self.ezftc.core.part.ControllablePart.Names.Tasks#MAIN_CONTROL_LOOP MAIN_CONTROL_LOOP} task. It will set the target power to the control power unless stop is requested.
     * IMPORTANT: This should not be called by the user!
     *  @param control the control object used to control the drive
     */
    @Override
    public void onRun(DriveControl control) {
        if(control.stop)
            stopRobot();
        else
            setTargetPower(control.power);
    }

    /**
     * This will be called when the bean manager is loading all parts during init . It is currently not doing anything.
     */
    @Override
    public void onBeanLoad() {

    }

    /**
     * This will be called when the robot is initialized. It is currently not doing anything.
     */
    @Override
    public void onInit() {
    }

    /**
     * This will be called the first time the robot is started. It is currently not doing anything.
     */
    @Override
    public void onInitialStart(){
    }

    /**
     * This will be called each time the robot is started. It is currently not doing anything.
     */
    @Override
    public void onStart() {

    }

    /**
     * This will be called each time the robot is stopped. It is currently not doing anything.
     */
    @Override
    public void onStop() {

    }

    /**
     * Interface that will convert drive powers to motor powers based on the drive mode.
     */
    interface DrivePowerConverter{
        double[] convert(double x, double y, double r);
    }
}
