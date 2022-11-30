package om.self.ezftc.core.part;

import om.self.task.core.Group;

public abstract class LoopedPartImpl<PARENT extends PartParent, SETTINGS, HARDWARE> extends Part<PARENT, SETTINGS, HARDWARE> implements LoopedPart{
    public LoopedPartImpl(PARENT parent, String name) {
        super(parent, name);
        constructLoop();
    }

    public LoopedPartImpl(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
        constructLoop();
    }
}
