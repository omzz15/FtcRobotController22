package org.firstinspires.ftc.teamcode.parts.slamra;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.base.Robot;
import org.firstinspires.ftc.teamcode.base.part.RobotPartSettings;
import org.firstinspires.ftc.teamcode.other.Position;

public class SlamraSettings extends RobotPartSettings {
	////////////
	//settings//
	////////////
	Position slamraRobotOffset = new Position(0,0,0);
	//general
	Position slamraStartPosition = new Position(0,0,0);
	public boolean useSlamra = true;

	@Override
	public void onInit(Robot robot) {

	}
}