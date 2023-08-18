package om.self.ezftc.utils;

/**
 * A bunch of useful constants
 */
public class Constants{
    /**
     * the name of millimeters in an inch
     */
    public static final float mmPerInch        = 25.4f;
    /**
     * the name of meters in an inch
     */
    public static final float mPerInch         = 0.0254f;
    /**
     * the size(in inches) of the side of a tile
     */
    public static final double tileSide = 23.5;

    public static final double millisecondsPerSecond = 1e3;
    public static final double secondsPerMillisecond = 1 / millisecondsPerSecond;

    public static final double nanoSecondsPerSecond = 1e9;
    public static final double secondsPerNanoSecond = 1 / nanoSecondsPerSecond;


    /**
     * Converts a vector3 that has coordinates in tiles to a vector3 that has coordinates in inches. It will convert the first 2 values (X,Y) and leave the third value (Z) alone.
     * @param p the vector to convert
     * @return the converted vector
     */
    public static Vector3 tileToInch(Vector3 p){
        return VectorMath.scaleAsVector2(p, tileSide);
    }

    /**
     * converts an array of floats from inches to mm
     * @param arr the array to convert
     * @return the converted array
     */
    public static float[] inchesToMM(float[] arr){
        float[] out = new float[arr.length];
        for (int i = 0; i < arr.length; i++){
            out[i] = arr[i] * mmPerInch;
        }
        return out;
    }
}