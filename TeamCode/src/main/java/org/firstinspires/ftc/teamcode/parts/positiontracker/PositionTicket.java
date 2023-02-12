package org.firstinspires.ftc.teamcode.parts.positiontracker;

import org.firstinspires.ftc.teamcode.parts.lifter.Lifter;

import om.self.ezftc.utils.Vector3;

public class PositionTicket {
    public final Vector3 position;
    public final Vector3 confidence;
    public final Vector3 accuracy;

    public PositionTicket(Vector3 position, Vector3 confidence, Vector3 accuracy) {
        this.position = position;
        this.confidence = confidence;
        this.accuracy = accuracy;
    }

    public PositionTicket(Vector3 position){
        this.position = position;
        this.confidence = new Vector3(.5);
        this.accuracy = new Vector3(.5);
    }
}
