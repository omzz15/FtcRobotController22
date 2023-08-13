package om.self.ezftc.core.part;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import om.self.task.core.Group;
import om.self.task.core.Task;

//TODO figure out how to reserve certain positions for specific parts to maintain an order
public abstract class ControllablePart<PARENT extends PartParent, SETTINGS, HARDWARE, CONTROL> extends Part<PARENT, SETTINGS, HARDWARE> {
    /**
     * This class is used to store the names of all the events, tasks, and control environments used by {@link ControllablePart}.
     * <br>
     * Note: This class also uses names from {@link Part.Names}
     * <br>
     * <br>
     * Check {@link Events} for event names.
     * <br>
     * Check {@link Tasks} for task names.
     * <br>
     * Check {@link ControlEnvironments} for control environment names.
     */
    public static final class Names{
        public static final class Events {
            /**
             * This event starts the main control loop that runs all the controllers and feeds the control to the {@link ControllablePart#onRun(Object)} method
             */
            public static final String START_CONTROLLERS = "START_CONTROLLERS";
            /**
             * This event stops the main control loop that runs all the controllers and feeds the control to the {@link ControllablePart#onRun(Object)} method
             */
            public static final String STOP_CONTROLLERS = "STOP_CONTROLLERS";
        }

        public static final class Tasks {
            /**
             * The name of the main control loop task. This task just calls {@link ControllablePart#onRun(Object)} every loop with the current control (output from {@link ControlEnvironment#getControl()} on {@link ControllablePart#currentControlEnvironment})
             */
            public static final String MAIN_CONTROL_LOOP = "main control loop";
        }

        public static final class ControlEnvironments {
            /**
             * This is the default control environment that is created in the constructor
             */
            public static final String DEFAULT = "default";
        }
    }

    /**
     * This class allows all the controllers to be stored and managed in one object to make it easy to create multiple control environments and switch between them.
     */
    class ControlEnvironment implements Cloneable{
        /**
         * The base that supplies the control object to the {@link #controllers} so they can use/modify it.
         * <br>
         * IMPORTANT: This value must be set before {@link #getControl()} is called or else it will throw a {@link NullPointerException}
         */
        private Supplier<CONTROL> baseController;
        /**
         * Consumers that take the control object from {@link #baseController} and use/modify it to be output by {@link #getControl()}.
         * <br>
         * Note: The order of the controllers is the order they are called in and if an earlier controller modifies the control object, that will be the object they receive.
         */
        private CopyOnWriteArrayList<Consumer<CONTROL>> controllers = new CopyOnWriteArrayList<>();
        /**
         * A map that stores the name of each controller and the controller itself. This is used for easy reference to the controllers.
         */
        private ConcurrentHashMap<String, Consumer<CONTROL>> controllerNameMapping = new ConcurrentHashMap<>();

        /**
         * Creates a control environment with a base controller and a list of controllers if specified.
         * @param baseController the base controller ({@link #baseController}). This must be set before {@link #getControl()} is called or else it will throw a {@link NullPointerException}
         * @param controllers maps of names to controllers. The controllers are added in the order they are specified in the maps.
         */
        public ControlEnvironment(Supplier<CONTROL> baseController, Map<String, Consumer<CONTROL>>... controllers) {
            setBaseController(baseController);
            Arrays.stream(controllers).forEach(map -> map.forEach(this::addController));
        }

        /**
         * Returns the base controller that is used to create the control object that is passed to the controllers.
         * @return the base controller ({@link #baseController})
         */
        public Supplier<CONTROL> getBaseController() {
            return baseController;
        }

        /**
         * Sets the base controller that is used to create the control object that is passed to the controllers.
         * @param baseController the base controller to set ({@link #baseController}) to
         */
        public void setBaseController(Supplier<CONTROL> baseController) {
            this.baseController = baseController;
        }

