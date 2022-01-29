// ------------------------------------------------------------------------- //
// Controls the input/output of the game (player input and display framerate)//
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-23                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D.Double;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.io.*;

public class Game extends JPanel implements ActionListener {

	/* SpriteHandlers */
	SpriteHandler tileHandle = null;  // tileset
	SpriteHandler handleP1 = null;    // animations for player one
	SpriteHandler handleItems = null; // tileset for items
	SpriteHandler[] handleEntities = new SpriteHandler[5]; // animations collection for enemies

	/* Level management */
	Level lvl = null;
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	Flag flag;

	/* Initial loading screen */
	Font font;
	JPanel menu;
	JSlider zoomSelect;
	JComboBox<String> levelList;
	JProgressBar mProgress;

	/* Game */
	Camera cam = null;
	boolean running = false;
	private double clock = 0d;
	boolean deathSequence = false;
	Player player = null;
	Renderer renderer;

	Frame frame;

	public Game(Frame frame) {
		super(new BorderLayout());

		/* Frame / window */
		this.frame = frame; // Keep a copy of the JFrame to adjust size
		this.frame.setResizable(true); // Resizable until level is loaded
		this.frame.addComponentListener(new ResizeListener());

		/* Set JPanel settings */
		this.setPreferredSize(new Dimension(Settings.resX(), Settings.resY()));
		this.setFocusable(true);

		if (!this.loadFonts()) { // Load required fonts for menu
			// Font loading failed, use default
			this.font = new Font(Font.DIALOG, Font.PLAIN, 18);
		}

		this.menu = this.buildMenu();
		this.menu.setVisible(true);
		this.add(this.menu, BorderLayout.CENTER);
	} /* End constructor */


	/**
	 * Loads fonts used by the start menu and HUD.
	 *
	 * @return true if operation completed successfully, else false
	 */
	public boolean loadFonts() {
		try {
			URL url = this.getClass().getResource("/resources/font.ttf");

			if (url == null) {
				return false;
			}

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
		}

	} /* End class ResizeListener */


