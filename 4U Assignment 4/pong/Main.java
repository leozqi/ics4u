package pong;

import javax.swing.*;

import static pong.Constants.*;


class Frame extends JFrame {
	/**
	 * Create a new JFrame.
	 * The Game class sets dimensions and so on which are defined in
	 * Constants.java
	 */
	public Frame() {
		/* Set common options */
		this.setTitle("Pong");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		/* Create a Game object to start the game */
		this.add(new Game());
		this.pack();
	}
}


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
		// The UIManager can change the value of Swing GUI keys to make
		// it suit the game's look (like a black background) without
		// setting every single button and frame that colour.

		// List of possible keys:
		// https://alvinalexander.com/java/java-uimanager-color-keys-list/
		
		// The constants used (like C_BACK) are defined in Constants.java
		// They are colours.
		UIManager.put("Panel.background", C_BACK);
		UIManager.put("Panel.foreground", C_FORE);

		UIManager.put("Button.background", C_BACK);
		UIManager.put("Button.foreground", C_FORE);

		UIManager.put("RadioButton.background", C_BACK);
		UIManager.put("RadioButton.foreground", C_FORE);

		UIManager.put("Label.background", C_BACK);
		UIManager.put("Label.foreground", C_FORE);

		// Create the game frame
		Frame f = new Frame();
	} /* End method run */
}
