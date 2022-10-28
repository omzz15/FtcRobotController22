package org.firstinspires.ftc.teamcode.parts.positiontracker;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.spartronics4915.lib.T265Camera;

import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPartHardware;

public class PositionTrackerHardware extends RobotPartHardware {
	BNO055IMU imu;
	volatile T265Camera slamera = null;
	Rev2mDistanceSensor redWallDistSensor;
	Rev2mDistanceSensor blueWallDistSensor;


	@Override
	public void onInit(Robot robot){
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.mode = BNO055IMU.SensorMode.IMU;
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json";
		parameters.loggingEnabled = false;
		parameters.loggingTag = "IMU";

		imu = robot.hardwareMap.get(BNO055IMU.class, "imu");
		imu.initialize(parameters);

		while (!imu.isGyroCalibrated() && !robot.shouldStop())
		{
			robot.opMode.idle();
		}

		redWallDistSensor = robot.hardwareMap.get(Rev2mDistanceSensor.class, "redWallDistSensor");
		blueWallDistSensor = robot.hardwareMap.get(Rev2mDistanceSensor.class, "blueWallDistSensor");
	}
}