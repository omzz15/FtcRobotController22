package org.firstinspires.ftc.teamcode.parts.lifter;

import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterTeleopSettings;

import om.self.ezftc.core.part.LoopedPart;
import om.self.ezftc.core.part.implementations.PartImpl;
import om.self.task.core.Group;

public class LifterTeleop extends PartImpl<Lifter> implements LoopedPart<Lifter> {
    private LifterTeleopSettings settings;

    public LifterTeleop(Lifter parent) {
        super(parent, "lifter teleop");
        setSettings(LifterTeleopSettings.makeDefault(parent.parent));
        constructLooped();
    }

    public LifterTeleop(Lifter parent, LifterTeleopSettings settings) {
        super(parent, "lifter teleop");
        setSettings(settings);
        constructLooped();
    }

    public LifterTeleopSettings getSettings() {
        return settings;
    }

    public void setSettings(LifterTeleopSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onBeanLoad() {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {
        if(parent.getSettings().useOldGrabber){
            parent.setBaseController(() -> new LifterControl(
                    (double) settings.heightSpeedSupplier.get(),
                    (double) settings.turnSpeedSupplier.get() * settings.turnSpeedMultiplier,
                    settings.grabberCloseSupplier.get()
            ), true);
        }
        else {
            parent.setBaseController(() -> new LifterControl(
                    (double) settings.heightSpeedSupplier.get(),
                    (double) settings.turnSpeedSupplier.get() * settings.turnSpeedMultiplier,
                    settings.grabberMoveSupplier.get()
            ), true);
        }
    }

    @Override
    public void onRun() {
        if(settings.goToBottomSupplier.get()) parent.setLiftToBottom();
        else if(settings.autoGrabSupplier.get()) parent.getTaskManager().runKeyedCommand(Lifter.TaskNames.autoGrab, Group.Command.START);
    }

    @Override
    public void onStop() {
    }
}