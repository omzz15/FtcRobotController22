package om.self.ezftc.core.part;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import om.self.task.core.Group;
import om.self.task.core.Task;
import om.self.task.core.TaskEx;

//TODO figure out how to reserve certain positions for specific parts to maintain an order
public abstract class ControllablePart<PARENT extends PartParent, SETTINGS, HARDWARE, CONTROL> extends Part<PARENT, SETTINGS, HARDWARE> {
    //----------Names----------//
    class EventNames{
        public static final String startControllers = "START_CONTROLLERS";
        public static final String stopControllers = "STOP_CONTROLLERS";
    }
    class TaskNames {
        public static final String mainControlLoop = "main control loop";
    }

    //controls
    private Supplier<CONTROL> baseController;
    private LinkedList<Consumer<CONTROL>> controllers = new LinkedList<>();
    private Hashtable<String, Consumer<CONTROL>> controllerNameMapping = new Hashtable<>();

    private LinkedList<Consumer<CONTROL>> controllersBackup = new LinkedList<>();
    private Hashtable<String, Consumer<CONTROL>> controllerNameMappingBackup = new Hashtable<>();

    /**
     * base constructor
     * @param parent the parent of this part
     * @param name the name of the part(used to register an event manager and task manager so it must be unique)
     * @param baseController the default(starting) controller. this can be changed later using {@link ControllablePart#setBaseController(Supplier, boolean)}
     */
    public ControllablePart(PARENT parent, String name, Supplier<CONTROL> baseController) {
        super(parent, name);
        constructControllable();
        setBaseController(baseController, true);
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
        constructControllable();
        setBaseController(baseController, true);
    }

    /**
     * constructor that doesn't set the base controller
     * @param parent the parent of this part
     * @param name the name of the part(used to register an event manager and task manager so it must be unique)
     * @deprecated please use {@link ControllablePart#ControllablePart(PARENT, String, Supplier<CONTROL>)} because this will not ensure that {@link ControllablePart#onRun(Object)} and controllers will be run
     */
    @Deprecated
    public ControllablePart(PARENT parent, String name) {
        super(parent, name);
        constructControllable();
    }

    public ControllablePart(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
        constructControllable();
    }

    void constructControllable(){
        //add main control loop
        Task controlLoop = new Task(TaskNames.mainControlLoop, getTaskManager());
        controlLoop.autoStart = false; // ensure it doesn't run right away
        controlLoop.setRunnable(() -> {
            CONTROL c = getBaseController().get();
            for (Consumer<CONTROL> controller: controllers) {controller.accept(c);}
            onRun(c);
        });//basically just runs the controllers
        //add events to stop and start controllers
        getEventManager().attachToEvent(EventNames.startControllers, "start control loop", () -> controlLoop.runCommand(Group.Command.START));
        getEventManager().attachToEvent(EventNames.stopControllers, "stop control loop", () -> controlLoop.runCommand(Group.Command.PAUSE));
    }

    public boolean isControlActive() {
        return getTaskManager().isChildRunning(TaskNames.mainControlLoop);
    }

    public Supplier<CONTROL> getBaseController() {
        return baseController;
    }

    public void setBaseController(Supplier<CONTROL> baseController, boolean start) {
        this.baseController = baseController;
        if(start)
            getTaskManager().runKeyedCommand(TaskNames.mainControlLoop, Group.Command.START);
    }

    public List<Consumer<CONTROL>> getControllers() {
        return controllers;
    }

    public void addController(String name, Consumer<CONTROL> controller){
        if(controllerNameMapping.containsKey(name)) return;
        controllerNameMapping.put(name, controller);
        controllers.add(controller);
    }

    public void addController(String name, Consumer<CONTROL> controller, Supplier<Boolean> end){
        Consumer<CONTROL> controllerWithEnd = (control) -> {
            controller.accept(control);
            if(end.get()) removeController(name);
        } ;

        addController(name, controllerWithEnd);
    }

    public void addController(String name, Consumer<CONTROL> controller, int location){
        controllerNameMapping.put(name, controller);
        controllers.add(location, controller);
    }

    public void addController(String name, Consumer<CONTROL> controller, Supplier<Boolean> end, int location){
        Consumer<CONTROL> controllerWithEnd = (control) -> {
            controller.accept(control);
            if(end.get()) removeController(name);
        } ;

        addController(name, controllerWithEnd, location);
    }

    public void removeController(String name){
        if(controllerNameMapping.containsKey(name))
            controllers.remove(controllerNameMapping.remove(name));
    }

    public void queRemoveController(String name){
        getTaskManager().attachChild("remove controller " + name, () -> removeController(name));
    }

    public void moveController(String name, int location){
        if(controllerNameMapping.containsKey(name)){
            Consumer<CONTROL> controller = controllerNameMapping.get(name);
            controllers.remove(controller);
            controllers.add(location, controller);
        }
        throw new RuntimeException("attempted to move controller named '" + name + "' in " + this.getClass() + " but it could not be found.");
    }

    public void createTempEnvironment(){
        controllersBackup = (LinkedList<Consumer<CONTROL>>) controllers.clone();
        controllerNameMappingBackup = (Hashtable<String, Consumer<CONTROL>>) controllerNameMapping.clone();

        controllers.clear();
        controllerNameMapping.clear();
    }

    public void killTempEnvironment(){
        controllers = (LinkedList<Consumer<CONTROL>>) controllersBackup.clone();
        controllerNameMapping = (Hashtable<String, Consumer<CONTROL>>) controllerNameMappingBackup.clone();
    }

    public abstract void onRun(CONTROL control);
}
