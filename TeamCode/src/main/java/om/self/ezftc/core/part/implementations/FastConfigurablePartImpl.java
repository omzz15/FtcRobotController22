package om.self.ezftc.core.part.implementations;

import om.self.ezftc.core.part.ConfigurablePart;
import om.self.ezftc.core.part.PartParent;
import om.self.task.core.Group;

public abstract class FastConfigurablePartImpl<PARENT extends PartParent, SETTINGS, HARDWARE> extends PartImpl<PARENT> implements ConfigurablePart<PARENT, SETTINGS, HARDWARE> {
    private SETTINGS settings;
    private HARDWARE hardware;

    public FastConfigurablePartImpl(PARENT parent, String name) {
        super(parent, name);
    }

    public FastConfigurablePartImpl(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
    }

    @Override
    public SETTINGS getSettings() {
        return settings;
    }

    @Override
    public void setSettings(SETTINGS settings) {
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    @Override
    public HARDWARE getHardware() {
        return hardware;
    }

    @Override
    public void setHardware(HARDWARE hardware) {
        onHardwareUpdate(hardware);
        this.hardware = hardware;
    }
}
