package om.self.ezftc.core.part.implementations;

import java.util.Hashtable;

import om.self.beans.core.BeanManager;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.core.part.PartParent;
import om.self.task.core.EventManager;
import om.self.task.core.Group;

public abstract class PartImpl<PARENT extends PartParent> implements Part<PARENT> {
    public final PARENT parent;
    private final Group taskManager;
    private final EventManager eventManager;
    private final String name;
    private final Hashtable<String, Object> vars = new Hashtable<>();

    public PartImpl(PARENT parent, String name) {
        this.parent = parent;
        this.name = name;
        taskManager = new Group(name, parent.getTaskManager());
        eventManager = new EventManager(name, parent.getEventManager());
        construct();
    }

    public PartImpl(PARENT parent, String name, Group taskManager){
        this.parent = parent;
        this.name = name;
        this.taskManager = new Group(name, taskManager);
        eventManager = new EventManager(name, parent.getEventManager());
        construct();
    }

    @Override
    public PARENT getParent() {
        return parent;
    }

    @Override
    public Hashtable<String, Object> getVars() {
        return vars;
    }

    @Override
    public String getName() {
        return name;
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
    public BeanManager getBeanManager() {
        return parent.getBeanManager();
    }
}
