package om.self.ezftc.core.part;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.Robot;
import om.self.task.core.Group;
import om.self.task.event.EventManager;
import om.self.task.other.Utils;

/**
 * Provides a simple configuration of events, beans, and tasks that allow you to make extensions(ex: robot parts or telemetry) to {@link Robot} or anything that implements {@link PartParent}. <br>
 * IMPORTANT: PLEASE make new documentation when creating a part. Any names used (Ex: event, group, and task names) should be stored in a public static Names class (or a public instance of a Names object if it is instance specific). Check {@link Robot.Names} for an example.
 * <br>
 * <br>
 * This class uses events from {@link Robot.Names.Events} and {@link}
 */
public abstract class Part<PARENT extends PartParent, SETTINGS, HARDWARE> implements PartParent {

    public final class Names{
        public final class Events{
            /**
             * This is the event that is triggered when the bean manager finishes loading this part (when {@link #onBeanLoad()} is finished)
             */
            public static final String LOADED = "LOADED";
        }
    }

    /**
     * The parent of this part
     */
    public final PARENT parent;
    /**
     * This parts task manager. It is attached to the parents task manager so if any of the parent groups are paused/resumed, this parts tasks will also be paused/resumed.
     */
    public final Group taskManager;
    /**
     * This parts event manager. Each part creates its own manager attached to the parents so to trigger an event globally (have it propagate through all children parts), use {@link EventManager#triggerEventRecursively(String)}
     */
    public final EventManager eventManager;
    /**
     * The name of this part. Used for identification and to register the event manager and task manager so it must be unique in the scope of the parent.
     */
    public final String name;

    /**
     * The settings of this part
     */
    private SETTINGS settings;
    /**
     * The hardware of this part
     */
    private HARDWARE hardware;

    /**
     * flag that tells if the part is running. Toggled using the {@link Robot.Names.Events#START} and {@link Robot.Names.Events#STOP} events
     */
    private boolean running;

    /**
     * A constructor that calls {@link #Part(PartParent, String, Group)} with the task manager of the parent for Group
     * @param parent the parent of this part (CAN NOT BE NULL)
     * @param name the name of this part (used to register an event manager and task manager so it must be unique in the scope of the parent)
     */
    public Part(PARENT parent, String name) {
        this(parent, name, parent.getTaskManager());
    }

    /**
     * A constructor that creates an event managers attached to the parents and a task managers attached to the custom task manager then initializes the events, tasks, and beans
     * @param parent the parent of this part (CAN NOT BE NULL)
     * @param name the name of this part (used to register an event manager and task manager so it must be unique in the scope of the parent)
     * @param taskManager the custom task manager that is the parent for this parts task manager
     */
    public Part(PARENT parent, String name, Group taskManager){
        this.parent = parent;
        this.name = name;
        this.taskManager = new Group(name, taskManager);
        eventManager = new EventManager(name, parent.getEventManager());

        //-----event manager-----//
        //make/attach events
        eventManager.attachToEvent(Robot.Names.Events.INIT, "onInit", this::onInit);
        eventManager.attachToEvent(Robot.Names.Events.INITIAL_START, "onInitialStart", this::onInitialStart);
        eventManager.attachToEvent(Robot.Names.Events.START, "onStart", () -> {
            onStart();
            running = true;
        });
        eventManager.attachToEvent(Robot.Names.Events.STOP, "onStop", () -> {
            onStop();
            running = false;
        });
        eventManager.attachToEvent(Robot.Names.Events.STOP, "stop taskManager", () -> taskManager.runCommand(Group.Command.PAUSE));

        //add bean!!
        getBeanManager().addBean(this, this::loadBean, true, false);
    }

    /**
     * Loads the bean using {@link #onBeanLoad()} and triggers the {@link Names.Events#LOADED} event
     */
    private void loadBean(){
        onBeanLoad();
        eventManager.triggerEvent(Names.Events.LOADED);
    }

    /**
     * Gets the name of this part. Generally for identification or debugging purposes
     * @return the name of this part
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the directory of the part parent for identification purposes in relation to the top level part (robot)
     *
     * @return the directory of the part parent
     */
    @Override
    public String getDir() {
        return parent.getDir() + PartParent.dirChar + getName();
    }

