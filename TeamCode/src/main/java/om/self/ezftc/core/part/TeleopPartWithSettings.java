package om.self.ezftc.core.part;

public abstract class TeleopPartWithSettings<PART extends RobotPart<?,?>, SETTINGS> extends TeleopPart<PART>{
    private SETTINGS settings;

    public TeleopPartWithSettings(PART parent, SETTINGS settings) {
        super(parent);
        setSettings(settings);
    }

    public SETTINGS getSettings(){
        return settings;
    }

    public void setSettings(SETTINGS settings){
        onSettingsUpdate(settings);
        this.settings = settings;
    }

    public void onSettingsUpdate(SETTINGS settings){}
}
