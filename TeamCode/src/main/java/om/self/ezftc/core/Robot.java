package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.part.PartParent;
import om.self.task.core.Group;
import om.self.task.core.OrderedGroup;
import om.self.task.event.EventManager;

/**
 * Sets up the framework by configuring base events, adding all the core beans(ex: OpMode), and generating other beans such as the task manager.
 * <br>
 * <br>
 * Check out {@link Names} for all the names of events and groups used by the robot.
 */
public class Robot implements PartParent{
    /**
     * This stores all the names used by the robot
     * <br>
     * <br>
     * Check out {@link Events} for all the names of events used by the robot.
     * <br>
     * Check out {@link Groups} for all the names of groups used by the robot.
     */
    public static final class Names{
        /**
         * This stores all the events used by the robot. This contains many common events that are used throughout (Ex: Part)<br>
         * IMPORTANT: Every part creates its own event manager so even thought these events are used in parts, they won't be triggered in children unless you use {@link EventManager#triggerEventRecursively(String)} <br>
         * Note: These event names should be in relation to the event manager from getEventManager() or the path to the event manager should be specified in the JavaDoc. If you have a child event manager, use the directory of the event (Ex: if the manager 'sub' is attached to 'main' then the event 'event' should be "sub/event")
         */
        public static final class Events {
            /**
             * This is the event that is triggered when the robot is initialized
             */
            public static final String INIT = "INIT";
            /**
             * This is the event that is triggered after the bean manager loads all the beans
             */
            public static final String ALL_BEAN_LOADED = "ALL_BEAN_LOADED";
            /**
             * This is the event that is triggered when the robot is first started. This will run before the {@link #START} event
             */
            public static final String INITIAL_START = "INITIAL_START";
            /**
             * This is the event that is triggered everytime the robot is started
             */
            public static final String START = "START";
            /**
             * This is the event that is triggered everytime the robot is stopped
             */
            public static final String STOP = "STOP";
        }

        /**
         * This stores all the names of groups used by the robot. <br>
         * Note: These groups should be reachable from the group returned by getTaskManager(). If they are multiple levels down, the path to the group should be specified in the JavaDoc. <br>
         * Note 2: Robot is a little bit of an exception because {@link #getTaskManager()} returns the regular task manager which is a child of the main task manager.
         */
        public static final class Groups {
            /**
             * This is the group that runs early tasks (check out {@link Robot#startTaskManager})
             */
            public static final String START = "start tasks";
            /**
             * This is the group that runs regular tasks (check out {@link Robot#regularTaskManager})
             */
            public static final String REGULAR = "regular tasks";
            /**
             * This is the group that runs late tasks (check out {@link Robot#endTaskManager})
             */
            public static final String END = "end tasks";
        }


    }

    //managers
    /**
     * This is the top level event manager that handles all the events and other event managers. This is used to trigger top level events found in {@link Names.Events} <br>
     * IMPORTANT: To trigger top level events in everything (including parts), use {@link EventManager#triggerEventRecursively(String)}
     */
    public final EventManager eventManager = new EventManager("main");

    /**
     * This is the top level task manager group that handles all tasks (runnables) and other groups. <br>
     * Note: This is not intended to be used directly, use the groups defined in {@link Names.Groups}
     */
    public final Group taskManager = new OrderedGroup("main");
    /**
     * This group is for tasks that need to be run before regular tasks are run. (Ex: running code to find robot position)
     */
    public final Group startTaskManager = new Group(Names.Groups.START, taskManager);
    /**
     * This is the main group where most tasks and groups will go to be run normally.
     */
    public final Group regularTaskManager = new Group(Names.Groups.REGULAR, taskManager);
    /**
     * This group is for tasks that need to be run after all other tasks. (Ex: sending out telemetry)
     */
    public final Group endTaskManager = new Group(Names.Groups.END, taskManager);
    /**
     * Handles all beans (which include but are not limited to this and all parts) and dependency injection
     */
    public final BeanManager beanManager = new BeanManager();

