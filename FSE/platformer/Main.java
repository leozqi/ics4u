// ------------------------------------------------------------------------- //
// The Main class loads the main window and creates the Game class to start  //
// the game.                                                                 //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Main.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import javax.swing.*;

class Frame extends JFrame {

	/**
	 * Create a new JFrame window to contain the Game.
	 *
	 * The Frame class acts as a main container for the Game. All rendering
	 * of Game elements themselves are within the Game class.
	 */
	public Frame() {
		/* Set common options */
		this.setTitle(Settings.TITLE); // Title is a defined constant
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
		// Create the GUI in the same thread as the AWT thread.

		// This is a best practice adopted from the Java docs:
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
