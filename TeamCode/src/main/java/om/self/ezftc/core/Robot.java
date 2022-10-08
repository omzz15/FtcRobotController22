package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import om.self.beans.PackageBeanManager;
import om.self.task.core.EventManager;
import om.self.task.core.Group;

public class Robot {
    //managers
    public final EventManager eventManager = new EventManager();
    public final PackageBeanManager beanManager = new PackageBeanManager();
    public final Group taskManager = new Group("main");

    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    public void init(){
        //add all the beans
        beanManager.addBean(this, false, true);
        beanManager.addBean(taskManager, false, true);
        beanManager.addBean(eventManager, false, true);

        beanManager.addBean(opMode, false, true);
        beanManager.addBean(opMode.hardwareMap, false, true);
        beanManager.addBean(opMode.telemetry, false, true);
        beanManager.addBean(opMode.internalOpModeServices, false, true);

        //load everything
        beanManager.load();

        //add events
        eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> taskManager.runCommand(Group.Command.PAUSE));

        //trigger init
        eventManager.triggerEvent(EventManager.CommonTrigger.INIT);
    }

    public void start(){
        eventManager.triggerEvent(EventManager.CommonTrigger.START);
    }

    public void run(){
        taskManager.run();
    }

    public void stop(){
        eventManager.triggerEvent(EventManager.CommonTrigger.STOP);
    }
}