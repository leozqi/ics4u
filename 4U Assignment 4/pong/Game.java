package pong;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.awt.geom.*;

/* Constants are defined here: */
import static pong.Constants.*;


public class Game extends JPanel implements ActionListener {
	/* Objects set at start */
	Random randGen = new Random();

	// Create a default font (Courier typeface)
	Font dFont = new Font("Courier", Font.BOLD, F_SIZE);

	// Create a small font (Courier typeface)
	Font sFont = new Font("Courier", Font.BOLD, F_SIZE / 5);

	/* Game Timer */
	// Convert FPS to frame delay in milleseconds.
	// Every second is a thousand milliseconds, and frames per second is
	// the reciprocal of seconds per frame.
	// Therefore divide 1000 by FPS to convert to delay per frame.
	static final int MS_FRAME_DELAY = (int)(1000d / FRAMES_SEC);

	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

	// Tells when the game is running. Is false when the game is showing the
	// start menu
	boolean running = false;

	/* Create game objects */
	Paddle lPaddle = new Paddle();
	Paddle rPaddle = new Paddle();
	Ball ball = new Ball();

	/* Player scores */
	int lScore = 0;
	int rScore = 0;

	/* Graphics widgets */
	// Hold all the buttons we make for the menu (see `buildMenu()`)
	// Left side: lButtons
	// Right side: rButtons
	ArrayList<JRadioButton> lButtons = new ArrayList<JRadioButton>();
	ArrayList<JRadioButton> rButtons = new ArrayList<JRadioButton>();

	JPanel menu;     // JPanel menu inside a menu.
	JLabel mDescrip; // Description of who won


	/**
	 * The Game class extends the JPanel class to render the Pong game.
	 * The Game class operates in two phases.
	 * 1. Renders a menu with Swing buttons to select what each paddle will
	 *    play as (PC or human player) and a start button to begin the game.
	 *
	 * 2. Renders the actual game with the paint() method.
	 *
	 * Once a player wins a game, the Game class will "reshow" the menu to
	 * play again.
	 */
	public Game() {
		/* JPanel configuration */
		// Set a new size:
		this.setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
		this.setFocusable(true);

		/* First time setup */
		// Create a menu with Swing components with a first time msg:
		this.menu = this.buildMenu("Pick each paddle's player below");
		this.menu.setVisible(true);
		this.add(this.menu);
	} /* End constructor */


	/**
	 * Builds a start menu with Swing components (Radiobuttons and labels).
	 * 
	 * This menu is used to select what each of the two paddles' mode is.
	 * A paddle can be a (basic) CPU player or a human player.
	 *
	 * The menu is also used as a "start menu" where you can press the main
	 * button to start the game.
	 *
	 * The final menu looks like the below:
	 * ---------------------------
	 * |                         |
	 * |          Pong!          |
	 * |                         |
	 * |  Left side  Right side  |
	 * |  () Player  () Player   |
	 * |  () CPU     () CPU      |
	 * |  () . . .   () . . .    |
	 * |                         |
	 * |          Start!         |
	 * |                         |
	 * ---------------------------
	 */
	private JPanel buildMenu(String msg) {
		JPanel retMenu = new JPanel();        // Menu to return is in a JPanel

		JLabel mTitle  = new JLabel("Pong!"); // Title
		mDescrip = new JLabel(msg);           // Shows win message

		// Labels for "Left side" and "Right side" selection buttons.
		JLabel lDescrip = new JLabel("Left side:");
		JLabel rDescrip = new JLabel("Right side:");

		// Start button
		JButton mStart  = new JButton("Start!");
		// the Game class listens for this ActionCommand
		mStart.setActionCommand("start");
		mStart.addActionListener(this);

		// A Java layout arranges the labels and buttons on the menu.
		// The Swing GroupLayout is the most flexible and easy to use
		// for this (moderately) complicated layout:
		// https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
		//
		// The GroupLayout works by arranging elements based on their
		// specified positions in TWO separate layouts, one vertical
		// and one horizontal.
		//
		// Create a GroupLayout:
		GroupLayout lay = new GroupLayout(retMenu);
		retMenu.setLayout(lay);

		lay.setAutoCreateGaps(true);           // Gaps between elements
		lay.setAutoCreateContainerGaps(true);  // Gaps between containers

		// Create a LeftHorizontalButtonGroup
		GroupLayout.ParallelGroup lHBGrp = lay.createParallelGroup(
			GroupLayout.Alignment.LEADING
		);
		// Create a LeftVerticalButtonGroup
		GroupLayout.SequentialGroup lVBGrp = lay.createSequentialGroup();

		// Create a RightHorizontalButtonGroup
		GroupLayout.ParallelGroup rHBGrp = lay.createParallelGroup(
			GroupLayout.Alignment.LEADING
		);
		// Create a RightVerticalButtonGroup
		GroupLayout.SequentialGroup rVBGrp = lay.createSequentialGroup();

		// Add the left description in the left horizontal AND vertical
		// groups:
		lHBGrp.addComponent(lDescrip);
		lVBGrp.addComponent(lDescrip);

		// Do the same for the right description:
		rHBGrp.addComponent(rDescrip);
		rVBGrp.addComponent(rDescrip);

		// Create two button groups for the RadioButtons to make sure
		// only one of each group can be selected at a time.
		//
		// A paddle cannot both be for a human player and a CPU.
		ButtonGroup lButtonGrp = new ButtonGroup();
		ButtonGroup rButtonGrp = new ButtonGroup();

		// `count` is used to select two buttons to be on by default.
		// Otherwise, a paddle may have NO modes, which is a NO NO. :)
		int count = 0;
		JRadioButton lB; // Store the left button (temporarily)
		JRadioButton rB; // Store the right button

		// Iterate over all possible modes (defined in Mode enum)
		// An enum can be iterated over like an array with the values()
		// builtin method.
		for (Mode m : Mode.values()) {
			lB = new JRadioButton(m.toString());
			rB = new JRadioButton(m.toString());

			// First mode will be default mode
			if (count == 0) {
				lB.setSelected(true);
				rB.setSelected(true);
			}

			// Add button to ArrayList to retrieve values later.
			lButtons.add(lB);
			rButtons.add(rB);

			// Add button to button groups so only one may be selected
			// for each group
			lButtonGrp.add(lB);
			rButtonGrp.add(rB);

			// Add buttons to GUI layouts
			lHBGrp.addComponent(lB);
			lVBGrp.addComponent(lB);
			rHBGrp.addComponent(rB);
			rVBGrp.addComponent(rB);

			count++;
		}

		// The horizontal group can be visualized like this:
		// lay.createParallelGroup() (are all parallel horizontally)
		// |-----------------------------|
		// | mTitle                      |
		// |-----------------------------|
		// | mDescrip                    | lay.createSequentialGroup()
		// |-----------------------------| (are all sequential horizontally)
		// | lHBGrp       | rHBGrp       |
		// |-----------------------------|
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

		// The vertical group can be visualized like this:
		// lay.createSequentialGroup() (are all sequential vertically)
		// |-----------------------------|
		// | lHBGrp       | rHBGrp       | lay.createParallelGroup()
		// |-----------------------------| (are all parallel vertically)
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
		return retMenu; // Return the built menu
	} /* End method buildMenu */


