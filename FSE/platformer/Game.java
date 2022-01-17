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

public class Game extends JPanel {

	SpriteHandler tileHandle;
	Level lvl;
	Camera cam;
	boolean running = true;
	Renderer renderer;
	private double clock;

	public Game() {
		this.setPreferredSize(new Dimension(Settings.resX, Settings.resY));
		this.setFocusable(true);

		this.tileHandle = SpriteHandler.createFromFile(
			this, "/resources/tiles.png", Settings.internUnit,
			Settings.internSep, Settings.internSep
		);
		if (this.tileHandle == null) {
			System.out.println("Could not be loaded");
		}

		this.lvl = new Level("/resources/lvl1.lvl");
		if (this.lvl == null) {
			System.out.println("Level could not be loaded");
		}
		cam = new Camera(this.lvl.getLevel(this.tileHandle), 1.0);

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
		if (this.clock > 100) {
			System.out.println("" + this.lvl.getBounds().contains(5, 5));
			System.out.println("" + this.lvl.getBounds().isEmpty());
			System.out.println("" + this.lvl.getBounds().isPolygonal());
			System.out.println("" + this.lvl.getBounds().isRectangular());
			this.clock = 0;
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

		g2d.drawImage(cam.getView(), r.x, r.y, null);
		/*
		BufferedImage bi = img1.getSubimage(xScroll, yScroll, xDim, yDim);
		g2d.drawImage(bi, 0, 0, null);
		*/
	} /* End method paint */

} /* End class Game */
