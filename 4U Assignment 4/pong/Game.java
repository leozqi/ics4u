/*
 * Game.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "Game" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "Game" class extends the JPanel class to actually render the Pong game.
 */


package pong;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.geom.*;

/* Constants are defined here: */
import static pong.Constants.*;


/**
 * The Game class extends the JPanel class to render the Pong game.
 *
 * The Game class operates in two phases.
 * 1. Renders a menu with Swing buttons to select what each paddle will
 *    play as (PC or human player) and a start button to begin the game. In this
 *    phase, the game is NOT running (this.running field is false).
 *
 * 2. Renders the actual game with the paint() method until a match is over.
 *    Matches are up to 10 points or whatever is defined in the G_POINTS constant.
 *    In this phase, the game IS running.
 *
 * Additionally, there is a third state, "paused". The game can only be paused
 * when in the second phase. When a player wins a round, the game's elements
 * will be frozen for G_PAUSE seconds before a new round begins to give both
 * players time to prepare. Whether or not a game is paused is given by the
 * this.paused field, while the pause() method description has the details on
 * how pausing works.
 *
 * Once a player wins a game, the Game class will "reshow" the menu to
 * play again.
 */
public class Game extends JPanel implements ActionListener {
	/* Objects set at start */
	Random randGen = new Random();

	// Create a default font (Courier typeface)
	Font dFont = new Font("Courier", Font.BOLD, F_SIZE);

	// Create a small font (Courier typeface)
	Font sFont = new Font("Courier", Font.BOLD, F_SIZE / 5);

	// Stores when the game is running. Is false when the game is showing the
	// start menu
	private volatile boolean running = false;

	// Updates and redraws game in separate thread. See Renderer class below.
	private Renderer renderer;

	// Pauser pauses the game in between rounds. See the pause() method.
	private Timer pauser = new Timer(G_PAUSE * 1000, this);

	// Stores whether or not the game is paused.
	private volatile boolean paused = false;

	/* Create game objects */
	private volatile Paddle lPaddle = new Paddle();
	private volatile Paddle rPaddle = new Paddle();
	private volatile Ball ball = new Ball();

	/* Player scores */
	private volatile int lScore = 0;
	private volatile int rScore = 0;

	/* Graphics widgets */
	// Hold all the buttons we make for the menu (see `buildMenu()`)
	// Left side: lButtons
	// Right side: rButtons
	ArrayList<JRadioButton> lButtons = new ArrayList<JRadioButton>();
	ArrayList<JRadioButton> rButtons = new ArrayList<JRadioButton>();

	JPanel menu;     // JPanel menu inside a menu.
	JLabel mDescrip; // Description of who won


	/**
	 * Initialize the game and show the start menu.
	 *
	 * Sets up the pause timer after each round and the dimensions of the
	 * game, and builds and shows the start menu.
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

		this.pauser.setRepeats(false);
		this.pauser.setActionCommand("unpause");
	} /* End constructor */


