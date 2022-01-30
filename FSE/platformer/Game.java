// ------------------------------------------------------------------------- //
// The Game class displays main menus and controls loading and updating of   //
// levels.                                                                   //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Game.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.Double;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import java.net.URL;
import java.io.*;

public class Game extends JPanel implements ActionListener {

	/**
	 * SpriteHandlers manage the image resources loaded through different
	 * image paths.
	 *
	 * Each SpriteHandler can retrieve tiles from one spritesheet through
	 * a specified X and Y grid location on the spritesheet.
	 *
	 * Spritesheets are initialized in the `loadGraphics` method.
	 */
	SpriteHandler handleTile  = null; // tileset
	SpriteHandler handleP1    = null; // animations for player one

	// Collection of SpriteHandlers for entities
	// See EntityType for list of all types of entities
	// [0]: Slimes
	// [1]: Endgame flag
	// [2]: Items
	// [3]: Torches
	// [4]: Flies
	//
	// Set array of SpriteHandlers equal to number of types of entities
	// (Enumeration length of EntityType
	SpriteHandler[] handleEntities = new SpriteHandler[EntityType.values().length];

	// Store previous zoom of level to determine if new graphics assets
	// need to be loaded.
	double prevZoom = 0;

	// Smoothly darken levels
	double darkenLvlCnt = 0;

	/* Level management */
	Level lvl = null; // Stores a level loaded into the game
	// Store entities in the level (moving sprites)
	ArrayList<Entity> entities = new ArrayList<Entity>();

	/* Initial loading screen */
	Font font;                   // Font used for all text
	Menu menu;                   // Menu displayed at start

	/* Game */
	Camera cam = null;           // Camera draws game elements onto JPanel
	boolean running = false;     // Whether or not a level is currently running
	private double clock = 0d;   // Timer to keep track of constant game events
	boolean endSequence = false; // Begin exiting level and displaying end msg
	Player player = null;        // Keep track of player
	Renderer renderer;           // Renderer provides separate game loop thread

	Frame frame;                 // Store outer window containing Game.

	/**
	 * The Game object manages initial loading of filesystem resources and
	 * controls the main game loop of levels.
	 *
	 * During initial creation of a Game, a main menu is shown with the Menu
	 * class. Menu items are accessed through various Menu methods, while
	 * a Menu communicates actions like starting the game to the Game by
	 * registering the Game as an ActionListener.
	 *
	 * The background of the Game is set as dark while the background of the
	 * inner Menu is kept as default so that the menu's items can be clearly
	 * distinguished from extra space. BoxLayout documentation was used to
	 * set "horizontal glue" to centre the menu.
	 *
	 * <https://docs.oracle.com/javase/8/docs/api/javax/swing/BoxLayout.html>
	 *
	 * The entire Game window is fully resizable thanks to a ComponentAdapter
	 * that listens for these events, and changes the viewport width and
	 * height accordingly. These values are kept track of at all times in
	 * the Settings singleton class; they are used by the Camera class to
	 * resize the game's display while it is being played.
	 *
	 * @param frame Frame of parent window JFrame for resizing purposes.
	 */
	public Game(Frame frame) {
		/* Layout management */
		// Use a Java BoxLayout to correctly manage layout of Menu.
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		/* Window management */
		this.frame = frame; // Keep copy of JFrame for resize functionality
		this.frame.setResizable(true); // Enable resizing

		// Adjust Camera viewport based on resized window
		this.frame.addComponentListener(new ResizeListener());

		/* Set JPanel settings */
		this.setPreferredSize(new Dimension(Settings.resX(), Settings.resY()));
		this.setFocusable(true);

		/* Load fonts */
		if (!this.loadFonts()) { // Load required fonts for menu
			// Font loading failed, use default
			this.font = new Font(Font.DIALOG, Font.PLAIN, 18);
		}

		/* Load music */
		// If music loading fails, all sound related functionality will
		// fail silently as designed. Sounds are non-essential.
		this.loadMusic();

		/* Create menu */
		this.menu = new Menu(
			this,                 // ActionListener
			Settings.TITLE,       // Title
			Settings.DESCRIPTION, // Description
			this.font,            // Font
			this.getLevelNames()  // Level names to pick
		);

		this.showMenu();              // Show title screen menu

		// Space the menu between two "horizontal glues" to centre it.
		this.add(Box.createHorizontalGlue());
		this.add(this.menu);
		this.add(Box.createHorizontalGlue());
	} /* End constructor */


