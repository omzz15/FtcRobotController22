package om.self.ezftc.core.part;

import om.self.task.core.Group;

public abstract class StatefullPart<PARENT extends PartParent, SETTINGS, HARDWARE, STATE> extends Part<PARENT, SETTINGS, HARDWARE>{
    private boolean lockState = false;
    private STATE state;

    public StatefullPart(PARENT parent, String name) {
        super(parent, name);
    }

    public StatefullPart(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
    }

    public abstract void onStateUpdate(STATE state);

    public boolean isLockState() {
        return lockState;
    }

    public void setLockState(boolean lockState) {
        this.lockState = lockState;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        if(lockState) return;
        state = sanitizeState(state);
        onStateUpdate(state);
        this.state = state;
    }

    public abstract STATE sanitizeState(STATE state);
}
