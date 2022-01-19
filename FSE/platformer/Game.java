// ------------------------------------------------------------------------- //
// Controls the input/output of the game (player input and display framerate)//
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-23                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D.Double;

public class Game extends JPanel {

	/* SpriteHandlers */
	SpriteHandler tileHandle;  // tileset
	SpriteHandler handleP1;    // animations for player one
	SpriteHandler handleItems; // tileset for items

	/* Level management */
	Level lvl;

	/* Game */
	Camera cam;
	boolean running = true;
	private double clock;
	Player player;
	Renderer renderer;

	public Game() {
		this.setPreferredSize(new Dimension(Settings.resX, Settings.resY));
		this.setFocusable(true);

		this.tileHandle = SpriteHandler.createFromFile(
			this, "/resources/tiles.png",
			Settings.internUnit, Settings.internUnit,
			Settings.internSep, Settings.internSep
		);
		if (this.tileHandle == null) {
			System.out.println("Could not be loaded");
		}

		this.handleP1 = SpriteHandler.createFromFile(
			this, "/resources/p1.png",
			36, 49, 0, 0
		);
		if (this.handleP1 == null) {
			System.out.println("P1 resources could not be loaded.");
		}

		this.handleItems = SpriteHandler.createFromFile(
			this, "/resources/items.png",
			35, 35, 0, 0
		);
		if (this.handleItems == null) {
			System.out.println("Handle items failed to load.");
		}

		this.lvl = new Level(tileHandle, Biome.SANDY);
		if (!this.lvl.loadFile("/resources/lvl1.lvl")) {
			System.out.println("Level could not be loaded");
		}

		this.player = new Player("Bob", 100, handleP1, new Rectangle2D.Double(80, 50, 33, 46), null);

		cam = new Camera(this.lvl.getLevel(this.tileHandle), 1.0, player);


		this.addKeyListener(player);
		this.renderer = new Renderer();
		this.renderer.start();
	} /* End constructor */


	private class Renderer extends Thread {

		@Override
		public void run() {
			long prevT = System.nanoTime(); // Previous time (ns)
			long currT;                     // Current time (ns)

			double diffT;                   // Difference in time

			while (running && (!this.isInterrupted())) {
				currT = System.nanoTime();
				diffT = (currT - prevT) / Settings.NANOS;
				prevT = currT;

				try {
				SwingUtilities.invokeAndWait(new Updater(diffT));
				} catch (Exception e) {
					continue;
				}
			}
		}
	} /* End class Renderer */


	private class Updater implements Runnable {

		private double diffT;

		public Updater(double diffT) {
			super();
			this.diffT = diffT;
		} /* End constructor */


		public void run() {
			update(diffT);
			repaint();
		} /* End implemented method run */

	} /* End class Updater */


	public void update(double diffT) {
		player.update(diffT, this.lvl.getBounds());
		if (this.clock > 100) {
			return;
		}
		this.clock += diffT;
	} /* End method update */


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (this.tileHandle == null) { return; }

		Graphics2D g2d = (Graphics2D) g;
		Point p = MouseInfo.getPointerInfo().getLocation();
		Point screenLoc = this.getLocationOnScreen();

		Point r = new Point(p.x - screenLoc.x, p.y - screenLoc.y);

		cam.beam(g2d);
	} /* End method paint */

} /* End class Game */
