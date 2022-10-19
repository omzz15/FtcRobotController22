package om.self.ezftc.core;

import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

/**
 * Provides a simple configuration of events, beans, and tasks that allow you to make part extensions(ex: telemetry).
 * PLEASE make new documentation when creating a part extension.
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>START_(part name) --> START_(part name)_(extension name)</li>
 *     <li>STOP_(part name) --> STOP_(part name)_(extension name)</li>
 * </ul>
 * @param <PART> the type of the part you want to extend
 */
public abstract class PartExtension<PART extends RobotPart<?,?>> {
    private PART part;
    private final String name;

    public PartExtension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void init(PART part, EventManager eventManager){
        //-----part-----//
        this.part = part;
        Task task = new Task(name, part.getTaskManager());
        task.setRunnable(this::onRun);

        //-----event manager-----//
        //make/attach start
        String startEventNameBase = "START_" + part.getName().toUpperCase();
        String startEventName = startEventNameBase + "_" + name.toUpperCase();
        eventManager.attachToEvent(startEventName, this::onStart);
        eventManager.attachToEvent(startEventName, () -> task.runCommand(Group.Command.START));
        eventManager.attachToEvent(startEventNameBase, () -> eventManager.triggerEvent(startEventName));

        //make/attach stop
        String stopEventNameBase = "STOP_" + part.getName().toUpperCase();
        String stopEventName = startEventNameBase + "_" + name.toUpperCase();
        eventManager.attachToEvent(stopEventName, this::onStop);
        eventManager.attachToEvent(stopEventName, () -> task.runCommand(Group.Command.PAUSE));
        eventManager.attachToEvent(stopEventNameBase, () -> eventManager.triggerEvent(stopEventName));

        onInit();
    }

    public PART getPart() {
        return part;
    }

    public abstract void onInit();

    public abstract void onRun();

    public abstract void onStart();

    public abstract void onStop();
}
