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
	double zoom;

	public Camera(BufferedImage lvlMap, double zoom) {
		this.map = lvlMap;
		this.zoom = zoom;

		if (zoom != 1) {
			this.map = Utilities.resize(
				lvlMap,
				(int) (lvlMap.getWidth()*zoom),
				(int) (lvlMap.getHeight()*zoom)
			);
		} else {
			this.map = lvlMap;
		}
	} /* End constructor */


	public void beam(Graphics2D g2d, Entity focus, ArrayList<Item> items) {
		BufferedImage canvas = Utilities.copy(map);

		Graphics2D brush = canvas.createGraphics();

		// Draw focus
		Point2D pF = focus.getPoint();
		brush.drawImage(focus.getSprite(), (int)pF.getX(), (int)pF.getY(), null);

		Point2D pE;
		Item subject;
		/* Draw all entity sets */
		for (int i = 0; i < items.size(); i++) {
			subject = null;
			subject = items.get(i);
			pE = subject.getPoint();

			brush.drawImage(
				subject.getSprite(),
				(int)pE.getX(),
				(int)pE.getY(),
				null
			);
		}
		brush.dispose();

		g2d.drawImage(
			canvas,
			Settings.halfResX - focus.getCentreX(),
			(int)((Settings.halfResY * 1.3) - focus.getCentreY()),
			null
		);
	} /* End method beam */

} /* End class Camera */
