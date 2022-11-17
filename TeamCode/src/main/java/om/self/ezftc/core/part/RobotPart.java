package om.self.ezftc.core.part;

import om.self.ezftc.core.Robot;
import om.self.ezftc.core.part.implementations.PartImpl;
import om.self.task.core.EventManager;
import om.self.task.core.Group;

public abstract class RobotPart extends PartImpl<Robot> {
    public RobotPart(Robot parent, String name) {
        super(parent, name, parent.getTaskManager(Robot.RunPosition.REGULAR));
    }

    public RobotPart(Robot parent, String name, Robot.RunPosition runPosition) {
        super(parent, name, parent.getTaskManager(runPosition));
    }
}
