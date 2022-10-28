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
    private final EventManager eventManager;//TODO add separate event manager for each part
    private final String name;

    public Part(PARENT parent, String name) {
        this.parent = parent;
        this.name = name;
        taskManager = new Group(name,  parent.getTaskManager());
        eventManager = parent.getEventManager();

        //-----part-----//
        Task task = new Task(name, getTaskManager());
        task.setRunnable(this::onRun);

        //-----event manager-----//
        //make/attach start
        eventManager.attachToEvent("INIT" + getDir(), this::onInit);
        eventManager.attachToEvent("INIT" + parent.getDir(), () -> eventManager.triggerEvent("INIT" + getDir()));
        //make/attach start
        eventManager.attachToEvent("START" + getDir(), this::onStart);
        eventManager.attachToEvent("START" + getDir(), () -> task.runCommand(Group.Command.START));
        eventManager.attachToEvent("START" + parent.getDir(), () -> eventManager.triggerEvent("START" + getDir()));

        //make/attach stop
        eventManager.attachToEvent("STOP" + getDir(), this::onStop);
        eventManager.attachToEvent("STOP" + getDir(), () -> task.runCommand(Group.Command.PAUSE));
        eventManager.attachToEvent("STOP" + parent.getDir(), () -> eventManager.triggerEvent("STOP"));
    }

    private String getEventFullName(String eventName, boolean putInFront){
        if(putInFront) return eventName.toUpperCase() + getDir();
        else return getPureDir() + "_" + eventName.toUpperCase();
    }

    public void attachToEvent(String eventName, boolean putInFront, Runnable event){
        eventManager.attachToEvent(getEventFullName(eventName, putInFront), event);
    }

    public void triggerEvent(String eventName, boolean isInFront)

    @Override
    public String getName() {
        return name;
    }

    @Override
    /**
     * this will just call all parent getDir() and will have a '_' in the front. Use getPureDir() if you don't want the '_'
     */
    public String getDir(){
        return parent.getDir() + "_" + name.toUpperCase();
    }

    public String getPureDir(){
        String pDir = parent.getDir();
        if(pDir == null) return name.toUpperCase();
        return pDir + "_" + name.toUpperCase();
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