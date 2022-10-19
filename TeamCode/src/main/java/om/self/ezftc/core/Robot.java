package om.self.ezftc.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeServices;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import om.self.beans.PackageBeanManager;
import om.self.beans.core.Utils;
import om.self.ezftc.other.control.ControlGenerator;
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
 * USED BEANS:
 * <ul>
 *     <li>NONE</li>
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
    private final Set<String> parts = new HashSet<>();
    public final OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    public Robot(OpMode opMode, String... parts){
        this.opMode = opMode;
        this.parts.addAll(Arrays.stream(parts).collect(Collectors.toList()));
    }

    public Set<String> getParts() {
        return parts;
    }

    public void addPart(String part){
        parts.add(part);
    }

    public void removePart(String part){
        parts.remove(part);
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
        beanManager.setFilter(
                bean -> {
                    Part part = Utils.getAnnotationRecursively(bean.getClass(), Part.class);
                    return part == null || parts.contains(part.value());
                }
        );
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