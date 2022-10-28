package org.firstinspires.ftc.teamcode.parts.slamra;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.spartronics4915.lib.T265Camera;
import com.spartronics4915.lib.T265Helper;
import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPart;
import org.firstinspires.ftc.teamcode.other.Position;

public class Slamra extends RobotPart {
	//variables
	public volatile T265Camera slamera = null;
	Position currentPose = null;
	Pose2d robotOffset = new Pose2d(0,0,0);

	//constructors

	public Slamra(Robot robot, SlamraSettings settings) {
		super(robot, null, settings);
	}
	public Slamra(Robot robot) {
		super(robot, null, new SlamraSettings());
	}

	//RobotPart Methods
	@Override
	public void onConstruct() {}

	@Override
	public void onInit() {
		if (slamera == null) {
			slamera = T265Helper.getCamera(
					new T265Camera.OdometryInfo(
							robotOffset,.1
					), robot.hardwareMap.appContext);
		}
		if (!slamera.isStarted()) slamera.start();
	}

	@Override
	public void onStart() {}

	@Override
	public void onPause() {}

	@Override
	public void onUnpause() {}

	@Override
	public void onRunLoop(short runMode) {
		TelemetryPacket packet = new TelemetryPacket();
		T265Camera.CameraUpdate up = slamera.getLastReceivedCameraUpdate();
		if (up == null) return;
		Pose2d update = up.pose;
		currentPose = new Position(update.getX(), update.getY(), update.getHeading());
	}

	@Override
	public void onAddTelemetry() {
		robot.addTelemetry("Slamra Raw", currentPose.toString());
	}

	@Override
	public void onStop() {}
}