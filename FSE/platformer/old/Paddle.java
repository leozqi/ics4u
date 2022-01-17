/*
 * Paddle.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "Paddle" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "Paddle" class represents a Paddle in the game of Pong.
 */


package pong;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

import static pong.Constants.*;


/**
 * Simulates the Pong game's paddle.
 *
 * A "paddle" is a rectangle with width P_WIDTH and height P_HEIGHT. It moves
 * in the 2D playing grid of height S_HEIGHT and width S_WIDTH.
 *
 * This class provides a bounding-box via the `getBounds()` method to draw the
 * Paddle onto the screen and other methods that should be called to move the
 * Paddle.
 *
 * The Paddle extends the KeyAdapter class so it can directly receive player
 * input through the keyboard. The paddle can also determine where to move by
 * itself to reach the ball if it is set to a COMPUTER_. . . mode with the
 * `calcDirection()` method.
 */
public class Paddle extends KeyAdapter {
	private Mode mode;                // Store the mode of the paddle
	private Rectangle2D.Double shape; // Store shape as bounding box.

	private HorizontalD side;  // Store the side of the paddle (LEFT or RIGHT)

	// Store the direction of the paddle (UP or DOWN)
	// This is volatile because KeyEvents are from the main thread, but
	// movingTo is accessed also by the update thread too
	private volatile VerticalD movingTo;

	/* For the CPU automatic moving modes */

	private double yTarget; // Store the position the paddle aims to move to
	private Random randGen = new Random(); // Random number generator for chance
	private CalcObj prevCalc = new CalcObj(0, VerticalD.NEUTRAL); // Previous stored ball angle and vertical direction


	/**
	 * Initialize a new Paddle.
	 *
	 * See the `reset` method for how the paddle's position is determined.
	 *
	 * @param mode the mode of the paddle (See Mode.java for possible modes)
	 *             The NEUTRAL mode will result in the paddle being on the
	 *             right.
	 * @param side the side the paddle should be at (LEFT or RIGHT)
	 */
	public Paddle(Mode mode, HorizontalD side) {
		super();

		this.mode = mode;
		this.side = side;
		this.shape = new Rectangle2D.Double();

		this.reset();
	} /* End (Mode, HorizontalD) constructor */


	/**
	 * Initialize a default Paddle.
	 *
	 * The "default paddle" is used when the paddle's `setMode` method
	 * will be used to change its mode later. Its mode is COMPUTER_SIMPLE
	 * and its side is LEFT.
	 */
	public Paddle() {
		// Call own constructor
		this(Mode.COMPUTER_SIMPLE, HorizontalD.LEFT);
	} /* End default constructor */


	/**
	 * Resets the paddle to its default starting position.
	 *
	 * The paddle starts in on either the left or right side of the grid in
	 * the centre Y position and a neutral up/down direction.
	 */
	public void reset() {
		this.movingTo = VerticalD.NEUTRAL;

		// Set x position based on position being LEFT or RIGHT. NEUTRAL
		// position will result in a position of RIGHT.
		double xPos;
		if (this.side == HorizontalD.LEFT) {
			// P_SPACE is the padding between the paddle and the side.
			// 0 --------- | |
			//       ^     | |
			//    P_SPACE  | |
			xPos = 0 + P_SPACE;
		} else {
			// On the right, the width of the paddle is also taken into account
			xPos = S_WIDTH - P_SPACE - P_WIDTH;
		}

		// Set the rectangle representing the paddle to correspond with
		// starting position.
		this.shape.setRect(
			xPos,
			S_CENTRE_Y - (P_HEIGHT / 2),
			P_WIDTH,
			P_HEIGHT
		);
	} /* End method reset */


	/**
	 * Changes the direction of the paddle's vertical movement based on
	 * key presses.
	 *
	 * The left human player controls the paddle with the W/S keys while the
	 * right human player controls the paddle with the Left arrow/Right arrow
	 * keys.
	 *
	 * If the paddle is not a human player, it will exit out of this method
	 * immediately and not take into account key presses.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (this.mode != Mode.PLAYER) { return; }

		// Get integer key code of the pressed key
		int code = e.getKeyCode();

		// Only change direction based on the side of the paddle.
		switch (this.side) {
		case LEFT:
			switch (code) {
			case KeyEvent.VK_W:
				this.changeDirection(VerticalD.UP);
				break;
			case KeyEvent.VK_S:
				this.changeDirection(VerticalD.DOWN);
				break;
			}
			break;
		case RIGHT:
			switch (code) {
			case KeyEvent.VK_UP:
				this.changeDirection(VerticalD.UP);
				break;
			case KeyEvent.VK_DOWN:
				this.changeDirection(VerticalD.DOWN);
				break;
			}
			break;
		}
	} /* End method keyPressed */


