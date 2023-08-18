package om.self.ezftc.prebuilt.drive.headerkeeper;

import org.apache.commons.lang3.ObjectUtils;
import om.self.ezftc.prebuilt.drive.base.Drive;
import om.self.ezftc.prebuilt.positiontracker.PositionTracker;

import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.AngleMath;
import om.self.ezftc.utils.PID;
import om.self.supplier.modifiers.EdgeExModifier;
import om.self.task.core.Group;
import om.self.task.other.TimedTask;

/**
 * A class that keeps the robot's heading constant when no turn input is detected. This is useful for driving in a straight line.
 */
public class HeaderKeeper extends Part<Drive, HeaderKeeperSettings, ObjectUtils.Null> {
    private PositionTracker pt;
    private double lastHeading;
    private final PID pid = new PID();

    private final TimedTask headerKeeper = new TimedTask("keep header");
    private final EdgeExModifier edgeModifier = new EdgeExModifier();

    /**
     * Creates a header keeper with the given parent and settings.
     * <br>
     * IMPORTANT: This needs a {@link PositionTracker} to function properly. The bean manager will automatically find one if it is present else it will throw an error.
     * @param parent the parent drive
     * @param settings the settings
     */
    public HeaderKeeper(Drive parent, HeaderKeeperSettings settings) {
        super(parent, "header keeper");
        setSettings(settings);
    }

    /**
     * Creates a header keeper with the given parent and default settings.
     * <br>
     * IMPORTANT: This needs a {@link PositionTracker} to function properly. The bean manager will automatically find one if it is present else it will throw an error.
     * @param parent the parent drive
     */
    public HeaderKeeper(Drive parent) {
        super(parent, "header keeper");
        setSettings(HeaderKeeperSettings.makeDefault());
    }

    /**
     * called whenever the settings are updated.
     * @param settings the new settings
     */
    @Override
    public void onSettingsUpdate(HeaderKeeperSettings settings) {
        pid.PIDs = settings.pidCoefficients;
        pid.minClamp = -settings.maxI;
        pid.maxClamp = settings.maxI;
        buildHeaderKeeper(settings);
    }

    /**
     * Loads the position tracker to get angle.
     */
    @Override
    public void onBeanLoad() {
        pt = getBeanManager().getBestMatch(PositionTracker.class, false, false);
    }

    /**
     * rebuilds the header keeper task with the given settings.
     * @param settings the settings
     */
    private void buildHeaderKeeper(HeaderKeeperSettings settings){
        headerKeeper.clear();
        headerKeeper.addStep(pid::resetValue);
        headerKeeper.addDelay(settings.headingSettleDelay);
        headerKeeper.addStep(() -> {
            lastHeading = pt.getCurrentPosition().Z;
            pid.resetErrors();
        });
        headerKeeper.addStep(() -> pid.updatePID(AngleMath.findAngleError(pt.getCurrentPosition().Z, lastHeading)), () -> false);
    }

    /**
     * initializes the header keeper task and the edge modifier that activates and deactivates it.
     */
    @Override
    public void onInit() {
        //create header keeper task
        headerKeeper.attachParent(getTaskManager());
        buildHeaderKeeper(getSettings());

        //automatically trigger and pause
        edgeModifier.setOnRise(headerKeeper::restart);
        edgeModifier.setOnFall(() -> headerKeeper.runCommand(Group.Command.PAUSE));
    }

    @Override
    public void onInitialStart() {

    }

    /**
     * starts the part by adding a controller to the default control environment in drive.
     */
    @Override
    public void onStart() {
        parent.getDefualtControlEnvironment().addController("header keeper", (control) -> {
            if(edgeModifier.apply(Math.abs(control.power.Z) < getSettings().minRegisterVal)){
                control.power = control.power.withZ(pid.returnValue());
            }
        });
    }

    /**
     * stops the part by removing the controller from the default control environment in drive.
     */
    @Override
    public void onStop() {
        parent.getDefualtControlEnvironment().removeController("header keeper");
    }
}
