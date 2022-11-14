package om.self.ezftc.core.part;

import om.self.task.core.Group;

public abstract class ConfigurablePart<PARENT extends PartParent, SETTINGS, HARDWARE> extends LoopedPart<PARENT> {
    private SETTINGS settings;
    private HARDWARE hardware;

    public ConfigurablePart(PARENT parent, String name){
        super(parent, name);
    }

    public ConfigurablePart(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
    }

    public void setConfig(SETTINGS settings, HARDWARE hardware){
        setSettings(settings);
        setHardware(hardware);
    }

    public SETTINGS getSettings() {
        return settings;
    }

    public void setSettings(SETTINGS settings) {
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    public HARDWARE getHardware() {
        return hardware;
    }

    public void setHardware(HARDWARE hardware) {
        onHardwareUpdate(hardware);
        this.hardware = hardware;
    }


    //----------Triggered methods----------//
    public void onSettingsUpdate(SETTINGS settings){}

    public void onHardwareUpdate(HARDWARE hardware){}
}
