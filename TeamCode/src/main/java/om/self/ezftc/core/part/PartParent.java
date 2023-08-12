package om.self.ezftc.core.part;

import om.self.beans.core.BeanManager;
import om.self.task.core.Group;
import om.self.task.event.EventManager;

/**
 * A simple interface that defines the methods that a part parent must have so a part can get the required things.
 *
 * @see om.self.ezftc.core.Robot
 * @see om.self.ezftc.core.part.Part
 */
public interface PartParent {
    /**
     * The character used to separate directories (parts) in the path to this part from the top level
     */
    String dirChar = "/";

    /**
     * Gets the name of the part parent for identification purposes
     * @return the name of the part parent
     */
    String getName();

    /**
     * Gets the directory of the part parent for identification purposes in relation to the top level part (robot)
     * @return the directory of the part parent
     */
    String getDir();

    /**
     * Gets the task manager of the part parent so the part can add tasks and run code
     * @return the task manager of the part parent
     */
    Group getTaskManager();

    /**
     * Gets the event manager of the part parent so the part can add and trigger events
     * @return the event manager of the part parent
     */
    EventManager getEventManager();

    /**
     * Gets the bean manager so the part can add itself as a bean and get other beans for dependency injection. <br>
     * IMPORTANT: This should only return the top level bean manager. No other bean managers should even be created.
     * @return the bean manager of the part parent (should be the top level bean manager)
     */
    BeanManager getBeanManager();
}
