package org.firstinspires.ftc.teamcode.parts.positiontracker.slamra;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Transform2d;
import com.spartronics4915.lib.T265Camera;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.utils.Vector3;

public class Slamra extends LoopedPartImpl<PositionTracker, SlamraSettings, ObjectUtils.Null> {
	//variables
	private static T265Camera slamra;

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
			slamra = new T265Camera(getSettings().slamraRobotOffset.toTransform2d(), 0.1, hardwareMap.appContext);
		}
	}

	@Override
	public void onStart() {
		slamra.start();
	}

	@Override
	public void onSettingsUpdate(SlamraSettings settings) {
		slamra.setPose(settings.slamraStartPosition.toPose2d());
	}

	@Override
	public void onRun() {
		T265Camera.CameraUpdate up = slamra.getLastReceivedCameraUpdate();
		if (up == null) return;
		Pose2d update = up.pose;
		parent.setCurrentPosition(new Vector3(update.getX(), update.getY(), update.getRotation().getDegrees()));
	}

	@Override
	public void onStop() {
		slamra.stop();
	}
}