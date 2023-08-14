package om.self.ezftc.utils;

/**
 * An immutable 2D vector class
 */
public class Vector2{
    public final double X,Y;

    /**
     * Creates a vector with the given values
     * @param X the x value
     * @param Y the y value
     */
    public Vector2(double X, double Y){
        this.X = X;
        this.Y = Y;
    }

    /**
     * Creates a vector with the given values
     * @param vals an array of minimum length 2 with the values
     */
    public Vector2(double[] vals){
        this.X = vals[0];
        this.Y = vals[1];
    }

    /**
     * Creates a vector at the origin (0,0)
     */
    public Vector2(){
        X = 0;
        Y = 0;
    }

    /**
     * Converts the vector to an array
     * @return an array with the values of the vector {x,y}
     */
    public double[] toArray() {
        return new double[]{X,Y};
    }

    /**
     * Gets the value at the given index
     * @param index the index to get the value from (0 = x, 1 = y)
     * @return the value at the given index
     * @throws IndexOutOfBoundsException if the index is not 0 or 1
     */
    public double get(int index){
        switch(index){
            case 0: return X;
            case 1: return Y;
            default: throw new IndexOutOfBoundsException("the index '" + index + "' is not inside a vector 2");
        }
    }

    /**
     * Returns a new vector2 that has the x and y components switched
     * @return a new vector with the x and y components switched
     */
    public Vector2 switchXY(){
        return new Vector2(Y, X);
    }

    /**
     * Returns a new vector2 that has the x and y components inverted (multiplied by -1)
     * @return a new vector with the x and y components inverted
     */
    public Vector2 invert(){
        return new Vector2(-Y, -X);
    }

    /**
     * Returns a new vector2 that has the x component inverted (multiplied by -1)
     * @return a new vector with the x component inverted
     */
    public Vector2 invertX(){
        return new Vector2(-X, Y);
    }

    /**
     * Returns a new vector2 that has the y component inverted (multiplied by -1)
     * @return a new vector with the y component inverted
     */
    public Vector2 invertY(){
        return new Vector2(X, -Y);
    }

    /**
     * Returns a new vector2 that has the x component set to the given value
     * @param X the new x value
     * @return a new vector with the x component set to the given value
     */
    public Vector2 withX(double X){return new Vector2(X,Y);}

    /**
     * Returns a new vector2 that has the y component set to the given value
     * @param Y the new y value
     * @return a new vector with the y component set to the given value
     */
    public Vector2 withY(double Y){return new Vector2(X,Y);}

    /**
     * Checks if the current vector2 is within the tolerance of the target vector
     * @param targetPos the target vector
     * @param tol the tolerance vector
     * @return true if the current vector is within the tolerance of the target vector
     */
    public boolean inTolerance(Vector2 targetPos, Vector2 tol){
        return VectorMath.inTolerance(this, targetPos, tol);
    }

    /**
     * Verifies that the given object is a vector2 and has the same values as the current vector
     * @param obj the object to compare to
     * @return true if the object is a vector2 and has the same values as the current vector
     */
    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Vector2)) return false;
        Vector2 other = (Vector2) obj;
        return other.X == X && other.Y == Y;
    }

    /**
     * Converts the vector2 to a string with the given number of decimals
     * @param decimals the number of decimals to use
     * @return a string representation of the vector
     */
    public String toString(int decimals){
        return "Vector2{X=" + String.format("%."+ decimals +"f", X) + ", Y=" + String.format("%."+ decimals +"f", Y) + "}";
    }

    /**
     * Converts the vector2 to a string with 2 decimals
     * @return a string representation of the vector
     */
    @Override
    public String toString() {
        return toString(2);
    }
}