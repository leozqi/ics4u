package pong;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.geom.*;

/* Self-defined */
import static pong.Constants.*;

public class Game extends JPanel implements ActionListener {
	Timer renderer;
	boolean running = false;
	Random random = new Random();

	Paddle lpaddle;
	Paddle rpaddle;
	Ball ball;

	Font font = new Font("Courier", Font.BOLD, F_SIZE);
	Font titlef = new Font("Courier", Font.PLAIN, F_SIZE);

	int lScore = 0;
	int rScore = 0;

	JRadioButton lPSelect, lCSelect, rPSelect, rCSelect;
	JPanel storeMenu;

	public Game() {
		this.lpaddle = new Paddle();
		this.rpaddle = new Paddle();

		this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		this.setBackground(C_BACK);
		this.setFocusable(true);

		this.storeMenu = this.buildMenu();
		this.add(this.storeMenu, BorderLayout.CENTER);
	}

	private JPanel buildMenu() {
		JPanel menu = new JPanel();
		menu.setBackground(C_BACK);

		JLabel title = new JLabel("Pong!");
		JLabel lDescrip = new JLabel("Left side:");
		JLabel rDescrip = new JLabel("Right side:");
		title.setBackground(C_BACK);
		title.setForeground(C_FORE);
		lDescrip.setBackground(C_BACK);
		lDescrip.setForeground(C_FORE);
		rDescrip.setBackground(C_BACK);
		rDescrip.setForeground(C_FORE);

		ButtonGroup leftButtons = new ButtonGroup();
		lPSelect = new JRadioButton("Player");
		lCSelect = new JRadioButton("Computer");

		lPSelect.setBackground(C_BACK);
		lPSelect.setForeground(C_FORE);
		lCSelect.setBackground(C_BACK);
		lCSelect.setForeground(C_FORE);

		leftButtons.add(lPSelect);
		leftButtons.add(lCSelect);

		ButtonGroup rightButtons = new ButtonGroup();
		rPSelect = new JRadioButton("Player");
		rCSelect = new JRadioButton("Computer");

		rPSelect.setBackground(C_BACK);
		rPSelect.setForeground(C_FORE);
		rCSelect.setBackground(C_BACK);
		rCSelect.setForeground(C_FORE);

		rightButtons.add(rPSelect);
		rightButtons.add(rCSelect);

		JButton b = new JButton("Start!");
		
		b.setBackground(C_BACK);
		b.setForeground(C_FORE);
		b.addActionListener(this);

		GroupLayout l = new GroupLayout(menu);
		menu.setLayout(l);

		l.setAutoCreateGaps(true);
		l.setAutoCreateContainerGaps(true);

		l.setHorizontalGroup(
			l.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(title)
				.addGroup(l.createSequentialGroup()
					.addGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lDescrip)
						.addComponent(lPSelect)
						.addComponent(lCSelect))
					.addGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(rDescrip)
						.addComponent(rPSelect)
						.addComponent(rCSelect)))
				.addComponent(b)
		);

		l.setVerticalGroup(
			l.createSequentialGroup()
				.addComponent(title)
				.addGroup(l.createParallelGroup()
					.addGroup(l.createSequentialGroup()
						.addComponent(lDescrip)
						.addComponent(lPSelect)
						.addComponent(lCSelect))
					.addGroup(l.createSequentialGroup()
						.addComponent(rDescrip)
						.addComponent(rPSelect)
						.addComponent(rCSelect)))
				.addComponent(b)
		);

		return menu;
	}


	private void startLoop() {

		if (lPSelect.isSelected()) {
			this.lpaddle.setMode(Mode.PLAYER, HorizontalD.LEFT);
		} else if (lCSelect.isSelected()) {
			this.lpaddle.setMode(Mode.COMPUTER_SIMPLE, HorizontalD.LEFT);
		}

		if (rPSelect.isSelected()) {
			this.rpaddle.setMode(Mode.PLAYER, HorizontalD.RIGHT);
		} else if (rCSelect.isSelected()) {
			this.rpaddle.setMode(Mode.COMPUTER_SIMPLE, HorizontalD.RIGHT);
		}

		this.addKeyListener(this.lpaddle);
		this.addKeyListener(this.rpaddle);

		this.ball = new Ball();

		// Convert from frames per second to delay before each frame
		int msFrameDelay =  (int) (1000d / FRAMES_SEC);

		this.renderer = new Timer(msFrameDelay, this);

		this.running = true;
		renderer.start();

		this.remove(this.storeMenu);
	} /* End startLoop */


	private void newRound() {
		lpaddle.reset();
		rpaddle.reset();
		ball.reset();
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (!this.running) { return; }
		Graphics2D g2d = (Graphics2D) g;

		/* Render paddles */
		g2d.setColor(C_FORE);
		g2d.fill(this.lpaddle.getBounds());
		g2d.fill(this.rpaddle.getBounds());

		/* Render ball */
		g2d.fill(this.ball.getBounds());

		/* Render mid-section */
		for (int i = (M_SIZE / 2); i < S_HEIGHT; i += M_SIZE) {
			if ((i / M_SIZE) % 2 != 0) { continue; }
			g2d.fillRect(
				centreX() - (M_SIZE / 2), i - (M_SIZE / 2),
				M_SIZE, M_SIZE
			);
		}

		//g2d.draw(new Line2D.Double(centreX(), 0, centreX(), S_HEIGHT));

		/* Render scores */
		g2d.setFont(font);
		this.showScore(g2d, lScore, HorizontalD.LEFT);
		this.showScore(g2d, rScore, HorizontalD.RIGHT);
	}


	private void showScore(Graphics2D g, int score, HorizontalD side) {
		FontMetrics f = g.getFontMetrics();
		String s = String.format("%02d", score);

		Rectangle2D boundBox = f.getStringBounds(s, g);

		switch (side) {
		case LEFT:
			g.drawString(
				s,
				(float)(0 + (centreX() / 2) - (boundBox.getWidth() / 2)),
				(float)(0 + boundBox.getHeight() + F_PAD)
			);
			break;
		case RIGHT:
			g.drawString(
				s,
				(float)(S_WIDTH - (centreX() / 2) - (boundBox.getWidth() / 2)),
				(float)(0 + boundBox.getHeight() + F_PAD)
			);
			break;
		}
	}


	public void update() {
		Rectangle2D.Double ballPos = ball.getBounds();
		if (ballPos.getMaxX() >= S_WIDTH) {
			// lpaddle point
			lScore++;
			newRound();
		} else if (ballPos.getMinX() <= 0) {
			// rpaddle point
			rScore++;
			newRound();
		}


		ball.calcAngle(
			lpaddle.touching(ballPos),
			rpaddle.touching(ballPos),
			lpaddle.getBounds(),
			rpaddle.getBounds()
		);

		lpaddle.calculateDirection(ballPos);
		rpaddle.calculateDirection(ballPos);

		lpaddle.move(P_SPD);
		rpaddle.move(P_SPD);

		ball.move();
	}


	public void actionPerformed(ActionEvent e) {
		if (!this.running) {
			startLoop();
			return;
		}
		this.update();
		this.repaint();
	}
}
