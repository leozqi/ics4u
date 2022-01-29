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
	 * @param darken whether or not the level should be darkened, and
	 *               light effects applied to light sources.
	 */
	public void beam(
		Graphics2D g2d, Entity focus, ArrayList<Entity> entities,
		double zoom, boolean darken
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

			if (subject.getAttributes().contains(Attribute.LIGHTING)) {
				lightSources[lightCnt++] = new Point2D.Double(
					subject.getBounds().getCenterX(),
					subject.getBounds().getCenterY()
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
		lightSources[lightCnt++] = new Point2D.Double(
			focus.getBounds().getCenterX(),
			focus.getBounds().getCenterY()
		);
		brush.drawImage(
			focus.getSprite(), // BufferedImage of focus
			(int)(loc.getX()), // X of focus origin
			(int)(loc.getY()), // Y of focus origin
			null
		);


		if (darken) {
			BufferedImage dark = new BufferedImage(
				(int)Settings.levelWidth(), (int)Settings.levelHeight(),
				BufferedImage.TYPE_INT_ARGB
			);

			Graphics2D pan = dark.createGraphics();
			pan.setPaint(new Color(0, 0, 0, 255));
			pan.fill(new Rectangle2D.Double(0, 0, Settings.levelWidth(),
						Settings.levelHeight()));
			pan.setPaintMode();
			pan.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));
			for (int light = 0; light < lightCnt; light++) {
				Point2D centre = lightSources[light];
				float radius = (float)(120 * Settings.zoom());
				RadialGradientPaint rgp = new RadialGradientPaint(
					centre, radius,
					new float[] {0f, 1f}, new Color[] {
					new Color(0, 0, 0, 0), new Color(0, 0, 0, 150)
				});
				pan.setPaint(rgp);
				pan.fill(new Ellipse2D.Double(centre.getX() - radius, centre.getY() - radius, radius * 2, radius * 2));
			}

			pan.dispose();
			brush.drawImage(dark, 0, 0, null);
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
		float yPos = (float) ((Settings.resY() / 2) - fontArea.getCenterY());

		g2d.drawString(msg, xPos, yPos);
	} /* End method showMsg */


	/**
	 * Apply "tunnel effect" to a level where everything is dark except
	 * for a radius around the player.
	 *
	 * @param g2d Graphics2D object.
	 * @param focus focus to centre on.
	 */
	public void applyDarkness(Graphics2D g2d, Entity focus, float lighting) {
		Point2D centre = focus.getPoint(); // get location to centre

		RadialGradientPaint rgp = new RadialGradientPaint(
			centre, lighting, new float[] {0f, 1f}, new Color[] {
			new Color(0, 0, 0, 0), new Color(0, 0, 0, 255)
		});

		g2d.setPaint(rgp);
		g2d.fill(new Rectangle2D.Double(0, 0, Settings.resX(), Settings.resY()));
	} /* End method showMsg */


} /* End class Camera */