    /**
     * Gets the task manager of the part so other parts can add tasks and run code on this part
     * @return the task manager of the part ({@link #taskManager}).
     */
    @Override
    public Group getTaskManager(){
        return taskManager;
    }

    /**
     * Gets the event manager of the part so other parts can add and trigger events on this part
     * @return the event manager of the part ({@link #eventManager})
     */
    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Triggers an event on this part. <br>
     * Note: This calls the recursive version of {@link EventManager#triggerEventRecursively(String)} so the event will propagate through all children parts
     * @param event the event to trigger
     */
    public void triggerEvent(String event){
        eventManager.triggerEventRecursively(event);
    }

    @Deprecated
    public void triggerEvent(Enum event){
        eventManager.triggerEventRecursively(event);
    }

    /**
     * Gets the bean manager so the part can add itself as a bean and get other beans for dependency injection. <br>
     * Note: This should only return the top level bean manager. No other bean managers should even be created.
     * @return the bean manager of the part parent (should be the top level bean manager)
     */
    @Override
    public BeanManager getBeanManager(){
        return parent.getBeanManager();
    }

    /**
     * Gets the settings of this part. It could return null.
     * @return the settings of this part
     */
    public SETTINGS getSettings() {
        return settings;
    }

    /**
     * Calls {@link #onSettingsUpdate(Object)} then sets the settings of this part
     * @param settings the new settings
     */
    public void setSettings(SETTINGS settings) {
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    /**
     * Gets the hardware of this part. It could return null.
     * @return the hardware of this part
     */
    public HARDWARE getHardware() {
        return hardware;
    }

    /**
     * Calls {@link #onHardwareUpdate(Object)} then sets the hardware of this part
     * @param hardware the new hardware
     */
    public void setHardware(HARDWARE hardware) {
        onHardwareUpdate(hardware);
        this.hardware = hardware;
    }

    /**
     * Sets the settings and hardware of this part
     * @param settings the new settings
     * @param hardware the new hardware
     */
    public void setConfig(SETTINGS settings, HARDWARE hardware){
        setSettings(settings);
        setHardware(hardware);
    }

    /**
     * Gets if this part is running.
     * @return {@link #running}
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the info of this part including its task and event manager.
     * @param start the string to start each line with
     * @param tab the string to use as a tab for indentation
     * @return the info of this part
     */
    public String getInfo(String start, String tab){
        return  start + name + " (" + getDir() + "):\n" + //Name and directory
                start + tab + "Task Manager:\n" + //Task manager title
                taskManager.getInfo(start + Utils.repeat(tab, 2), tab) + "\n" + //all task manager info
                start + tab + "Event Manager:\n" + //Event manager title
                eventManager.getInfo(start + Utils.repeat(tab, 2), tab) + "\n";//all event manager info
    }

    @Override
    public String toString(){
        return getInfo("", "│\t");
    }

    /**
     * Method that gets called by the bean manager to load this part
     */
    public abstract void onBeanLoad();

    /**
     * Method that gets called when {@link Robot.Names.Events#INIT} is triggered to initialize the part. <br>
     * WARNING: beans may not be loaded onInit, so please use the {@link #onBeanLoad()} or {@link #onStart()} methods to access beans.
     */
    public abstract void onInit();

    /**
     * Called when the settings of this part are updated.
     * @param settings the new settings
     */
    public void onSettingsUpdate(SETTINGS settings){}

    /**
     * Called when the hardware of this part is updated.
     * @param hardware the new hardware
     */
    public void onHardwareUpdate(HARDWARE hardware){}

    /**
     * Method that gets called when {@link Robot.Names.Events#INITIAL_START} is triggered to run code on specifically the first start. <br>
     * Note: This method is run before {@link Robot.Names.Events#START} is triggered and {@link #onStart()} runs.
     */
    public abstract void onInitialStart();

    /**
     * Method that gets called when {@link Robot.Names.Events#START} is triggered to start the part.
     */
    public abstract void onStart();

    /**
     * Method that gets called when {@link Robot.Names.Events#STOP} is triggered to stop the part.
     */
    public abstract void onStop();
}
