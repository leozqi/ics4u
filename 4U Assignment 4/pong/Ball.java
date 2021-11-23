package pong;

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

import static pong.Constants.*;


/**
 * Simulates the Pong game's bouncing ball.
 *
 * The "ball" is assumed to be a square with side lengths of size
 * Constants.B_SIZE (only really because it looks kind of cool that way, as
 * opposed to a circle). It moves in a two-dimensional grid defined by the Java
 * AWT "classes" or framework, of height Constants.S_HEIGHT and width
 * Constants.S_WIDTH. The grid's origin is on the top-left corner, and the
 * ball's given X and Y coordinates are at the ball's top-left corner.
 *
 * The class provides a bounding-box via the `getBounds()` method to draw the
 * ball onto the screen, and various other methods to move the ball.
 *
 * See VerticalD.java for the enum representing vertical directions/positions.
 * See HorizontalD.java for the enum representing horizontal directions/positions.
 * See Constants.java for all constants used here.
 */
public class Ball {
	private Rectangle2D.Double shape; // Store shape for painting on screen and calculations
	private double startX, startY;    // Store the starting (x, y) pos
	private double angle;             // Store ball angle in degrees
	private double speed;             // Store ball speed

	// Toggles whether the ball's angle has already recalculated
	// This is important when the ball hits an obstacle; not having a toggle
	// means the angle will be recalculated many times.
	private boolean toggle;

	// If facingUp is true, the angle will be facing up
	// Otherwise, the angle will face down (the angle measure is still the same)
	private boolean facingUp;

	// If facingRight is true, the angle will be facing right
	// Otherwise, the angle will face left (the angle measure will be negative)
	private boolean facingRight;

	// Random number generator to get random starting angles
	private Random randGen = new Random();


	/**
	 * Initialize a new default Ball.
	 * 
	 * See the `reset()` method.
	 */
	public Ball() {
		this.shape = new Rectangle2D.Double();
		reset();
	} /* End default constructor */


	/**
	 * Resets the ball to its default starting position.
	 *
	 * The default ball starts in the centre of the grid with a random
	 * trajectory (max angle of B_MAX_START_ANGLE) and a speed of B_SPD.
	 */
	public void reset() {
		// The ball must start at the field's CENTRE X and Y position;
		// Shift the ball left and up by half its size for the ball's
		// origin to correspond with the ball's centre in the middle.
		this.startX = S_CENTRE_X - (B_SIZE / 2);
		this.startY = S_CENTRE_Y - (B_SIZE / 2);

		// randGen should not be too small
		// Therefore add half of the max angle to all of its results,
		// then generate an integer up to a half of the max angle
		this.angle  = this.randGen.nextInt(B_MAX_START_ANGLE / 2) + (B_MAX_START_ANGLE / 2);

		// Whether the ball is facing up or right is a random boolean.
		this.facingUp    = this.randGen.nextBoolean();
		this.facingRight = this.randGen.nextBoolean();

		this.speed  = B_SPD;
		this.toggle = false;

		// Set the rectangle representing the ball to correspond with
		// size and starting position
		this.shape.setRect(this.startX, this.startY, B_SIZE, B_SIZE);
	} /* End method reset */


	/**
	 * Check if the ball is strictly within the bounds of the field.
	 *
	 * The field is defined as an area of the screen within S_HEIGHT and
	 * S_WIDTH:
	 *
	 *          0------------------------ S_WIDTH
	 *          |                       |
	 *          |                       |
	 * S_HEIGHT |       The field       |
	 *          |                       |
	 *          |-----------------------|
	 *
	 * @return if the ball is within the field, false otherwise.
	 */
	public boolean ballInBounds() {
		return (this.ballInXBounds() && this.ballInYBounds());
	} /* End method ballInBounds */


	/**
	 * Check if the ball is strictly within the X bounds of the field.
	 *
	 * See the `ballInBounds` method.
	 */
	public boolean ballInXBounds() {
		return (
			(this.shape.getMaxX() <= S_WIDTH)
			&& (this.shape.getMinX() >= 0)
		);
	} /* End method ballInXBounds */


	/**
	 * Check if the ball is strictly within the Y bounds of the field.
	 *
	 * See the `ballInBounds` method.
	 */
	public boolean ballInYBounds() {
		return (
			(this.shape.getMaxY() <= S_HEIGHT)
			&& (this.shape.getMinY() >= 0)
		);
	} /* End method ballInYBounds */


