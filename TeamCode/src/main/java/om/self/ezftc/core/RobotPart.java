package om.self.ezftc.core;

import om.self.beans.core.Autowired;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

public class RobotPart<SETTINGS, HARDWARE>{
    private String name;
    private final Group taskManager = new Group(null);
    private SETTINGS settings;
    private HARDWARE hardware;


    public SETTINGS getSettings() {
        return settings;
    }

    @Autowired
    public void setSettings(SETTINGS settings) {
        this.settings = settings;
    }

    public HARDWARE getHardware() {
        return hardware;
    }

    @Autowired
    public void setHardware(HARDWARE hardware) {
        this.hardware = hardware;
    }

    /**
     * this sets the name for any descriptors and for the task manager Group
     * @apiNote YOU MUST call this before starting robot, and it must be unique unless events should be linked and it can override the task manager
     * @param name the name of this part
     */
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Autowired
    public void construct(Group taskManager, EventManager eventManager){
        //-----task manager-----//
        this.taskManager.setName(name);
        taskManager.attachChild(this.taskManager);
        new Task("main loop", this.taskManager).setRunnable(this::onRun);

        //-----event manager-----//
        //make/attach init
        String initEventName = "INIT_" + name.toUpperCase();
        eventManager.attachToEvent(initEventName, this::onInit);
        eventManager.attachToEvent(EventManager.CommonTrigger.INIT, () -> eventManager.triggerEvent(initEventName));

        //make/attach start
        String startEventName = "START_" + name.toUpperCase();
        eventManager.attachToEvent(startEventName, this::onStart);
        eventManager.attachToEvent(startEventName, () -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> eventManager.triggerEvent(startEventName));

        //make/attach stop
        String stopEventName = "STOP_" + name.toUpperCase();
        eventManager.attachToEvent(stopEventName, this::onStop);
        eventManager.attachToEvent(stopEventName, () -> taskManager.runCommand(Group.Command.PAUSE));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> eventManager.triggerEvent(stopEventName));
    }
    public Group getTaskManager(){
        return taskManager;
    }


    //----------Triggered methods----------//
    public void onInit(){}

    public void onStart(){}

    public void onRun(){}

    public void onStop(){}
}
