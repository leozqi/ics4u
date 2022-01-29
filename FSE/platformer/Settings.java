// ------------------------------------------------------------------------- //
// Stores all in-game constants for easy reference                           //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.Color;

public class Settings {

	// Create only one "global" instance of Settings for all game-wide settings.
	private static Settings inst = new Settings(); // Store all settings

	/**
	 * Stores all game-wide values.
	 *
	 * These are values that all game objects might need to know; however
	 * they are all not constants as many are modifiable by the user as
	 * settings. Some values are constants; they are `static` and `final`.
	 *
	 * Data protection is achieved through use of getters / setters.
	 *
	 * Thread-safety, although mostly irrelevant as the updater thread's
	 * resources are not used by any other threads in Game.java, is
	 * achieved with `synchronized` statements. Docs I referenced
	 * (no code was directly taken):
	 *
	 * <https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html>
	 */
	private Settings() { /* No need to instantiate */ }


	/**
	 * Get the one Settings object the whole game's elements reference.
	 *
	 * Only necessary to change Settings values, not to use a value; to
	 * just reference a value, static methods are available.
	 */
	public static Settings get() {
		return inst;
	} /* End method get */


	private int resX = 1600; // X resolution of screen in pixels

	/**
	 * Return width of screen in pixels.
	 */
	public static int resX() {
		synchronized(inst) { return inst.resX; }
	} /* End method resX */

	/**
	 * Set width of screen in pixels.
	 */
	public void setResX(int width) {
		synchronized(this) { this.resX = width; }
	} /* End method setResX */


	private int resY = 900; // Y resolution of screen in pixels

	/**
	 * Return height of screen in pixels.
	 */
	public static int resY() {
		synchronized(inst) { return inst.resY; }
	} /* End method resY */

	/**
	 * Set height of screen in pixels.
	 */
	public void setResY(int height) {
		synchronized(this) { this.resY = height; }
	} /* End method setResY */


	private double zoom = 1; // Zoom of screen (1 = 100%)

	/**
	 * Return zoom of level.
	 */
	public static double zoom() {
		synchronized(inst) { return inst.zoom; }
	} /* End method zoom */

	/**
	 * Set zoom of level.
	 */
	public void setZoom(double zoom) {
		synchronized(this) { this.zoom = zoom; }
	} /* End method setZoom */


	private double levelWidth = this.resX; // Width of current level

	/**
	 * Return width of current level in pixels.
	 */
	public static double levelWidth() {
		synchronized(inst) { return inst.levelWidth; }
	} /* End method levelWidth */

	/**
	 * Set width of current level in pixels.
	 */
	public void setLevelWidth(double width) {
		synchronized(this) { this.levelWidth = width; }
	} /* End method setLevelWidth */


	private double levelHeight = resY; // Height of current level

	/**
	 * Return height of current level in pixels.
	 */
	public static double levelHeight() {
		synchronized(inst) { return inst.levelHeight; }
	} /* End method zoom */

	/**
	 * Set height of current level in pixels.
	 */
	public void setLevelHeight(double height) {
		synchronized(this) { this.levelHeight = height; }
	} /* End method setZoom */


	public static final int UNIT = 35; // Tile size at 1x zoom
	public static final int SEP = 0;   // Separator between tiles in spritesheet
	public static final int internSep = 1;
	public static final int defaultSep = 0;

	public static final double FRAMES_SEC = 120d;
	public static final double NANOS = 1000000000d / FRAMES_SEC;

	public static final int PLAYER_WIDTH = 33;
	public static final int PLAYER_HEIGHT = 46;
	public static final int P_WIDTH = 36;
	public static final int P_HEIGHT = 49;
	public static final double P_JUMP = 5;
	public static final double P_SPD = 0.05;

	public static final double E_MAX_SPD = 2;
	public static final double E_MIN_SPD = 0.2;
	public static final double E_GRAVITY = 0.09;

	public static final int P_SPACE = 0;

	public static final String FILE_EXT = "lvl";

	public static final Color COLOUR_SKY = new Color(208, 244, 247, 255);
	public static final Color COLOUR_ROCK = new Color(131, 148, 148);
	public static final Color COLOUR_TITLE = new Color(120, 50, 150);

	public static final int SLIME_WIDTH = 26;
	public static final int SLIME_HEIGHT = 14;

} /* End class Settings */
