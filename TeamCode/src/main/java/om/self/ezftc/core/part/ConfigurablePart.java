package om.self.ezftc.core.part;

public interface ConfigurablePart<PARENT extends PartParent, SETTINGS, HARDWARE> extends Part<PARENT> {
    default void setConfig(SETTINGS settings, HARDWARE hardware){
        setSettings(settings);
        setHardware(hardware);
    }

    default SETTINGS getSettings(){
        return (SETTINGS) getVars().get("settings");
    }

    default void setSettings(SETTINGS settings){
        onSettingsUpdate(settings);
        getVars().put("settings", settings);
    }

    default HARDWARE getHardware(){
        return (HARDWARE) getVars().get("hardware");
    }

    default void setHardware(HARDWARE hardware){
        onHardwareUpdate(hardware);
        getVars().put("hardware", hardware);
    }


    //----------Triggered methods----------//
    void onSettingsUpdate(SETTINGS settings);

    void onHardwareUpdate(HARDWARE hardware);
}
