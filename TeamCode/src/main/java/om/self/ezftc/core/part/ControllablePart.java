package om.self.ezftc.core.part;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import om.self.task.core.Group;
import om.self.task.core.Task;

//TODO figure out how to reserve certain positions for specific parts to maintain an order
public abstract class ControllablePart<PARENT extends PartParent, SETTINGS, HARDWARE, CONTROL> extends ConfigurablePart<PARENT, SETTINGS, HARDWARE> {
    //----------Names----------//
    public static class EventNames{
        public static final String startControllers = "START_CONTROLLERS";
        public static final String stopControllers = "STOP_CONTROLLERS";
    }
    public static class TaskNames {
        public static final String mainControlLoop = "main control loop";
    }

    private final List<Consumer<CONTROL>> controllers = new LinkedList<>();
    private final Hashtable<String, Consumer<CONTROL>> nameMapping = new Hashtable<>();

    private Supplier<CONTROL> baseController;

    public ControllablePart(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
        construct();
    }

    public ControllablePart(PARENT parent, String name) {
        super(parent, name);
        construct();
    }

    private void construct(){
        //add main control loop
        Task controlLoop = new Task(TaskNames.mainControlLoop, getTaskManager());
        controlLoop.autoStart = false; // ensure it doesn't run right away
        controlLoop.setRunnable(this::run);
        //add events to stop and start controllers
        getEventManager().attachToEvent(EventNames.startControllers, "start control loop", () -> controlLoop.runCommand(Group.Command.START));
        getEventManager().attachToEvent(EventNames.stopControllers, "stop control loop", () -> controlLoop.runCommand(Group.Command.PAUSE));
    }

    public boolean isControlActive() {
        return getTaskManager().isChildRunning(TaskNames.mainControlLoop);
    }

    public Supplier<CONTROL> getBaseController() {
        return baseController;
    }

    public void setBaseController(Supplier<CONTROL> baseController, boolean start) {
        this.baseController = baseController;
        if(start)
            getTaskManager().runKeyedCommand(TaskNames.mainControlLoop, Group.Command.START);
    }

    public List<Consumer<CONTROL>> getControllers() {
        return controllers;
    }

    private void run(){
        CONTROL c = baseController.get();
        for (Consumer<CONTROL> controller: controllers) {controller.accept(c);}
        onRun(c);
    }

    public void addController(String name, Consumer<CONTROL> controller){
        nameMapping.put(name, controller);
        controllers.add(controller);
    }

    public void addController(String name, Consumer<CONTROL> controller, int location){
        nameMapping.put(name, controller);
        controllers.add(location, controller);
    }

    public void removeController(String name){
        if(nameMapping.containsKey(name))
            controllers.remove(nameMapping.remove(name));
    }

    public abstract void onRun(CONTROL control);
}
