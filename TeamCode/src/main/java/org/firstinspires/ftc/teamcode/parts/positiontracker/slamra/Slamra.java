package org.firstinspires.ftc.teamcode.parts.positiontracker.slamra;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.spartronics4915.lib.T265Camera;
import com.spartronics4915.lib.T265Helper;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.Vector3;

public class Slamra extends LoopedPartImpl<PositionTracker, SlamraSettings, ObjectUtils.Null> {
	//variables
	volatile T265Camera slamra;

	//constructors
	public Slamra(PositionTracker parent) {
		super(parent, "slamra");
		setSettings(SlamraSettings.makeDefault());
	}

	public Slamra(PositionTracker parent, SlamraSettings settings) {
		super(parent, "slamra");
		setSettings(settings);
	}

	@Override
	public void onBeanLoad() {

	}

	@Override
	public void onInit() {
		if (slamra == null) {
			slamra = T265Helper.getCamera(
					new T265Camera.OdometryInfo(
							getSettings().robotOffset.toPose2d(),
							getSettings().encoderCovariance
					), parent.parent.opMode.hardwareMap.appContext);
		}
		if (!slamra.isStarted()) slamra.start();
	}

	@Override
	public void onStart() {
		slamra.setPose(parent.getCurrentPosition().toPose2d());

	}

	@Override
	public void onSettingsUpdate(SlamraSettings settings) {

	}

	@Override
	public void onRun() {
		T265Camera.CameraUpdate up = slamra.getLastReceivedCameraUpdate();
		if (up == null) return;
		Pose2d update = up.pose;
		parent.setCurrentPosition(new Vector3(update.getX(), update.getY(), update.getHeading()));
	}

	@Override
	public void onStop() {
		//slamra.stop();
		//slamra.free();
	}
}