    //other things
    /**
     * Contains all the FTC stuff like hardware map and game-pads
     */
    public final OpMode opMode;

    /**
     * used to trigger {@link Names.Events#INITIAL_START} event
     */
    private boolean initialStart = true;

    /**
     * Configures the {@link #eventManager} and task managers (groups) then adds itself as a bean to {@link #beanManager} so it can be accessed by other components.
     * @param opMode the opMode of the robot
     */
    public Robot(OpMode opMode) {
        this.opMode = opMode;
        //add events
        eventManager.attachToEvent(Names.Events.INIT, "load dependencies", () -> {
            beanManager.load();
            eventManager.triggerEventRecursively(Names.Events.ALL_BEAN_LOADED);
        });

        //these should be useless because there is no parent task manager but if something breaks try uncommenting them
        //eventManager.attachToEvent(Events.START, "start taskManager",() -> taskManager.runCommand(Groups.Command.START));
        //eventManager.attachToEvent(Events.STOP, "stop taskManager", () -> taskManager.runCommand(Groups.Command.PAUSE));

        //add different task stages
        startTaskManager.autoStopPolicy = om.self.task.core.Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        startTaskManager.runCommand(om.self.task.core.Group.Command.START);
        regularTaskManager.autoStopPolicy = om.self.task.core.Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        regularTaskManager.runCommand(om.self.task.core.Group.Command.START);
        endTaskManager.autoStopPolicy = om.self.task.core.Group.AutoManagePolicy.DISABLED; //ensure it never turns off so order is maintained
        endTaskManager.runCommand(om.self.task.core.Group.Command.START);
        //add bean!!
        getBeanManager().addBean(this, null, false, true);
    }

    /**
     * Returns its name. This is needed to be a {@link PartParent}.
     * @return "robot"
     */
    @Override
    public String getName() {
        return "robot";
    }

    /**
     * Returns its directory. This is needed to be a {@link PartParent}.
     * @return "robot"
     */
    @Override
    public String getDir() {
        return getName();
    }

    /**
     * Returns the top level regular task manager. <br>
     * Note: This is NOT the main task manager, it is the regular task manager which is a child of the main task manager ({@link #taskManager})
     * @return {@link #regularTaskManager}
     */
    @Override
    public Group getTaskManager() {
        return regularTaskManager;
    }

    /**
     * Returns the top level event manager.
     * @return {@link #eventManager}
     * @see #eventManager
     */
    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Returns the bean manager so other components can get beans using {@link BeanManager#getBestMatch(Class, boolean)}.
     * @return {@link #beanManager}
     */
    @Override
    public BeanManager getBeanManager(){return beanManager;}

    /**
     * Triggers the {@link Names.Events#INIT} event recursively which will also load all beans then trigger the {@link Names.Events#ALL_BEAN_LOADED} event recursively.
     */
    public void init(){
        eventManager.triggerEventRecursively(Names.Events.INIT);
    }

    /**
     * This will trigger the {@link Names.Events#START} event recursively. <br>
     * Note: If it is the first time starting the robot, it will trigger the {@link Names.Events#INITIAL_START} as well.
     */
    public void start(){
        if(initialStart){
            eventManager.triggerEventRecursively(Names.Events.INITIAL_START);
            initialStart = false;
        }
        eventManager.triggerEventRecursively(Names.Events.START);
    }

    /**
     * This runs the main task manager ({@link #taskManager}) which will run everything in the robot. (This is referred to as a loop in some parts of the library)<br>
     * Note: Even if the robot is stopped, calling this will still run the task manager which could run code so this SHOULD NOT be called if you have stopped the robot.
     */
    public void run(){
        taskManager.run();
    }

    /**
     * This will trigger the {@link Names.Events#STOP} event recursively.
     */
    public void stop(){
        eventManager.triggerEventRecursively(Names.Events.STOP);
    }
}