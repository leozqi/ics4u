// ------------------------------------------------------------------------- //
// Takes care of rendering levels                                            //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-01-10                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

class Camera {

	BufferedImage map;
	double zoom;

	public Camera(BufferedImage lvlMap, double zoom) {
		this.map = lvlMap;
		this.zoom = zoom;
	} /* End constructor */


	public BufferedImage getView() {
		if (zoom == 1) {
			return map;
		} else {
			return resize(
				map,
				(int)(map.getWidth()*zoom),
				(int)(map.getHeight()*zoom)
			);
		}
	} /* End method getView */


	public BufferedImage resize(BufferedImage img, int w, int h) {
		Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage ret = new BufferedImage(w, h, img.getType());

		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return ret;
	} /* End method resize */

} /* End class Camera */
