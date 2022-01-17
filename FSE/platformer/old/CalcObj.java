/*
 * CalcObj.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "CalcObj" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "CalcObj" class stores both an angle in degrees and a vertical direction
 * in one object.
 */


package pong;

/**
 * Single object storing both an angle in degrees and a vertical direction.
 *
 * Objects of this class are mainly returned by a Ball object to be given to a
 * Paddle with the mode COMPUTER_SMART. It allows only one object to be passed
 * for the Paddle to calculate where to go based on the contained angle and
 * direction to intercept the Ball object.
 *
 * See VerticalD.java for defined vertical directions within the enum VerticalD.
 */
public class CalcObj {
	public double angle;  // Stores an angle in degrees.
	public VerticalD dir; // Stores a vertical direction like UP or DOWN.


	/**
	 * Creates a CalcObj.
	 *
	 * @param angle the angle of the CalcObj
	 * @param dir the VerticalD direction of the CalcObj
	 */
	public CalcObj(double angle, VerticalD dir) {
		this.angle = angle;
		this.dir = dir;
	} /* End default constructor */
} /* End class CalcObj */

