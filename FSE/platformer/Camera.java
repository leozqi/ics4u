// ------------------------------------------------------------------------- //
// The Camera class renders game elements onto the main JPanel with more     //
// control.                                                                  //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Camera.java                                                     //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Camera {

	BufferedImage map; // Store image of level to display

	/**
	 * Create a Camera.
	 *
	 * A Camera first renders the base level as one image stored in the
	 * `map` field. Then, entities and the player are drawn on top each
	 * frame.
	 *
	 * @param lvlMap base image of the level.
	 */
	public Camera(BufferedImage lvlMap) {
		this.map = lvlMap;
	} /* End constructor */


	/**
	 * Render one frame of the level centred on `focus` based on the
	 * positions of the `entities`.
	 *
	 * Draws the frame directly onto a rendering element such as a
	 * BufferedImage or `paint()` function of a Swing component via a
	 * Graphics2D object.
	 *
	 * @param g2d Graphics2D object.
	 * @param focus Entity the Camera should focus on.
	 * @param entities entities to draw onto the level loaded into the
	 *                 Camera.
	 * @param zoom zoom of the level.
	 */
	public void beam(
		Graphics2D g2d, Entity focus, ArrayList<Entity> entities,
		double zoom
	) {
		// Copy image used to display the level as otherwise painted
		// entities will be duplicated on subsequent frames.
		BufferedImage canvas = Utilities.copy(map);

		// Paint on canvas
		Graphics2D brush = canvas.createGraphics();

		/* Draw `focus`: centred entity */
		Point2D loc = focus.getPoint(); // Location to draw
		brush.drawImage(
			focus.getSprite(), // BufferedImage of focus
			(int)(loc.getX()), // X of focus origin
			(int)(loc.getY()), // Y of focus origin
			null
		);

		/* Draw other entities */
		Entity subject;
		// Iterate over all entities
		for (int i = 0; i < entities.size(); i++) {
			subject = entities.get(i);   // Get subject from list
			loc = subject.getPoint();    // Get point from subject

			// Draw subject
			brush.drawImage(
				subject.getSprite(), // BufferedImage of focus
				(int)(loc.getX()),   // X of subject origin
				(int)(loc.getY()),   // Y of subject origin
				null
			);
		}
		brush.dispose(); // Free brush; finished using it

		// Scale X and Y to viewports.
		double xCoord = ((Settings.resX() / 2)) - focus.getCentreX();
		double yCoord = ((Settings.resY() / 2) * 1.2) - focus.getCentreY();

		// Draw whole composed image onto object
		g2d.drawImage(
			canvas,
			// Scale X of image into available space
			(int)Math.max(
				// Limit level-scroll to rightermost of level
				Settings.resX() - canvas.getWidth(),
				// Limit level-scroll to leftermost of level
				Math.min(0, xCoord) // 
			),
			(int)Math.max(
				// Limit level-scroll to bottom of level
				Settings.resY() - canvas.getHeight(),
				// Limit level-scroll to top of level
				Math.min(0, yCoord)
			),
			null
		);
	} /* End method beam */


	/**
	 * Draw a message directly onto a rendering element such as a
	 * BufferedImage or `paint()` function of a Swing component via a
	 * Graphics2D object.
	 *
	 * @param g2d Graphics2D object.
	 * @param font the font to use for the message.
	 * @param msg the message to display.
	 */
	public void showMsg(Graphics2D g2d, Font font, String msg) {
		g2d.setFont(font); // Set font to draw message with

		// Calculate size of final message with FontMetrics
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D fontArea = fm.getStringBounds(msg, g2d);

		float xPos = (float) ((Settings.resX() / 2) - fontArea.getCenterX());
		float yPos = (float) ((Settings.resY() / 2) - fontArea.getCenterY());

		g2d.drawString(msg, xPos, yPos);

	} /* End method showMsg */

} /* End class Camera */
