package om.self.ezftc.core;

import java.io.Serializable;

@Profile(profile = "fe")
public class RobotPart<SETTINGS extends RobotSettings, HARDWARE extends RobotHardware> implements Serializable {
    private String name;
    private Robot robot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
