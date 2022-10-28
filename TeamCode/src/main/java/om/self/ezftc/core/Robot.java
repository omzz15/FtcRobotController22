package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.LinkedList;
import java.util.List;

import om.self.ezftc.core.part.PartParent;
import om.self.ezftc.core.part.RobotPart;
import om.self.task.core.EventManager;
import om.self.task.core.Group;

/**
 * Sets up the framework by configuring base events, adding all the core beans(ex: OpMode), and generating other bean from classes
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT</li>
 *     <li>START</li>
 *     <li>STOP</li>
 * </ul>
 */
public class Robot implements PartParent{
    //managers
    public final EventManager eventManager = new EventManager();
    public final Group taskManager = new Group("main");

    //other things
    public final List<RobotPart<?,?>> parts = new LinkedList<>(); //TODO: figure out how to add more isolation by removing this
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
        //add events
        eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> taskManager.runCommand(Group.Command.PAUSE));
    }

    public<T extends RobotPart<?,?>> T getPartByClass(Class<T> cls){//TODO: figure out how to add more isolation by removing this
        for (RobotPart<?,?> part: parts)
            if(cls.isInstance(part)) return (T) part;

        throw new RuntimeException("could not find a part of " + cls + " . PLease update code to not be dependent on other parts");
    }

    @Override
    public String getName() {
        return "robot";
    }

    @Override
    public String getDir() {
        return null;
    }

    @Override
    public Group getTaskManager() {
        return taskManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    public void init(){
        //trigger init
        eventManager.triggerEvent(EventManager.CommonTrigger.INIT);
    }

    public void start(){
        eventManager.triggerEvent(EventManager.CommonTrigger.START);
    }

    public void run(){
        taskManager.run();
    }

    public void stop(){
        eventManager.triggerEvent(EventManager.CommonTrigger.STOP);
    }
}