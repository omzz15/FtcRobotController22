package om.self.ezftc.core.part;


import om.self.task.core.Group;
import om.self.task.core.Task;

/**
 * Provides a simple configuration of events, beans, and tasks that allow you to make extensions(ex: robot parts or telemetry).
 * PLEASE make new documentation when creating a part.
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>START_(part name) --> START_(part name)_(extension name)</li>
 *     <li>STOP_(part name) --> STOP_(part name)_(extension name)</li>
 * </ul>
 * @param <PARENT> the type of the part you want to extend
 */
public interface LoopedPart<PARENT extends PartParent> extends Part<PARENT>{
    //----------Names----------//
    class TaskNames {
        public static final String mainLoop = "main loop";
    }

    default void constructLooped(){
        new Task(TaskNames.mainLoop, getTaskManager()).setRunnable(this::onRun);
    }

    void onRun();
}
