package org.firstinspires.ftc.teamcode.parts.vision;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPart;
import org.firstinspires.ftc.teamcode.base.part.RobotPartHardware;
import org.firstinspires.ftc.teamcode.other.Position;
import org.firstinspires.ftc.teamcode.other.Utils;

import java.util.ArrayList;
import java.util.List;

public class Vision extends RobotPart<RobotPartHardware, VisionSettings> {
	/////////////////////////
	//objects and variables//
	/////////////////////////
	//general
	int cameraMonitorViewId;
	boolean usingWebcam = true;

	//vuforia
	OpenGLMatrix lastLocation = null;
	VuforiaLocalizer vuforia = null;
	VuforiaTrackables targets = null;
	private Position position = null;
	List<VuforiaTrackable> allTargets = new ArrayList<>();
	WebcamName webcamName;
	int vuforiaState = 0;//0 is nothing, 1 is constructed, 2 is initialized, and 3 is started
	public boolean targetVisible = false;
	public boolean newPositionAvailable = false;
	List<Recognition> updatedRecognitions = null;
	public int duckPos= 1;
	//tensorflow
	TFObjectDetector tfod;
	int tensorFlowState = 0;//0 is nothing, 1 is constructed, 2 is initialized, and 3 is started


	////////////////
	//constructors//
	////////////////
	public Vision(Robot robot, VisionSettings settings) {
		super(robot, null, settings);
	}

	public Vision(Robot robot) {
		super(robot, null, new VisionSettings());
	}


	////////
	//init//
	////////
	void initAll() {
		initCameraFully();
		if (settings.useVuforia) {
			initVuforia();
			if (settings.useTensorFlow)
				initTensorFlow();
		}
		if (settings.dashVideoSource != VisionSettings.VideoSource.NONE)
			startDashboardCameraStream();
	}

	void initCameraFully() {
		checkCameraType();
		getCMVId();
		if (usingWebcam)
			getWebcamName();
	}

	void getWebcamName() {
		webcamName = robot.hardwareMap.get(WebcamName.class, settings.webcamName);
	}

	void checkCameraType() {
		try {
			robot.hardwareMap.get(WebcamName.class, settings.webcamName);
			usingWebcam = true;
		} catch (Exception e) {
			usingWebcam = false;
		}
	}

	void getCMVId() //gets the camera Id to make initialization easier for vision objects
	{
		cameraMonitorViewId = robot.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", robot.hardwareMap.appContext.getPackageName());
	}


	/////////////
	//dashboard//
	/////////////
	void startDashboardCameraStream(int maxFPS, VisionSettings.VideoSource videoSource) {
		if (vuforiaState >= 2 && videoSource == VisionSettings.VideoSource.VUFORIA)
			FtcDashboard.getInstance().startCameraStream(vuforia, maxFPS);
		else if (tensorFlowState >= 2 && videoSource == VisionSettings.VideoSource.TENSORFLOW)
			FtcDashboard.getInstance().startCameraStream(tfod, maxFPS);
	} // starts a dashboard stream at a certain fps

	void startDashboardCameraStream() {
		startDashboardCameraStream(settings.maxFPS, settings.dashVideoSource);
	}

	void stopDashboardCameraStream() {
		FtcDashboard.getInstance().stopCameraStream();
	} // stops the dashboard stream


	///////////
	//vuforia//
	///////////
	//construct
	void constructVuforia() {
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = settings.VUFORIA_KEY;
		parameters.useExtendedTracking = false;

		if (usingWebcam) {
			parameters.cameraName = webcamName;
		} else {
			parameters.cameraDirection = settings.CAMERA_CHOICE;
		}

		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		vuforiaState = 1;
	}

	//init
	void initVuforia() {
		constructVuforia();
		loadAsset(settings.VUFORIA_MODEL_ASSET);
		initAllTargets();
		setCameraTransform(settings.cameraPosition, settings.phoneRotation);
		vuforiaState = 2;
	}

	void loadAsset(String assetName) {
		targets = vuforia.loadTrackablesFromAsset(assetName);
	} // loads the vuforia assets from a file

	void initAllTargets() {
		allTargets.addAll(targets);
		identifyTarget(0, "Blue Storage", -Utils.Constants.halfField, Utils.Constants.oneAndHalfTile, Utils.Constants.mmTargetHeight, 90, 0, 90);
		identifyTarget(1, "Blue Alliance Wall", Utils.Constants.halfTile, Utils.Constants.halfField, Utils.Constants.mmTargetHeight, 90, 0, 0);
		identifyTarget(2, "Red Storage", -Utils.Constants.halfField, -Utils.Constants.oneAndHalfTile, Utils.Constants.mmTargetHeight, 90, 0, 90);
		identifyTarget(3, "Red Alliance Wall", Utils.Constants.halfTile, -Utils.Constants.halfField, Utils.Constants.mmTargetHeight, 90, 0, 180);
	}

