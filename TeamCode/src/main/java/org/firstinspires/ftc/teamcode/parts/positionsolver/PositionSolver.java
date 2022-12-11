//package org.firstinspires.ftc.teamcode.parts.positionsolver;
//
//import org.apache.commons.lang3.ObjectUtils;
//import org.firstinspires.ftc.teamcode.parts.drive.Drive;
//import org.firstinspires.ftc.teamcode.parts.drive.DriveControl;
//import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.ChannelSolverSettings;
//import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.PositionSolverSettings;
//import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;
//
//import om.self.ezftc.core.part.ControllablePart;
//import om.self.ezftc.core.part.LoopedPartImpl;
//import om.self.ezftc.core.part.Part;
//import om.self.ezftc.utils.PID;
//import om.self.ezftc.utils.Vector2;
//import om.self.ezftc.utils.Vector3;
//
//public class PositionSolver extends Part<Drive, PositionSolverSettings, ObjectUtils.Null> {
//    protected PositionTracker positionTracker;
//
//    private final ChannelSolver<PositionSolver, DriveControl> xChannel = new ChannelSolver(this, "x channel") {
//        @Override
//        public double getError(double target) {
//            return 0;
//        }
//
//        @Override
//        public Vector3 move(Object base) {
//            return null;
//        }
//
//        @Override
//        public ControllablePart getControlled() {
//            return null;
//        }
//    };
//
//    private final ChannelSolver<PositionSolver, DriveControl> yChannel = new ChannelSolver(this, "y channel") {
//        @Override
//        public double getError(double target) {
//            return 0;
//        }
//
//        @Override
//        public Vector3 move(Object base) {
//            return null;
//        }
//
//        @Override
//        public ControllablePart getControlled() {
//            return null;
//        }
//    };
//
//    private final ChannelSolver<PositionSolver, DriveControl> rChannel = new ChannelSolver(this, "r channel") {
//        @Override
//        public double getError(double target) {
//            return 0;
//        }
//
//        @Override
//        public Vector3 move(Object base) {
//            return null;
//        }
//
//        @Override
//        public ControllablePart getControlled() {
//            return null;
//        }
//    };
//
//    public PositionSolver(Drive parent) {
//        super(parent, "position solver");
//        setSettings(PositionSolverSettings.makeDefault());
//    }
//
//    public PositionSolver(Drive parent, PositionSolverSettings settings){
//        super(parent, "position solver");
//        setSettings(settings);
//    }
//
//
//
//    @Override
//    public void onBeanLoad() {
//        positionTracker = getBeanManager().getBestMatch(PositionTracker.class, false, false);
//    }
//
//    @Override
//    public void onSettingsUpdate(PositionSolverSettings positionSolverSettings) {
//        xPID.PIDs = positionSolverSettings.xPIDCoefficients;
//        yPID.PIDs = positionSolverSettings.yPIDCoefficients;
//        rPID.PIDs = positionSolverSettings.rPIDCoefficients;
//    }
//
//    @Override
//    public void onInit() {
//
//    }
//
//    @Override
//    public void onStart() {
//
//    }
//
//    @Override
//    public void onStop() {
//
//
//
//    }
//
//    /*
//    private void addMoveToPositionTask(){
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
//     */
//
//}
