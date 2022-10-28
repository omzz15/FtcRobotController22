package om.self.ezftc.core.part;


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
 * @param <PART> the type of the part you want teleop for
 */
public abstract class TeleopPart<PART extends RobotPart<?,?>> extends Part<PART> {
    public TeleopPart(PART parent) {
        super(parent, "teleop");
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
