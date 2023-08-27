package om.self.ezftc.prebuilt.positiontracker.imu;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;

import om.self.ezftc.utils.Vector3;

public class IMUSettings {
    /**
     * The order of the axes of the IMU.
     * <br>
     * Note: The heading of the robot is from the third axis.
     */
    public final AxesOrder axesOrder;
    public final boolean flipAngle;

    public IMUSettings(AxesOrder axesOrder, boolean flipAngle) {
        this.axesOrder = axesOrder;
        this.flipAngle = flipAngle;
    }

    public static IMUSettings makeDefault(){
        return new IMUSettings(
                AxesOrder.XYZ,
                false
        );
    }
}
