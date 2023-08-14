package om.self.ezftc.utils.experimental;

public class DoubleVector extends Vector<Double>{
    public DoubleVector(Double... components) {
        super(components);
    }

    /**
     * Adds two vectors together
     *
     * @param v2 the vector to add to this vector
     * @return the sum of the two vectors
     */
    @Override
    public Vector<Double> add(Vector<Double> v2) {
        int min = Math.min(getSize(), v2.getSize());
        Double[] newComponents = new Double[min];
        for(int i = 0; i < min; i++){
            newComponents[i] = getComponent(i) + v2.getComponent(i);
        }
        DoubleVector newVector = new DoubleVector(newComponents);
        return switchComponents(v2.switchComponents(newVector));
    }

    /**
     * Subtracts two vectors
     *
     * @param v2 the vector to subtract from this vector
     * @return the difference of the two vectors
     */
    @Override
    public Vector<Double> subtract(Vector<Double> v2) {
        return null;
    }

    /**
     * Multiplies a vector by a scalar
     *
     * @param scale the scalar to multiply the vector by
     * @return the scaled vector
     */
    @Override
    public Vector<Double> multiply(Double scale) {
        return null;
    }

    /**
     * Divides a vector by a scalar
     *
     * @param scale the scalar to divide the vector by
     * @return the scaled vector
     */
    @Override
    public Vector<Double> divide(Double scale) {
        return null;
    }

    /**
     * matrix multiplies 2 vectors
     *
     * @param v2 the vector to multiply this vector by
     * @return the product of the two vectors
     */
    @Override
    public Vector<Double> matrixMultiply(Vector<Double> v2) {
        return null;
    }

    /**
     * Gets the distance between two vectors in each dimension
     *
     * @param v2 the vector to get the distance to
     * @return the distance between the two vectors as a new vector
     */
    @Override
    public Vector<Double> getDistances(Double v2) {
        return null;
    }

    /**
     * Gets the total distance between two vectors
     *
     * @param v2 the vector to get the distance to
     * @return the total distance between the two vectors
     */
    @Override
    public Double getDistance(Vector<Double> v2) {
        return null;
    }

    /**
     * Gets the angle between two vectors
     *
     * @param v2 the vector to get the angle to
     * @return the angle between the two vectors
     */
    @Override
    public Double getAngle(Vector<Double> v2) {
        return null;
    }

    /**
     * Gets the dot product of two vectors
     *
     * @param v2 the vector to get the dot product with
     * @return the dot product of the two vectors
     */
    @Override
    public Double dotProduct(Vector<Double> v2) {
        return null;
    }

    /**
     * Gets the cross product of two vectors
     *
     * @param v2 the vector to get the cross product with
     * @return the cross product of the two vectors
     */
    @Override
    public Vector<Double> crossProduct(Vector<Double> v2) {
        return null;
    }
}
