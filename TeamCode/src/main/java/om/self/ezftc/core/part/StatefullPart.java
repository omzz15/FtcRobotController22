package om.self.ezftc.core.part;

import om.self.task.core.Group;

/**
 * A part that has a state that can be set and updated
 * @param <PARENT> the type of the parent
 * @param <SETTINGS> the type of the settings
 * @param <HARDWARE> the type of the hardware
 * @param <STATE> the type of the state
 */
public abstract class StatefullPart<PARENT extends PartParent, SETTINGS, HARDWARE, STATE> extends Part<PARENT, SETTINGS, HARDWARE>{
    /**
     * Prevents the state from being changed.
     */
    private boolean lockState = false;
    /**
     * The main target state of the part.
     * <br>
     * Note: If {@link #tempState} is active, this can still be updated but will not be fed to {@link #onStateUpdate(Object)} until {@link #killTempState()} is called.
     */
    private STATE state;
    /**
     * If the state can be null
     */
    public boolean allowNull = false;
    /**
     * A temporary state that can be set and then removed without affecting the main state. While active, the main state can still be changed but it will not call {@link #onStateUpdate(Object)}.
     */
    private STATE tempState;

    /**
     * A constructor that calls {@link Part#Part(PartParent, String)} to initialize itself.
     * @param parent the parent of this part
     * @param name the name of this part
     */
    public StatefullPart(PARENT parent, String name) {
        super(parent, name);
    }

    /**
     * A constructor that calls {@link Part#Part(PartParent, String, Group)} to initialize itself.
     * @param parent the parent of this part
     * @param name the name of this part
     * @param taskManager the parent of this part's task manager
     */
    public StatefullPart(PARENT parent, String name, Group taskManager) {
        super(parent, name, taskManager);
    }

    /**
     * Gets if the state is locked
     * @return {@link #lockState}
     */
    public boolean isStateLocked() {
        return lockState;
    }

    /**
     * checks if there is currently a temp state active
     * @return if {@link #tempState} is not null
     */
    public boolean isStateTemp(){
        return tempState != null;
    }

    /**
     * Sets the {@link #lockState} flag to prevent the state from being changed.
     * @param lockState If the state should be locked to the current one
     */
    public void setLockState(boolean lockState) {
        this.lockState = lockState;
    }

    /**
     * Sets the current state by sanitizing the state with {@link #sanitizeState(Object)}, calling {@link #onStateUpdate(Object)}, and storing the state to {@link #state}.
     * <br>
     * Note: If {@link #lockState} is true, the state will not be updated. If a temp state is active, the state will be updated but {@link #onStateUpdate(Object)} will not be called.
     * @param state the new state
     * @throws RuntimeException if {@link #allowNull} is false and the new state is null
     * @return if the state was updated
     */
    public boolean setState(STATE state) {
        if(lockState) return false;
        if (state == null && !allowNull) throw new RuntimeException("allowNull has been set to false so states can not be null!");

        state = sanitizeState(state);
        if(!isStateTemp()) onStateUpdate(state);
        this.state = state;

        return true;
    }

    /**
     * Similar to {@link #setState(Object)} but activates the temp state by setting {@link #tempState} instead of {@link #state}. If a temp state is currently active, it will be overridden.
     * @param state the new temp state
     * @param overrideLock if the {@link #lockState} flag should be ignored
     * @return if the temp state was activated
     */
    public boolean activateTempState(STATE state, boolean overrideLock){
        if(lockState && !overrideLock) return false;
        if (state == null && !allowNull) throw new RuntimeException("allowNull has been set to false so states can not be null!");

        state = sanitizeState(state);
        onStateUpdate(state);
        tempState = state;

        return true;
    }

    /**
     * Kills the temp state by setting {@link #tempState} to null and calling {@link #onStateUpdate(Object)} with {@link #state}.
     */
    public void killTempState(){
        tempState = null;
        onStateUpdate(state);
    }

    /**
     * Gets the current target state which is the last state fed to {@link #onStateUpdate(Object)}.
     * @return {@link #state} if there is no temp state active, otherwise {@link #tempState}
     */
    public STATE getTargetState(){
        if(isStateTemp()) return tempState;
        return state;
    }

    /**
     * This should return the current state of the part. This can be used for telemetry or to check if a part has reached its target state.
     * @return the current state of the part. This is different than {@link #getTargetState()} because this should be the actual state of the part, not {@link #state}.
     */
    public abstract STATE getCurrentState();

    /**
     * This gets called anytime there is a new target state for the part
     * @param state the new target state (after being sanitized)
     */
    public abstract void onStateUpdate(STATE state);

    /**
     * This is used to clean the state and make sure everything is valid
     * @param state the state to sanitize
     * @return the sanitized state
     */
    public abstract STATE sanitizeState(STATE state);
}
