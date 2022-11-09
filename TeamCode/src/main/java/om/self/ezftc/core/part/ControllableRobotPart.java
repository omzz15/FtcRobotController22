package om.self.ezftc.core.part;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import om.self.ezftc.core.Robot;
import om.self.task.core.Group;
import om.self.task.core.Task;

//TODO figure out how to reserve certain positions for specific parts to maintain an order
public abstract class ControllableRobotPart<SETTINGS, HARDWARE, CONTROL> extends RobotPart<SETTINGS, HARDWARE> {
    private final List<Function<CONTROL, CONTROL>> controllers = new LinkedList<>();
    private final Hashtable<String, Function<CONTROL, CONTROL>> nameMapping = new Hashtable<>();
    private Supplier<CONTROL> baseController;

    public ControllableRobotPart(Robot robot, String name, Class<?>... dependencies) {
        super(robot, name, dependencies);
        construct();
    }

    public ControllableRobotPart(Robot robot, String name, SETTINGS settings, HARDWARE hardware, Class<?>... dependencies) {
        super(robot, name, settings, hardware, dependencies);
        construct();
    }

    public ControllableRobotPart(Robot robot, String name, Supplier<CONTROL> baseController, Class<?>... dependencies) {
        super(robot, name, dependencies);
        construct();
        setBaseController(baseController, true);
    }

    public ControllableRobotPart(Robot robot, String name, SETTINGS settings, HARDWARE hardware, Supplier<CONTROL> baseController, Class<?>... dependencies) {
        super(robot, name, settings, hardware, dependencies);
        construct();
        setBaseController(baseController, true);
    }

    private void construct(){
        //add control main loop
        Task controlLoop = new Task("main control loop", getTaskManager());
        controlLoop.autoStart = false; // ensure it doesn't run right away
        controlLoop.setRunnable(this::run);
        //add events to stop and start controllers
        getEventManager().attachToEvent("START_CONTROLLERS", "start main control loop", () -> controlLoop.runCommand(Group.Command.START));
        getEventManager().attachToEvent("STOP_CONTROLLERS", "stop main control loop", () -> controlLoop.runCommand(Group.Command.PAUSE));
    }

    public boolean isControlActive() {
        return getTaskManager().isChildRunning("main control loop");
    }

    public Supplier<CONTROL> getBaseController() {
        return baseController;
    }

    public void setBaseController(Supplier<CONTROL> baseController, boolean start) {
        this.baseController = baseController;
        if(start)
            getTaskManager().runKeyedCommand("main control loop", Group.Command.START);
    }

    public List<Function<CONTROL, CONTROL>> getControllers() {
        return controllers;
    }

    private void run(){
        CONTROL c = baseController.get();
        for (Function<CONTROL, CONTROL> controller: controllers) {c = controller.apply(c);}
        onRun(c);
    }

    public void addController(String name, Function<CONTROL, CONTROL> function){
        nameMapping.put(name, function);
        controllers.add(function);
    }

    public void addController(String name, Function<CONTROL, CONTROL> function, int location){
        nameMapping.put(name, function);
        controllers.add(location, function);
    }

    public void removeController(String name){
        if(nameMapping.containsKey(name))
            controllers.remove(nameMapping.remove(name));
    }

    public abstract void onRun(CONTROL control);
}
