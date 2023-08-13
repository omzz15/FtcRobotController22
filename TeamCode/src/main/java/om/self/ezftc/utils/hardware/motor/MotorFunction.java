package om.self.ezftc.utils.hardware.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

/**
 * A simple interface for running functions on a list of motors. This is not really used in the library but could be good to have.
 */
public interface MotorFunction {
    /**
     * Runs a function on a list of motors
     * @param motors a list of motors
     * @param value the value to run the function with. This could be a single value or an array of values
     * @param separateValues whether or not the values should be separated. If this is true then the value should be an array of values
     * @return the result of the function. Generally this is null but it could be the result of a get function
     */
    Object run(List<DcMotor> motors, Object value, boolean separateValues);

    /**
     * Function to set the motor power on a list of motors
     */
    MotorFunction setMotorPower = (motors, value, separateValues) -> {
        for (int i = 0; i < motors.size(); i++)
            if (separateValues) {
                motors.get(i).setPower(((double[]) value)[i]);
            }else
                motors.get(i).setPower((double) value);
        return null;
    };

    /**
     * Function to set the motor target position on a list of motors
     */
    MotorFunction setTargetPosition = (motors, value, separateValues) -> {
        for (int i = 0; i < motors.size(); i++)
            if (separateValues)
                motors.get(i).setTargetPosition(((int[]) value)[i]);
            else
                motors.get(i).setTargetPosition((int) value);
        return null;
    };

    /**
     * Function to get the positions from a list of motors
     */
    MotorFunction getMotorPositions = (motors, value, separateValues) -> {
        int[] pos = new int[motors.size()];
        for (int i = 0; i < motors.size(); i++)
            pos[i] = motors.get(i).getCurrentPosition();
        return pos;
    };
}
