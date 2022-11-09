package om.self.ezftc.core.part;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import om.self.ezftc.core.Robot;
import om.self.task.core.EventManager;

public abstract class RobotPart<SETTINGS, HARDWARE> extends Part<Robot>{
    private SETTINGS settings;
    private HARDWARE hardware;

    private final List<Class<?>> dependenciesCls = new LinkedList<>();

    private final Hashtable<Class<?>, Object> dependencies = new Hashtable<>();

    public RobotPart(Robot robot, String name, Class<?>... dependencies){
        super(robot, name);
        construct(robot, settings, hardware, dependencies);
    }

    public RobotPart(Robot robot, String name, SETTINGS settings, HARDWARE hardware, Class<?>... dependencies){
        super(robot, name);
        construct(robot, settings, hardware, dependencies);
    }

    private void construct(Robot robot, SETTINGS settings, HARDWARE hardware, Class<?>... dependencies){
        robot.parts.put(getClass(), this);//TODO: figure out how to add more isolation by removing this
        dependenciesCls.addAll(Arrays.stream(dependencies).collect(Collectors.toList()));
        getEventManager().attachToEvent(EventManager.CommonTrigger.INIT, "setConfig", () -> setConfig(settings, hardware));
        getEventManager().attachToEvent(EventManager.CommonTrigger.INIT, "loadDependencies", () -> loadDependencies(dependenciesCls));
    }

    public void addDependency(Class<?> cls){
        dependenciesCls.add(cls);
    }

    public<T> T getDependency(Class<T> cls){
        if(!dependencies.containsKey(cls)) throw new RuntimeException("the dependency " + cls.getName() + "was not added and is not present");
        return (T)dependencies.get(cls);
    }

    private void loadDependencies(Collection<Class<?>> classes){
        for(Class<?> cls : classes){
            Optional<?> optional = getParent().getPartByClass(cls);
            if(optional.isPresent()) dependencies.put(cls,optional.get());
            else dependencies.put(cls, makeDependency(cls));
        }
    }

    private<T> T makeDependency(Class<T> cls){
        try{
            return cls.getConstructor().newInstance();
        } catch (Exception ignore){}

        try{
            return cls.getConstructor(Robot.class).newInstance(getParent());
        } catch (Exception ignore) {}

        try {
            return cls.getConstructor(getClass()).newInstance(this);
        } catch (Exception ignore) {}

        throw new RuntimeException("could not generate dependency of type " + cls.getName() + " for the part" + getName());
    }

    public void setConfig(SETTINGS settings, HARDWARE hardware){
        setSettings(settings);
        setHardware(hardware);
    }

    public SETTINGS getSettings() {
        return settings;
    }

    public void setSettings(SETTINGS settings) {
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    public HARDWARE getHardware() {
        return hardware;
    }

    public void setHardware(HARDWARE hardware) {
        onHardwareUpdate(hardware);
        this.hardware = hardware;
    }


    //----------Triggered methods----------//
    public void onSettingsUpdate(SETTINGS settings){}

    public void onHardwareUpdate(HARDWARE hardware){}
}
