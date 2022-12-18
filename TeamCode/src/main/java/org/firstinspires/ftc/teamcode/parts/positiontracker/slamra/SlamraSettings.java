package org.firstinspires.ftc.teamcode.parts.positiontracker.slamra;

import om.self.ezftc.utils.Vector3;

public class SlamraSettings{
	////////////
	//settings//
	////////////
	public final Vector3 slamraRobotOffset;
	//general
    public final Vector3 slamraStartPosition;

    public SlamraSettings(Vector3 slamraRobotOffset, Vector3 slamraStartPosition) {
        this.slamraRobotOffset = slamraRobotOffset;
        this.slamraStartPosition = slamraStartPosition;
    }

    public static SlamraSettings makeDefault(){
        return new SlamraSettings(
                new Vector3(0,0,0),
                new Vector3(0,0,0)
        );
    }
}