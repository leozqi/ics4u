import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.*;

public class PaintApp {
	public static void main(String[] args) {
		JFrame j = new JFrame();

		j.setTitle("Paint 1D");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setSize(800, 600);

		Canvas c = new Canvas(800, 600);

		j.add(c);
		j.pack();

		j.setVisible(true);
	}
}

class Canvas
extends JPanel
implements KeyListener, MouseMotionListener, MouseListener {
	int xSize, ySize;

	RenderingHints renderHints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON
	);

	public enum Stamp {
		CIRCLE,
		SQUARE
	}

	private Stamp mode;


	public Canvas(int xSize, int ySize) {
		super();

		this.xSize = xSize;
		this.ySize = ySize;

		this.setVisible(true);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.setPreferredSize(new Dimension(this.xSize, this.ySize));
		this.addKeyListener(this);

		//this.setBorder(BorderFactory.createLineBorder(Color.black, 10));

		this.mode = Stamp.CIRCLE;
	}


	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHints(this.renderHints);

		Rectangle2D rectRed = new Rectangle2D.Double(0, 0, 50, 50);
		Rectangle2D rectBlue = new Rectangle2D.Double(50, 0, 50, 50);
		Rectangle2D rectGreen = new Rectangle2D.Double(100, 0, 50, 50);

		g2d.setColor(Color.red); g2d.fill(rectRed);
		g2d.setColor(Color.blue); g2d.fill(rectBlue);
		g2d.setColor(Color.green); g2d.fill(rectGreen);
	}

	@Override
	public void keyPressed(KeyEvent e)  {
		int c = e.getKeyCode();

		switch (c) {
			case KeyEvent.VK_LEFT:
				this.mode = Stamp.SQUARE; break;
			case KeyEvent.VK_RIGHT:
				this.mode = Stamp.CIRCLE; break;
		}

		e.consume();
	}

	@Override public void keyTyped(KeyEvent e)    {}
	@Override public void keyReleased(KeyEvent e) {}

	@Override public void mousePressed(MouseEvent e)  {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseDragged(MouseEvent e)  {}
	@Override public void mouseMoved(MouseEvent e)    {}
	@Override public void mouseExited(MouseEvent e)   {}
	@Override public void mouseEntered(MouseEvent e)  {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() >= 0 && e.getY() <= 49) {
			this.setBackground(Color.red);
		} else if (e.getX() >= 50 && e.getY() <= 99) {
			this.setBackground(Color.blue);
		} else if (e.getX() >= 100 && e.getY() <= 149) {
			this.setBackground(Color.green);
		}
	}
}
