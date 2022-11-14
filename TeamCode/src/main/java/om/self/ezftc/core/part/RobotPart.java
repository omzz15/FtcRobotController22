package om.self.ezftc.core.part;

import om.self.ezftc.core.Robot;
import om.self.task.core.Group;

public abstract class RobotPart<SETTINGS, HARDWARE> extends ConfigurablePart<Robot, SETTINGS, HARDWARE> {
    public RobotPart(Robot parent, String name) {
        super(parent, name, parent.getTaskManager(Robot.RunPosition.REGULAR));
    }

    public RobotPart(Robot parent, String name, Robot.RunPosition runPosition) {
        super(parent, name, parent.getTaskManager(runPosition));
    }
}