	/**
	 * Calculates if the ball's angle should change, based on whether or
	 * not the ball is touching the paddles or within the field.
	 *
	 * If the ball's angle should change, calculate the new angle.
	 *
	 * @param  lBox the bounding box of the left paddle.
	 * @param  rBox the bounding box of the right paddle.
	 * @return CalcObj holding ball's angle and vertical direction, for
	 *         the use of the Smart CPU to calculate where to go.
	 */
	public CalcObj calcAngle(Rectangle2D.Double lBox, Rectangle2D.Double rBox) {
		boolean lTouch = this.shape.intersects(
			lBox.getX(), lBox.getY(), lBox.getWidth(), lBox.getHeight()
		);
		boolean rTouch = this.shape.intersects(
			rBox.getX(), rBox.getY(), rBox.getWidth(), rBox.getHeight()
		);

		// Check if angle must be recalculated. Three reasons why:
		// 1. Touching left paddle.
		// 2. Touching right paddle.
		// 3. Out of bounds.
		if (!(lTouch || rTouch || (!this.ballInBounds()))) {
			// The ball is NOT touching anything or out of bounds
			// Continue to return current CalcObj status.

			// Ball is no longer touching anything, toggle.
			this.toggle = false;
			// Return CalcObj based on boolean
			if (this.facingUp) {
				return new CalcObj(this.angle, VerticalD.UP);
			} else {
				return new CalcObj(this.angle, VerticalD.DOWN);
			}
		} else if (this.toggle) {
			// Angle has already been recalculated before, but
			// Ball is still hitting obstacle.
			//
			// A CalcObj(0, VerticalD.NEUTRAL) tells the CPU paddle
			// to disregard this result.
			return new CalcObj(0, VerticalD.NEUTRAL);
		}

		// Check if ball is:
		// 1. Touching left paddle
		// 2. Touching right paddle
		// 3. Out of X bounds
		if (lTouch || rTouch || !this.ballInXBounds()) {
			// Yes; flip the ball's X direction
			this.facingRight = !this.facingRight;
		}

		// Check if ball is out of Y bounds
		if (!this.ballInYBounds()) {
			// Yes; flip the ball's Y direction
			this.facingUp = !this.facingUp;
		}

		// If the ball's touched any paddle: calculate new angle
		if (lTouch || rTouch) {
			// Get the correct paddle's Y position
			// (for calculating the ball's new angle)
			double calcVal = 0;

			// If lTouch is true get left paddle's centerY, else
			// get right paddle's
			calcVal = lTouch ? lBox.getCenterY() : rBox.getCenterY();

			// New angle = |Distance between ballY and centre of paddle| / half of the paddle * max angle
			this.angle = (
				Math.abs(calcVal - this.shape.getCenterY())
				/ (((double) P_HEIGHT) / 2)
				* B_MAX_ANGLE
			);

			// Find whether this angle is going up or down
			if (calcVal - this.shape.getCenterY() > 0) {
				// Going Up
				this.facingUp = true;
			} else if (calcVal - this.shape.getCenterY() < 0) {
				this.facingUp = false;
			}
		}

		// No matter what obstacle was hit, do the following:
		this.speed *= (1d + B_SPD_INCREASE); // Multiply ball speed by B_SPD_INCREASE
		this.toggle = true;                  // Toggle on so no new angles will be calculated

		// Return correct vertical direction for Smart CPU
		if (this.facingUp) {
			return new CalcObj(this.angle, VerticalD.UP);
		} else {
			return new CalcObj(this.angle, VerticalD.DOWN);
		}
	} /* End method calcAngle */


	/**
	 * Moves the ball the correct amount of pixels across the screen based
	 * on the amount of time passed.
	 *
	 * this.speed is how much the ball should move every FPS. Since the
	 * game updates frames as much as possible, the function adjusts the
	 * amount moved based on the time passed for a smoother game.
	 */
	public void move(double time) {
		// Calculate actual pixels moved in flight path line based on
		// time
		double actualSpd = time * this.speed;

		// Calculate x pixel movement with the cosine function
		double x = Math.cos(Math.toRadians(this.angle)) * actualSpd;

		// Calculate y pixel movement with the sine function
		double y = Math.sin(Math.toRadians(this.angle)) * actualSpd;

		// Change the y movement or x movement to be negative based
		// on whether or not the ball is facing up or right
		if (this.facingUp)     { y *= -1; }
		if (!this.facingRight) { x *= -1; }

		// Perform the actual changes on the bounding box of the ball.
		this.shape.setRect(
			this.shape.getX()+x, this.shape.getY()+y,
			this.shape.getWidth(), this.shape.getHeight()
		);
	} /* End method move */


	/**
	 * Returns the bounding box of the ball.
	 *
	 * The bounding box of the ball is a Rectangle2D.Double shape that may
	 * be drawn onto the screen with Graphics2D or for calculation purposes
	 * with other shapes.
	 */
	public Rectangle2D.Double getBounds() {
		// getBounds2D() returns only a Rectangle2D so it must be cast
		return (Rectangle2D.Double) this.shape.getBounds2D();
	} /* End method getBounds */
} /* End class Ball */

