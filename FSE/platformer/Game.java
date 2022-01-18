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

	SpriteHandler tileHandle;
	Level lvl;
	Camera cam;
	boolean running = true;
	Renderer renderer;
	private double clock;
	Player player;
	SpriteHandler handleP1;

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
			33, 46,
			Settings.internSep, Settings.internSep
		);
		if (this.tileHandle == null) {
			System.out.println("P1 resources could not be loaded.");
		}

		this.lvl = new Level("/resources/lvl1.lvl");
		if (this.lvl == null) {
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
