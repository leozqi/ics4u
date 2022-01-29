// ------------------------------------------------------------------------- //
// The Menu class builds a menu with controls for the start screen.          //
// levels.                                                                   //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Menu.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {

	private JLabel title;
	private JLabel descrip;
	private JComboBox lvlSelect;
	private JSlider zoomBar;
	private JButton start;

	/**
	 * Builds a start menu with Swing components (Radiobuttons, buttons,
	 * and labels).
	 *
	 * This start menu is used to select a level from available levels in the
	 * filesystem.
	 *
	 * Documentation used:
	 *
	 * Comboboxes:
	 * <https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html>
	 * <https://docs.oracle.com/javase/8/docs/api/javax/swing/JButton.html>
	 *
	 * The final menu looks like the below:
	 * ----------------------------------------
	 * |                                      |
	 * |             Game Title!              |
	 * |                  ________________    |
	 * |  Pick a level:   |  Level 1.lvl |    |
	 * |                  ----------------    |
	 * |                                      |
	 * |  Zoom:           *-------*------*    |
	 * |                                      |
	 * |               Start!                 |
	 * |                                      |
	 * ----------------------------------------
	 */
	public Menu(
		Game buttonCall, String title, String descrip, Font font,
		String[] levelNames
	) {
		super();

		/* Set up fonts by creating fonts with different sizes */
		Font titleFont  = font.deriveFont(80f); // Font for title
		Font dialogFont = font.deriveFont(18f); // Font for messages
		Font startFont  = font.deriveFont(50f); // Font for start button

		/* Title block */
		this.title = new JLabel(title);
		this.title.setFont(titleFont);
		this.title.setForeground(Settings.COLOUR_TITLE);

		this.descrip = new JLabel(descrip);
		this.descrip.setFont(dialogFont);

		/* Settings block */
		// Create "pick a level" descriptor
		JLabel selDescrip = new JLabel("Pick a level:");
		selDescrip.setFont(dialogFont);

		// Create combobox level selector
		this.lvlSelect = new JComboBox<String>(levelNames);
		this.lvlSelect.setFont(dialogFont);
		this.lvlSelect.setMaximumSize(lvlSelect.getPreferredSize());

		// Create zoom slider descriptor
		JLabel sliDescrip = new JLabel("Select a zoom:");
		sliDescrip.setFont(dialogFont);

		// Create and configure zoom slider bar
		// Use constructor: JSlider(Orientation, start, end, initial)
		this.zoomBar = new JSlider(JSlider.HORIZONTAL, 100, 200, 100);
		this.zoomBar.setMajorTickSpacing(50);  // ticks spaced at 50
		this.zoomBar.setSnapToTicks(true);     // Only select 100, 150, 200 zoom
		this.zoomBar.setPaintLabels(true);     // Draw labels
		this.zoomBar.setPaintTicks(true);      // Draw ticks for each
		this.zoomBar.setPaintTrack(true);      // Draw track for paint
		this.zoomBar.setFont(dialogFont); // Set the correct font

		// Create start button
		this.start = new JButton("Start!");
		this.start.setFont(startFont);

		// buttonCall listens for this ActionCommand
		this.start.setActionCommand("start");
		this.start.addActionListener(buttonCall);

		// A Java layout arranges the labels and buttons on the menu.
		// The Swing GroupLayout is the most flexible and easy to use:
		// https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
		//
		// The GroupLayout works by arranging elements based on their
		// specified positions in TWO separate layouts, one vertical
		// and one horizontal.
		//
		// Create a GroupLayout:
		GroupLayout lay = new GroupLayout(this);
		this.setLayout(lay);

		lay.setAutoCreateGaps(true);           // Gaps between elements
		lay.setAutoCreateContainerGaps(true);  // Gaps between containers

		// Order items in parallel horizontally (on top of one another)
		lay.setHorizontalGroup(
			lay.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(this.title)             // Title
			.addComponent(this.descrip)           // Message description
			.addGroup(lay.createSequentialGroup()
				.addComponent(selDescrip)     // "Pick a level"
				.addComponent(this.lvlSelect) // List of levels
			)
			.addGroup(lay.createSequentialGroup()
				.addComponent(sliDescrip)     // Zoom descrip
				.addComponent(
					this.zoomBar,         // Zoom bar
					GroupLayout.PREFERRED_SIZE,
					GroupLayout.DEFAULT_SIZE,
					GroupLayout.PREFERRED_SIZE
				)
			)
			.addComponent(start)                  // Start button
		);

		// Order items in sequential order (on top of one another)
		lay.setVerticalGroup(
			lay.createSequentialGroup()
			.addPreferredGap( // Add a gap to centre titles
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE   // No limit on max size
			)
			.addComponent(this.title) // Add title
			.addPreferredGap(         // Add max gap
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE   // No limit on max size
			)
			.addComponent(this.descrip)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				50
			)
			.addGroup(lay.createParallelGroup()   // Horizontally together
				.addComponent(selDescrip)     // "Pick a level"
				.addComponent(this.lvlSelect) // Level combobox
			)
			.addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				50  // Max 50-pixels of a gap
			)
			.addGroup(lay.createParallelGroup()   // Horizontally together
				.addComponent(sliDescrip)     // Zoom description
				.addComponent(this.zoomBar)   // Zoom slider bar
			)
			.addPreferredGap(         // Add max gap to start button
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE
			)
			.addComponent(this.start) // Add start button
			.addPreferredGap(         // Add max gap to centre to bottom
				LayoutStyle.ComponentPlacement.RELATED,
				GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE
			)
		);
	} /* End constructor */


	/**
	 * Get currently selected level's filename.
	 *
	 * @return filename of the level.
	 */
	public String getCurrentLevel() {
		return (String) this.lvlSelect.getSelectedItem();
	} /* End method getCurrentLevel */


	/**
	 * Get current zoom specified on the menu slider.
	 */
	public double getCurrentZoom() {
		// Change zoom to a decimal value (1.5) instead of a percent
		// 150%.
		return this.zoomBar.getValue() / 100d;
	} /* End method getCurrentZoom */


	/**
	 * Set description of the menu.
	 */
	public void setDescription(String description) {
		this.descrip.setText(description);
	} /* End method setDescription */

} /* End class Menu */
