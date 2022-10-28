package org.firstinspires.ftc.teamcode.parts.positiontracker;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.spartronics4915.lib.T265Camera;
import com.spartronics4915.lib.T265Helper;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPart;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.drive.settings.DriveSettings;
import org.firstinspires.ftc.teamcode.other.Position;
import org.firstinspires.ftc.teamcode.other.Utils;
import org.firstinspires.ftc.teamcode.parts.vision.Vision;

public class PositionTracker extends RobotPart<PositionTrackerHardware,PositionTrackerSettings> {
	/////////////
	//variables//
	/////////////
	//position///
	private Position encoderPosition;
	private Pose2d slamraPosition;
	private Position visionPosition;
	private Position currentPosition;
	public Position slamraFieldStart = null;
	public Position slamraRobotOffset = new Position(0,0,0);

	//LK final nonsense
	Position slamraRawPose = new Position (0,0,0);  // better name for currentPose?
	Position slamraRobotPose = new Position (0,0,0);
	// Vector3 slamraRobotOffset = new Vector3(-6.5,2.25,90);
	Position slamraFieldOffset = new Position (0,0,0);
	Position slamraFinal = new Position(0,0,0);
	//end LK

	//wheels
	private int[] lastMotorPos;
	private int[] currMotorPos;

	//rotation
	private Orientation currentAllAxisRotations = new Orientation();
	private double rotationOffset;

	//angular velocity
	private AngularVelocity currentAngularVelocity = new AngularVelocity();

	//flags
	private boolean useWallDistanceSensors = false;


	////////////////
	//constructors//
	////////////////
	public PositionTracker(Robot robot, PositionTrackerHardware hardware, PositionTrackerSettings settings) {
		super(robot, hardware, settings);
	}

	public PositionTracker(Robot robot) {
		super(robot, new PositionTrackerHardware(), new PositionTrackerSettings());
	}

	////////////////////
	//accessor methods//
	////////////////////
	public AngularVelocity getCurrentAngularVelocity(){
		return currentAngularVelocity;
	}

	public Orientation getCurrentAllAxisRotations(){
		return currentAllAxisRotations;
	}

	public Position getCurrentPosition(){
		return currentPosition;
	}

	////////
	//init//
	////////
	void setStartPosition() {
		updateAngles();
		setAngle((float) settings.startPosition.R);
		currentPosition = settings.startPosition;
		encoderPosition = settings.encoderStartPosition;
		visionPosition = settings.encoderStartPosition;
		slamraPosition = settings.slamraStartPosition;
	}


	//////////////////
	//angle tracking//
	//////////////////
	Orientation getAngles() {
		Orientation angles = hardware.imu.getAngularOrientation(AxesReference.EXTRINSIC, settings.axesOrder, AngleUnit.DEGREES);
		if (settings.flipAngle)
			angles.thirdAngle *= -1;
		angles.thirdAngle -= rotationOffset;
		angles.thirdAngle = (float) Utils.Math.scaleAngle(angles.thirdAngle);
		return angles;
	}

	void updateAngles() {
		currentAngularVelocity = hardware.imu.getAngularVelocity();
		currentAllAxisRotations = getAngles();
		encoderPosition.R = currentAllAxisRotations.thirdAngle;
	}

	public void resetAngle() {
		setAngle(0);
	}

	public void setAngle(float angle) {
		updateAngles();
		rotationOffset += encoderPosition.R - angle;
	}


	/////////////////////////////
	//encoder position tracking//
	/////////////////////////////
	//init
	public void initEncoderTracker() {
		lastMotorPos = robot.getPartByClass(Drive.class).hardware.getMotorPositions("drive motors");
	}
	//update
	void updateEncoderPosition() {
		//get motor difference from last measure
		currMotorPos = robot.getPartByClass(Drive.class).hardware.getMotorPositions("drive motors");

		int[] diff = new int[4];

		for (int i = 0; i < 4; i++) {
			diff[i] = currMotorPos[i] - lastMotorPos[i];
		}

		//get the X and Y movement of the robot
		double XMove;
		double YMove;
		if (((DriveSettings) robot.getPartByClass(Drive.class).settings).driveMode == DriveSettings.DriveMode.MECANUM) {
			XMove = (.25 * (-diff[0] + diff[2] + diff[1] - diff[3])) / settings.ticksPerInchSideways;
			YMove = (.25 * (diff[0] + diff[2] + diff[1] + diff[3])) / settings.ticksPerInchForward;
		} else if (((DriveSettings) robot.getPartByClass(Drive.class).settings).driveMode == DriveSettings.DriveMode.TANK) {
			//experimental
			XMove = 0;
			YMove = (.25 * (diff[0] + diff[1] + diff[2] + diff[3])) / settings.ticksPerInchForward;
		} else {
			//experimental
			XMove = (.5 * (diff[1] + diff[3])) / settings.ticksPerInchSideways;
			YMove = (.5 * (diff[0] + diff[2])) / settings.ticksPerInchForward;
		}

		//rotate movement and add to robot positionTracker
		encoderPosition.X += YMove * java.lang.Math.sin(encoderPosition.R * java.lang.Math.PI / 180) - XMove * java.lang.Math.cos(encoderPosition.R * java.lang.Math.PI / 180);
		encoderPosition.Y += XMove * java.lang.Math.sin(encoderPosition.R * java.lang.Math.PI / 180) + YMove * java.lang.Math.cos(encoderPosition.R * java.lang.Math.PI / 180);

		//update last motor position
		lastMotorPos = currMotorPos;
	}