	/**
	 * Builds a start menu with Swing components (Radiobuttons, buttons,
	 * and labels).
	 *
	 * NOTE: This code's structure was originally from my Pong project.
	 *
	 * This start menu is used to select a level from available levels in the
	 * filesystem.
	 *
	 * Documentation used:
	 *
	 * Comboboxes:
	 * <https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html>
	 *
	 * The final menu looks like the below:
	 * ---------------------------
	 * |                         |
	 * |       Alien Bros        |
	 * |                         |
	 * |  Pick a level:          |
	 * |  ---------------------  |
	 * |  |    Level 1        |  |
	 * |  ---------------------  |
	 * |                         |
	 * |          Start!         |
	 * |                         |
	 * ---------------------------
	 */
	private JPanel buildMenu() {
		JPanel retMenu = new JPanel();        // Menu to return is in a JPanel

		JLabel mTitle  = new JLabel("Go Oust!"); // Title
		mTitle.setFont(this.font.deriveFont(80f));
		mTitle.setForeground(new Color(120, 50, 150));
		JLabel mDescrip = new JLabel("Pick a level!");
		mDescrip.setFont(this.font.deriveFont(18f));
		mProgress = new JProgressBar();

		// Get all levels that can be played
		levelList = new JComboBox<String>(this.getLevelNames());
		levelList.setFont(this.font.deriveFont(20f));
		levelList.setMaximumSize( levelList.getPreferredSize());

		JLabel mSliderDescrip = new JLabel("Select a zoom:");
		mSliderDescrip.setFont(this.font.deriveFont(18f));
		zoomSelect = new JSlider(JSlider.HORIZONTAL, 50, 200, 100);
		zoomSelect.setMajorTickSpacing(50);
		zoomSelect.setSnapToTicks(true);
		zoomSelect.setPaintLabels(true);
		zoomSelect.setPaintTicks(true);
		zoomSelect.setPaintTrack(true);
		zoomSelect.setFont(this.font.deriveFont(18f));

		// Start button
		// Doc used: https://docs.oracle.com/javase/8/docs/api/javax/swing/JButton.html
		JButton mStart  = new JButton("Start!");
		mStart.setFont(this.font.deriveFont(50f));
		// the Game class listens for this ActionCommand to start the game
		mStart.setActionCommand("start");
		mStart.addActionListener(this);

		// A Java layout arranges the labels and buttons on the menu.
		// The Swing GroupLayout is the most flexible and easy to use:
		// https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
		//
		// The GroupLayout works by arranging elements based on their
		// specified positions in TWO separate layouts, one vertical
		// and one horizontal.
		//
		// Create a GroupLayout:
		GroupLayout lay = new GroupLayout(retMenu);
		retMenu.setLayout(lay);

		lay.setAutoCreateGaps(true);           // Gaps between elements
		lay.setAutoCreateContainerGaps(true);  // Gaps between containers

		// Order items in parallel horizontally (on top of one another)
		lay.setHorizontalGroup(
			lay.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(mTitle)     // Title
			.addGroup(lay.createSequentialGroup()
				.addComponent(mDescrip)   // Description
				.addComponent(levelList)  // List of levels
			)
			.addGroup(lay.createSequentialGroup()
				.addComponent(mSliderDescrip)
				.addComponent(
					zoomSelect,
					GroupLayout.PREFERRED_SIZE,
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE
				)
			)
			.addComponent(mStart)
			.addComponent(mProgress)  // Progress bar
		);

		lay.setVerticalGroup(
			lay.createSequentialGroup()
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE
			)
			.addComponent(mTitle)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addGroup(lay.createParallelGroup()
				.addComponent(mDescrip)
				.addComponent(levelList)
			)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE, 50
			)
			.addGroup(lay.createParallelGroup()
				.addComponent(mSliderDescrip)
				.addComponent(zoomSelect)
			)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE
			)
			.addComponent(mStart)
			.addComponent(mProgress)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE
			)
		);
		return retMenu; // Return the built menu
	} /* End method buildMenu */


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "start") {
			mProgress.setIndeterminate(true);
			try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					startGame();
				}
			});
			} catch (Exception err) {}
		}
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
		this.tileHandle = SpriteHandler.createFromFile(
			this, tilePath,
			Settings.UNIT, Settings.UNIT,
			Settings.SEP, Settings.SEP,
			Settings.zoom(), true
		);
		if (this.tileHandle == null) { return false; }

		// Load player animations
		this.handleP1 = SpriteHandler.createFromFile(
			this, "/resources/p1.png",
			Settings.P_WIDTH, Settings.P_HEIGHT, Settings.zoom()
		);
		if (this.handleP1 == null) { return false; }

		// Load item resources
		this.handleItems = SpriteHandler.createFromFile(
			this, "/resources/items.png",
			Settings.UNIT, Settings.UNIT, Settings.zoom()
		);
		if (this.handleItems == null) { return false; }

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

		return true;
	} /* End method loadGraphics */


	/**
	 * Load level data.
	 *
	 * Level data is loaded with the use of the tileHandle sprite source.
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
	public boolean loadLevel(String path, Biome biome) {
		if (this.tileHandle == null) { return false; } // pics not loaded

		// Only keep track of one level at a time.
		this.lvl = new Level(this.tileHandle, this.handleEntities, biome, Settings.zoom());

		if (!this.lvl.loadFile("/resources/" + path)) {
			return false;
		}

		switch (biome) {
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

		this.menu.setVisible(true);
	} /* End method newGame */


	public void startGame() {
		Settings.get().setZoom(zoomSelect.getValue() / 100d);
		this.loadGraphics();

		String levelName = (String) levelList.getSelectedItem();

		this.loadLevel(levelName, Biome.SANDY);

		Point2D pSpawn = this.lvl.getPlayerStart();

		this.player = new Player(
			"Bob",
			5, handleP1,
			new Rectangle2D.Double(
				pSpawn.getX(),
				pSpawn.getY(),
				Settings.PLAYER_WIDTH * Settings.zoom(),
				Settings.PLAYER_HEIGHT * Settings.zoom()), null
		);

		this.enemies = this.lvl.getEnemies();
		this.flag = this.lvl.getFlag();

		this.cam = new Camera(this.lvl.getLevel(this.tileHandle, Settings.zoom()));

		this.menu.setVisible(false);

		this.addKeyListener(player);
		this.renderer = new Renderer();
		this.renderer.start();
		this.running = true;
		this.deathSequence = false;
		this.mProgress.setIndeterminate(false);
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

		if (this.clock > 200 && deathSequence) { 
			exitGame();
		}

		Shape bounds = this.lvl.getBounds();
		Shape climbable = this.lvl.getClimbable(); // get climbable area
		player.update(diffT, bounds, climbable);

		checkHittingBox();
		checkHittingItem();
		for (int i = 0; i < this.enemies.size(); i++) {
			Enemy e = this.enemies.get(i);
			e.update(diffT, bounds);
			if (player.isTouching(e.getBounds())) {
				player.die();
				this.clock = 0;
				deathSequence = true;
			}
		}

		if (player.isTouching(flag.getBounds())) {
			deathSequence = true;
		}
	} /* End method updateEnemies */


	public void checkHittingItem() {
		Item check; // store each item here for processing

		// Cannot remove items spent up during processing, would ruin
		// index. Store for removal after.
		int toRemoveCnt = 0;
		Item[] toRemove = new Item[items.size()];

		// Iterate over all items
		for (int item = 0; item < items.size(); item++) {
			check = items.get(item);
			// Check if touching player
			if (check.isTouching(player.getBounds())) {
				player.applyAttribute(check.getAttribute()); // Apply one time effect of item
				toRemove[toRemoveCnt] = check; // Schedule for removal
				toRemoveCnt++;
			}
		}

		// Remove any items queued
		if (toRemoveCnt > 0) {
			for (Item removable : toRemove) {
				this.items.remove(removable);
			}
		}
	} /* End method checkHittingItem */


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
		items.add(new Item(
			bounds.getX(),
			bounds.getY()-(Settings.UNIT*Settings.zoom()),
			handleItems, ItemType.HEALTH
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

		if (!this.running) { return; } // No more to paint; not running

		Graphics2D g2d = (Graphics2D)g;
		cam.beam(g2d, player, items, enemies, flag, Settings.zoom());
	} /* End method paint */

} /* End class Game */
