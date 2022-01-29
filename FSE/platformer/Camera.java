// ------------------------------------------------------------------------- //
// Renders onto the main JPanel with more control.                           //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-01-10                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

class Camera {

	BufferedImage map;

	public Camera(BufferedImage lvlMap) {
		this.map = lvlMap;
	} /* End constructor */


	public void beam(
		Graphics2D g2d, Entity focus, ArrayList<Item> items,
		ArrayList<Enemy> enemies, Flag flag, double zoom
	) {
		BufferedImage canvas = Utilities.copy(map);

		Graphics2D brush = canvas.createGraphics();

		// Draw focus
		Point2D pF = focus.getPoint();
		brush.drawImage(
			focus.getSprite(),
			(int)(pF.getX()),
			(int)(pF.getY()),
			null
		);

		Point2D pE;
		Entity subject;
		/* Draw all entity sets */
		for (int i = 0; i < items.size(); i++) {
			subject = null;
			subject = items.get(i);
			pE = subject.getPoint();

			brush.drawImage(
				subject.getSprite(),
				(int)(pE.getX()),
				(int)(pE.getY()),
				null
			);
		}

		/* Draw all enemies */
		for (int i = 0; i < enemies.size(); i++) {
			subject = enemies.get(i);
			pE = subject.getPoint();

			brush.drawImage(
				subject.getSprite(),
				(int)(pE.getX()),
				(int)(pE.getY()),
				null
			);
		}

		subject = flag;
		pE = flag.getPoint();

		brush.drawImage(
			subject.getSprite(),
			(int)(pE.getX()),
			(int)(pE.getY()),
			null
		);

		brush.dispose();

		double xCoord = ((Settings.resX() / 2)) - focus.getCentreX();
		double yCoord = ((Settings.resY() / 2) * 1.3) - focus.getCentreY();

		g2d.drawImage(
			canvas,
			(int)Math.max(
				Settings.resX() - canvas.getWidth(),
				Math.min(0, xCoord)
			),
			(int)Math.max(
				Settings.resY() - canvas.getHeight(),
				Math.min(0, yCoord)
			),
			null
		);
	} /* End method beam */

} /* End class Camera */
