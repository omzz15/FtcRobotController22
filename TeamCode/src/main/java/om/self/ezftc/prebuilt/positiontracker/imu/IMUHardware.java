package om.self.ezftc.prebuilt.positiontracker.imu;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import om.self.ezftc.core.Robot;

public class IMUHardware {
    public final BNO055IMU imu;
    public final BNO055IMU.Parameters parameters;

    public IMUHardware(BNO055IMU imu, BNO055IMU.Parameters parameters) {
        this.imu = imu;
        this.parameters = parameters;
    }

    public static IMUHardware makeDefault(HardwareMap map){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";

        return new IMUHardware(map.get(BNO055IMU.class, "imu"), parameters);
    }
}