        /**
         * Generates the control object and passes it to all controllers then returns the control object.
         * <br>
         * Note: This is automatically used by the main control loop task ({@link Names.Tasks#MAIN_CONTROL_LOOP}) so you don't need to call it yourself.
         * @return the control object
         */
        public CONTROL getControl(){
            CONTROL control = baseController.get();
            for (Consumer<CONTROL> controller: controllers) {controller.accept(control);}
            return control;
        }

        /**
         * Returns the list of all the controllers in this environment.
         * @return all controllers ({@link #controllers})
         */
        public List<Consumer<CONTROL>> getControllers() {
            return controllers;
        }

        /**
         * Returns the hashmap of all the controllers to their names in this environment.
         * @return all names and controllers ({@link #controllerNameMapping})
         */
        public ConcurrentHashMap<String, Consumer<CONTROL>> getControllerNameMapping() {
            return controllerNameMapping;
        }

        /**
         * Adds a controller to the list in the specified location.
         * @param name the name of the controller. This is used to reference the controller in the {@link #controllerNameMapping} and when updating/removing it.
         * @param controller the controller to add
         * @param location the location to add the controller at
         * @return true if the controller was added successfully, false if the name is already used
         */
        public boolean addController(String name, Consumer<CONTROL> controller, int location){
            if(controllerNameMapping.containsKey(name)) return false;
            controllerNameMapping.put(name, controller);
            controllers.add(location, controller);
            return true;
        }

        /**
         * Adds a controller to the list in the specified location and adds an end condition to the controller.
         * @param name the name of the controller. This is used to reference the controller in the {@link #controllerNameMapping} and when updating/removing it.
         * @param controller the controller to add
         * @param end the end condition. This is a supplier that returns true when the controller should be removed
         * @param location the location to add the controller at
         * @return true if the controller was added successfully, false if the name is already used
         */
        public boolean addController(String name, Consumer<CONTROL> controller, Supplier<Boolean> end, int location){
            Consumer<CONTROL> controllerWithEnd = (control) -> {
                controller.accept(control);
                if(end.get()) removeController(name);
            } ;

            return addController(name, controllerWithEnd, location);
        }

        /**
         * Adds a controller to the end of the list.
         * @param name the name of the controller. This is used to reference the controller in the {@link #controllerNameMapping} and when updating/removing it.
         * @param controller the controller to add
         * @return true if the controller was added successfully, false if the name is already used
         */
        public boolean addController(String name, Consumer<CONTROL> controller){
            if(controllerNameMapping.containsKey(name)) return false;
            controllerNameMapping.put(name, controller);
            controllers.add(controller);
            return true;
        }

        /**
         * Adds a controller to the end of the list and adds an end condition to the controller.
         * @param name the name of the controller. This is used to reference the controller in the {@link #controllerNameMapping} and when updating/removing it.
         * @param controller the controller to add
         * @param end the end condition. This is a supplier that returns true when the controller should be removed
         * @return true if the controller was added successfully, false if the name is already used
         */
        public boolean addController(String name, Consumer<CONTROL> controller, Supplier<Boolean> end){
            Consumer<CONTROL> controllerWithEnd = (control) -> {
                controller.accept(control);
                if(end.get()) removeController(name);
            } ;

            return addController(name, controllerWithEnd);
        }

        /**
         * Updates the controller at the specified name to the new controller.
         * @param name the name of the controller to update
         * @param controller the new controller
         * @return true if the controller was updated successfully, false if the name is not used
         */
        public boolean updateController(String name, Consumer<CONTROL> controller){
            if(!controllerNameMapping.containsKey(name)) return false;
            controllers.set(controllers.indexOf(controllerNameMapping.put(name, controller)), controller);
            return true;
        }

        /**
         * Removes the controller at the specified name.
         * @param name the name of the controller to remove
         * @return true if the controller was removed successfully, false if the name is not used
         */
        public boolean removeController(String name){
            if(!controllerNameMapping.containsKey(name)) return false;
            return controllers.remove(controllerNameMapping.remove(name));
        }

