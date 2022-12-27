package org.firstinspires.ftc.teamcode.parts.positiontracker.settings;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import om.self.ezftc.utils.Vector3;

public class PositionTrackerSettings {

	public final AxesOrder axesOrder;
	public final boolean flipAngle;
	public final int stalePosTime;
	public final Vector3 startPosition;

	public PositionTrackerSettings(AxesOrder axesOrder, boolean flipAngle, int stalePosTime, Vector3 startPosition) {
		this.axesOrder = axesOrder;
		this.flipAngle = flipAngle;
		this.stalePosTime = stalePosTime;
		this.startPosition = startPosition;
	}

	public static PositionTrackerSettings makeDefault(){
		return new PositionTrackerSettings(
				AxesOrder.XYZ,
				true,
				30,
				new Vector3(0, 0, 0)
		);
	}
}