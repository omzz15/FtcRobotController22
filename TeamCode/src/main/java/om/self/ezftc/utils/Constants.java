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

    /**
     * Converts a vector3 that has coordinates in tiles to a vector3 that has coordinates in inches. It will convert the first 2 values (X,Y) and leave the third value (Z) alone.
     * @param p the vector to convert
     * @return the converted vector
     */
    public static Vector3 tileToInch(Vector3 p){
        return VectorMath.multiplyAsVector2(p, tileSide);
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