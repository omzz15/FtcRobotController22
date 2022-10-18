package om.self.ezftc.core;

import om.self.beans.Bean;
import om.self.task.core.EventManager;

/**
 * Provides telemetry for a part. All classes extending TelemetryPart will have the 'telemetry' tag so they wont be loaded unless the tag is also present in BeanManager
 * PLEASE make new documentation when creating telemetry for a part.
 * <br>
 * <br>
 * USED EVENTS:
 * <ul>
 *     <li>INIT_(part name) --> INIT_(part name)_TELEMETRY</li>
 *     <li>START_(part name) --> START_(part name)_TELEMETRY</li>
 *     <li>STOP_(part name) --> STOP_(part name)_TELEMETRY</li>
 * </ul>
 * USED BEANS:
 * <ul>
 *     <li>{@link PART}</li>
 *     <li>{@link EventManager}</li>
 * </ul>
 * ADDED BEANS:
 * <ul>
 *   <li>{@link TelemetryPart}</li>
 * @param <PART> the type of the part you want telemetry for
 */
@Bean(alwaysLoad = true, tags = {"telemetry"})
public abstract class TelemetryPart<PART extends RobotPart<?,?>> extends PartExtension<PART> {
    public TelemetryPart() {
        super("telemetry");
    }

    //for convenience I provided default implementations for non important methods
    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
