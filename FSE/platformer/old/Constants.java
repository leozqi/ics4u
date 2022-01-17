/*
 * Constants.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "Constants" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "Constants" class provides common constants for the "pong" package.
 */


package pong;

import java.awt.Color; // For Colour constants

/**
 * Holds all the constants for the Pong application.
 *
 * All the other classes that need these constants import this class "statically"
 * to use them directly into their code:
 *
 * https://docs.oracle.com/javase/8/docs/technotes/guides/language/static-import.html
 *
 * Many of these constants are based on just two, adjustable constants: G_UNIT
 * and FRAMES_SEC.
 *
 * A "G_UNIT" is a Game Unit and directly corresponds with the size of the game
 * display. A greater G_UNIT scales the whole game up. To visualize, the G_UNIT
 * is equal to the pixel side-length of the ball displayed in the game.
 *
 * "FRAMES_SEC" is the "frames per second" and measures how fast the game should
 * updates its game elements.
 */
public final class Constants {

	private Constants() {}

	/* Game constants */

	/* Coordinate system
	 *
	 * 0 ---------------- S_WIDTH pixels
	 * |
	 * |
	 * |
	 * |
	 * S_HEIGHT pixels
	 */

	public static final int    G_UNIT     = 15;
	public static final double FRAMES_SEC = 120d;

	/* Game constants */

	public static final String G_TITLE = "Pong!"; // Title of the game window

	public static final int G_POINTS = 10; // Points to win a game
	public static final int G_PAUSE  = 3;  // 3 second pause time before rounds

	/* Calculation constants */

	// Represents the delay in nanoseconds between frames
	public static final double NANOS = 1000000000 / FRAMES_SEC;

	/* Screen constants */

	// S_WIDTH is the width of the screen, S_HEIGHT is the height.
	// This originally corresponded with an area of 855 * 525 (chosen
	// arbitarily) when G_UNIT was 15.
	public static final int S_WIDTH    = 57 * G_UNIT;
	public static final int S_HEIGHT   = 35 * G_UNIT;

	// The centre X and Y coordinates of the screen, if origin is in the
	// top-left.
	public static final int S_CENTRE_X = (S_WIDTH / 2) + 1;
	public static final int S_CENTRE_Y = (S_HEIGHT / 2) + 1;

	/* Paddle constants */

	// Paddle widths and heights
	public static final int    P_WIDTH  = 1 * G_UNIT;
	public static final int    P_HEIGHT = 5 * G_UNIT;

	// Space between the paddle and the edge of the screen
	public static final int    P_SPACE  = 2 * G_UNIT;

	// Paddle speed (arbitary but scales)
	public static final double P_SPD    = (20 * G_UNIT) / FRAMES_SEC;

	/* Ball constants */

	// Ball side length measurement
	public static final int    B_SIZE = 1 * G_UNIT;

	// Ball speed (arbitary but scales)
	public static final double B_SPD  = (20 * G_UNIT) / FRAMES_SEC;

	// Maximum random starting angle of a ball served
	public static final int    B_MAX_START_ANGLE = 10;

	// Maximum angle of a ball when hit by a paddle
	public static final int    B_MAX_ANGLE = 50;

	// Speed increase ratio of a ball after collision with border or paddle
	public static final double B_SPD_INCREASE = 0.05d; // 5% increase

	// Max speed of a ball as ratio of its original speed
	public static final double B_SPD_MAX = 8d; // 800%

	/* Midsection */

	// Size of a square within the midsection divider
	public static final int M_SIZE = (G_UNIT / 3) * 2;

	// Amount of squares to skip for each visible square in the midsection
	public static final int M_SKIP = M_SIZE / 4;

	/* Colours */

	// Foreground colour
	public static final Color C_FORE = Color.white;

	// Background colour
	public static final Color C_BACK = Color.black;

	// Font size (point)
	public static final int F_SIZE = 5 * G_UNIT;

	// Font padding
	public static final int F_PAD = G_UNIT;
} /* End class Constants */

