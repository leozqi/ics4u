import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.geom.*;

public class Game extends JPanel implements ActionListener {

	/* Game constants */

	/* Coordinate system

	0 -------------- sWidth
	|
	|
	|
	|
	sHeight
	*/
	static final int S_WIDTH = 630, S_HEIGHT = 450;
	static final int B_SIZE = 15;
	static final int P_WIDTH = 15;
	static final int P_HEIGHT = 75;
	static final int P_SPACE = 30;
	static final int M_SIZE = 10;


	/* Game Loop:
	two different game "threads":
	one updates flight path of ball, etc.
	one repaints.
	*/
	static final double FRAMES_SEC = 1000;

	static final double B_SPD = 200d / FRAMES_SEC;
	static final double P_SPD = 200d / FRAMES_SEC; // 10 units per tick

	Color foreground = Color.white;
	Color background = Color.black;

	Timer renderer;
	boolean running;
	Random random = new Random();

	Paddle player;
	Paddle computer;
	Ball ball;

	public Game() {
		/*
		0
		| |
		| |
		| |
		*/
		this.player = new Paddle(
			true, HorizontalD.LEFT,
			0 + P_SPACE, this.centreY() - (P_HEIGHT/2),
			P_WIDTH, P_HEIGHT
		);

		this.computer = new Paddle(
			false, HorizontalD.RIGHT,
			S_WIDTH - P_SPACE - P_WIDTH, this.centreY() - (P_HEIGHT/2),
			P_WIDTH, P_HEIGHT
		);

		double traj = random.nextInt(20);
		if (random.nextBoolean()) {
			traj *= -1; // for negative trajectories
		}

		this.ball = new Ball(
			HorizontalD.LEFT,
			this.centreX() - B_SIZE, this.centreY() - B_SIZE,
			B_SIZE,
			traj
		);

		int msFrameDelay =  (int) (1000d / FRAMES_SEC);

		this.renderer = new Timer(msFrameDelay, this);

		this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		this.setBackground(this.background);
		this.setFocusable(true);
		this.addKeyListener(new Controller());

		startLoop();
	} /* End constructor */

	private int centreX() { return (S_WIDTH / 2) + 1; }
	private int centreY() { return (S_HEIGHT / 2) + 1; }

	private void startLoop() {
		this.running = true;
		renderer.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		/* Render paddles */
		g2d.setColor(this.foreground);
		g2d.fill(this.player.getBounds());
		g2d.fill(this.computer.getBounds());

		/* Render ball */
		g2d.fill(this.ball.getBounds());

		/* Render mid-section */
		for (int i = (M_SIZE / 2); i < S_HEIGHT; i += M_SIZE) {
			if ((i / M_SIZE) % 4 != 0) { continue; }
			g2d.fillRect(
				centreX() - (M_SIZE / 2), i - (M_SIZE / 2),
				M_SIZE, M_SIZE
			);
		}
	}

	public void update() {
		Rectangle2D.Double ballPos = ball.getBounds();
		// Move paddles
		if (player.touching(ballPos)
			|| computer.touching(ballPos)
			|| ballPos.getMaxY() >= S_WIDTH
			|| ballPos.getMinY() <= 0) {
			ball.calcTraj();
		}

		player.move(P_SPD);

		Rectangle2D.Double compPos = computer.getBounds();

		if (ballPos.getCenterY() > compPos.getCenterY()) {
			computer.changeDirection(VerticalD.DOWN);
		} else if (ballPos.getCenterY() < compPos.getCenterY()) {
			computer.changeDirection(VerticalD.UP);
		} else {
			computer.changeDirection(VerticalD.NEUTRAL);
		}
		computer.move(P_SPD);

		ball.move(B_SPD);
	}

	public void actionPerformed(ActionEvent e) {
		if (!this.running) { return; }
		this.update();
		this.repaint();
	}

	private class Controller extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode(); // gets the key that was keyPressed

			switch (code) {
			case KeyEvent.VK_UP:
				player.changeDirection(VerticalD.UP);
				break;
			case KeyEvent.VK_DOWN:
				player.changeDirection(VerticalD.DOWN);
				break;
			}
		}

		public void keyReleased(KeyEvent e) {
			player.changeDirection(VerticalD.NEUTRAL);
		}
	}
}
