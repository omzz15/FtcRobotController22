//package om.self.ezftc.core.part;
//
//
///**
// * Provides telemetry for a part. All classes extending TelemetryPart will have the 'telemetry' tag so they wont be loaded unless the tag is also present in BeanManager
// * PLEASE make new documentation when creating telemetry for a part.
// * <br>
// * <br>
// * USED EVENTS:
// * <ul>
// *     <li>START_(part name) --> START_(part name)_TELEMETRY</li>
// *     <li>STOP_(part name) --> STOP_(part name)_TELEMETRY</li>
// * </ul>
// * @param <PART> the type of the part you want telemetry for
// */
//public abstract class TelemetryPart<PART extends LoopedRobotPart<?,?>> extends Part<PART> {
//    public TelemetryPart(PART parent) {
//        super(parent, "telemetry");
//    }
//
//    //for convenience I provided default implementations for non important methods
//    @Override
//    public void onInit() {
//
//    }
//
//    @Override
//    public void onStart() {
//
//    }
//
//    @Override
//    public void onStop() {
//
//    }
//}