	/**
	 * Loads fonts used by the start menu and HUD.
	 *
	 * The font used within the game is Super Mario 256, a free-to-use font
	 * located in the /resources/ path relative to the classpath. This method
	 * loads that font (or any other font) as a TrueType font located at the
	 * "font.ttf" resources path.
	 *
	 * @return true if operation completed successfully, else false
	 */
	public boolean loadFonts() {
		try {
			// Attempt to load font file
			URL url = this.getClass().getResource("/resources/font.ttf");
			if (url == null) { return false; } // Path invalid; fail

			// Create font based on file
			this.font = Font.createFont(
				Font.TRUETYPE_FONT,
				new File(url.toURI())
			);
		} catch (Exception e) {
			return false; // Font file invalid
		}
		return true; // Success
	} /* End method loadFonts */


	/**
	 * Provides resize functionality by notifying other game components
	 * when the window has been modified.
	 *
	 * This class accomplishes this by modifying two global fields contained
	 * in the Settings singleton class, `resX` and `resY`. These represent
	 * the width and height of the new screen respectively.
	 */
	class ResizeListener extends ComponentAdapter {

		/**
		 * Called whenever a component is resized.
		 *
		 * For the purposes of this class, that component is the main
		 * window.
		 */
		@Override
		public void componentResized(ComponentEvent componentEvent) {
			// Get Settings singleton; set ResX and ResY
			// based on new width and height of JFrame
			Settings.get().setResX(Game.this.frame.getWidth());
			Settings.get().setResY(Game.this.frame.getHeight());

			// Set preferred size of JPanel equal to size of JFrame
			Game.this.setPreferredSize(new Dimension(
				Settings.resX(),
				Settings.resY()
			));

			if (!Game.this.running) {
				// Repaint for menu objects if Game is NOT
				// running; if game is running, the paint method
				// automatically takes care of scaling.
				//
				// However, if Game is NOT running there is no
				// other thread to handle this, so it should
				// be done on each resize.
				repaint();
			}
		} /* End method componentResized */

	} /* End class ResizeListener */


	/**
	 * Listen for menu button-actions and action commands to change the
	 * Game's state.
	 *
	 * The start button of the main menu is tied to this method; pressing
	 * it starts the game via this method. Between the time a level is loaded
	 * there is a small wait, which this method notifies the user of with
	 * the Menu.setDescription method.
	 *
	 * @param event Event to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Not start button; return
		if (!(event.getActionCommand() == "start")) { return; }

		// Give indicator to user that game is loading before level
		// shows
		this.menu.setDescription("Loading . . .");

		try {
			// Try to load levels and start the game.
			// Load in the Swing AWT loop to run in the same thread
			// as the painting methods.
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					startGame(); // Load levels
				}
			});
		} catch (Exception err) {} // If fail, ignore button / command
	} /* End method actionPerformed */


