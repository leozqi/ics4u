import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

class PGraphics extends JComponent {
	private int width;
	private int height;

	public PGraphics(int w, int h) {
		width = w;
		height = h;
	}

	protected void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // turns g into a graphics2d obj.

		Ellipse2D.Double e1 = new Ellipse2D.Double(200, 75, 100, 100);
		g2d.setColor(Color.black);
		g2d.fill(e1);

		Rectangle2D.Double r = new Rectangle2D.Double(0, 0, 100, 100);
		g2d.setColor(new Color(30, 200, 30));
		g2d.fill(r);
	}
}

public class Main {
	public static void main(String[] args) {
		int w = 800;
		int h = 600;

		JFrame f = new JFrame();

		PGraphics dc = new PGraphics(800, 600);

		// Setup graphics.

		f.setSize(w, h);
		f.setTitle("First Drawing in Java");
		f.add(dc);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
