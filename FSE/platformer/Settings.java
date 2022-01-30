// ------------------------------------------------------------------------- //
// The Settings class provides a global singleton instance from which other  //
// objects may access game-wide constants and change game-wide settings.     //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Settings.java                                                   //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.Color;
import java.awt.event.KeyEvent;

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
	 * Get singleton Settings object used by all game elements.
	 *
	 * Only necessary to change a Setting value, not to use a value; to
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


	private double brightness = 120d; // Brightness of dark levels (radius of light)

	/**
	 * Return brightness of current level.
	 *
	 * The "brightness" of a dark level is how large the radius the light
	 * effect of the level should be. A greater brightness means the player
	 * can see further away from themselves.
	 */
	public static double brightness() {
		synchronized(inst) { return inst.brightness; }
	} /* End method zoom */

	/**
	 * Set brightness of current level.
	 */
	public void setBrightness(double brightness) {
		synchronized(this) { this.brightness = brightness; }
	} /* End method setZoom */


	/* Game string constants */
	public static final String TITLE = "Go Oust!"; // Name of game
	public static final String DESCRIPTION = "Help Oust the alien reach the flag!";
	public static final String CONTROLS = "W to jump; A and D to move sideways";
	public static final String FILE_EXT = "lvl";   // File extension for levels

	/* Game units */
	public static final int UNIT = 35; // Tile size at 1x zoom
	public static final int SEP = 0;   // Separator between tiles in spritesheet

	/* Game constants */
	public static final double TIME_END = 200; // Time to end level after player death / victory

	/* Game colours */
	// Colours are all defined as either RGB (red-green-blue) or RGBA
	// (red-green-blue-alpha)
	public static final Color COLOUR_SKY   = new Color(208, 244, 247, 255);
	public static final Color COLOUR_ROCK  = new Color(152, 152, 152);
	public static final Color COLOUR_TITLE = new Color(120, 50, 150);
	public static final Color COLOUR_GOLD  = new Color(255, 203, 45);

	// Gradient colour of darkening effect of levels
	public static final Color COLOUR_GRADIENT = new Color(0, 0, 0, 200);

	/* Control constants */
	public static final int KEY_UP = KeyEvent.VK_W;    // Up keybinding
	public static final int KEY_DOWN = KeyEvent.VK_S;  // Down keybinding
	public static final int KEY_LEFT = KeyEvent.VK_A;  // Left keybinding
	public static final int KEY_RIGHT = KeyEvent.VK_D; // Right keybinding

	/* Display constants */
	public static final double FRAMES_SEC = 120d; // Frames per second of game
	public static final double NANOS = 1000000000d / FRAMES_SEC; // Wait of game loop
	public static final float  SCORE_SEP = 25; // Separation between score
	public static final float  FONT_LARGE = 80f; // Large font

	/* Spritesheet constants */
	public static final int S_WIDTH  = 26; // Slime width
	public static final int S_HEIGHT = 14; // Slime height

	public static final int P_WIDTH  = 36; // Player width
	public static final int P_HEIGHT = 49; // Player height
	public static final int P_SPACE  = 5;  // Location above player's head

	public static final int F_WIDTH  = 37; // Fly width
	public static final int F_HEIGHT = 18; // Fly height

	/* Entity constants */
	public static final double P_JUMP = 5;       // Player jump velocity
	public static final double P_SPD = 0.05;     // Player acceleration

	public static final double E_MAX_SPD = 2;    // Entity max velocity
	public static final double E_MIN_SPD = 0.2;  // Entity minimum velocity
	public static final double E_GRAVITY = 0.09; // Entity default gravity

} /* End class Settings */
