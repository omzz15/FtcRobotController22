package om.self.ezftc.core.part;

import om.self.task.core.Group;

public abstract class StatefullPart<PARENT extends PartParent, SETTINGS, HARDWARE, STATE> extends Part<PARENT, SETTINGS, HARDWARE>{

    public StatefullPart(PARENT parent, String name, STATE initialState) {
        super(parent, name);
        onStateUpdate(initialState);
    }

    public StatefullPart(PARENT parent, String name, Group taskManager, STATE initialState) {
        super(parent, name, taskManager);
        onStateUpdate(initialState);
    }

    public abstract void onStateUpdate(STATE state);
}
