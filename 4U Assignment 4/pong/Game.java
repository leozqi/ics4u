package pong;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.geom.*;

/* Self-defined */
import static pong.Constants.*;

public class Game extends JPanel implements ActionListener {
	/* Set at start */
	Random randGen = new Random();

	Font dFont = new Font("Courier", Font.BOLD, F_SIZE);
	Font sFont = new Font("Courier", Font.BOLD, F_SIZE / 5);

	/* Changed every new game */
	static final int MS_FRAME_DELAY = (int)(1000d / FRAMES_SEC);
	Timer renderer = new Timer(MS_FRAME_DELAY, this);

	boolean running = false;

	Paddle lPaddle = new Paddle();
	Paddle rPaddle = new Paddle();
	Ball ball = new Ball();

	int lScore = 0;
	int rScore = 0;

	/* Graphics widgets */
	ArrayList<JRadioButton> lButtons = new ArrayList<JRadioButton>();
	ArrayList<JRadioButton> rButtons = new ArrayList<JRadioButton>();

	JPanel menu;
	JLabel mDescrip;


	public Game() {
		this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		this.setFocusable(true);

		/* First time setup */
		this.menu = this.buildMenu(" ");
		this.menu.setVisible(true);
		this.add(this.menu);
	} /* End constructor */


	private JPanel buildMenu(String msg) {
		/*
		 * ---------------------------
		 * |                         |
		 * |          Pong           |
		 * |                         |
		 * |  Left side  Right side  |
		 * |  () Player  () Player   |
		 * |  () . . .   () . . .    |
		 * |                         |
		 * ---------------------------
		 */
		JPanel retMenu = new JPanel();
		JLabel mTitle  = new JLabel("Pong!");

		mDescrip = new JLabel(msg);

		JLabel lDescrip = new JLabel("Left side:");
		JLabel rDescrip = new JLabel("Right side:");

		JButton mStart  = new JButton("Start!");
		mStart.setActionCommand("start");
		mStart.addActionListener(this);

		GroupLayout lay = new GroupLayout(retMenu);
		retMenu.setLayout(lay);
		lay.setAutoCreateGaps(true);
		lay.setAutoCreateContainerGaps(true);

		GroupLayout.ParallelGroup lHBGrp = lay.createParallelGroup(
			GroupLayout.Alignment.LEADING
		);
		GroupLayout.SequentialGroup lVBGrp = lay.createSequentialGroup();

		GroupLayout.ParallelGroup rHBGrp = lay.createParallelGroup(
			GroupLayout.Alignment.LEADING
		);
		GroupLayout.SequentialGroup rVBGrp = lay.createSequentialGroup();

		lHBGrp.addComponent(lDescrip);
		lVBGrp.addComponent(lDescrip);
		rHBGrp.addComponent(rDescrip);
		rVBGrp.addComponent(rDescrip);

		ButtonGroup lButtonGrp = new ButtonGroup();
		ButtonGroup rButtonGrp = new ButtonGroup();

		int count = 0;
		JRadioButton lB;
		JRadioButton rB;
		// Left buttons and right buttons
		for (Mode m : Mode.values()) {
			lB = new JRadioButton(m.toString());
			rB = new JRadioButton(m.toString());

			if (count == 0) {
				lB.setSelected(true);
				rB.setSelected(true);
			}

			lButtons.add(lB);
			rButtons.add(rB);

			lButtonGrp.add(lB);
			rButtonGrp.add(rB);

			lHBGrp.addComponent(lB);
			lVBGrp.addComponent(lB);

			rHBGrp.addComponent(rB);
			rVBGrp.addComponent(rB);

			count++;
		}

		lay.setHorizontalGroup(
			lay.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(mTitle)
				.addComponent(mDescrip)
				.addGroup(lay.createSequentialGroup()
					.addGroup(lHBGrp)
					.addGroup(rHBGrp)
				)
				.addComponent(mStart)
		);

		lay.setVerticalGroup(
			lay.createSequentialGroup()
				.addComponent(mTitle)
				.addComponent(mDescrip)
				.addGroup(lay.createParallelGroup()
					.addGroup(lVBGrp)
					.addGroup(rVBGrp)
				)
			.addComponent(mStart)
		);
		return retMenu;
	} /* End method buildMenu */


	private void newGame(HorizontalD winner) {
		this.renderer.stop();
		this.running = false;
		switch (winner) {
		case LEFT:
			this.mDescrip.setText(
				lPaddle.getName() + " won the last round!"
			);
			break;
		case RIGHT:
			this.mDescrip.setText(
				rPaddle.getName() + " won the last round!"
			);
			break;
		}
		this.menu.setVisible(true);
	} /* End method newGame */


