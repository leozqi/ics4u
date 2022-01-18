// ------------------------------------------------------------------------- //
// Handle sprite loading within the game.                                    //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

public final class Settings {

	// Use 16:9 display (most common)
	public static int resX = 800;
	public static int resY = 450;

	public static int halfResX = 400;
	public static int halfResY = 225;

	public static int internResX = 560; // Scales to units of 70
	public static int internResY = 315;

	public static int internUnit = 35;
	public static int internSep  = 1;

	public static final double FRAMES_SEC = 120d;
	public static final double NANOS = 1000000000d / FRAMES_SEC;

	public static final double P_MAX_SPD = 2;

} /* End class Settings */
