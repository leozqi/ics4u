// ------------------------------------------------------------------------- //
// The Game class displays main menus and controls loading and updating of   //
// levels.                                                                   //
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
	// [0]: Slimes
	// [1]: Endgame flag
	// [2]: Items
	SpriteHandler[] handleEntities = new SpriteHandler[3];

	Level lvl = null; // Stores a level loaded into the game
	ArrayList<Entity> entities = new ArrayList<Entity>();

	/* Initial loading screen */
	Font font;                   // Font used for all text
	Menu menu;                   // Menu displayed at start

	/* Game */
	Camera cam = null;
	boolean running = false;
	private double clock = 0d;
	boolean endSequence = false;
	Player player = null;
	Renderer renderer;

	private RenderingHints rh = new RenderingHints(
		RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON
	);

	Frame frame;

	public Game(Frame frame) {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(Settings.COLOUR_ROCK);

		/* Window management */
		this.frame = frame; // Keep copy of JFrame for resize functionality
		this.frame.setResizable(true); // Enable resizing

		// Adjust Camera viewport based on resized window
		this.frame.addComponentListener(new ResizeListener());

		/* Set JPanel settings */
		this.setPreferredSize(new Dimension(Settings.resX(), Settings.resY()));
		this.setFocusable(true);

		if (!this.loadFonts()) { // Load required fonts for menu
			// Font loading failed, use default
			this.font = new Font(Font.DIALOG, Font.PLAIN, 18);
		}

		this.menu = new Menu(
			this,
			"Go Oust!",
			"Help Oust the alien reach the flag!",
			this.font,
			this.getLevelNames()
		);
		this.menu.setVisible(true);
		this.add(Box.createHorizontalGlue());
		this.add(this.menu);
		this.add(Box.createHorizontalGlue());
	} /* End constructor */


	/**
	 * Loads fonts used by the start menu and HUD.
	 *
	 * @return true if operation completed successfully, else false
	 */
	public boolean loadFonts() {
		try {
			URL url = this.getClass().getResource("/resources/font.ttf");

			if (url == null) { return false; }

			this.font = Font.createFont(
				Font.TRUETYPE_FONT,
				new File(url.toURI())
			);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	} /* End method loadFonts */


	class ResizeListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent componentEvent) {
			Settings.get().setResX(Game.this.frame.getWidth());
			Settings.get().setResY(Game.this.frame.getHeight());
			Game.this.setPreferredSize(new Dimension(
				Settings.resX(),
				Settings.resY()
			));

			if (!Game.this.running) {
				repaint();
			}
		}

	} /* End class ResizeListener */


	/**
	 * Listen for menu button-actions and action commands to change the
	 * Game's state.
	 *
	 * The start button of the main menu is tied to this method; pressing
	 * the start button starts the game via this method.
	 *
	 * @param event Event to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Not start button; return
		if (!(event.getActionCommand() == "start")) { return; }

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

		this.menu.setDescription("Loading . . .");
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
		String tilePath = "/resources/tiles-100.png";
		if (Settings.zoom() == 2) {
			tilePath = "/resources/tiles-200.png";
		} else if (Settings.zoom() == 1.5) {
			tilePath = "/resources/tiles-150.png";
		}

		// Load background tiles
		this.handleTile = SpriteHandler.createFromFile(
			this, tilePath,
			Settings.UNIT, Settings.UNIT,
			Settings.SEP, Settings.SEP,
			Settings.zoom(), true
		);
		if (this.handleTile == null) { return false; }

		// Load player animations
		this.handleP1 = SpriteHandler.createFromFile(
			this, "/resources/p1.png",
			Settings.P_WIDTH, Settings.P_HEIGHT, Settings.zoom()
		);
		if (this.handleP1 == null) { return false; }

		// Load slimes
		this.handleEntities[0] = SpriteHandler.createFromFile(
			this, "/resources/slime.png",
			Settings.SLIME_WIDTH, Settings.SLIME_HEIGHT, Settings.zoom()
		);
		if (this.handleEntities[0] == null) { return false; }

		// Load endgoal
		this.handleEntities[1] = SpriteHandler.createFromFile(
			this, "/resources/flags.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleEntities[1] == null) { return false; }

		// Load item resources
		this.handleEntities[2] = SpriteHandler.createFromFile(
			this, "/resources/items.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleEntities[2] == null) { return false; }

		return true;
	} /* End method loadGraphics */


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
			this.handleTile,
			this.handleEntities,
			Settings.zoom()
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
	 * Get every level's name for display.
	 *
	 * The level picker will allow selection of different levels.
	 */
	public String[] getLevelNames() {
		// Get full path of relative path
		URL url = this.getClass().getResource("/resources/");
		List<Path> files;
		try {
			// Use Utilities method to walk through all files in
			// relative resource path
			files = Utilities.findFiles(
				Paths.get(url.getPath()),
				Settings.FILE_EXT // File extension as string
			);
		} catch (IOException e) {
			// Error:
			return null;
		}

		// Create array to return
		String[] ret = new String[files.size()];

		for (int i = 0; i < files.size(); i++) {
			// Display only the name for the file menu, no need
			// for full path
			ret[i] = files.get(i).getFileName().toString();
		}
		return ret;
	} /* End method getLevelPaths */


	private void exitGame() {
		this.running = false;
		this.renderer.interrupt();
		this.renderer = null;
		this.entities.clear();

		this.setBackground(Settings.COLOUR_ROCK);

		this.menu.setDescription("Play again?");
		this.menu.setVisible(true);
	} /* End method newGame */


	public void startGame() {
		Settings.get().setZoom(this.menu.getCurrentZoom());
		this.loadGraphics();

		this.loadLevel(this.menu.getCurrentLevel());

		Point2D pSpawn = this.lvl.getPlayerStart();
		Settings.get().setLevelWidth(this.lvl.getWidth());
		Settings.get().setLevelHeight(this.lvl.getHeight());

		this.player = new Player(
			5, handleP1,
			// Starting location
			new Rectangle2D.Double(
				pSpawn.getX(),
				pSpawn.getY(),
				Settings.PLAYER_WIDTH * Settings.zoom(),
				Settings.PLAYER_HEIGHT * Settings.zoom()
			),
			null
		);

		this.entities.addAll(this.lvl.getEntities());

		this.cam = new Camera(this.lvl.getLevel(this.handleTile, Settings.zoom()));

		this.menu.setVisible(false);

		this.addKeyListener(player);
		this.renderer = new Renderer();
		this.renderer.start();
		this.running = true;
		this.endSequence = false;
	} /* End method startGame */


	/**
	 * Provides a separate render thread.
	 *
	 * The implementation is the same from my Pong project.
	 */
	private class Renderer extends Thread {

		@Override
		public void run() {
			long prevT = System.nanoTime(); // Previous time (ns)
			long currT;                     // Current time (ns)

			double diffT;                   // Difference in time

			// While level is running
			while (running && (!this.isInterrupted())) {
				currT = System.nanoTime();
				diffT = (currT - prevT) / Settings.NANOS;
				prevT = currT;

				try {
				// Invoke from the Swing thread for best practices
				SwingUtilities.invokeAndWait(new Updater(diffT));
				} catch (Exception e) {
					// If error occurs, just create a new
					// frame
					continue;
				}
			}
		} /* End method run */

	} /* End class Renderer */


	private class Updater implements Runnable {

		private double diffT;

		/**
		 * Updater runs the actual update and redraw functions.
		 *
		 * It's a runnable class as that is what SwingUtilities requires.
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
			update(diffT);
			repaint();
		} /* End implemented method run */

	} /* End class Updater */


	/**
	 * Perform updates for each frame.
	 *
	 * Updates only apply to entities as level tiles are pre-calculated
	 * during level load.
	 */
	public void update(double diffT) {
		if (!this.running) { return; }
		this.clock += diffT;

		if (this.clock > 500 && endSequence) { 
			exitGame();
		}

		Shape bounds = this.lvl.getBounds();
		Shape climbable = this.lvl.getClimbable(); // get climbable area
		player.update(diffT, bounds, climbable);

		checkHittingBox();
		updateEntities(diffT, bounds);
	} /* End method update */

	public void updateEntities(double diffT, Shape bounds) {
		Entity ent;

		// Remove entities that are dead, etc.
		int toRemoveCnt = 0;
		Entity[] toRemove = new Entity[this.entities.size()];

		for (int i = 0; i < this.entities.size(); i++) {
			ent = this.entities.get(i);
			ent.update(diffT, bounds);

			if (ent instanceof Enemy) {
				if (player.isTouching(ent.getBounds())) {
					player.die();
					this.clock = 0;
					endSequence = true;
				}
				if (!ent.isAlive()) {
					toRemove[toRemoveCnt] = ent;
					toRemoveCnt++;
				}
			} else if (ent instanceof Flag) {
				if (player.isTouching(ent.getBounds())) {
					this.clock = 0;
					endSequence = true;
				}
			} else if (ent instanceof Item) {
				Item item = (Item) ent;
				if (item.isTouching(player.getBounds())) {
					player.applyAttribute(item.getAttribute());
					toRemove[toRemoveCnt] = item;
					toRemoveCnt++;
				}
			}
		}

		// Remove any items queued
		if (toRemoveCnt > 0) {
			for (Entity removable : toRemove) {
				this.entities.remove(removable);
			}
		}
	} /* End method updateEntities */


	public void checkHittingBox() {
		SpecBounds boxes = this.lvl.getSpecBounds();

		Area boxLocation = (Area) boxes.containsRemove(
			player.getCentreX(),
			player.getPoint().getY() - 5
		);

		if (boxLocation == null) {
			// Player didn't hit any box with head
			return;
		}

		// Get and store coordinates from location
		Rectangle2D.Double bounds = (Rectangle2D.Double) boxLocation.getBounds2D();

		// Create item from location
		entities.add(new Item(
			bounds.getX(),
			bounds.getY()-(Settings.UNIT*Settings.zoom()),
			handleEntities[2], EntityType.HEALTH
		));
	} /* End method checkHittingBox */


	/**
	 * Draw frame with updated positions.
	 *
	 * Uses the Camera class to scale and adjust camera angles with
	 * entities.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (!this.running) { return; } // No need to paint; Game is not
		                               // running.

		Graphics2D g2d = (Graphics2D)g;
		cam.beam(g2d, player, entities, Settings.zoom());
		if (endSequence) {
			if (this.player.isAlive()) {
				cam.showMsg(g2d, this.font.deriveFont(80f), "Victory!");
			} else {
				cam.showMsg(g2d, this.font.deriveFont(80f),"You died . . .");
			}
		}
	} /* End method paint */

} /* End class Game */
