package om.self.ezftc.utils;

public class VectorMath {
    public static Vector3 add(Vector3 v1, Vector3 v2){
        return new Vector3(v1.X + v2.X, v1.Y + v2.Y, v1.Z + v2.Z);
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2){
        return new Vector3(v1.X - v2.X, v1.Y - v2.Y, v1.Z - v2.Z);
    }

    public static double dotProduct(Vector3 v1, Vector3 v2){
        return v1.X * v2.X + v1.Y * v2.Y + v1.Z + v2.Z;
    }

    public static Vector3 multiply(Vector3 v1, Vector3 v2){
        return new Vector3(); //todo add matrix multiplication
    }

    public static Vector3 multiply(Vector3 v1, double val){
        return new Vector3(v1.X * val, v1.Y * val, v1.Z * val);
    }

    public static Vector3 divide(Vector3 v1, double val){
        return new Vector3(v1.X / val, v1.Y / val, v1.Z / val);
    }

    public static boolean inTolerance(double[] currPos, double[] targetPos, double[] tol){
        for(int i = 0; i < 3; i++)
            if(Math.abs(targetPos[i] - currPos[i]) > tol[i]) return false;
        return true;
    }

    public static boolean inTolerance(Vector3 currPos, Vector3 targetPos, Vector3 tol){
        return inTolerance(currPos.toArray(), targetPos.toArray(), tol.toArray());
    }
}
