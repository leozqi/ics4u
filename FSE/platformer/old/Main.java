/*
 * Main.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "Main" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "Main" class is the main entry point of the "pong" package, and starts
 * the game and the game's GUI.
 */


package platformer;

import javax.swing.*;


/**
 * Provides a window for the display panel provided by the Game class.
 */
class Frame extends JFrame {
	/**
	 * Create a new JFrame.
	 * The Game class sets dimensions and so on which are defined in
	 * Constants.java
	 */
	public Frame() {
		/* Set common options */
		this.setTitle("test!"); // Title is a defined constant
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		/* Create a Game object to start the game */
		this.add(new Game());
		this.pack();
	} /* End default constructor */
} /* End class Frame */


/**
 * Provides the main method to start the game.
 */
public class Main {
	/**
	 * Start the program by creating an initial GUI.
	 */
	public static void main(String[] args) {
		// Create the GUI in a separate thread:
		// https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	} /* End method main */


	/**
	 * Create an initialized GUI with the appropriate settings with the
	 * Frame class.
	 */
	public static void createGUI() { 
		// Create the game frame to start!
		Frame f = new Frame();
	} /* End method run */
} /* End class Main */