	private void startGame() {
		this.lScore = 0;
		this.rScore = 0;

		int count = 0;
		JRadioButton lB;
		JRadioButton rB;
		for (Mode m : Mode.values()) {
			lB = this.lButtons.get(count);
			rB = this.rButtons.get(count);

			if (lB.isSelected()) {
				this.lPaddle.setMode(m, HorizontalD.LEFT);
				if (m == Mode.PLAYER) {
					this.addKeyListener(this.lPaddle);
				}
			} else if (rB.isSelected()) {
				this.rPaddle.setMode(m, HorizontalD.RIGHT);
				if (m == Mode.PLAYER) {
					this.addKeyListener(this.rPaddle);
				}
			}
			count++;
		}

		this.ball.reset();
		this.running = true;
		this.renderer.start();
		this.menu.setVisible(false);
	} /* End method startGame */


	private void newRound() {
		lPaddle.reset();
		rPaddle.reset();
		ball.reset();
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (!this.running) { return; }
		Graphics2D g2d = (Graphics2D) g;

		/* Render paddles */
		g2d.setColor(C_FORE);
		g2d.fill(this.lPaddle.getBounds());
		g2d.fill(this.rPaddle.getBounds());

		/* Render ball */
		g2d.fill(this.ball.getBounds());

		/* Render mid-section */
		for (int i = (M_SIZE / 2); i < S_HEIGHT; i += M_SIZE) {
			if ((i / M_SIZE) % 2 != 0) { continue; }
			g2d.fillRect(
				S_CENTRE_X - (M_SIZE / 2), i - (M_SIZE / 2),
				M_SIZE, M_SIZE
			);
		}

		/* Render scores */
		g2d.setFont(dFont);
		this.showMsg(
			g2d, String.format("%02d", lScore),
			HorizontalD.LEFT, VerticalD.UP
		);
		this.showMsg(
			g2d, String.format("%02d", rScore),
			HorizontalD.RIGHT, VerticalD.UP
		);

		g2d.setFont(sFont);
		this.showMsg(
			g2d, lPaddle.getName(),
			HorizontalD.LEFT, VerticalD.DOWN
		);
		this.showMsg(
			g2d, rPaddle.getName(),
			HorizontalD.RIGHT, VerticalD.DOWN
		);
	}


	private void showMsg(Graphics2D g, String msg, HorizontalD hFace, VerticalD vFace) {
		FontMetrics f = g.getFontMetrics();
		Rectangle2D boundBox = f.getStringBounds(msg, g);

		float yOffset = 0;
		switch (vFace) {
		case UP:
			yOffset = (float)(0 + boundBox.getHeight() + F_PAD);
			break;
		case DOWN:
			yOffset = (float)(S_HEIGHT - boundBox.getHeight() - F_PAD);
			break;
		}

		float xOffset = 0;
		switch (hFace) {
		case LEFT:
			xOffset = (float)(
				0 + (S_CENTRE_X / 2)
				- (boundBox.getWidth() / 2)
			);
			break;
		case RIGHT:
			xOffset = (float)(
				S_WIDTH - (S_CENTRE_X / 2)
				- (boundBox.getWidth() / 2)
			);
			break;
		}
		g.drawString(msg, xOffset, yOffset);
	} /* End method showMsg */


	public void update() {
		if (lScore >= 10) {
			this.newGame(HorizontalD.LEFT);
			return;
		} else if (rScore >= 10) {
			this.newGame(HorizontalD.RIGHT);
			return;
		}

		Rectangle2D.Double ballPos = ball.getBounds();
		if (ballPos.getMaxX() >= S_WIDTH) {
			// lPaddle point
			this.lScore++;
			this.newRound();
		} else if (ballPos.getMinX() <= 0) {
			// rPaddle point
			this.rScore++;
			this.newRound();
		}

		boolean lTouching = lPaddle.touching(ballPos);
		boolean rTouching = rPaddle.touching(ballPos);

		CalcObj calc = ball.calcAngle(
			lTouching,
			rTouching,
			lPaddle.getBounds(),
			rPaddle.getBounds()
		);

		lPaddle.calcDirection(ballPos, rPaddle.getBounds(), rTouching, calc);
		rPaddle.calcDirection(ballPos, lPaddle.getBounds(), lTouching, calc);

		lPaddle.move(P_SPD);
		rPaddle.move(P_SPD);

		ball.move();
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "start") {
			startGame();
			return;
		} else if (this.running) {
			this.update();
			this.repaint();
		}
	}
}
