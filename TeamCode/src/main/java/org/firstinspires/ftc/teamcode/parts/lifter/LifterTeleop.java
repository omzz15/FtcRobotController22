package org.firstinspires.ftc.teamcode.parts.lifter;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.lifter.settings.LifterTeleopSettings;

import java.util.function.Supplier;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.supplier.suppliers.EdgeSupplier;
import om.self.task.core.TaskEx;

public class LifterTeleop extends LoopedPartImpl<Lifter, LifterTeleopSettings, ObjectUtils.Null> {
    private LifterTeleopSettings settings;

    private EdgeSupplier preDropEdge = new EdgeSupplier();

    Supplier<Boolean> magic = new EdgeSupplier(() -> parent.parent.opMode.gamepad2.a).getRisingEdgeSupplier();

    public LifterTeleop(Lifter parent) {
        super(parent, "lifter teleop");
        setSettings(LifterTeleopSettings.makeDefault(parent.parent));
    }

    public LifterTeleop(Lifter parent, LifterTeleopSettings settings) {
        super(parent, "lifter teleop");
        setSettings(settings);
    }

    public LifterTeleopSettings getSettings() {
        return settings;
    }

    public void setSettings(LifterTeleopSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onBeanLoad() {}

    @Override
    public void onInit() {
        preDropEdge.setBase(() -> settings.preDropSupplier.get() > -1);
    }

    @Override
    public void onStart() {
        parent.setBaseController(() -> new LifterControl(
                (double) settings.heightSpeedSupplier.get(),
                (double) settings.turnSpeedSupplier.get() * settings.turnSpeedMultiplier,
                settings.grabberCloseSupplier.get()
        ), true);
    }

    @Override
    public void onRun() {
        parent.setCone(settings.coneChangeSupplier.get() + parent.getCone());

        if(preDropEdge.isRisingEdge() && parent.isGrabberClosed()){
            parent.setPole(settings.preDropSupplier.get());
            parent.startAutoPreDrop();
        }
        else if(settings.autoGrabSupplier.get())
            parent.startAutoGrab();
        else if(settings.autoDockSupplier.get())
            parent.startAutoDock();
        else if(settings.autoDropSupplier.get() && parent.isGrabberClosed())
            parent.startAutoDrop2();

        if(magic.get()){
            parent.setLiftPosition(parent.getLiftPosition() - 200);
        }

        if(parent.parent.opMode.gamepad2.start || parent.parent.opMode.gamepad1.start){
            parent.emergencyStop();
        }

        parent.parent.opMode.telemetry.addData("cone", parent.getCone());
    }

    @Override
    public void onStop() {
        parent.setBaseControllerToDefault(parent.isControlActive());
    }
}