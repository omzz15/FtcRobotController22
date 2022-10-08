package om.self.ezftc.core;

import om.self.beans.core.Autowired;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.Task;

public abstract class TeleopPart<PART extends RobotPart<?,?>> {
    private PART part;

    @Autowired
    public void construct(PART part, EventManager eventManager){
        //-----part-----//
        this.part = part;
        Task teleop = new Task("teleop", part.getTaskManager());
        teleop.setRunnable(this::teleopCode);

        //-----event manager-----//
        //make/attach start
        String startEventNameBase = "START_" + part.getName().toUpperCase();
        String startEventName = startEventNameBase + "_TELEOP";
        eventManager.attachToEvent(startEventName, this::onStart);
        eventManager.attachToEvent(startEventName, () -> teleop.runCommand(Group.Command.START));
        eventManager.attachToEvent(startEventNameBase, () -> eventManager.triggerEvent(startEventName));

        //make/attach stop
        String stopEventNameBase = "STOP_" + part.getName().toUpperCase();
        String stopEventName = startEventNameBase + "_TELEOP";
        eventManager.attachToEvent(stopEventName, this::onStop);
        eventManager.attachToEvent(stopEventName, () -> teleop.runCommand(Group.Command.PAUSE));
        eventManager.attachToEvent(stopEventNameBase, () -> eventManager.triggerEvent(stopEventName));
    }

    public PART getPart() {
        return part;
    }

    public abstract void teleopCode();

    public void onStart(){}

    public void onStop(){}
}
