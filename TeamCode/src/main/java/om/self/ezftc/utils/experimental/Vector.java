package om.self.ezftc.utils.experimental;

import androidx.annotation.NonNull;

import java.util.Arrays;

/**
 * A generic vector interface for variable size vectors that supports basic vector operations (add, subtract, multiply, divide, dot product, cross product, matrix multiplication, distance, angle)
 * @param <T> the type of each component of the vector
 */
public abstract class Vector<T> implements Cloneable {
    private final T[] components;

    public Vector(T... components) {
        this.components = components;
    }

    /**
     * gets the size of the vector (the number of components)
     * @return the size of the vector
     */
    public int getSize(){
        return components.length;
    }

    /**
     * Gets the component of the vector at the specified index. The index starts at 0 so the last component is at index size - 1. For example the X component of a 3D vector would be at index 0 and the Z component would be at index 2
     * @param index the index of the component to get
     * @return the component of the vector at the specified index
     * @throws IndexOutOfBoundsException if the index is outside of the range 0 to size -1
     */
    public T getComponent(int index){
        return components[index];
    }

    /**
     * Gets the component of the vector as an array
     * @return the components of the vector as an array
     */
    public T[] getComponents(){
        return components;
    }

    /**
     * Adds two vectors together
     * @param v2 the vector to add to this vector
     * @return the sum of the two vectors
     */
    public abstract Vector<T> add(Vector<T> v2);

    /**
     * Subtracts two vectors
     * @param v2 the vector to subtract from this vector
     * @return the difference of the two vectors
     */
    public abstract Vector<T> subtract(Vector<T> v2);

    /**
     * Multiplies a vector by a scalar
     * @param scale the scalar to multiply the vector by
     * @return the scaled vector
     */
    public abstract Vector<T> multiply(T scale);

    /**
     * Divides a vector by a scalar
     * @param scale the scalar to divide the vector by
     * @return the scaled vector
     */
    public abstract Vector<T> divide(T scale);

    /**
     * matrix multiplies 2 vectors
     * @param v2 the vector to multiply this vector by
     * @return the product of the two vectors
     */
    public abstract Vector<T> matrixMultiply(Vector<T> v2);

    /**
     * Gets the distance between two vectors in each dimension
     * @param v2 the vector to get the distance to
     * @return the distance between the two vectors as a new vector
     */
    public abstract Vector<T> getDistances(T v2);

    /**
     * Gets the total distance between two vectors
     * @param v2 the vector to get the distance to
     * @return the total distance between the two vectors
     */
    public abstract T getDistance(Vector<T> v2);

    /**
     * Gets the angle between two vectors
     * @param v2 the vector to get the angle to
     * @return the angle between the two vectors
     */
    public abstract T getAngle(Vector<T> v2);

    /**
     * Gets the dot product of two vectors
     * @param v2 the vector to get the dot product with
     * @return the dot product of the two vectors
     */
    public abstract T dotProduct(Vector<T> v2);

    /**
     * Gets the cross product of two vectors
     * @param v2 the vector to get the cross product with
     * @return the cross product of the two vectors
     */
    public abstract Vector<T> crossProduct(Vector<T> v2);

    /**
     * switches each component of the current vector with the corresponding component of the passed in vector. If the passed in vector is smaller than the current vector then the remaining components will be from the current vector. If the passed in vector is larger than the current vector then it will just return the passed in vector
     * @param v2 the vector to switch components with
     * @return this vector with the switched components
     */
    public Vector<T> switchComponents(Vector<T> v2){
        if (v2.getSize() > getSize())
            return v2;

        for(int i = 0; i < getSize(); i++)
            components[i] = v2.getComponent(i);

        return this;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector<?> vector = (Vector<?>) o;
        return Arrays.equals(components, vector.components);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(components);
    }
}
