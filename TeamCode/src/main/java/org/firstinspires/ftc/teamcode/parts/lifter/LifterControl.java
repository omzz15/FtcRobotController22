package org.firstinspires.ftc.teamcode.parts.lifter;

public class LifterControl {
    public double lifterPower;
    public double turningPower;
    public boolean close;

    public static boolean open2; //TODO make non static

    public LifterControl(double lifterPower, double turningPower, boolean close) {
        this.lifterPower = lifterPower;
        this.turningPower = turningPower;
        this.close = close;
    }
}
