package om.self.ezftc.core.part;

import om.self.task.core.EventManager;
import om.self.task.core.Group;

public interface PartParent {
    String getName();

    Group getTaskManager();

    EventManager getEventManager();
}
