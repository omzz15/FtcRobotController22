package om.self.ezftc.core.part;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import om.self.task.core.Group;
import om.self.task.core.Task;

//TODO figure out how to reserve certain positions for specific parts to maintain an order
public interface ControllablePart<PARENT extends PartParent, CONTROL> extends Part<PARENT> {
    //----------Names----------//
    class EventNames{
        public static final String startControllers = "START_CONTROLLERS";
        public static final String stopControllers = "STOP_CONTROLLERS";
    }
    class TaskNames {
        public static final String mainControlLoop = "main control loop";
    }


    default void constructControllable(){
        getVars().put("controllers", new LinkedList<CONTROL>());
        getVars().put("nameMapping", new Hashtable<String, Consumer<CONTROL>>());

        //add main control loop
        Task controlLoop = new Task(TaskNames.mainControlLoop, getTaskManager());
        controlLoop.autoStart = false; // ensure it doesn't run right away
        controlLoop.setRunnable(() -> {
            CONTROL c = getBaseController().get();
            for (Consumer<CONTROL> controller: getControllers()) {controller.accept(c);}
            onRun(c);
        });//basically just runs the controllers
        //add events to stop and start controllers
        getEventManager().attachToEvent(EventNames.startControllers, "start control loop", () -> controlLoop.runCommand(Group.Command.QUE_START));
        getEventManager().attachToEvent(EventNames.stopControllers, "stop control loop", () -> controlLoop.runCommand(Group.Command.QUE_PAUSE));
    }

    default boolean isControlActive() {
        return getTaskManager().isChildRunning(TaskNames.mainControlLoop);
    }

    default Supplier<CONTROL> getBaseController() {
        return (Supplier<CONTROL>) getVars().get("baseController");
    }

    default void setBaseController(Supplier<CONTROL> baseController, boolean start) {
        getVars().put("baseController", baseController);
        if(start)
            getTaskManager().runKeyedCommand(TaskNames.mainControlLoop, Group.Command.START);
    }

    default List<Consumer<CONTROL>> getControllers() {
        return (List<Consumer<CONTROL>>) getVars().get("controllers");
    }

    default Hashtable<String, Consumer<CONTROL>> getNameMapping(){
        return (Hashtable<String, Consumer<CONTROL>>)getVars().get("nameMapping");
    }

    default void addController(String name, Consumer<CONTROL> controller){
        getNameMapping().put(name, controller);
        getControllers().add(controller);
    }

    default void addController(String name, Consumer<CONTROL> controller, int location){
        getNameMapping().put(name, controller);
        getControllers().add(location, controller);
    }

    default void removeController(String name){
        if(getNameMapping().containsKey(name))
            getControllers().remove(getNameMapping().remove(name));
    }

    void onRun(CONTROL control);
}
