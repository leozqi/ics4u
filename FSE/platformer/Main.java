

package platformer;

import javax.swing.*;

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
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		/* Create a Game object to start the game */
		this.add(new Game(this));
		this.pack();
	} /* End default constructor */

} /* End class Frame */


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
		// The UIManager is what Swing uses to set the colours, style of
		// Swing components.

		// This program uses the UIManager to change the value of Swing
		// GUI keys to make it suit the game's look, (like a black
		// background) without setting every single button and frame
		// to that colour. This is done with the put method.

		// The UIManager docs used:
		// https://docs.oracle.com/javase/8/docs/api/javax/swing/UIManager.html

		// List of possible keys used:
		// https://alvinalexander.com/java/java-uimanager-color-keys-list/

		// The constants used (like C_BACK) are defined in Constants.java
		// They are colours.
		/*
		UIManager.put("Panel.background", C_BACK);
		UIManager.put("Panel.foreground", C_FORE);

		UIManager.put("Button.background", C_BACK);
		UIManager.put("Button.foreground", C_FORE);

		UIManager.put("RadioButton.background", C_BACK);
		UIManager.put("RadioButton.foreground", C_FORE);

		UIManager.put("Label.background", C_BACK);
		UIManager.put("Label.foreground", C_FORE);
		*/
		// Create the game frame to start!
		Frame f = new Frame();
	} /* End method run */

} /* End class Main */
