package om.self.ezftc.core.part;

import om.self.ezftc.core.Robot;
import om.self.task.core.Group;
import om.self.task.core.Task;

/**
 * A simple interface that adds a main loop to a part that calls {@link #onRun()} every loop. <br>
 * IMPORTANT: In order for this to work, you must call {@link #constructLoop()} in the constructor (or before the {@link Robot.Names.Events#START} event is triggered).
 */
public interface LoopedPart extends PartParent{
    final class Names{
        public final class Tasks{
            /**
             * The name of the main loop task. This task just calls {@link #onRun()} every loop
             */
            public static final String MAIN_LOOP = "main loop";
        }
    }

    /**
     * Constructs the main loop task and attaches it to the {@link Robot.Names.Events#START} event
     */
    default void constructLoop(){
        Task t = new Task(Names.Tasks.MAIN_LOOP, getTaskManager());
        t.setRunnable(this::onRun);
        getEventManager().attachToEvent(Robot.Names.Events.START, "start main loop", () -> t.runCommand(Group.Command.START));
    }

    /**
     * This method is called every loop
     */
    void onRun();
}
