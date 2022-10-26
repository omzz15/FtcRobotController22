package om.self.ezftc.core;

import om.self.task.core.EventManager;
import om.self.task.core.Group;

public interface PartParent {
    String getName();

    /**
     * used to get the full directory of this part(basically all the parent names and then this things name). Please use all uppercase and _ between directories
     * @return the full directory
     */
    String getDir();

    Group getTaskManager();

    EventManager getEventManager();
}