	/**
	 * Builds a start menu with Swing components (Radiobuttons, buttons,
	 * and labels).
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
		// Doc used: https://docs.oracle.com/javase/8/docs/api/javax/swing/JButton.html
		JButton mStart  = new JButton("Start!");
		// the Game class listens for this ActionCommand to start the game
		mStart.setActionCommand("start");
		mStart.addActionListener(this);

		// A Java layout arranges the labels and buttons on the menu.
		// The Swing GroupLayout is the most flexible and easy to use:
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
	 * End the current game and creates a new game by reshowing the menu and
	 * displaying a win message for the winner of the previous round.
	 *
	 * This method does not START the new game; this is done by the startGame
	 * method which is triggered by the mStart (menu start) button shown
	 * with the menu.
	 *
	 * @param winner the HorizontalD direction of the winner (LEFT or RIGHT)
	 */
	private void newGame(HorizontalD winner) {
		// Stop the renderer by interrupting the thread
		// This way, unfortunately, is the surest way of making sure
		// the thread stops. This should be safe because the thread
		// catches interrupted exceptions.
		this.renderer.interrupt();

		// Mark the renderer as null so that the thread may be garbage
		// collected before a new thread is made once a new game is started.
		this.renderer = null; 

		// The game is no longer running because the menu is being shown.
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


	/**
	 * Starts a game created by `newGame`.
	 *
	 * This method starts a new game by:
	 * 1. Resetting player scores
	 * 2. Setting each paddle to its correct mode based on the selected
	 *    RadioButtons.
	 * 3. Creating a new Renderer object (thread) to render the game.
	 */
	private void startGame() {
		// Reset player scores
		this.lScore = 0;
		this.rScore = 0;

		// Iterate over the ArrayLists holding the left and right
		// side radiobuttons with `count`.
		int count = 0;

		JRadioButton lB; // Temporarily hold left radiobutton
		JRadioButton rB; // Temporarily hold right radiobutton

		// Iterate over all possible modes (this is how the menu's
		// options are created).
		for (Mode m : Mode.values()) {
			lB = this.lButtons.get(count);
			rB = this.rButtons.get(count);

			if (lB.isSelected()) {
				// If left button is selected:
				// Set the left paddle to the current mode
				this.lPaddle.setMode(m, HorizontalD.LEFT);
				if (m == Mode.PLAYER) {
					// If the mode is human, add the Paddle
					// as a keylistener
					this.addKeyListener(this.lPaddle);
				}
			}
			if (rB.isSelected()) {
				// If right button is selected:
				// Do the same as the left button, but for
				// the right paddle (rPaddle)
				this.rPaddle.setMode(m, HorizontalD.RIGHT);
				if (m == Mode.PLAYER) {
					this.addKeyListener(this.rPaddle);
				}
			}
			count++;
		}

		// Reset the ball's position before starting the game
		this.ball.reset();

		// Begin the game by setting running to true.
		this.running = true;

		// Create a new renderer to update and repaint game elements
		// (paddles and ball)
		// The Renderer class is a thread with a while loop that runs
		// independently.
		this.renderer = new Renderer();
		this.renderer.start();

		// Hide the menu
		this.menu.setVisible(false);

		// Pause the game at start for moment to prepare
		this.pause();
	} /* End method startGame */


	/**
	 * Provides a separate thread to constantly update and display the
	 * Pong game elements.
	 *
	 * Renderer provides a thread that if started will update and repaint
	 * the positions of the paddles, ball, and all other displayed elements.
	 *
	 * This thread's calculations are based on the amount of nanoseconds
	 * passed each loop. By dividing the change in time against NANOS, the
	 * amount of nanoseconds that SHOULD be passed for each frame or each
	 * 'tick', the method arrives at a value diffT.
	 *
	 * `diffT` represents the decimal "percentage" of time passed compared
	 * to how much it SHOULD have passed. A diffT of 1 means that exactly
	 * as much time passed as was supposed to; a diffT of 0.1 means 10% of
	 * the time has passed. This value is then passed to an Updater, which
	 * does the actual updating with the `update` and `repaint` methods.
	 *
	 * For example, if the diffT was 0.3, 30% of the time has passed since
	 * the function was called, so the Updater will move the ball 30% of
	 * the distance it should go (based on frames per second). This creates
	 * smoother movement.
	 */
	private class Renderer extends Thread {
		/**
		 * Starts the Renderer.
		 */
		@Override
		public void run() {
			long prevT = System.nanoTime(); // Previous time in ns
			long currT;                     // Current time in ns

			// "Difference" in time; see class description.
			double diffT;

			// While the game is running and the thread has not been interrupted
			// (The game is running condition is mostly a fallback)
			while (running && (!this.isInterrupted())) {
				currT = System.nanoTime();       // Get current time

				// Calculate the time passed; divide by
				// NANOS to get decimal of how much time passed
				// compared to time that SHOULD be passed for
				// speed values to equal B_SPD and P_SPD
				diffT = (currT - prevT) / NANOS;

				// Current time is next loop's previous time
				prevT = currT;

				try {
				// Invoke in "Event-dispatch thread" because
				// GUI elements will be modified:

				// https://docs.oracle.com/javase/8/docs/api/javax/swing/SwingUtilities.html

				// This method normally takes a Runnable, so
				// the Updater class simply implements a Runnable

				// Use invokeAndWait so that not too many new
				// draws and updates are submitted at once
				// (There are NO other waits or limits to how
				// many updates there are every second, etc
				// besides this)
				SwingUtilities.invokeAndWait(new Updater(diffT));
				} catch (Exception e) {
					// Interrupted or some other error
					// Continue the loop
					continue;
				}
			}
		} /* End overridden method run */
	} /* End class Renderer extends Thread */


	/**
	 * Runnable object that when run updates and repaints the game.
	 *
	 * This class is meant to be used with SwingUtilities.invoke . . . to
	 * easily do two steps:
	 *
	 * 1. Update the positions of the paddles and ball according to the
	 *    "difference in time."
	 * 2. Repaint all the game elements.
	 */
	private class Updater implements Runnable {
		private double diffT;


		/**
		 * Creates a new Updater
		 *
		 * @param diffT the "difference in time"; see the Renderer
		 *              class description.
		 */
		public Updater(double diffT) {
			super();
			this.diffT = diffT;
		} /* End default constructor */


		/**
		 * Update and repaint.
		 */
		public void run() {
			// Run the Game object's update method to update the
			// positions of all game elements.
			update(diffT);

			// Run the Game object (a JPanel)'s repaint method to
			// redraw all game elements
			repaint();
		} /* End implemented method run */
	} /* End class Updater */


	/**
	 * Sets up a new round.
	 *
	 * Resets the positions of both paddles and the ball to start a new
	 * round.
	 */
	private void newRound() {
		lPaddle.reset();
		rPaddle.reset();
		ball.reset();
	} /* End method newRound */


	/**
	 * Paints all game elements onto the screen.
	 *
	 * This method is called with the `repaint` method for this Pong game.
	 * It does not update the positions of the shapes 
	 *
	 * @param g the graphics object to use for the painting.
	 */
	@Override
	public void paint(Graphics g) {
		// Paint default objects
		super.paint(g);

		// If the game is not running, do not continue
		if (!this.running) { return; }
		Graphics2D g2d = (Graphics2D) g;

		/* Render paddles */

		g2d.setColor(C_FORE);               // Change to foreground colour
		g2d.fill(this.lPaddle.getBounds()); // Fill paddles
		g2d.fill(this.rPaddle.getBounds());

		/* Render ball */

		g2d.fill(this.ball.getBounds());

		/* Render mid-section */

		this.showMidline(g2d);

		/* Render text */

		g2d.setFont(dFont); // Set the font to the default

		// Show left player's score
		this.showMsg(
			g2d, String.format("%02d", lScore), // Pad scores to 00
			HorizontalD.LEFT, VerticalD.UP
		);
		// Show right player's score
		this.showMsg(
			g2d, String.format("%02d", rScore),
			HorizontalD.RIGHT, VerticalD.UP
		);

		// Show the guides to which side is what type of player
		// Ex: "Player: Right" and "Simple CPU: Left"
		g2d.setFont(sFont); // Set the font to small so it can fit
		this.showMsg(
			g2d, lPaddle.toString(), // lPaddle.toString describes
			                         // current paddle mode.
			HorizontalD.LEFT, VerticalD.DOWN
		);
		this.showMsg(
			g2d, rPaddle.toString(),
			HorizontalD.RIGHT, VerticalD.DOWN
		);

		if (this.paused) {
			// Show "get ready" message when game is paused
			String lAdd = ""; // Extra message to add on left
			String rAdd = ""; // Extra message to add on right

			if (this.lPaddle.getMode() == Mode.PLAYER) {
				lAdd = " (W/S) keys to move";
			}
			if (this.rPaddle.getMode() == Mode.PLAYER) {
				rAdd = " (Up/Down) keys to move";
			}
			// Show messages:
			this.showMsg(g2d, "Get ready!"+lAdd, HorizontalD.LEFT, VerticalD.NEUTRAL);
			this.showMsg(g2d, "Get ready!"+rAdd, HorizontalD.RIGHT, VerticalD.NEUTRAL);
		}
	} /* End method paint */


	/**
	 * Display the midline dividing the two "courts".
	 *
	 * The midsection consists of small squares that go from the top of the
	 * screen (Y = 0) to the bottom of the screen (Y = S_HEIGHT). The side
	 * length of one of the small squares is M_SIZE.
	 *
	 * @param g the Graphics2D object to use to paint.
	 */
	private void showMidline(Graphics2D g) {
		// Use (M_SIZE / 2) as half the length of a square because Java
		// has all shape origins in the top-left corner: M_SIZE / 2 is
		// the centre Y of a square.
		for (int i = (M_SIZE / 2); i < S_HEIGHT; i += M_SIZE) {
			if ((i / M_SIZE) % M_SKIP != 0) {
				// M_SKIP controls how many squares of separation
				// in M_SIZE units are between each painted square

				// Taking i / M_SIZE gives the amount of squares
				// painted so far; for the modulo of M_SKIP to
				// be equal to zero, this value must be a multiple
				// of M_SKIP

				// Otherwise, the square is not painted
				continue;
			}
			// Paint the square
			g.fillRect(
				S_CENTRE_X - (M_SIZE / 2), // X is centre X (centred)
				i - (M_SIZE / 2),          // Y is the current Y (centred)
				M_SIZE,                    // Width
				M_SIZE                     // Height
			);
		}
	} /* End method showMidline */


	/**
	 * Displays a text message on the game screen.
	 *
	 * The message displayed can be several positions, based on the hPos
	 * (horizontal position) and vPos (vertical position) specified.
	 *
	 * @param g the Graphics2D object to use to paint.
	 * @param msg the message to display.
	 * @param hPos the horizontal position of the message; ex. LEFT, NEUTRAL
	 *             (representing centre) and RIGHT.
	 * @param vPos the vertical position of the message; ex. UP, NEUTRAL
	 *             (representing centre) and DOWN.
	 */
	private void showMsg(Graphics2D g, String msg, HorizontalD hPos, VerticalD vPos) {
		// The FontMetrics object provides stats about how big a string
		// will be when displayed on the screen, instead of in point size:
		// https://docs.oracle.com/javase/8/docs/api/java/awt/FontMetrics.html
		FontMetrics f = g.getFontMetrics();

		// boundBox represents the area the painted string will occupy
		Rectangle2D boundBox = f.getStringBounds(msg, g);

		float yOffset = 0; // Y position of the message (with MESSAGE
		                   // origin bottom-left)
		switch (vPos) {
		case UP:
			// Will be at Y of zero, but add height and font padding.
			//        | F_PAD                |
			//        | boundBox.getHeight() |
			// (x, y) ------------------------
			yOffset = (float)(0 + boundBox.getHeight() + F_PAD);
			break;
		case DOWN:
			// Will be at max y, minus padding only.
			yOffset = (float)(S_HEIGHT - F_PAD);
			break;
		case NEUTRAL:
			// Will be at centre so minus half the string's height
			yOffset = (float)(S_CENTRE_Y - (boundBox.getHeight() / 2));
			break;
		}

		float xOffset = 0; // X position of the message (with MESSAGE
		                   // origin bottom-left)
		switch (hPos) {
		case LEFT:
			xOffset = (float)(
				0 + (S_CENTRE_X / 2)        // Left-centre
				- (boundBox.getWidth() / 2) // Minus half of text width
			);
			break;
		case RIGHT:
			xOffset = (float)(
				S_WIDTH - (S_CENTRE_X / 2)  // Right-centre
				- (boundBox.getWidth() / 2) // Minus half of text width
			);
			break;
		case NEUTRAL:
			// Will be at centre-centre minus half of text width
			xOffset = S_CENTRE_X - (float)(boundBox.getWidth() / 2);
			break;
		}
		// Draw the string: The x and y arguments provided will be the
		// x and y of the baseline of the string, at the first character.
		// https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html
		g.drawString(msg, xOffset, yOffset);
	} /* End method showMsg */


	/**
	 * "Pauses" the game.
	 *
	 * The pausing functionality simply works by setting the `this.paused`
	 * field to true. This makes the game stop updating the game elements'
	 * positions. At the same time, the `this.pauser` time is activated.
	 *
	 * This timer is defined at the initialization of Game to send an
	 * ActionCommand to the actionPerformed method when G_PAUSE seconds are
	 * finished. The actionPerformed method then stops the timer and
	 * "unpauses" the game while starting a new round.
	 */
	private void pause() {
		this.paused = true;

		// Restart the timer to rebegin the countdown from start
		this.pauser.restart();
	} /* End method pause */


	/**
	 * Update the game elements' positions according to the time passed.
	 *
	 * @param diffT the "difference in time" (see the Renderer class within
	 *              this file)
	 */
	private void update(double diffT) {
		// Don't update any element positions if the game is paused
		if (this.paused) { return; }

		/* Check if either paddle has scored a point in a game */

		// Get bound box of ball.
		Rectangle2D.Double ballPos = ball.getBounds();
		if (ballPos.getMaxX() >= S_WIDTH) {
			// lPaddle point (ball over right edge)
			this.lScore++;
			// Check if the game can be ended
			if (lScore >= 10) {
				// Start a new game
				this.newGame(HorizontalD.LEFT);
			} else {
				// "pause" the game for G_PAUSE seconds and then start a new round
				this.pause();
			}
			return; // No need to move game elements below if paused
		} else if (ballPos.getMinX() <= 0) {
			// rPaddle point (ball over left edge)
			this.rScore++;
			if (rScore >= 10) {
				this.newGame(HorizontalD.RIGHT);
			} else {
				this.pause();
			}
			return;
		}

		/* Calculate ball angle */

		boolean lTouching = lPaddle.touching(ballPos);
		boolean rTouching = rPaddle.touching(ballPos);

		// The CalcObj class holds the ball's current angle and and
		// vertical direction. It's passed to each of the paddles in
		// case they are CPU players who can make use of that information.
		CalcObj calc = ball.calcAngle(lPaddle.getBounds(), rPaddle.getBounds());

		/* Calculate paddle movement */

		// Check if paddle is operated by human
		// The Mode enum contains all the possible modes of a paddle.
		if (lPaddle.getMode() != Mode.PLAYER) {
			// CPU paddles must calculate their movement direction
			lPaddle.calcDirection(ballPos, rPaddle.getBounds(), rTouching, calc);
		}

		if (rPaddle.getMode() != Mode.PLAYER) {
			rPaddle.calcDirection(ballPos, lPaddle.getBounds(), lTouching, calc);
		}

		// Move the paddles first (to intercept the ball), then the ball.
		lPaddle.move(diffT);
		rPaddle.move(diffT);
		ball.move(diffT);
	} /* End method update */


	/**
	 * Constantly listen to events from the start menu or from the pause timer.
	 * If the event is a timer event, unpause the game. If it is a
	 * button event, start the game. Extension of the ActionListener class.
	 *
	 * @param e an event received (only relevant if it is the start button).
	 */
	public void actionPerformed(ActionEvent e) {
		if (e == null) {
			// Command is invalid
			return;
		}

		if (e.getActionCommand().equals("start")) {
			startGame();
		} else if (e.getActionCommand().equals("unpause")){
			this.paused = false;
			this.pauser.stop();
			this.newRound();
		}
	} /* End method actionPerformed */
} /* End class Game */
