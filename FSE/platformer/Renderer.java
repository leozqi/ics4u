package platformer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Renderer extends JFrame {

SpriteHandler tileHandle;
BufferStrategy buffStrat;
Clock clock;
Level lvl;
BufferedImage img1;
int xScroll = 0;
int yScroll = 0;
boolean running = true;

public Renderer() {
	this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
	this.setPreferredSize(new Dimension(Settings.resX, Settings.resY));
	this.pack();
	this.setLocationRelativeTo(null);
	this.setVisible(true);

	// Create a general double-buffering strategy
	createBufferStrategy(3);
	buffStrat = this.getBufferStrategy();

	this.tileHandle = SpriteHandler.createFromFile(
		this, "/resources/tiles.png", Settings.internUnit,
		Settings.internSep, Settings.internSep
	);
	if (this.tileHandle == null) {
		System.out.println("Could not be laoded");
	}

	this.lvl = new Level("/resources/lvl1.lvl");
	if (this.lvl == null) {
		System.out.println("Level could not be loaded");
	}
	this.img1 = this.lvl.getLevel(this.tileHandle);

	this.clock = new Clock();
	this.clock.start();
} /* End constructor */


private class Clock extends Thread {

	@Override
	public void run() {
		long prevT = System.nanoTime();
		long currT;
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
	this.xScroll += diffT;
} /* End method update */


@Override
public void paint(Graphics g) {
	Graphics2D g2d;
	//super.paint(g);
	do {
		do {
			g2d = (Graphics2D) buffStrat.getDrawGraphics();
			g2d.drawImage(img1, 0-xScroll, yScroll, null);
			g2d.dispose();
		} while (buffStrat.contentsRestored());

		buffStrat.show();

	} while (buffStrat.contentsLost());
} /* End method paint */

} /* End class Renderer */
