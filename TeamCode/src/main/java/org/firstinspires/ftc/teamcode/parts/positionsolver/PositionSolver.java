package org.firstinspires.ftc.teamcode.parts.positionsolver;

import org.apache.commons.lang3.ObjectUtils;
import org.firstinspires.ftc.teamcode.parts.drive.Drive;
import org.firstinspires.ftc.teamcode.parts.positionsolver.settings.PositionSolverSettings;
import org.firstinspires.ftc.teamcode.parts.positiontracker.PositionTracker;

import om.self.ezftc.core.part.LoopedPartImpl;
import om.self.ezftc.core.part.Part;
import om.self.ezftc.utils.PID;
import om.self.ezftc.utils.Vector3;

public class PositionSolver extends Part<Drive, PositionSolverSettings, ObjectUtils.Null> {
    private PositionTracker positionTracker;

    private Vector3 targetPos;
    private long startTime;
    private int timesToStayInTolerance;

	private PID xPID;
    private PID yPID;
    private PID rPID;


    public PositionSolver(Drive parent) {
        super(parent, "position solver");
        setSettings(PositionSolverSettings.makeDefault());
    }

    public PositionSolver(Drive parent, PositionSolverSettings settings){
        super(parent, "position solver");
        setSettings(settings);
    }


    @Override
    public void onBeanLoad() {
        positionTracker = getBeanManager().getBestMatch(PositionTracker.class, false, false);
    }

    @Override
    public void onSettingsUpdate(PositionSolverSettings positionSolverSettings) {
        xPID.PIDs = positionSolverSettings.xPIDCoefficients;
        yPID.PIDs = positionSolverSettings.yPIDCoefficients;
        rPID.PIDs = positionSolverSettings.rPIDCoefficients;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {



    }
    /*
    private void addMoveToPositionTask(){
		s = () -> {
			currentPos = ((PositionTracker) robot.getPartByClass(PositionTracker.class)).getCurrentPosition();

			//calculate the error vector
			errorVectorMag = java.lang.Math.sqrt(java.lang.Math.pow((targetPos[0] - currentPos.X), 2) + java.lang.Math.pow((targetPos[1] - currentPos.Y), 2));
			errorVectorRot = java.lang.Math.toDegrees(java.lang.Math.atan2((targetPos[1] - currentPos.Y), (targetPos[0] - currentPos.X)));
			//LK reversed the X and Y above

			//take out robot rotation
			errorVectorRot -= currentPos.R;
			errorVectorRot = Utils.Math.scaleAngle(errorVectorRot);

			//get the errors comps
			// lk made power0 negative
			powers[0] = -xPID.updatePIDAndReturnValue(errorVectorMag * java.lang.Math.sin(java.lang.Math.toRadians(errorVectorRot)));
			powers[1] = yPID.updatePIDAndReturnValue(errorVectorMag * java.lang.Math.cos(java.lang.Math.toRadians(errorVectorRot)));
			powers[2] = rPID.updatePIDAndReturnValue(Utils.Math.findAngleError(currentPos.R, targetPos[2]));

			if (currentPos.inTolerance(targetPos, tol))
				numOfTimesInTolerance++;
			else numOfTimesInTolerance = 0;

			((Drive) robot.getPartByClass(Drive.class)).moveRobot(powers, false, false);
		};

		e = () -> ((System.currentTimeMillis() - startTime > maxTime) || (numOfTimesInTolerance > timesToStayInTolerance) || done);

		//start
		t.addStep(() -> {
			done = false;
			unpause();
			robot.getPartByClass(Drive.class).pause(true);
		});
		//main loop
		t.addStep(s, e);
		//end
		t.addStep(() -> {
			robot.getPartByClass(Drive.class).unpause();
			pause(true);
			done = true;
		});

		movementTasks.addTask("Move To Vector3", t, true, false);
	}
     */

}