	/**
	 * Changes the direction of the paddle's vertical movement to neutral
	 * when a key is released.
	 *
	 * If the paddle is not a human player, it will exit out of this method
	 * immediately nad not take into account key releases.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (this.mode != Mode.PLAYER) { return; }

		int code = e.getKeyCode();

		// This event will be given to ALL paddles
		// Only apply NEUTRAL movement if THIS paddle corresponds
		// with the keys released
		// (Where the KeyEvent corresponds with the paddle's side)
		if ((
			// Conditions for the left paddle
			this.side == HorizontalD.LEFT &&
			(code == KeyEvent.VK_W || code == KeyEvent.VK_S)
			)
			||
			(
			// Conditions for the right paddle
			this.side == HorizontalD.RIGHT &&
			(code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN)
		)) {
			// Stop the paddle
			this.changeDirection(VerticalD.NEUTRAL);
		}
	} /* End method keyReleased */


	/**
	 * Calculates the direction this paddle should go in to meet the ball.
	 *
	 * All computer mode paddles will adjust their direction to go towards
	 * a Y position of y-target. The only difference between them is how they
	 * arrive at their specific yTarget value.
	 *
	 * If the paddle's mode is human (PLAYER), this method will immediately
	 * exit.
	 */
	public void calcDirection(
		Rectangle2D.Double ballBox, Rectangle2D.Double oppBox,
		boolean oppTouching, CalcObj calc
	) {
		switch (this.mode) {
		case PLAYER:
			// Exit; irrelevant to keyboard-controlled paddles
			return;
		case COMPUTER_SIMPLE:
			// Always follow the ball's Y position for simple computers
			this.yTarget = ballBox.getCenterY();
			break;
		case COMPUTER_SMART:
			// Do special calculations in the `calcSmart` method
			// This is mainly for organization

			// Make sure the CalcObj is valid: a valid CalcObj
			// will not have a NEUTRAL vertical direction.

			// An invalid CalcObj means the ball is still touching
			// an obstacle, so no new yTarget calculations need
			// to be made.
			if (calc.dir != VerticalD.NEUTRAL) {
				this.calcSmart(ballBox, oppBox, calc);
			}
			break;
		}

		// Adjust direction to go towards yTarget
		if (this.yTarget > this.shape.getCenterY()) {
			this.changeDirection(VerticalD.DOWN);
		} else if (this.yTarget < this.shape.getCenterY()) {
			this.changeDirection(VerticalD.UP);
		} else {
			this.changeDirection(VerticalD.NEUTRAL);
		}
	} /* End method calcDirection */


	/**
	 * Calculates the yTarget of a COMPUTER_SMART paddle.
	 *
	 * The COMPUTER_SMART paddle attempts to use the angle and vertical
	 * direction of the ball to calculate the final Y position of the ball
	 * at the X position of the paddle, even with bounce shots that hit
	 * the top or bottom of the grid.
	 *
	 * This method works by simulating the flight path of the ball. It
	 * measures out the total change in X of the ball before it hits the
	 * paddle, and then incrementally moves its own "ball" (whose position
	 * is represented by xCounter for X and y for Y) at the ball's current
	 * angle and vertical direction through the grid, changing vertical
	 * direction when hitting the top or bottom of the grid.
	 */
	private void calcSmart(
		Rectangle2D.Double ballBox, Rectangle2D.Double oppBox,
		CalcObj calc
	) {
		// First initally set the yTarget to the ball's Y.
		this.yTarget = ballBox.getCenterY();

		// The ball's change in X position
		double dBallX = Math.abs(ballBox.getCenterX() - this.shape.getCenterX());

		// The ball's change in Y position
		double dBallY;

		// Where the paddle's Y should be.
		double y = 0;

		// The x value this method is at within dBallX.
		double xCounter = 0;

		// Iterate through dBallX while changing dBallY to match the Y of the ball
		while (dBallX > 0) {
			// The change in the "virtual ball"'s Y if it moved xCounter
			dBallY = xCounter * Math.tan(Math.toRadians(calc.angle));

			// Based on the ball's vertical direction, change its y
			switch (calc.dir) {
			case UP:
				// The ball's Y would decrease if it moved up the screen
				y = ballBox.getCenterY() - dBallY;
				break;
			case DOWN:
				// The ball's Y would increase if it moved down the screen
				y = ballBox.getCenterY() + dBallY;
				break;
			}

			if (y <= 0 || y >= S_HEIGHT) {
				// Switch the direction of the virtual ball
				// if upper/lower field bounds are hit.
				if (calc.dir == VerticalD.UP) {
					calc.dir = VerticalD.DOWN;
				}
				else {
					calc.dir = VerticalD.UP;
				}

				xCounter = 0; // this way ball's Y will be reset too
			}
			xCounter += G_UNIT; // Step through iteration
			dBallX -= G_UNIT;
		}

		this.yTarget = y; // Set target y to be calculated y of virtual ball.
	} /* End method calcSmart */


