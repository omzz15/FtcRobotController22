package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashSet;
import java.util.Set;

import om.self.ezftc.other.control.ControlGenerator;
import om.self.ezftc.other.hardware.motor.MotorGenerator;
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
    Set<RobotPart<?,?>> parts = new HashSet<>();
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    @Override
    public String getName() {
        return "robot";
    }

    @Override
    public String getDir() {
        return "";
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
        //add events
        eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> taskManager.runCommand(Group.Command.PAUSE));

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