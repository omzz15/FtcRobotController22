package om.self.ezftc.core.part;

import om.self.ezftc.core.Robot;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

public class RobotPart<SETTINGS, HARDWARE> extends Part<Robot>{
    private String name;
    private final Group taskManager = new Group(null);
    private SETTINGS settings;
    private HARDWARE hardware;

    public RobotPart(Robot robot, String name, SETTINGS settings, HARDWARE hardware){
        super(robot, name);
        robot.parts.add(this);//TODO: figure out how to add more isolation by removing this
        setSettings(settings);
        setHardware(hardware);
    }

    public SETTINGS getSettings() {
        return settings;
    }

    public void setSettings(SETTINGS settings) {
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    public HARDWARE getHardware() {
        return hardware;
    }

    public void setHardware(HARDWARE hardware) {
        onHardwareUpdate(hardware);
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

    public void init(Robot robot, SETTINGS settings, HARDWARE hardware){
        //-----task manager-----//
        this.taskManager.setName(name);
        taskManager.attachChild(this.taskManager);
        new Task("main loop", this.taskManager).setRunnable(this::onRun);

        //-----event manager-----//
        //make/attach start
        String startEventName = "START_" + name.toUpperCase();
        robot.eventManager.attachToEvent(startEventName, this::onStart);
        robot.eventManager.attachToEvent(startEventName, () -> taskManager.runCommand(Group.Command.START));
        robot.eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> robot.eventManager.triggerEvent(startEventName));

        //make/attach stop
        String stopEventName = "STOP_" + name.toUpperCase();
        robot.eventManager.attachToEvent(stopEventName, this::onStop);
        robot.eventManager.attachToEvent(stopEventName, () -> taskManager.runCommand(Group.Command.PAUSE));
        robot.eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> robot.eventManager.triggerEvent(stopEventName));

        setSettings(settings);
        setHardware(hardware);
        onInit();
    }

    public void init(Robot robot){
        init(robot, settings, hardware);
    }

    public Group getTaskManager(){
        return taskManager;
    }


    //----------Triggered methods----------//
    public void onInit(){}

    public void onSettingsUpdate(SETTINGS settings){}

    public void onHardwareUpdate(HARDWARE hardware){}

    public void onStart(){}

    public void onRun(){}

    public void onStop(){}
}
