package om.self.ezftc.utils.hardware.servo;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

/**
 * A simple interface for running functions on a list of servos. This is not really used in the library but could be good to have.
 */
public interface ServoFunction {
    /**
     * Runs a function on a list of servos
     * @param servos a list of servos
     * @param value the value to run the function with. This could be a single value or an array of values
     * @param separateValues whether or not the values should be separated. If this is true then the value should be an array of values
     * @return the result of the function
     */
    Object run(List<Servo> servos, Object value, boolean separateValues);

    /**
     * Function to set the servo position on a list of servos.
     * <br>
     * <br>
     * servos: the list of servos to set the position of
     * <br>
     * value: the value to set the position to. This could be a single value or an array of values
     * <br>
     * separateValues: whether or not the values should be separated. If this is true then the value should be an array of values
     */
    ServoFunction setServoPosition = (servos, value, separateValues) -> {
        for(int i = 0; i < servos.size(); i++)
            if(separateValues)
                servos.get(i).setPosition(((double[])value)[i]);
            else
                servos.get(i).setPosition((double)value);
        return null;
    };

    /**
     * Function to get the positions from a list of servos
     * <br>
     * <br>
     * servos: the list of servos to get the positions from
     * <br>
     * value: this is ignored
     * <br>
     * separateValues: this is ignored
     */
    ServoFunction getServoPositions = (servos, value, separateValues) -> {
        double[] pos = new double[servos.size()];
        for (int i = 0; i < servos.size(); i++)
            pos[i] = servos.get(i).getPosition();
        return pos;
    };
}