        /**
         * Moves the controller at the specified name to the new location.
         * @param name the name of the controller to move
         * @param location the new location of the controller
         * @return true if the controller was moved successfully, false if the name is not used
         */
        public boolean moveController(String name, int location){
            if(!controllerNameMapping.containsKey(name)) return false;

            Consumer<CONTROL> controller = controllerNameMapping.get(name);
            controllers.remove(controller);
            controllers.add(location, controller);

            return true;
        }

        /**
         * Creates a copy of this control environment. This is used to create a deep copy (no references) of the current environment so it can be modified without affecting the original.
         * @return a deep copy of this control environment
         * @throws CloneNotSupportedException It will not throw this
         */
        public ControlEnvironment clone() throws CloneNotSupportedException {
            ControlEnvironment environment = (ControlEnvironment) super.clone();

            environment.controllerNameMapping = new ConcurrentHashMap<>(controllerNameMapping);
            environment.controllers = new CopyOnWriteArrayList<>(controllers);

            return environment;
        }
    }

    /**
     *
     */
    private final HashMap<String, ControlEnvironment> controlEnvironments = new HashMap<>();
    private ControlEnvironment currentControlEnvironment;

    /**
     * base constructor
     * @param parent the parent of this part
     * @param name the name of the part(used to register an event manager and task manager so it must be unique)
     * @param baseController the default(starting) controller. this can be changed later using {@link ControllablePart#setBaseController(Supplier, boolean)}
     */
    public ControllablePart(PARENT parent, String name, Supplier<CONTROL> baseController) {
        this(parent, name, parent.getTaskManager(), baseController);
    }

    /**
     * base constructor with a custom task manager
     * @param parent the parent of this part
     * @param name the name of the part(used to register an event manager and task manager so it must be unique)
     * @param taskManager the cut
     * @param baseController the default(starting) controller. this can be changed later using {@link ControllablePart#setBaseController(Supplier, boolean)}
     */
    public ControllablePart(PARENT parent, String name, Group taskManager, Supplier<CONTROL> baseController) {
        super(parent, name, taskManager);

        addControlEnvironment(Names.ControlEnvironment.DEFAULT, new ControlEnvironment(baseController), true);

        Task controlLoop = new Task(Names.Tasks.MAIN_CONTROL_LOOP, getTaskManager());
        controlLoop.autoStart = false; // ensure it doesn't run right away
        controlLoop.setRunnable(() -> {
            onRun(currentControlEnvironment.getControl());
        });//basically just runs the controllers
        //add events to stop and start controllers
        getEventManager().attachToEvent(Names.Events.START_CONTROLLERS, "start control loop", () -> controlLoop.runCommand(Group.Command.START));
        getEventManager().attachToEvent(Names.Events.STOP_CONTROLLERS, "stop control loop", () -> controlLoop.runCommand(Group.Command.PAUSE));
    }

    public HashMap<String, ControlEnvironment> getControlEnvironments() {
        return controlEnvironments;
    }

    public ControlEnvironment getCurrentControlEnvironment() {
        return currentControlEnvironment;
    }

    public boolean setCurrentControlEnvironment(String name) {
        if(!controlEnvironments.containsKey(name)) return false;
        currentControlEnvironment = controlEnvironments.get(name);
        return true;
    }

    public void setControlEnvironmentToDefault(){
        setCurrentControlEnvironment(Names.ControlEnvironment.DEFAULT);
    }

    public boolean addControlEnvironment(String name, ControlEnvironment controlEnvironment, boolean setCurrent) {
        if(controlEnvironments.containsKey(name)) return false;
        controlEnvironments.put(name, controlEnvironment);
        if(setCurrent) currentControlEnvironment = controlEnvironment;
        return true;
    }

    public ControlEnvironment getControlEnvironment(String name) {
        return controlEnvironments.get(name);
    }

    public boolean removeControlEnvironment(String name) {
        return controlEnvironments.remove(name) != null;
    }

    public boolean isControlActive() {
        return getTaskManager().isChildRunning(Names.Tasks.MAIN_CONTROL_LOOP);
    }

    public abstract void onRun(CONTROL control);
}
