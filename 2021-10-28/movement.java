import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.*;

public class movement extends JPanel implements ActionListener, KeyListener {

	Timer tm = new Timer (1,this);//this this is for action listener
	int x=0, y=0,velX =1, velY=0;

	public movement() {
		tm.start();
		addKeyListener(this);//this refers to keylistener and tells the program to watch for keys
		setFocusable(true);//Enables keyListener
		setFocusTraversalKeysEnabled(false);//means we won't use shift or tab keys

	}


	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.red);
		g.fillRect(x, y, 50, 50);
	}

	public void actionPerformed(ActionEvent e) {
		if (x < 0) {
			velX = 0;
			x = 0;
		}

		if (x > 500) {
			velX = 0;
			x = 500;
		}

		if (y < 0) {
			velY = 0;
			y = 0;
		}

		if (y > 400) {
			velY = 0;
			y = 400;
		}

		x = x + velX;
		y = y + velY;//pressing keys will increase these values later
		repaint();



	}

	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();//gets each code per key click

		if (c == KeyEvent.VK_LEFT) {//Left Arrow Key
			velX = -1;
			velY = 0;
		}

		if (c ==KeyEvent.VK_RIGHT) {//Right arrow Key
			velX = 1;
			velY = 0;
		}

		if (c == KeyEvent.VK_UP) {//Left Arrow Key
			velX = 0;
			velY = -1;
		}

		if (c ==KeyEvent.VK_DOWN) {//Right arrow Key
			velX = 0;
			velY = 1;
		}

	}

	public void keyTyped(KeyEvent e){


	}
	public void keyReleased(KeyEvent e) {
		//velX = 0;
		//velY = 0;
	}

}
