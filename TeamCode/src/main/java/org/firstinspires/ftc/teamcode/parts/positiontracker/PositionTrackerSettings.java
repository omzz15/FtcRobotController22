package org.firstinspires.ftc.teamcode.parts.positiontracker;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPartSettings;
import org.firstinspires.ftc.teamcode.other.Position;

public class PositionTrackerSettings extends RobotPartSettings {
	//angle tracking
	boolean flipAngle = true;
	AxesOrder axesOrder = AxesOrder.XYZ; // the last axis should be the rotation of the robot

	// Start position of robot on field in Autonomous (need an autonomous flag here)
	Position startPosition = new Position(0,0,0);
	Position emmetRobotOffset = new Position(-2.25,-6.5,0);
	Position emmetFieldOffset = new Position(16,60, 0);  // 9 63 180
	//encoder tracking
	public boolean useEncoders = true;
	static public float ticksPerInchSideways = 55;
	static public float ticksPerInchForward = 48;
	Position encoderStartPosition = new Position(startPosition.X,startPosition.Y, startPosition.R);

	//wall distance sensor
	final double blueWallYDist = 63;
	final double redWallYDist = -63;
	double minValidDistance = 6;

	//slamra tracking
	public boolean useSlamra = true;
	double odometryCovariance = 0.1;
	// Offset of slamera camera in inches and radians
	Pose2d robotOffset = new Pose2d();
	//Pose2d robotOffset = new Pose2d(2.25,6.5,Math.toRadians(90));
	Pose2d slamraStartPosition = new Pose2d(startPosition.X,startPosition.Y, Math.toRadians(startPosition.R));
	Position leftSlamraOffset = new Position(-4.5,-0.75,-90);
	Position rightSlamraOffset = new Position(-4.5, -.5, 90);

	//vision tracking
	public boolean useVision = true;

	public boolean positionTrackingEnabled(){
		return runMode > 0 && (useEncoders);
	}

	@Override
	public void onInit(Robot robot) { startPosition.toPose2d(false);
	}
}