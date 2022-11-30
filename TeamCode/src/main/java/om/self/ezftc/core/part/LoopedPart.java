package om.self.ezftc.core.part;

import om.self.task.core.Task;

public interface LoopedPart extends PartParent{
    class TaskNames {
        public static final String mainLoop = "main loop";
    }

    default void constructLoop(){
        new Task(TaskNames.mainLoop, getTaskManager()).setRunnable(this::onRun);
    }

    void onRun();
}
