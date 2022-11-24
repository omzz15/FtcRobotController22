package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Supplier;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.part.PartParent;
import om.self.task.core.EventManager;
import om.self.task.core.Group;
import om.self.task.core.OrderedGroup;

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
    public boolean enableTelemetry = true;
    public boolean enableDashboard = true;
    public boolean enableTelemetryDebug = true;
    public boolean enableDashboardDebug = true;

    //managers
    public final EventManager eventManager = new EventManager("main");

    public final Group taskManager = new OrderedGroup("main");
    public final Group startTaskManager = new Group("start tasks", taskManager);
    public final Group regularTaskManager = new Group("regular tasks", taskManager);
    public final Group endTaskManager = new Group("end tasks", taskManager);

    public final BeanManager beanManager = new BeanManager();

    //other things
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
        //add events
        eventManager.attachToEvent(EventManager.CommonEvent.INIT, "load dependencies", beanManager::load);
        eventManager.attachToEvent(EventManager.CommonEvent.START, "start taskManager",() -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonEvent.STOP, "stop taskManager", () -> taskManager.runCommand(Group.Command.PAUSE));
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

    public Group getTaskManager(RunPosition runPosition){
        switch (runPosition){
            case START: return startTaskManager;
            case REGULAR: return regularTaskManager;
            case END: return endTaskManager;
        }
        throw new RuntimeException("tried to call getTaskManager with argument " + runPosition + " which is not valid");
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public BeanManager getBeanManager(){return beanManager;}


    public void addDebugSource(Supplier<String> source){
        if(enableTelemetryDebug){

        }
        if(enableDashboardDebug){

        }
    }


    public void init(){eventManager.triggerEventRecursively(EventManager.CommonEvent.INIT);}

    public void start(){
        eventManager.triggerEventRecursively(EventManager.CommonEvent.START);
    }

    public void run(){
        taskManager.run();
    }

    public void stop(){
        eventManager.triggerEventRecursively(EventManager.CommonEvent.STOP);
    }

    public enum RunPosition{
        START,
        REGULAR,
        END
    }
}