	void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
		VuforiaTrackable aTarget = targets.get(targetIndex);
		aTarget.setName(targetName);
		aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
	}

	void setCameraTransform(float[] position, float[] rotation) {
		OpenGLMatrix robotFromCamera = OpenGLMatrix
				.translation(position[1], position[0], position[2])
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, rotation[1], rotation[2], rotation[0]));

		/**  Let all the trackable listeners know where the phone is.  */
		for (VuforiaTrackable trackable : allTargets) {
			if (usingWebcam)
				((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(webcamName, robotFromCamera);
			else
				((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, settings.CAMERA_CHOICE);
		}
	}

	//start
	public void startVuforia() {
		targets.activate();
		vuforiaState = 3;
	}

	public void stopVuforia() {
		targets.deactivate();
		vuforiaState = 2;
	}

	//run
	void runVuforia() {
		targetVisible = false;
		for (VuforiaTrackable trackable : allTargets) {
			if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
				targetVisible = true;

				// getUpdatedRobotLocation() will return null if no new information is available since
				// the last time that call was made, or if the trackable is not currently visible.
				OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
				if (robotLocationTransform != null) {
					lastLocation = robotLocationTransform;
					Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
					position = new Position( lastLocation.getTranslation().get(0) / Utils.Constants.mmPerInch, lastLocation.getTranslation().get(1) / Utils.Constants.mmPerInch, rotation.firstAngle);
					newPositionAvailable = true;
				}
				break;
			}
		}
	}

	//get
	public Position getPosition(){
		newPositionAvailable = false;
		return position;
	}


	//////////////
	//tensorflow//
	//////////////
	//construct
	void constructTensorFlow() {
		int tfodMonitorViewId = robot.hardwareMap.appContext.getResources().getIdentifier(
				"tfodMonitorViewId", "id", robot.hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfodParameters.minResultConfidence = settings.minResultConfidence;
		tfodParameters.isModelTensorFlow2 = true;
		tfodParameters.inputSize = 320;
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(settings.TFOD_MODEL_ASSET, settings.LABELS);
		tensorFlowState = 1;
	}

	void initTensorFlow() {
		constructTensorFlow();
		tensorFlowState = 2;
	}

	void startTensorFlow() {
		tfod.activate();
		tfod.setZoom(settings.magnification, 16.0 / 9.0);
		tensorFlowState = 3;
	}

	void stopTensorFlow() {
		tfod.deactivate();
		tensorFlowState = 2;
	}

	void runTensorFlow() {
		updatedRecognitions = tfod.getUpdatedRecognitions();
	}

	public int duckPos(){
		if (updatedRecognitions != null) {
			duckPos = robot.autoBlue ? 1 : 3;
			robot.addTelemetry("# Object Detected", updatedRecognitions.size());
			// step through the list of recognitions and display boundary info.
			int i = 0;
			for (Recognition recognition : updatedRecognitions) {
				if(recognition.getLabel() == "Block") {
					//roight
					if ((recognition.getLeft() + recognition.getRight()) / 2 > 275) {
						duckPos = robot.autoBlue ? 3 : 2;
					//leftt
					} else if ((recognition.getLeft() + recognition.getRight()) / 2 < 275) {
						duckPos = robot.autoBlue ? 2 : 1;
					}
				}
				String item = String.format("%s: pos %.0f - %.0f (%d)", recognition.getLabel(), recognition.getLeft(), recognition.getRight(), duckPos);
				robot.addTelemetry(String.format("TFOD (%d)", i),item );
				i++;
			}
		} else {robot.addTelemetry("updatedRecognitions", "null");}
		robot.addTelemetry("Block Pos", duckPos);
		return duckPos;
	}
	/////////////////////
	//RobotPart Methods//
	/////////////////////
	@Override
	public void onConstruct() {

	}

	@Override
	public void onInit() {
		initAll();
	}

	@Override
	public void onStart() {
		if(settings.runVuforiaInRunLoop() && vuforiaState == 2)
			startVuforia();
		if(settings.runTensorFlowInRunLoop() && tensorFlowState == 2)
			startTensorFlow();
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onUnpause() {

	}

	@Override
	public void onRunLoop(short runMode) {
		if(runMode == 1){
			if(settings.runVuforiaInRunLoop() && vuforiaState == 3)
				runVuforia();
			if(settings.runTensorFlowInRunLoop() && vuforiaState == 3)
				runTensorFlow();
		}
	}

	//TODO add telemetry and stop for vision
	@Override
	public void onAddTelemetry() {
		/*if (updatedRecognitions != null) {
			robot.addTelemetry("# Object Detected", updatedRecognitions.size());
			// step through the list of recognitions and display boundary info.
			int i = 0;
			for (Recognition recognition : updatedRecognitions) {
				String item = String.format("%s: pos %.0f - %.0f", recognition.getLabel(), recognition.getLeft(), recognition.getRight());
				robot.addTelemetry(String.format("TFOD (%d)", i),item);
				//robot.addTelemetry(String.format("label (%d)", i), recognition.getLabel());
				//robot.addTelemetry("top  left", String.format("%.0f,%.0f", recognition.getLeft(), recognition.getTop()));
				//robot.addTelemetry("bot right", String.format("%.0f,%.0f", recognition.getRight(), recognition.getBottom()));
				i++;
			}
		}*/
	}

	@Override
	public void onStop() {

	}
}