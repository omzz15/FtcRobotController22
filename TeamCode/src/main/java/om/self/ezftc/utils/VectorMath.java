package om.self.ezftc.utils;

/**
 * A useful class for doing vector math
 */
public class VectorMath {
    /**
     * Adds two vector3s together
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the sum of the two vectors
     */
    public static Vector3 add(Vector3 v1, Vector3 v2){
        return new Vector3(v1.X + v2.X, v1.Y + v2.Y, v1.Z + v2.Z);
    }

    /**
     * Adds two vector2s together
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the sum of the two vectors
     */
    public static Vector2 add(Vector2 v1, Vector2 v2){
        return new Vector2(v1.X + v2.X, v1.Y + v2.Y);
    }

    /**
     * Subtracts two vector3s
     * @param v1 the vector to subtract from
     * @param v2 the vector to subtract
     * @return the difference of the two vectors
     */
    public static Vector3 subtract(Vector3 v1, Vector3 v2){
        return new Vector3(v1.X - v2.X, v1.Y - v2.Y, v1.Z - v2.Z);
    }

    /**
     * performs the dot product of two vector3s
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product of the two vectors
     */
    public static double dotProduct(Vector3 v1, Vector3 v2){
        return v1.X * v2.X + v1.Y * v2.Y + v1.Z + v2.Z;
    }

    /**
     * Scales the components of the first vector by the components of the second vector
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the scaled vector
     */
    public static Vector3 scale(Vector3 v1, Vector3 v2){
        return new Vector3(v1.X * v2.X, v1.Y * v2.Y, v1.Z * v2.Z);
    }

    /**
     * Scales the vector by a scalar
     * @param v1 the vector to scale
     * @param val the scalar to scale the vector by
     * @return the scaled vector
     */
    public static Vector3 scale(Vector3 v1, double val){
        return new Vector3(v1.X * val, v1.Y * val, v1.Z * val);
    }

    /**
     * Scales the X and Y components of the vector by a scalar
     * @param v1 the vector to scale
     * @param val the scalar to scale the vector by
     * @return the scaled vector
     */
    public static Vector3 scaleAsVector2(Vector3 v1, double val){
        return new Vector3(v1.X * val, v1.Y * val, v1.Z);
    }

    /**
     * translates a vector 3 along the local x and y axis (in relation to the Vector3) by treating z like an angle(in degrees)
     * @return the translated vector
     */
    public static Vector3 translateAsVector2(Vector3 val, double x, double y){
        double r = Math.toRadians(val.Z);
        double xOut = val.X + y * Math.cos(r) + x * Math.sin(r);
        double yOut = val.Y - x * Math.cos(r) + y * Math.sin(r);
        return new Vector3(xOut, yOut, val.Z);
    }

    /**
     * checks if an array of numbers is within tolerance of another array of numbers by checking the distance of element against the tolerance
     * @param currPos the current position
     * @param targetPos the target position
     * @param tol the tolerance
     * @return true if the current position is within the tolerance of the target position
     */
    public static boolean inTolerance(double[] currPos, double[] targetPos, double[] tol){
        for(int i = 0; i < 3; i++)
            if(Math.abs(targetPos[i] - currPos[i]) > tol[i]) return false;
        return true;
    }

    /**
     * calls {@link #inTolerance(double[], double[], double[])} with the arrays of the vectors
     * @param currPos the current position
     * @param targetPos the target position
     * @param tol the tolerance
     * @return true if the current position is within the tolerance of the target position
     */
    public static boolean inTolerance(Vector3 currPos, Vector3 targetPos, Vector3 tol){
        return inTolerance(currPos.toArray(), targetPos.toArray(), tol.toArray());
    }

    /**
     * calls {@link #inTolerance(double[], double[], double[])} with the arrays of the vectors
     * @param currPos the current position
     * @param targetPos the target position
     * @param tol the tolerance
     * @return true if the current position is within the tolerance of the target position
     */
    public static boolean inTolerance(Vector2 currPos, Vector2 targetPos, Vector2 tol){
        return inTolerance(currPos.toArray(), targetPos.toArray(), tol.toArray());
    }
}
