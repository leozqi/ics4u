import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.*;

public class GameBase extends JPanel implements ActionListener, KeyListener {
	private Timer tm = new Timer(1, this); // Framerate

	private int x = 0, y = 0;
	private int velX = 0, velY = 0;
	private int size = 50;

	private int xSize = 500, ySize = 500;

	public GameBase() {
		tm.start();

		this.setPreferredSize(new Dimension(xSize, ySize));

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
	} /* End constructor */

	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.red);
		g.fillRect(this.x, this.y, size, size);
	}

	public void actionPerformed(ActionEvent e) {
		if (this.x < 0) { this.velX = 0; this.x = 0; }
		if (this.x > xSize-size) { this.velX = 0; this.x = xSize-size; }

		if (this.y < 0) { this.velY = 0; this.y = 0; }
		if (this.y < ySize-size) {this.velY = 0; this.y = ySize-size; }

		this.x = this.x + this.velX;
		this.y = this.y + this.velY;
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();//gets each code per key click

		if (c == KeyEvent.VK_LEFT) {//Left Arrow Key
			this.velX = -1;
			//this.velY = 0;
		}

		if (c ==KeyEvent.VK_RIGHT) {//Right arrow Key
			this.velX = 1;
			//this.velY = 0;
		}

		if (c == KeyEvent.VK_UP) {//Left Arrow Key
			//this.velX = 0;
			this.velY = -1;
		}

		if (c ==KeyEvent.VK_DOWN) {//Right arrow Key
			//this.velX = 0;
			this.velY = 1;
		}

	}

	public void keyTyped(KeyEvent e){


	}
	public void keyReleased(KeyEvent e) {
		this.velX = 0;
		this.velY = 0;
	}
}
