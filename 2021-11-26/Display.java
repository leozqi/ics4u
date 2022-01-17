import java.awt.*;


public class Display extends JPanel implements ActionListener {
	public Sentinent player;
	public Image img;
	Timer time;

	
	public Display() {
		player = new Sentinent();

		addKeyListener(new AL());

		setFocusable(true);
		ImageIcon i = new ImageIcon("bg.jpg");
		img = i.getImage();
		time = new Timer(5, this);
		time.start();
	}


	public void actionPerformed(ActionEvent e) {
		player.move();
		repaint();
	}

	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(img, 0, 0, null); // Draw background
		g2d.drawImage(player.getImage(), player.getX(), player.getY(), null);
	}
}
