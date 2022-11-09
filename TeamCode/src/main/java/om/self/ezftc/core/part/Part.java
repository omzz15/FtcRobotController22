package om.self.ezftc.core.part;

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
public abstract class Part<PARENT extends PartParent> implements PartParent {
    private final PARENT parent;
    private final Group taskManager;
    private final EventManager eventManager;
    private final String name;

    public Part(PARENT parent, String name) {
        this.parent = parent;
        this.name = name;
        taskManager = new Group(name,  parent.getTaskManager());
        eventManager = new EventManager(name, parent.getEventManager());

        //-----task-----//
        Task task = new Task("main loop", getTaskManager());
        task.setRunnable(this::onRun);

        //-----event manager-----//
        //make/attach start
        eventManager.attachToEvent(EventManager.CommonTrigger.INIT, "onInit", this::onInit);
        //make/attach start
        eventManager.attachToEvent(EventManager.CommonTrigger.START, "onStart", this::onStart);
        eventManager.attachToEvent(EventManager.CommonTrigger.START, "start taskManager", () -> task.runCommand(Group.Command.START));
        //make/attach stop
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, "onStop", this::onStop);
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, "stop taskManager", () -> task.runCommand(Group.Command.PAUSE));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Group getTaskManager(){
        return taskManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    public PARENT getParent(){
        return parent;
    }

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onRun();

    public abstract void onStop();
}
