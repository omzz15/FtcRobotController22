package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import om.self.ezftc.other.control.ControlGenerator;
import om.self.ezftc.other.hardware.motor.MotorGenerator;
import om.self.task.core.EventManager;
import om.self.task.core.Group;

/**
 * Sets up the framework by configuring base events, adding all the core beans(ex: OpMode), and generating other bean from classes
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT</li>
 *     <li>START</li>
 *     <li>STOP</li>
 * </ul>
 */
public class Robot {
    //managers
    public final EventManager eventManager = new EventManager();
    public final Group taskManager = new Group("main");

    //other things
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    public void init(){
        ControlGenerator.generate(opMode);
        MotorGenerator.generate(opMode.hardwareMap);

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