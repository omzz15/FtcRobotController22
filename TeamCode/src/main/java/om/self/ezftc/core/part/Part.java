package om.self.ezftc.core.part;

import om.self.beans.core.BeanManager;
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
    public static final class TaskNames{
        public static final String mainLoop = "main loop";
    }


    public final PARENT parent;
    private final Group taskManager;
    private final EventManager eventManager;
    private final String name;

    public Part(PARENT parent, String name, boolean enableLoop) {
        this.parent = parent;
        this.name = name;
        taskManager = new Group(name,  parent.getTaskManager());
        eventManager = new EventManager(name, parent.getEventManager());
        construct(enableLoop);
    }

    public Part(PARENT parent, String name, Group taskManager, boolean enableLoop){
        this.parent = parent;
        this.name = name;
        this.taskManager = taskManager;
        eventManager = new EventManager(name, parent.getEventManager());
        construct(enableLoop);
    }

    private void construct(boolean enableLoop){
        //-----event manager-----//
        //make/attach start
        eventManager.attachToEvent(EventManager.CommonEvent.INIT, "onInit", this::onInit);
        //make/attach start
        eventManager.attachToEvent(EventManager.CommonEvent.START, "onStart", this::onStart);
        //make/attach stop
        eventManager.attachToEvent(EventManager.CommonEvent.STOP, "onStop", this::onStop);
        eventManager.attachToEvent(EventManager.CommonEvent.STOP, "stop taskManager", () -> taskManager.runCommand(Group.Command.PAUSE));
        //add bean!!
        getBeanManager().addBean(this, this::onBeanLoad, true, false);

        if(enableLoop)
            constructLoop();
    }

    private void constructLoop(){
        new Task(TaskNames.mainLoop, getTaskManager()).setRunnable(this::onRun);
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

    @Override
    public BeanManager getBeanManager(){
        return parent.getBeanManager();
    }

    public abstract void onBeanLoad();

    public abstract void onInit();

    public abstract void onStart();

    public void onRun(){}

    public abstract void onStop();
}
