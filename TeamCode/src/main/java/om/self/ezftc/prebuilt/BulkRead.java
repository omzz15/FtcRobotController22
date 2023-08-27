package om.self.ezftc.prebuilt;

import com.qualcomm.hardware.lynx.LynxModule;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.LoopedPart;
import om.self.ezftc.core.part.Part;

/**
 * A class that reads bulk data from all hubs at the end of each loop to speedup reading data. Use this if you are reading a lot of data and loop times are slow.
 * <br>
 * Note: This part will automatically start bulk reading. Noting needs to be done other than adding it to the robot.
 */
public class BulkRead extends Part<Robot, ObjectUtils.Null, ObjectUtils.Null> implements LoopedPart {
    /**
     * A list of all hubs to bulk read from
     */
    private List<LynxModule> allHubs;

    /**
     * Creates a bulk read part for the given parent
     * @param parent the parent robot
     */
    public BulkRead(Robot parent) {
        super(parent, "bulk read", parent.endTaskManager);
        constructLoop();
    }

    /**
     * reads all the data from the hubs
     * <br>
     * IMPORTANT: This should not be called manually.
     */
    @Override
    public void onRun() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    @Override
    public void onBeanLoad() {

    }

    /**
     * sets the bulk caching mode to manual for all hubs
     * <br>
     * IMPORTANT: This should not be called manually.
     */
    @Override
    public void onInit() {
        allHubs = parent.opMode.hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    @Override
    public void onInitialStart() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