	/**
	 * Load graphics from the resources directory.
	 *
	 * This loads all the graphics from spritesheets configured for this
	 * program.
	 *
	 * @return true if operation completed successfully, else false.
	 */
	public boolean loadGraphics() {
		// There are three files for three different tile zoom levels.
		// Load the appropriate one for the seelected zoom.
		String tilePath = "/resources/tiles-100.png";
		if (Settings.zoom() == 2) {
			tilePath = "/resources/tiles-200.png";
		} else if (Settings.zoom() == 1.5) {
			tilePath = "/resources/tiles-150.png";
		}

		// Load background tiles
		// Current object must be passed to reference its classpath
		// and get the correct file.
		this.handleTile = SpriteHandler.createFromFile(
			this, tilePath,
			Settings.UNIT, Settings.UNIT,
			Settings.SEP, Settings.SEP,
			Settings.zoom(), true
		);
		// If loading fails for any graphics object, return fail signal
		if (this.handleTile == null) { return false; }

		// Load player animations
		// P_WIDTH and P_HEIGHT, as well as all subsequent WIDTH and
		// HEIGHT constants, refer to the heights of each tile within
		// the spritesheet.
		this.handleP1 = SpriteHandler.createFromFile(
			this, "/resources/p1.png",
			Settings.P_WIDTH, Settings.P_HEIGHT, Settings.zoom()
		);
		if (this.handleP1 == null) { return false; }

		// Load entity SpriteHandler under their location stored by
		// the EntityTypes enumeration.

		// Load slimes
		this.handleEntities[EntityType.SLIME.costume] = SpriteHandler.createFromFile(
			this, "/resources/slime.png",
			Settings.S_WIDTH, Settings.S_HEIGHT, Settings.zoom()
		);
		if (this.handleEntities[EntityType.SLIME.costume] == null) { return false; }

		// Load endgoal
		this.handleEntities[EntityType.FLAG.costume] = SpriteHandler.createFromFile(
			this, "/resources/flags.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleEntities[EntityType.FLAG.costume] == null) { return false; }

		// Load item resources
		this.handleEntities[EntityType.G_COIN.costume] = SpriteHandler.createFromFile(
			this, "/resources/items.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleEntities[EntityType.G_COIN.costume] == null) { return false; }

		// Load torches
		this.handleEntities[EntityType.TORCH.costume] = SpriteHandler.createFromFile(
			this, "/resources/torch.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleEntities[EntityType.TORCH.costume] == null) { return false; }

		// Load flies
		this.handleEntities[EntityType.FLY.costume] = SpriteHandler.createFromFile(
			this, "/resources/fly.png",
			Settings.F_WIDTH, Settings.F_HEIGHT, Settings.zoom()
		);
		if (this.handleEntities[EntityType.FLY.costume] == null) { return false; }

		return true;
	} /* End method loadGraphics */


	/**
	 * Load game music.
	 *
	 * This method should only be called once for the whole game. It
	 * prepares audio resources via the `SoundHandler` class for all
	 * game elements to use.
	 *
	 * @return true if successful, otherwise false.
	 */
	public boolean loadMusic() {
		boolean status = true;

		// If registering any sound fails, then it will return false
		// The AND operation of true and false is false, so any
		// one fail will lead to the whole method returning false.

		// Load default theme song
		status = status && SoundHandler.get().register(
			"THEME_DEFAULT",
			"theme_default.wav"
		);

		// Load title theme song
		status = status && SoundHandler.get().register(
			"THEME_TITLE",
			"theme_title.wav"
		);

		// Load coin-grab sound
		status = status && SoundHandler.get().register(
			"SOUND_COIN",
			"sound_coin.wav"
		);

		// Load jumping sound
		status = status && SoundHandler.get().register(
			"SOUND_JUMP",
			"sound_jump.wav"
		);

		// Load sizzle sound
		status = status && SoundHandler.get().register(
			"SOUND_SIZZLE",
			"sound_sizzle.wav"
		);

		// Load sound of player being hit
		status = status && SoundHandler.get().register(
			"SOUND_HIT",
			"sound_hit.wav"
		);

		return status; // Return success or failure
	} /* End method loadMusic */


	/**
	 * Load level data.
	 *
	 * Level data is loaded with the use of the handleTile sprite source.
	 * When loading level data, all collision boxes are calculated from
	 * the raw pixels of the tiles and whether or not they are transparent.
	 *
	 * Therefore, this method will take time.
	 *
	 * @param path the path of the level FROM the resources directory,
	 *             ie. "level.lvl" NOT "/resources/level.lvl"
	 * @param biome the biome / "look" of the level.
	 * @return true if completed successfully, else false.
	 */
	public boolean loadLevel(String path) {
		if (this.handleTile == null) { return false; } // pics not loaded

		// Only keep track of one level at a time.
		this.lvl = new Level(
			this.handleTile,     // Tile spritesheet
			this.handleEntities, // Entity array spritesheet
			Settings.zoom()      // Zoom level of Settings
		);

		// Have the level class load the file specified at path
		if (!this.lvl.loadFile("/resources/" + path)) {
			// An error has occured when the level class loaded
			// the level; return false
			return false;
		}

		// Change background based on biome of level
		switch (this.lvl.getBiome()) {
		case ROCKY:
			this.setBackground(Settings.COLOUR_ROCK);
			break;
		default:
			this.setBackground(Settings.COLOUR_SKY);
		}

		return true;
	} /* End method loadLevel */


	/**
	 * Get every level name for display.
	 *
	 * This method searches the /resources/ path (in the same directory
	 * as the classes) for level files. Level files contain information for
	 * one level to be loaded each using the `Level` class, and end in a
	 * file extension defined as Settings.FILE_EXT.
	 *
	 * The menu levelpicker uses this method to get all levels.
	 *
	 * @return String[] of all level filenames found, with extension.
	 */
	public String[] getLevelNames() {
		// Get full path of relative path
		URL url = this.getClass().getResource("/resources");
		List<Path> files;
		try {
			// Use Utilities method to walk through all files in
			// relative resource path
			files = Utilities.findFiles(
				url.toURI(),      // Uniform Resource Identifier
				Settings.FILE_EXT // File extension as string
			);
		} catch (Exception e) {
			// Error, simply exit
			return null;
		}

		// Create array to return
		String[] ret = new String[files.size()];

		// Convert Path objects to Strings for display
		for (int i = 0; i < files.size(); i++) {
			// Display only the name for the file menu, no need
			// for full path
			ret[i] = files.get(i).getFileName().toString();
		}

		Arrays.sort(ret); // Sort names of levels into alphabetical order

		return ret;
	} /* End method getLevelPaths */


	/**
	 * Exit the current level.
	 *
	 * Exit the current level and display a message asking to play again.
	 */
	private void exitGame() {
		this.running = false;
		this.renderer.interrupt();
		this.renderer = null;
		this.entities.clear();

		// Signal for garbage-collect (may not happen) to clear old
		// level debris
		System.gc();

		this.menu.setDescription("Play again?");
		this.showMenu();
	} /* End method exitGame */


	/**
	 * Show the main menu and play main menu music.
	 *
	 * Used at the beginning when Game is first loaded and after completion
	 * of every level.
	 */
	public void showMenu() {
		// Stop all sounds
		SoundHandler.get().stopSounds();

		this.setBackground(Settings.COLOUR_ROCK); // Customize menu background
		this.menu.setVisible(true); // Show menu

		// Play theme song
		SoundHandler.get().playSound("THEME_TITLE", true);
	} /* End method showMenu */


	/**
	 * Start the selected level.
	 *
	 * Start the selected level by loading it from a file, along with
	 * image resources.
	 */
	public void startGame() {
		// Set zoom of global Settings to current menu zoom
		double currZoom = this.menu.getCurrentZoom(); // Set current zoom
		Settings.get().setZoom(currZoom);

		// Load graphics resources only if needed (if zoom has changed)
		if (currZoom != prevZoom) {
			prevZoom = currZoom;
			// Zoom has changed; reload graphics
			if (!this.loadGraphics()) {
				// Error condition:
				this.menu.setDescription("Error loading graphics.");
				return;
			}
		}

		// Load selected level of menu from file
		if (!this.loadLevel(this.menu.getCurrentLevel())) {
			// Error loading level
			this.menu.setDescription("Error loading level.");
			return;
		}
		// Store level width and height
		Settings.get().setLevelWidth(this.lvl.getWidth());
		Settings.get().setLevelHeight(this.lvl.getHeight());

		// Create player in level's starting position
		Point2D pSpawn = this.lvl.getPlayerStart();
		this.player = new Player(
			pSpawn.getX(),
			pSpawn.getY(),
			handleP1,
			null // Do not apply any attributes yet
		);
		// Connect KeyListener of game to player
		this.addKeyListener(player);

		// Load entities from level
		this.entities.addAll(this.lvl.getEntities());

		// Create camera from level
		this.cam = new Camera(this.lvl.getLevel(this.handleTile, Settings.zoom()));

		// Close menu
		this.menu.setVisible(false);

		// Stop all sounds (title theme) before level
		SoundHandler.get().stopSounds();

		// Create new running loop
		SoundHandler.get().playSound("THEME_DEFAULT", true);
		this.renderer = new Renderer(); // Create separate game thread
		this.renderer.start();          // Start thread
		this.running = true;            // Set level as running
		this.endSequence = false;       // Disable end sequence (game over screen)
	} /* End method startGame */


	/**
	 * Provide a separate render thread.
	 *
	 * The Renderer inner class provides a separate rendering thread for
	 * each level as a main game loop. It handles updating positions of
	 * items and redrawing frames without blocking the main Game class.
	 */
	private class Renderer extends Thread {

		/**
		 * Run game loop until interrupted or Game is not running.
		 */
		@Override
		public void run() {
			long prevT = System.nanoTime(); // Previous time (ns)
			long currT;                     // Current time (ns)

			double diffT;                   // Difference in time

			// While level is active
			while (running && (!this.isInterrupted())) {
				currT = System.nanoTime();
				diffT = (currT - prevT) / Settings.NANOS; // Difference in time
				prevT = currT;

				try {
				// Invoke from the Swing thread for best practices
				// Use a Runnable to update values and render to screen
				SwingUtilities.invokeAndWait(new Updater(diffT));
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						// Exit thread; level stopped
						Thread.currentThread().interrupt();
						return;
					}
					// Not interrupted; other exception
					// Just redraw frame
					continue;
				}
			}
		} /* End method run */

	} /* End class Renderer */


	/**
	 * Provide a Runnable object for Swing's AWT queue.
	 *
	 * Although the main game loop on a separate thread is a good time keeper,
	 * Java Swing best practices are to update graphical elements in the Java
	 * AWT (base rendering library) thread. This runnable allows the main
	 * game loop to pass methods onto that thread for updating.
	 */
	private class Updater implements Runnable {

		private double diffT;

		/**
		 * Updater runs the actual update and redraw functions.
		 *
		 * SwingUtilities requires a runnable object to execute.
		 */
		public Updater(double diffT) {
			super();
			this.diffT = diffT;
		} /* End constructor */


		/**
		 * Invoked by Swing Utilities each frame through Renderer.
		 */
		@Override
		public void run() {
			update(diffT); // Update data of all level elements
			repaint();     // Display all level elements
		} /* End implemented method run */

	} /* End class Updater */


	/**
	 * Perform updates for each frame.
	 *
	 * Updates only apply to entities as level tiles are pre-calculated
	 * during level load.
	 */
	public void update(double diffT) {
		// Do not update Game level main loop if Game is not running
		if (!this.running) { return; }

		/* Update clock */
		this.clock += diffT; // add diffT to the clock
		if (this.clock > Settings.TIME_END && endSequence) {
			// After player dies or wins, there is a countdown until
			// level exits to main menu
			exitGame();
		}

		/* Get areas to use to update entities */
		Shape bounds = this.lvl.getBounds();       // get impassable areas
		Shape climbable = this.lvl.getClimbable(); // get climbable areas
		Shape deadly = this.lvl.getDeadly();       // get deadly areas

		/* Update player */
		// Update player first, then other entities in order of precedence
		if (!player.isAlive() && !this.endSequence) {
			// Do not keep resetting end sequence if already set
			this.beginEndSequence();
		} else {
			player.update(diffT, bounds, climbable, deadly);
		}

		// Check if player hit item box (dynamically create items)
		checkHittingBox();

		/* Update other entities */
		updateEntities(diffT, bounds, deadly);
	} /* End method update */


	/**
	 * Begin end screen of either victory or death for player.
	 *
	 * Resets the clock of the level to display the message for n ticks.
	 * The update loop will exit to the main menu after that time.
	 */
	public void beginEndSequence() {
		// Already set; no need to do it again
		if (this.endSequence) { return; }

		this.clock = 0d;
		this.endSequence = true;
	} /* End method beginEndSequence */


	/**
	 * Update all entities loaded in the main game loop.
	 *
	 * @param diffT "difference in time" time adjustment per frame.
	 * @param bounds level collision boxes for entities to be aware of and
	 *               which entities should not go into.
	 * @param deadly level collision boxes for areas deadly to entities.
	 */
	public void updateEntities(double diffT, Shape bounds, Shape deadly) {
		Entity ent; // Store the current entity during iteration.

		// Store entities to be removed in an array
		int toRemoveCnt = 0;
		Entity[] toRemove = new Entity[this.entities.size()];

		// Iterate over all entities in the level
		for (int i = 0; i < this.entities.size(); i++) {
			ent = this.entities.get(i); // get entity from level
			ent.update(diffT, bounds);  // update all entities

			if ((!ent.isAlive()) || deadly.intersects(ent.getBounds())) {
				// If Entity is touching deadly area or
				// Entity is dead, remove from level
				toRemove[toRemoveCnt++] = ent;
				continue; // Skip to next entity
			}

			// `instanceof` checks if Entity is further a member
			// of a specific Entity subclass
			if (ent instanceof Enemy) {
				/* Handle enemy-specific updates */
				// If player touching enemy, player dies.
				if (player.isTouching(ent.getBounds())) {
					// Play hitting sound
					SoundHandler.get().playSound("SOUND_HIT", false);
					player.die();
					this.beginEndSequence(); // End level
				}
			} else if (ent instanceof Flag) {
				/* Handle flag-specific updates */
				if (player.isTouching(ent.getBounds())) {
					// Player win condition
					this.beginEndSequence(); // End level
				}
			} else if (ent instanceof Item) {
				/* Handle item-specific updates */
				Item item = (Item) ent;
				if (item.isTouching(player.getBounds())) {
					// Play application sound
					SoundHandler.get().playSound("SOUND_COIN", false);
					// Apply item attribute to player
					player.applyAttribute(item.getAttribute());
					// Item is expended
					toRemove[toRemoveCnt++] = item;
				}
			}
		}

		// Remove any items queued
		if (toRemoveCnt > 0) {
			for (Entity removable : toRemove) {
				// Removable may be empty
				if (removable != null) {
					this.entities.remove(removable);
				}
			}
		}
	} /* End method updateEntities */


	/**
	 * Check if player is hitting any item boxes of the level.
	 *
	 * Itemboxes randomly generate rewards for the player in the form
	 * of items appearing on top of them.
	 */
	public void checkHittingBox() {
		// Get locations of all boxes currently in the level.
		SpecBounds boxes = this.lvl.getSpecBounds();

		// Check if box contains point right above player's head.
		// If box does contain point, remove box
		// Settings.P_SPACE contains location above player's head used
		Area boxLocation = (Area) boxes.containsRemove(
			player.getCentreX(),
			player.getPoint().getY() - Settings.P_SPACE
		);

		if (boxLocation == null) {
			// Player didn't hit any box with head; exit
			return;
		}
		// Player did hit item box. Get and store coordinates from box.
		Rectangle2D bounds = boxLocation.getBounds2D();

		// Create random coin at location
		entities.add(Item.randomCoin(
			bounds.getX(), // X coordinates

			// Y coordinate is equal to one unit higher than item box
			bounds.getY() - (Settings.UNIT * Settings.zoom()),
			handleEntities[EntityType.G_COIN.costume] // Coin sprite
		));
	} /* End method checkHittingBox */


	/**
	 * Draw frame with updated positions.
	 *
	 * Uses the Camera class to scale and adjust camera angles with
	 * entities. The Camera will first use the image of the level it already
	 * has calculated as a base, then layer on top entities, the player,
	 * a potential darkening effect, and any text to display.
	 *
	 * The darkening effect is controlled by the Biome; currently, the
	 * "Rocky" or cave biome has the darkening effect. Darkening allows
	 * the player to only see themselves and a few other lit locations on
	 * the map unless they die or they complete the level for interesting
	 * effects. Darkening is applied gradually at start so players may see a
	 * sneek peak of the level.
	 *
	 * During the "end sequence" when the player is dead or has completed
	 * the level, a message is shown by the Camera.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// No need to paint if game is not running
		if (!this.running) { return; }

		Graphics2D g2d = (Graphics2D) g;

		boolean darken = false;
		if ((this.lvl.getBiome() == Biome.ROCKY) && (!endSequence)) {
			// Darken the level only if the biome is underground
			// and the level sequence has not ended.

			// If the level has ended, let the player see the whole
			// level
			darken = true;
		}

		// Amount to darken level by (Max 255 per colour space
		double darkenLvl = (int)(Math.min(this.clock, 255));

		// Display level and entities (With potential darkening effect)
		cam.beam(g2d, player, entities, Settings.zoom(), darken, darkenLvl);

		// Show scores
		cam.showScore(g2d, this.font.deriveFont(40f), player.getCoins());

		String msg;
		if (endSequence) {
			// Change message based on player status
			if (this.player.isAlive()) {
				msg = "You win!";
			} else {
				msg = "Game over!";
			}
			cam.showMsg(g2d, this.font.deriveFont(Settings.FONT_LARGE), msg);
		}
	} /* End method paint */

} /* End class Game */
