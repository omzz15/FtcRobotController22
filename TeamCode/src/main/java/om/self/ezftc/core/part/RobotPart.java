package om.self.ezftc.core.part;

import om.self.ezftc.core.Robot;

public abstract class RobotPart<SETTINGS, HARDWARE, CONTROL> extends Part<Robot, SETTINGS, HARDWARE, CONTROL>{
    public RobotPart(Robot parent, String name, boolean enableLoop) {
        super(parent, name, parent.getTaskManager(Robot.RunPosition.REGULAR), enableLoop);
    }

    public RobotPart(Robot parent, String name, Robot.RunPosition runPosition, boolean enableLoop) {
        super(parent, name, parent.getTaskManager(runPosition), enableLoop);
    }
}