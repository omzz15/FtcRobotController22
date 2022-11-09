package org.firstinspires.ftc.teamcode.parts.positiontracker.settings;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

public class PositionTrackerSettings {

	public final AxesOrder axesOrder;
	public final boolean flipAngle;
	public final int stalePosTime;
	public final double startAngle;

	public PositionTrackerSettings(AxesOrder axesOrder, boolean flipAngle, int stalePosTime, double startAngle) {
		this.axesOrder = axesOrder;
		this.flipAngle = flipAngle;
		this.stalePosTime = stalePosTime;
		this.startAngle = startAngle;
	}

	public static PositionTrackerSettings makeDefault(){
		return new PositionTrackerSettings(
				AxesOrder.XYZ,
				true,
				30,
				0
		);
	}
}