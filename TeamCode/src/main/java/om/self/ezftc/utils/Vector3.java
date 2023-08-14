package om.self.ezftc.utils;


import com.acmerobotics.roadrunner.geometry.Pose2d;

public class Vector3 extends Vector2 {
	public final double Z;

	/**
	 * Creates a vector with the given values
	 * @param X the x value
	 * @param Y the y value
	 * @param Z the z value (could also be used as an angle)
	 */
	public Vector3(double X, double Y, double Z){
		super(X,Y);
		this.Z = Z;
	}

	/**
	 * Creates a vector with the given values
	 * @param vals an array of minimum length 3 with the values
	 */
	public Vector3(double[] vals){
		super(vals);
		this.Z = vals[2];
	}

	/**
	 * Creates a vector at the origin (0,0,0)
	 */
	public Vector3(){
		super();
		Z = 0;
	}

	/**
	 * Creates a vector with the given values
	 * @param val the value to set x, y, and z to
	 */
	public Vector3(double val){
		super(val, val);
		this.Z = val;
	}

	/**
	 * Converts the vector to an array
	 * @return an array with the values of the vector {x,y,z}
	 */
	@Override
	public double[] toArray() {
		return new double[]{X,Y,Z};
	}

	/**
	 * Gets the value at the given index
	 * @param index the index to get the value from (0 = x, 1 = y, 2 = z)
	 * @return the value at the given index
	 * @throws IndexOutOfBoundsException if the index is not 0, 1, or 2
	 */
	public double get(int index){
		switch(index){
			case 0: return X;
			case 1: return Y;
			case 2: return Z;
			default: throw new IndexOutOfBoundsException("the index '" + index + "' is not inside a vector 3");
		}
	}

	/**
	 * Returns a new vector3 that has the x and y components switched
	 * @return a new vector with the x and y components switched
	 */
	public Vector3 switchXY(){
		return new Vector3(Y, X, Z);
	}

	/**
	 * Returns a new vector3 that has the x and z components switched
	 * @return a new vector with the x and z components switched
	 */
	public Vector3 switchXZ(){
		return new Vector3(Z, Y, X);
	}

	/**
	 * Returns a new vector3 that has the y and z components switched
	 * @return a new vector with the y and z components switched
	 */
	public Vector3 switchYZ(){
		return new Vector3(X, Z, Y);
	}

	/**
	 * Returns a new vector3 that has all components inverted (multiplied by -1)
	 * @return a new vector with inverted components
	 */
	public Vector3 invert(){
		return new Vector3(-Y, -X, -Z);
	}

	/**
	 * Returns a new vector3 that has the x component inverted (multiplied by -1)
	 * @return a new vector with the x component inverted
	 */
	public Vector3 invertX(){
		return new Vector3(-X, Y, Z);
	}

	/**
	 * Returns a new vector3 that has the y component inverted (multiplied by -1)
	 * @return a new vector with the y component inverted
	 */
	public Vector3 invertY(){
		return new Vector3(X, -Y, Z);
	}

	/**
	 * Returns a new vector3 that has the z component inverted (multiplied by -1)
	 * @return a new vector with the z component inverted
	 */
	public Vector3 invertZ(){
		return new Vector3(X, Y, -Z);
	}

	/**
	 * Returns a new vector3 that has the x component set to the given value
	 * @param X the new x value
	 * @return a new vector with the x component set to the given value
	 */
	public Vector3 withX(double X){return new Vector3(X,Y,Z);}

	/**
	 * Returns a new vector3 that has the x added to the current x component
	 * @param X the value to add to the x component
	 * @return a new vector with the x component added to the given value
	 */
	public Vector3 addX(double X){return new Vector3(this.X + X,Y,Z);}

	/**
	 * Returns a new vector3 that has the y component set to the given value
	 * @param Y the new y value
	 * @return a new vector with the y component set to the given value
	 */
	public Vector3 withY(double Y){return new Vector3(X,Y,Z);}

	/**
	 * Returns a new vector3 that has the y added to the current y component
	 * @param Y the value to add to the y component
	 * @return a new vector with the y component added to the given value
	 */
	public Vector3 addY(double Y){return new Vector3(X, this.Y + Y,Z);}

	/**
	 * Returns a new vector3 that has the z component set to the given value
	 * @param Z the new z value
	 * @return a new vector with the z component set to the given value
	 */
	public Vector3 withZ(double Z){return new Vector3(X,Y,Z);}

	/**
	 * Returns a new vector3 that has the z added to the current z component
	 * @param Z the value to add to the z component
	 * @return a new vector with the z component added to the given value
	 */
	public Vector3 addZ(double Z){return new Vector3(X,Y,this.Z + Z);}

	/**
	 * Converts the vector to a {@link Pose2d}
	 * <br>
	 * Note: this method treats the z value as an angle in degrees and converts it to radians for the pose2d
	 * @return a {@link Pose2d} with the giving x, y, and rotation values.
	 */
	public Pose2d toPose2d(){
		return new Pose2d(X, Y, Math.toRadians(Z));
	}

	/**
	 * Checks if the current vector3 is within the tolerance of the target vector
	 * @param targetPos the target vector
	 * @param tol the tolerance vector
	 * @return true if the current vector is within the tolerance of the target vector
	 */
	public boolean inTolerance(Vector3 targetPos, Vector3 tol){
		return VectorMath.inTolerance(this, targetPos, tol);
	}

	/**
	 * Checks if the current vector3 is equal to the obj by checking type and values
	 * @param obj the object to compare to
	 * @return true if the obj is a vector3 and has the same values as the current vector3
	 */
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof Vector3)) return false;
		Vector3 other = (Vector3) obj;
		return other.X == X && other.Y == Y && other.Z == Z;
	}

	/**
	 * Converts the vector to a string with the given number of decimals
	 * @param decimals the number of decimals to use
	 * @return a string with the vector values
	 */
	public String toString(int decimals){
		return "Vector3{X=" + String.format("%."+ decimals +"f", X) + ", Y=" + String.format("%."+ decimals +"f", Y) + ", Z=" + String.format("%."+ decimals +"f", Z) + "}";
	}

	/**
	 * Converts the vector to a string with 2 decimals
	 * @return a string with the vector values
	 */
	@Override
	public String toString() {
		return toString(2);
	}
}