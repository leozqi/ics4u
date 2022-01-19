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
import java.awt.image.*;

class Camera {

	BufferedImage map;
	double zoom;
	Player focus;

	public Camera(BufferedImage lvlMap, double zoom, Player focus) {
		this.map = lvlMap;
		this.zoom = zoom;
		this.focus = focus;
	} /* End constructor */


	public void beam(Graphics2D g2d) {
		g2d.drawImage(
			this.getView(),
			Settings.halfResX - focus.getCentreX(),
			(int)((Settings.halfResY * 1.3) - focus.getCentreY()),
			null
		);
	} /* End method beam */


	public BufferedImage getView() {
		BufferedImage d = copy(map);

		addEntities(d);
		if (zoom == 1) {
			return d;
		} else {
			return resize(
				d,
				(int)(d.getWidth()*zoom),
				(int)(d.getHeight()*zoom)
			);
		}
	} /* End method getView */


	private void addEntities(BufferedImage base) {
		Graphics2D g2d = base.createGraphics();
		Point2D pl = focus.getPoint();
		g2d.drawImage(focus.getSprite(), (int) pl.getX(), (int) pl.getY(), null);

		g2d.dispose();
	} /* End method addEntities */


	public BufferedImage resize(BufferedImage img, int w, int h) {
		Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage ret = new BufferedImage(w, h, img.getType());

		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return ret;
	} /* End method resize */


	public BufferedImage copy(BufferedImage img) {
		/* See https://stackoverflow.com/a/3514297 */
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

} /* End class Camera */
