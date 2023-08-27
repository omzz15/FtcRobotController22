package om.self.ezftc.prebuilt.positiontracker;

import om.self.ezftc.utils.Constants;
import om.self.ezftc.utils.Vector3;

public class PositionTrackerSettings {
	public final int stalePosTime;
	public final Vector3 startPosition;

	public PositionTrackerSettings(int stalePosTime, Vector3 startPosition) {
		this.stalePosTime = stalePosTime;
		this.startPosition = startPosition;
	}

	public static PositionTrackerSettings makeDefault(){
		return new PositionTrackerSettings(
				100,
				new Vector3(-1.5 * Constants.tileSide,62,90)
		);
	}

	public PositionTrackerSettings withStartPosition(Vector3 position){
		return new PositionTrackerSettings(stalePosTime, position);
	}
}