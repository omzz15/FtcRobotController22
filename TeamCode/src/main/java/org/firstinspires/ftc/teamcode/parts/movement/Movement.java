//package org.firstinspires.ftc.teamcode.parts.movement;
//
//import androidx.annotation.NonNull;
//
//import com.qualcomm.robotcore.hardware.PIDCoefficients;
//
//import org.firstinspires.ftc.teamcode.base.Robot;
//import org.firstinspires.ftc.teamcode.base.part.LoopedRobotPart;
//import org.firstinspires.ftc.teamcode.other.PID;
//import org.firstinspires.ftc.teamcode.other.Position;
//import org.firstinspires.ftc.teamcode.other.Utils;
//import org.firstinspires.ftc.teamcode.other.task.Task;
//import org.firstinspires.ftc.teamcode.other.task.TaskManager;
//import org.firstinspires.ftc.teamcode.other.task.TaskRunner;
//import org.firstinspires.ftc.teamcode.parts.drive.Drive;
//import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
//import org.firstinspires.ftc.teamcode.parts.positiontracker.settings.PositionTrackerSettings;
//
//public class Movement extends LoopedRobotPart {
//	TaskRunner movementTasks = new TaskRunner();
//
//	Position currentPos;
//	double[] targetPos = new double[3];
//	double[] tol = new double[3];
//	long startTime;
//	int maxTime;
//	double[] powers = new double[3];
//	double errorVectorRot;
//	double errorVectorMag;
//	int numOfTimesInTolerance;
//	int timesToStayInTolerance;
//
//	PID xPID;
//	PID yPID;
//	PID rPID;
//
//	boolean done = false;
//
//	public Movement(Robot robot){
//		super(robot, null, new MovementSettings());
//	}
//
//	public Movement(Robot robot, MovementSettings settings){
//		super(robot, null, settings);
//	}
//
//
//	////////////////////
//	//Movement Methods//
//	////////////////////
//	public void setMoveToPosition(double[] targetPos, double[] tol, int timesToStayInTolerance, int maxTime, PIDCoefficients moveXPID, PIDCoefficients moveYPID, PIDCoefficients turnPID, double maxSpeed)
//	{
//		if(((PositionTrackerSettings) robot.getPartByClass(PositionTracker.class).settings).positionTrackingEnabled() &&  robot.getPartByClass(Drive.class).settings.canUse())
//		{
//			currentPos = ((PositionTracker) robot.getPartByClass(PositionTracker.class)).getCurrentPosition();
//
//			if (!currentPos.inTolerance(targetPos, tol)) {
//				xPID = new PID(moveXPID, -maxSpeed, maxSpeed);
//				yPID = new PID(moveYPID, -maxSpeed, maxSpeed);
//				rPID = new PID(turnPID, -maxSpeed, maxSpeed);
//
//				numOfTimesInTolerance = 0;
//				this.timesToStayInTolerance = timesToStayInTolerance;
//				startTime = System.currentTimeMillis();
//				this.maxTime = maxTime;
//
//				this.targetPos = targetPos;
//				this.tol = tol;
//
//				movementTasks.getBackgroundTask("Move To Vector3").start();
//			}
//		}
//		else if(settings.sendTelemetry) robot.addTelemetry("error in Movement.setMoveToPosition: ", "robot can not move to position because it does not know its position or the drive is disabled");
//	}
//	public void setMoveToPosition(double[] targetPos, double[] tol, int timesToStayInTolerance, int maxTime, double maxSpeed) {
//		setMoveToPosition(targetPos, tol, timesToStayInTolerance, maxTime, ((MovementSettings) settings).moveXPID, ((MovementSettings) settings).moveYPID, ((MovementSettings) settings).turnPID, maxSpeed);
//	}
//	public void setMoveToPosition(@NonNull Position targetPos, double[] tol, int timesToStayInTolerance, int maxTime, double maxSpeed) {
//		setMoveToPosition(targetPos.toArray(), tol, timesToStayInTolerance, maxTime, maxSpeed);
//	}
//	public void setMoveToPosition(@NonNull Position targetPos, @NonNull Position tol, int timesToStayInTolerance, int maxTime, double maxSpeed){
//		setMoveToPosition(targetPos.toArray(), tol.toArray(), timesToStayInTolerance, maxTime, maxSpeed);
//	}
//	public void setMoveToPosition(double[] targetPos, @NonNull MoveToPosSettings mtps)
//	{
//		if(mtps.isPIDValid()) setMoveToPosition(targetPos, mtps.tol, mtps.timesInTol, mtps.maxRuntime, mtps.xPID, mtps.yPID, mtps.turnPID, mtps.maxPower);
//		else setMoveToPosition(targetPos, mtps.tol, mtps.timesInTol, mtps.maxRuntime, mtps.maxPower);
//	}
//	public void setMoveToPosition(@NonNull Position targetPos, MoveToPosSettings mtps){
//		setMoveToPosition(targetPos.toArray(), mtps);
//	}
//
//
//	private void addMoveToPositionTask(){
//		Task t = new Task();
//		Task.Step s;
//		Task.EndPoint e;
//
//		s = () -> {
//			currentPos = ((PositionTracker) robot.getPartByClass(PositionTracker.class)).getCurrentPosition();
//
//			//calculate the error vector
//			errorVectorMag = java.lang.Math.sqrt(java.lang.Math.pow((targetPos[0] - currentPos.X), 2) + java.lang.Math.pow((targetPos[1] - currentPos.Y), 2));
//			errorVectorRot = java.lang.Math.toDegrees(java.lang.Math.atan2((targetPos[1] - currentPos.Y), (targetPos[0] - currentPos.X)));
//			//LK reversed the X and Y above
//
//			//take out robot rotation
//			errorVectorRot -= currentPos.R;
//			errorVectorRot = Utils.Math.scaleAngle(errorVectorRot);
//
//			//get the errors comps
//			// lk made power0 negative
//			powers[0] = -xPID.updatePIDAndReturnValue(errorVectorMag * java.lang.Math.sin(java.lang.Math.toRadians(errorVectorRot)));
//			powers[1] = yPID.updatePIDAndReturnValue(errorVectorMag * java.lang.Math.cos(java.lang.Math.toRadians(errorVectorRot)));
//			powers[2] = rPID.updatePIDAndReturnValue(Utils.Math.findAngleError(currentPos.R, targetPos[2]));
//
//			if (currentPos.inTolerance(targetPos, tol))
//				numOfTimesInTolerance++;
//			else numOfTimesInTolerance = 0;
//
//			((Drive) robot.getPartByClass(Drive.class)).moveRobot(powers, false, false);
//		};
//
//		e = () -> ((System.currentTimeMillis() - startTime > maxTime) || (numOfTimesInTolerance > timesToStayInTolerance) || done);
//
//		//start
//		t.addStep(() -> {
//			done = false;
//			unpause();
//			robot.getPartByClass(Drive.class).pause(true);
//		});
//		//main loop
//		t.addStep(s, e);
//		//end
//		t.addStep(() -> {
//			robot.getPartByClass(Drive.class).unpause();
//			pause(true);
//			done = true;
//		});
//
//		movementTasks.addTask("Move To Vector3", t, true, false);
//	}
//
//	public Task addMoveToPositionToTask(Task task, Position position, MoveToPosSettings mtps, boolean waitForFinish){
//		Position modPosition = new Position(position.X, robot.autoBlue ? position.Y : -position.Y, robot.autoBlue ? position.R : -position.R);
//		task.addStep(() -> {setMoveToPosition(modPosition, mtps);});
//		if(waitForFinish) task.addStep(() -> (done));
//		return task;
//	}
//
//	public Task addMoveToPositionToTask(Task task, Position position, boolean waitForFinish){
//		return addMoveToPositionToTask(task,position, ((MovementSettings) settings).defaultPosSettings, waitForFinish);
//	}
//
//	public void stopMovement(){
//		done = true;
//	}
//
//	public boolean isDone(){
//		return done;
//	}
//
//	public void stopMovementTask(){
//		movementTasks.getBackgroundTask("Move To Vector3").reset();
//		pause(true);
//	}
//
//	private void attachTaskRunner(String name, TaskManager manager){
//		manager.attachTaskRunner(name, movementTasks);
//	}
//
//	/////////////////////
//	//LoopedRobotPart Methods//
//	/////////////////////
//	@Override
//	public void onConstruct() {}
//
//	@Override
//	public void onInit() {
//		pause(true);
//	}
//
//	@Override
//	public void onStart() {
//		addMoveToPositionTask();
//		attachTaskRunner("movement", robot.taskManager);
//	}
//
//	@Override
//	public void onPause() {
//		((Drive) robot.getPartByClass(Drive.class)).stopMovement();
//	}
//
//	@Override
//	public void onUnpause() {
//
//	}
//
//	@Override
//	public void onRunLoop(short runMode) {
//	}
//
//	@Override
//	public void onAddTelemetry() {
//		robot.addTelemetry("x: ", currentPos.X);
//		robot.addTelemetry("y: ", currentPos.Y);
//		robot.addTelemetry("rot: ", currentPos.R);
//		robot.addTelemetry("error mag: ", errorVectorMag);
//		robot.addTelemetry("error rot: ", errorVectorRot);
//		robot.sendTelemetry();
//	}
//
//	@Override
//	public void onStop() {
//
//	}
//
//
//}
