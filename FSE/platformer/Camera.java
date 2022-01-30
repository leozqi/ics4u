// ------------------------------------------------------------------------- //
// The Camera class renders game elements onto the main JPanel with more     //
// control.                                                                  //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
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

	// Provide option to smooth text during rendering
	private RenderingHints smoothText = new RenderingHints(
		RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON
	);

	/**
	 * Create a Camera.
	 *
	 * A Camera first renders the base level as one image stored in the
	 * `map` field. Then, entities and the player are drawn on top each
	 * frame. This way, the rendering cost of the level itself is small.
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
	 * @param darken whether or not the level should be darkened, and
	 *               light effects applied to light sources.
	 * @param darkenLvl if darkening is applied, how transparent the dark
	 *                  layer should be.
	 */
	public void beam(
		Graphics2D g2d, Entity focus, ArrayList<Entity> entities,
		double zoom, boolean darken, double darkenLvl
	) {
		// Copy image used to display the level as otherwise painted
		// entities will be duplicated on subsequent frames.
		BufferedImage canvas = Utilities.copy(map);

		/* Keep track of light sources */
		// Size could be all entities, +1 extra spot for the player
		Point2D[] lightSources = new Point2D[entities.size() + 1];
		int lightCnt = 0;
		Point2D loc;

		// Paint on canvas to not modify base image
		Graphics2D brush = canvas.createGraphics();

		/* Draw other entities */
		Entity subject;
		// Iterate over all entities
		for (int i = 0; i < entities.size(); i++) {
			subject = entities.get(i);   // Get subject from list
			loc = subject.getPoint();    // Get point from subject

			// If entity should light up, keep track of its position
			if (subject.getAttributes().contains(Attribute.LIGHTING)) {
				// Add centre of entity to list of all points
				// with light effect
				lightSources[lightCnt++] = new Point2D.Double(
					subject.getCentreX(),
					subject.getCentreY()
				);
			}

			// Draw subject
			brush.drawImage(
				subject.getSprite(), // BufferedImage of focus
				(int)(loc.getX()),   // X of subject origin
				(int)(loc.getY()),   // Y of subject origin
				null
			);
		}

		/* Draw `focus`: centred entity */
		loc = focus.getPoint();
		// Keep track of player; player needs light too
		lightSources[lightCnt++] = new Point2D.Double(
			focus.getCentreX(),
			focus.getCentreY()
		);
		brush.drawImage(
			focus.getSprite(), // BufferedImage of focus
			(int)(loc.getX()), // X of focus origin
			(int)(loc.getY()), // Y of focus origin
			null
		);

		if (darken) {
			// Apply darkness overlay
			BufferedImage overlay = this.applyDarkening(
				lightSources, lightCnt, darkenLvl
			);
			brush.drawImage(overlay, 0, 0, null);
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
	 * Create a darkening overlay for graphics effects.
	 *
	 * The overlay consists of a dark black rectangle with transparent
	 * gradients applied so that there is an illusion of light within the
	 * level. Gradients are applied with the RadialGradientPaint object:
	 *
	 * <https://docs.oracle.com/javase/8/docs/api/java/awt/RadialGradientPaint.html>
	 *
	 * Gradients were then painted onto the rectangle with the use of a custom
	 * AlphaComposite rule that would replace pixels of the rectangle with
	 * transparent pixels:
	 *
	 * <https://docs.oracle.com/javase/7/docs/api/java/awt/AlphaComposite.html>
	 *
	 * No code was directly taken from any docs to make this method.
	 *
	 * @param lightSources points of light on the level.
	 * @param lightCnt number of points of light on level.
	 * @param darkenLvl amount to darken the level by.
	 * @return BufferedImage overlay to apply.
	 */
	public BufferedImage applyDarkening(
		Point2D[] lightSources, int lightCnt, double darkenLvl
	) {
		// Create BufferedImage to apply overlay effect onto.
		BufferedImage overlay = new BufferedImage(
			(int)Settings.levelWidth(),  // Level width
			(int)Settings.levelHeight(), // Level height
			BufferedImage.TYPE_INT_ARGB  // Default type with alpha (transparency)
		);

		// Paint on the overlay
		Graphics2D g2d = overlay.createGraphics();

		// Apply black colour; transparency is set by darkenLvl, 255 being opaque
		g2d.setPaint(new Color(0, 0, 0, (int)(darkenLvl)));

		// Create dark rectangle
		g2d.fill(new Rectangle2D.Double(
			0, 0,                   // X, Y
			Settings.levelWidth(),  // Same as level width
			Settings.levelHeight()) // Same as level height
		);

		if (darkenLvl < Settings.COLOUR_GRADIENT.getAlpha()) {
			g2d.dispose();  // Finished
			return overlay; // Not dark enough for torch circles
		}

		// Enable overlaying transparent paint atop of rectangle through blending
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));

		// Iterate over all light sources and apply radial gradient paint
		// for light effect:
		for (int num = 0; num < lightCnt; num++) {
			// Centre of light effect application
			Point2D centre = lightSources[num];

			float radius = (float)(Settings.brightness() * Settings.zoom());

			// Create new Radial Gradient Paint
			RadialGradientPaint rgp = new RadialGradientPaint(
				centre, radius, // Centre and radius of circle
				new float[] {0f, 1f}, // Only two colours
				new Color[] {
					new Color(0, 0, 0, 0),   // Transparent
					Settings.COLOUR_GRADIENT // Almost opaque
				}
			);

			// Apply gradient to BufferedImage as a circle
			g2d.setPaint(rgp);
			g2d.fill(new Ellipse2D.Double(
				centre.getX()-radius, // Top-left corner
				centre.getY()-radius, // Top-left corner
				radius * 2, radius *2 // Diameter = r * 2
			));
		}
		g2d.dispose(); // Finished
		return overlay;
	} /* End method applyDarkening */


	/**
	 * Draw a message directly onto a rendering element.
	 *
	 * This element could be a BufferedImage or a Swing component via a
	 * Graphics2D object.
	 *
	 * @param g2d Graphics2D object.
	 * @param font the font to use for the message.
	 * @param msg the message to display.
	 */
	public void showMsg(Graphics2D g2d, Font font, String msg) {
		g2d.setFont(font); // Set font to draw message with
		g2d.setColor(Settings.COLOUR_TITLE); // Set colour of message

		g2d.setRenderingHints(smoothText);    // Render text smoothly.

		// Calculate size of final message with FontMetrics
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D fontArea = fm.getStringBounds(msg, g2d);

		float xPos = (float) ((Settings.resX() / 2) - fontArea.getCenterX());
		float yPos = (float) ((Settings.resY() / 2) - fontArea.getMaxY());

		g2d.drawString(msg, xPos, yPos);
	} /* End method showMsg */


	/**
	 * Draw coin score of player onto element.
	 *
	 * This element could be a BufferedImage or a Swing component via a
	 * Graphics2D object.
	 *
	 * @param g2d Graphics2D object.
	 * @param font the font to use for the message.
	 * @param score score to display.
	 * @param icon BufferedImage to display next to score
	 */
	public void showScore(Graphics2D g2d, Font font, int score) {
		g2d.setFont(font); // Set font to draw message with
		g2d.setColor(Settings.COLOUR_GOLD); // Score should be golden!
		g2d.setRenderingHints(smoothText);  // Render text smoothly.
		String msg = String.format("Coins: %04d", score); // Message to draw.

		// Calculate size of final message with FontMetrics
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D fontArea = fm.getStringBounds(msg, g2d);

		// Get position to draw score
		float xPos = (float) ( // X position is on rightermost of screen
			Settings.resX()
			- fontArea.getMaxX()
			- (Settings.SCORE_SEP * 2)
		);
		float yPos = (float) (0 + fontArea.getMaxY() + Settings.SCORE_SEP); // Y pos is on top

		// Draw score with additional offset
		g2d.drawString(msg, xPos, yPos);
	} /* End method showScore */

} /* End class Camera */
