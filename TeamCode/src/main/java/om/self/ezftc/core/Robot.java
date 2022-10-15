package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeServices;

import om.self.beans.PackageBeanManager;
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
 * ADDED BEANS:
 * <ul>
 *   <li>{@link Robot}</li>
 *   <li>{@link Group}(taskManager)</li>
 *   <li>{@link EventManager}</li>
 *   <li>{@link OpMode}</li>
 *   <li>{@link HardwareMap}</li>
 *   <li>{@link Telemetry}</li>
 *   <li>{@link OpModeServices}</li>
 */
public class Robot {
    //managers
    public final EventManager eventManager = new EventManager();
    public final PackageBeanManager beanManager = new PackageBeanManager();
    public final Group taskManager = new Group("main");

    //other things
    public final OpMode opMode;

    public Robot(OpMode opMode) {

        this.opMode = opMode;
    }

    public void init(){
        //add events
        eventManager.attachToEvent(EventManager.CommonTrigger.START, () -> taskManager.runCommand(Group.Command.START));
        eventManager.attachToEvent(EventManager.CommonTrigger.STOP, () -> taskManager.runCommand(Group.Command.PAUSE));

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