	/**
	 * Moves the paddle according to a "difference in time".
	 *
	 * See the Renderer class in the Game.java file for a detailed explanation.
	 *
	 * @param diffT the difference in time.
	 */
	public void move(double diffT) {
		// If a computer paddle is close enough to its target, stop
		// making minutae adjustments.
		double disToTarget = Math.abs(this.yTarget - this.shape.getCenterY());
		if (this.mode != Mode.PLAYER && disToTarget < (G_UNIT / 3)) {
			return; // within good enough threshold
		}

		double actualSpd = diffT * P_SPD; // Actual speed calculation

		// Make sure there are no changes to this.movingTo WHILE this
		// method uses its value.
		//
		// This might happen if a key event occurs at the same time as
		// the updater method call
		synchronized(this.movingTo) {
			switch (this.movingTo) {
			case UP:
				// Going up means a decrease in Y.
				// Set shape to be minus a Y of the actual speed.
				this.shape.setRect(
					this.shape.getX(),
					// The max Y a paddle can go to is 0
					// (hitting the top edge of screen)
					Math.max(0, this.shape.getY()-actualSpd),
					this.shape.getWidth(),
					this.shape.getHeight()
				);
				break;
			case DOWN:
				// Going down means an increase in Y.
				// Set shape to be plus a Y of the actual speed.
				this.shape.setRect(
					this.shape.getX(),
					// The min Y a paddle can go to is the
					// screen height minus its height, so that
					// the paddle's bottom just touches the edge
					// of the screen.
					Math.min(S_HEIGHT - P_HEIGHT, this.shape.getY()+actualSpd),
					this.shape.getWidth(),
					this.shape.getHeight()
				);
				break;
			}
		}
	} /* End method move */


	/**
	 * Return the bounding box of the paddle.
	 *
	 * @return Rectangle2D.Double bounding box that may be drawn onto the
	 *         screen with Graphics2D, or used for calculations with other
	 *         shapes.
	 */
	public Rectangle2D.Double getBounds() {
		return (Rectangle2D.Double) this.shape.getBounds2D();
	} /* End method getBounds */


	/**
	 * Return the mode of the paddle.
	 *
	 * @return mode of the paddle.
	 */
	public Mode getMode() {
		return this.mode;
	} /* End method getMode */


	/**
	 * Set the mode and side of the paddle.
	 *
	 * Used when the paddle is first set to a default state before real
	 * state is decided.
	 *
	 * @param mode the mode of the paddle. (List of possible modes are in
	 *             the Modes.java file)
	 * @param side the horizontal side the paddle will be on (LEFT or RIGHT)
	 */
	public void setMode(Mode mode, HorizontalD side) {
		this.mode = mode; // Set modes and sides
		this.side = side;
		this.reset();     // Reset the paddle to its new position
	} /* End method setMode */


	/**
	 * Changes the direction of the paddle.
	 *
	 * @param direction vertical direction to change to.
	 */
	private void changeDirection(VerticalD direction) {
		if (direction == this.movingTo) { return; } // Same direction

		// Make sure no other method is using movingTo (if a KeyEvent
		// and update happens at the same time this might happen):
		//
		// https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html

		synchronized(this.movingTo) {
			this.movingTo = direction;
		}
	} /* End method changeDirection */


	/**
	 * Calculates the direction this paddle should go in to meet the ball.
	 *
	 * All computer mode paddles will adjust their direction to go towards
	 * a Y position of y-target. The only difference between them is how they
	 * arrive at their specific yTarget value.
	 *
	 * If the paddle's mode is human (PLAYER), this method will immediately
	 * exit.
	 */
	/**
	 * Get the string representation of the paddle for display.
	 *
	 * @return string of paddle state in form "mode: horizontal position".
	 */
	@Override
	public String toString() {
		String s = "";
		switch (this.mode) {
		case PLAYER:
			s += "Player";
			break;
		case COMPUTER_SIMPLE:
			s += "Simple CPU";
			break;
		case COMPUTER_SMART:
			s += "Smart CPU";
			break;
		}
		
		switch (this.side) {
		case LEFT:
			s += ": Left";
			break;
		case RIGHT:
			s += ": Right";
			break;
		}
		return s;
	} /* End method toString */


	/**
	 * Return whether or not the paddle is touching another rectangle
	 * bounding box.
	 *
	 * @param rect bounding box to test for.
	 * @return true if touching bounding box, false otherwise.
	 */
	public boolean touching(Rectangle2D.Double rect) {
		return this.shape.intersects(
			rect.getX(), rect.getY(),
			rect.getWidth(), rect.getHeight()
		);
	} /* End method touching */
} /* End class Paddle */