	//////////////////
	//slamra tacking//
	//////////////////
	//init
	public void useLeftSlamra(){
		slamraRobotOffset = settings.leftSlamraOffset;
	}
	public void useRightSlamra(){
		slamraRobotOffset = settings.rightSlamraOffset;
	}
	void initSlamra() {
		if (hardware.slamera == null) {
			hardware.slamera = T265Helper.getCamera(
					new T265Camera.OdometryInfo(
							settings.robotOffset,
							settings.odometryCovariance
					), robot.hardwareMap.appContext);
		}
		if (!hardware.slamera.isStarted()) hardware.slamera.start();
	}

	//start
	void startSlamra(){
		hardware.slamera.setPose(settings.slamraStartPosition);
	}

	//get
	void updateSlamraPosition() {
		TelemetryPacket packet = new TelemetryPacket();
		T265Camera.CameraUpdate up = hardware.slamera.getLastReceivedCameraUpdate();
		if (up == null) return;
		Pose2d update = up.pose;
		slamraPosition = new Pose2d(update.getX() + settings.emmetFieldOffset.X,
				update.getY() + settings.emmetFieldOffset.Y,
				(update.getHeading() + Math.toRadians(90))
		);

		//LK test code
		slamraRawPose = new Position(update.getX(), update.getY(), Math.toDegrees(update.getHeading()));
		updateSlamraRobotPose();
		setSlamraFinal();
		//end LK test

	}

	//stop
	void stopSlamra(){

	}

//LK Slamra test functions
	void updateSlamraRobotPose() {
		double sX, sY, sR, rX, rY, rR;
		sX = slamraRawPose.X;
		sY = slamraRawPose.Y;
		sR = slamraRawPose.R;  // assuming was in radians
		//rX = robotOffset.getX();
		//rY = robotOffset.getY();
		//rR = robotOffset.getHeading();         // assuming was in degrees
		rX = slamraRobotOffset.X;
		rY = slamraRobotOffset.Y;
		rR = slamraRobotOffset.R;
		//x_robot*COS(RADIANS($C10))-y_robot*SIN(RADIANS($C10))
		slamraRobotPose.X = sX + (rX*Math.cos(Math.toRadians(sR)) - rY*Math.sin(Math.toRadians(sR)));
		//=x_robot*SIN(RADIANS($C10))+y_robot*COS(RADIANS($C10))
		slamraRobotPose.Y = sY + (rX*Math.sin(Math.toRadians(sR)) + rY*Math.cos(Math.toRadians(sR)));
		slamraRobotPose.R = sR + rR;
	}

	// run this once at start after getting first robot pose
	void setSlamraFieldOffset() {
		double fX, fY, fR, rX, rY, rR, sR;
		if (slamraFieldStart == null) {
			fX = slamraRobotPose.X;
			fY = slamraRobotPose.Y;
			fR = slamraRobotPose.R;
		} else {
			fX = slamraFieldStart.X;
			fY = slamraFieldStart.Y;
			fR = slamraFieldStart.R;
		}
		rX = slamraRobotPose.X;
		rY = slamraRobotPose.Y;
		rR = slamraRobotPose.R;
		slamraFieldOffset.R = fR - rR;
		sR = slamraFieldOffset.R;
		//=M4*COS(RADIANS(r_field_slam))-N4*SIN(RADIANS(r_field_slam))  m4=rX, n4=rY
		slamraFieldOffset.X = fX - (rX*Math.cos(Math.toRadians(sR)) - rY*Math.sin(Math.toRadians(sR)));
		//=M4*SIN(RADIANS(r_field_slam))+N4*COS(RADIANS(r_field_slam))
		slamraFieldOffset.Y = fY - (rX*Math.sin(Math.toRadians(sR)) + rY*Math.cos(Math.toRadians(sR)));
	}

