package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Supplier;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.part.PartParent;
import om.self.task.core.Group;
import om.self.task.core.OrderedGroup;
import om.self.task.event.EventManager;

/**
 * Sets up the framework by configuring base events, adding all the core beans(ex: OpMode), and generating other beans such as the task manager
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT</li>
 *     <li>INITIAL_START</li>
 *     <li>START</li>
 *     <li>STOP</li>
 * </ul>
 */
public class Robot implements PartParent{
    //managers
    public final EventManager eventManager = new EventManager("main");

    public final Group taskManager = new OrderedGroup("main");
    public final Group startTaskManager = new Group("start tasks", taskManager);
    public final Group regularTaskManager = new Group("regular tasks", taskManager);
    public final Group endTaskManager = new Group("end tasks", taskManager);

    public final BeanManager beanManager = new BeanManager();

    //other things
    public final OpMode opMode;

    private boolean initialStart = true;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
        //add events
        eventManager.attachToEvent(Events.INIT, "load dependencies", beanManager::load);
        eventManager.attachToEvent(Events.START, "start taskManager",() -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(Events.STOP, "stop taskManager", () -> taskManager.runCommand(Group.Command.PAUSE));
        //add different task stages
        startTaskManager.autoStopPolicy = Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        startTaskManager.runCommand(Group.Command.START);
        regularTaskManager.autoStopPolicy = Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        regularTaskManager.runCommand(Group.Command.START);
        endTaskManager.autoStopPolicy = Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        endTaskManager.runCommand(Group.Command.START);
        //add bean!!
        getBeanManager().addBean(this, null, false, true);
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

    @Override
    public BeanManager getBeanManager(){return beanManager;}


    public void init(){
        eventManager.triggerEventRecursively(Events.INIT);
    }

    public void start(){
        if(initialStart){
            eventManager.triggerEventRecursively(Events.INITIAL_START);
            initialStart = false;
        }
        eventManager.triggerEventRecursively(Events.START);
    }

    public void run(){
        taskManager.run();
    }

    public void stop(){
        eventManager.triggerEventRecursively(Events.STOP);
    }

    public static final class Events{
        public static final String INIT = "INIT";
        public static final String INITIAL_START = "INITIAL_START";
        public static final String START = "START";
        public static final String STOP = "STOP";
    }
}