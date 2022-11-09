package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Hashtable;
import java.util.Optional;

import om.self.ezftc.core.part.PartParent;
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
    public final EventManager eventManager = new EventManager("main");
    public final Group taskManager = new Group("main");

    //other things
    public final Hashtable<Class<?>, Object> parts = new Hashtable<>();
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
        //add events
        eventManager.attachToEvent(EventManager.CommonTrigger.START, "start taskManager",() -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, "stop taskManager", () -> taskManager.runCommand(Group.Command.PAUSE));
    }

    /**
     * used to get a part that is stored in the robot
     * @param cls the class of the part
     * @param <T> the type of the part
     * @return the part or an error if not found
     */
    public<T> Optional<T> getPartByClass(Class<T> cls){
        if(parts.containsKey(cls)) return Optional.of((T)parts.get(cls));
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "robot";
    }

    @Override
    public Group getTaskManager() {
        return taskManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    public void init(){eventManager.triggerEventRecursively(EventManager.CommonTrigger.INIT);}

    public void start(){
        eventManager.triggerEventRecursively(EventManager.CommonTrigger.START);
    }

    public void run(){
        taskManager.run();
    }

    public void stop(){
        eventManager.triggerEventRecursively(EventManager.CommonTrigger.STOP);
    }
}