	void setSlamraFinal() {
		//rotates slamra position to field coordinates & add offset
		double oX, oY, oR, rX, rY, rR, sR;
		rX = slamraRobotPose.X;
		rY = slamraRobotPose.Y;
		rR = slamraRobotPose.R;
		oX = slamraFieldOffset.X;
		oY = slamraFieldOffset.Y;
		oR = slamraFieldOffset.R;
		//=I11*COS(RADIANS(r_field_slam))-J11*SIN(RADIANS(r_field_slam))  i11=rX, j11=rY
		slamraFinal.X = (rX*Math.cos(Math.toRadians(oR)) - rY*Math.sin(Math.toRadians(oR))) + oX;
		//=I11*SIN(RADIANS(r_field_slam))+J11*COS(RADIANS(r_field_slam))
		slamraFinal.Y = (rX*Math.sin(Math.toRadians(oR)) + rY*Math.cos(Math.toRadians(oR))) + oY;
		slamraFinal.R = rR + oR;
	}
//end LK test

	public void DrawOnDashboard  (Position pos, Canvas field) {
		field.clear();
		final int robotRadius = 9; // inches
		final int robotEdge = 18;

		field.strokeCircle(pos.X, pos.Y, robotRadius);
		//field.fillRect(pos.X, pos.Y,robotEdge,robotEdge);

		double arrowX = Math.cos(Math.toRadians(pos.R)) * robotRadius, arrowY = Math.sin(Math.toRadians(pos.R)) * robotRadius;
		double x1 = pos.X + arrowX  / 2, y1 = pos.Y + arrowY / 2;
		double x2 = pos.X + arrowX, y2 = pos.Y + arrowY;
		field.strokeLine(x1, y1, x2, y2);
	}

	public void slamraInfo (){
		if (settings.useSlamra) {
			updateSlamraPosition();
			///robot.addTelemetry("slamra field offset", slamraFieldOffset.toString());
			robot.addTelemetry("slamra raw position", slamraRawPose.toString());
			///robot.addTelemetry("slamra robot offset", slamraRobotPose.toString());
			robot.addTelemetry("slamra final pos   ", slamraFinal.toString());
			robot.addTelemetry("slamra field start", slamraFieldStart.toString());
		}
	}

	//////////
	//vision//
	//////////
	void updateVisionPosition(){
		Vision v = (Vision) robot.getPartByClass(Vision.class);
		if(v != null && v.newPositionAvailable){
			visionPosition = v.getPosition();
		}
	}


	/////////
	//other//
	/////////
	private void updateYFromWallDistSensor(){
		if(robot.autoBlue){
			double blueDist = hardware.blueWallDistSensor.getDistance(DistanceUnit.INCH);
			if(blueDist <= settings.minValidDistance)
				currentPosition.Y = settings.blueWallYDist - blueDist;
		}
		else {
			double redDist = hardware.redWallDistSensor.getDistance(DistanceUnit.INCH);
			if (redDist <= settings.minValidDistance)
				currentPosition.Y = settings.redWallYDist + redDist;
		}
	}

	public void activateWallDistSensor(){
		useWallDistanceSensors = true;
	}

	public void deactivateWallDistSensor(){
		useWallDistanceSensors = false;
	}

	/////////////////////
	//RobotPart Methods//
	/////////////////////
	@Override
	public void onConstruct() {

	}

	@Override
	public void onInit() {
		encoderPosition = new Position();
		if (settings.useSlamra)
			initSlamra();
	}

	@Override
	public void onStart() {
		setStartPosition();
		if(settings.useEncoders)
			initEncoderTracker();

		//LK kludge to set field offset when starts running
		updateSlamraPosition();
		setSlamraFieldOffset();
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onUnpause() {

	}

	@Override
	public void onRunLoop(short runMode) {
		if(runMode == 1) {
			updateAngles();
			if (settings.useEncoders) {
				updateEncoderPosition();
				//currentPosition.X = encoderPosition.X;
				//currentPosition.Y = encoderPosition.Y;
				//currentPosition.Z = encoderPosition.Z;
			}

			if(settings.useVision)
				updateVisionPosition();

			if (settings.useSlamra) {
				updateSlamraPosition();
				// Here is where slamera is hard coded to be the robot position
				//currentPosition.X = slamraPosition.getX();
				//currentPosition.Y = slamraPosition.getY();
				//currentPosition.Z = slamraPosition.getHeading();
				currentPosition.X = slamraFinal.X;
				currentPosition.Y = slamraFinal.Y;
				currentPosition.R = slamraFinal.R;
			}
			if (useWallDistanceSensors)
				updateYFromWallDistSensor();
		}
	}

	@Override
	public void onAddTelemetry() {
		robot.addTelemetry("position", currentPosition.toString());
		robot.addTelemetry("encoder Pos", encoderPosition.toString());
		robot.addTelemetry("vision Pos", visionPosition.toString());
		//LK kludge
		///robot.addTelemetry("slamra field offset", slamraFieldOffset.toString());
		///robot.addTelemetry("slamra raw position", slamraRawPose.toString());
		///robot.addTelemetry("slamra robot offset", slamraRobotPose.toString());
		robot.addTelemetry("slamra final pos   ", slamraFinal.toString());
		DrawOnDashboard(currentPosition, robot.field);
	}

	@Override
	public void onStop() {
		if (settings.useSlamra)
			stopSlamra();
	}
}