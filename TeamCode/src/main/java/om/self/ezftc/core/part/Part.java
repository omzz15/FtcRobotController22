package om.self.ezftc.core.part;

import java.util.Hashtable;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.Robot;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

/**
 * Provides a simple configuration of events, beans, and tasks that allow you to make extensions(ex: robot parts or telemetry).
 * PLEASE make new documentation when creating a part.
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>START_(part name) --> START_(part name)_(extension name)</li>
 *     <li>STOP_(part name) --> STOP_(part name)_(extension name)</li>
 * </ul>
 * @param <PARENT> the type of the part you want to extend
 */
public interface Part<PARENT extends PartParent> extends PartParent {
    default void construct(){
        //-----event manager-----//
        //make/attach start
        getEventManager().attachToEvent(EventManager.CommonEvent.INIT, "onInit", this::onInit);
        //make/attach start
        getEventManager().attachToEvent(EventManager.CommonEvent.START, "onStart", this::onStart);
        //make/attach stop
        getEventManager().attachToEvent(EventManager.CommonEvent.STOP, "onStop", this::onStop);
        getEventManager().attachToEvent(EventManager.CommonEvent.STOP, "stop taskManager", () -> getTaskManager().runCommand(Group.Command.PAUSE));
        //add bean!!
        getBeanManager().addBean(this, this::onBeanLoad, true, false);
    }

    PARENT getParent();

    Hashtable<String, Object> getVars();

    void onBeanLoad();

    void onInit();

    void onStart();

    void onStop();
}
