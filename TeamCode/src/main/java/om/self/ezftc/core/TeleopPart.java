package om.self.ezftc.core;

import om.self.beans.Bean;
import om.self.beans.core.Autowired;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

/**
 * Provides teleop for a part. All classes extending TeleopPart will have the 'teleop' tag so they wont be loaded unless the tag is also present in BeanManager
 * PLEASE make new documentation when creating teleop for a part.
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT_(part name) --> INIT_(part name)_TELEOP</li>
 *     <li>START_(part name) --> START_(part name)_TELEOP</li>
 *     <li>STOP_(part name) --> STOP_(part name)_TELEOP</li>
 * </ul>
 * USED BEANS:
 * <ul>
 *     <li>{@link PART}</li>
 *     <li>{@link EventManager}</li>
 * </ul>
 * ADDED BEANS:
 * <ul>
 *   <li>{@link TeleopPart}</li>
 * @param <PART> the type of the part you want teleop for
 */
@Bean(alwaysLoad = true, tags = {"teleop"})
public abstract class TeleopPart<PART extends RobotPart<?,?>> extends PartExtension<PART> {
    public TeleopPart() {
        super("teleop");
    }

    //for convenience I provided default implementations for non important methods

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
