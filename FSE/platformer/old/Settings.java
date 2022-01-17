// ------------------------------------------------------------------------- //
// Handle sprite loading within the game.                                    //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

public final class Settings {

	private static int unit  = 10;
	private static int bUnit = 70;

	private static final int resXScale = 12;
	private static final int resYScale = 5;

	private static int resolutionX = bUnit * resXScale;
	private static int resolutionY = bUnit * resYScale;

	public static int getUnit() { return unit; }
	public static int getResolutionX() { return resolutionX; }
	public static int getResolutionY() { return resolutionY; }
	public static int getColumnNum() { return resXScale; }
	public static int getRowNum() { return resYScale; }


	public static void calcRes() {
		resolutionX = unit * resXScale;
		resolutionY = unit * resYScale;
	}


	public synchronized static void setUnit(int px) {
		unit = px;
		calcRes();
	} /* End method setResolutionX */

} /* End class Settings */