	/**
	 * End the current game and starts a new game by reshowing the menu and
	 * displaying a win message for the winner of the previous round.
	 *
	 * @param winner the HorizontalD direction of the winner (LEFT or RIGHT)
	 */
	private void newGame(HorizontalD winner) {
		// Stop the render timer
		this.exec.shutdownNow();
		this.running = false;

		// Display the correct winner message
		// mDescrip is the label showing the message
		switch (winner) {
		case LEFT:
			// The Paddle toString method gives the type of player
			// and its side, ex: "Simple CPU: Left"
			this.mDescrip.setText(
				lPaddle.toString() + " won the last round!"
			);
			break;
		case RIGHT:
			this.mDescrip.setText(
				rPaddle.toString() + " won the last round!"
			);
			break;
		}
		// Show the menu
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
			}
			if (rB.isSelected()) {
				this.rPaddle.setMode(m, HorizontalD.RIGHT);
				if (m == Mode.PLAYER) {
					this.addKeyListener(this.rPaddle);
				}
			}
			count++;
		}

		this.ball.reset();
		this.running = true;

		/*
		this.exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				update();
			}
		}, 0, MS_FRAME_DELAY, TimeUnit.MILLISECONDS);
		*/

		Thread appThread = new Thread() {
			public void run() {
				long lastTime = System.nanoTime();
				long nowTime;
				double diff;
				double nanos = 1000000000/FRAMES_SEC;
				while (true) {
					nowTime = System.nanoTime();

					diff = (nowTime - lastTime)/nanos; // time in seconds passed
					update(diff);
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								repaint();
							}
						});
					} catch (Exception e) {}
					lastTime = nowTime;
				}
			}
		};
		appThread.start();
/*
		this.exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				//update();
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							repaint();
						}
					});
				} catch (Exception e) {}
			}
		}, 0, MS_FRAME_DELAY, TimeUnit.MILLISECONDS);
*/
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
			g2d, lPaddle.toString(),
			HorizontalD.LEFT, VerticalD.DOWN
		);
		this.showMsg(
			g2d, rPaddle.toString(),
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


	public void update(double secPass) {
		/* Check score */
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

		lPaddle.move(secPass);
		rPaddle.move(secPass);

		ball.move(secPass);
	}


	/**
	 * Constantly listen to events from the start menu or from the timer.
	 * If the event is a timer event, update the game screen. If it is a
	 * button event, start the game. Extension of the ActionListener class.
	 *
	 * @param e an event received (only relevant if it is the start button).
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "start") {
			startGame();
			return;
		} else if (this.running) {
			//this.update();
			this.repaint();
		}
	} /* End method actionPerformed */
}
