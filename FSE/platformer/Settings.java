// ------------------------------------------------------------------------- //
// Stores all in-game constants for easy reference                           //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.Color;

public final class Settings {

	// Use 16:9 display (most common)
	public static int resX = 1900; // Resolution in pixels, X axis
	public static int resY = 1080; // Resolution in pixels, Y axis

	public static int halfResX = 400;
	public static int halfResY = 225;

	public static int internResX = 560; // Scales to units of 70
	public static int internResY = 315;

	public static int internUnit = 35;
	public static int internSep  = 1;
	public static int defaultSep = 0;

	public static final double FRAMES_SEC = 120d;
	public static final double NANOS = 1000000000d / FRAMES_SEC;

	public static final int P_WIDTH = 36;
	public static final int P_HEIGHT = 49;
	public static final double P_SPD = ((double)internUnit) / 700d;
	public static final double P_JUMP = ((double)internUnit) / 10d;

	public static final double P_MAX_SPD = 2;
	public static final double E_GRAVITY = 0.09;

	public static final int P_SPACE = 0;

	public static final String FILE_EXT = "lvl";

	public static final Color COLOUR_SKY = new Color(208, 244, 247, 255);

} /* End class Settings */
