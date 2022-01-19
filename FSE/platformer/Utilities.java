// ------------------------------------------------------------------------- //
// Stores different utilities used throughout the program.                   //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-18                                                   //
// Finish date: 2022-01-18                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D;

public class Utilities {

	/**
	 * Provides a set of static methods written for the platformer.
	 * 
	 * This class never needs to be instiantated.
	 */
	private Utilities() {}


	/**
	 * Strip all transparent space from tile bounding box for collision checks.
	 *
	 * This method is much better than a rectangular bounding box for objects
	 * that are NOT round.
	 *
	 * Iteration of a BufferedImage pixel by pixel was adapted
	 * (and considerably simplified) from an answer here:
	 *
	 * <https://stackoverflow.com/a/19486347>.
	 *
	 * Code to detect transparent pixels and create a single unified area
	 * from each pixel was by me through reading the docs below:
	 *
	 * <https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#getRGB-int-int->
	 * <https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html#getAlpha-->
	 *
	 * Quote:
	 *
	 * > An alpha value of 1.0 or 255 means that the color is completely
	 * > opaque and an alpha value of 0 or 0.0 means that the color is
	 * > completely transparent. When constructing a Color with an explicit
	 * > alpha or getting the color/alpha components of a Color, the color
	 * > components are never premultiplied by the alpha component. 
	 *
	 * Although this method has a large computation time penalty, it is only
	 * done once for every object in the level and enables fast collision
	 * checking.
	 */
	public static Area exactBounds(BufferedImage bi, int xOffset, int yOffset) {
		Area ret = new Area(); // Area to store final bounds.
		Color color;  // Color object to determin transparency.

		// Iterate over all pixels (x then y)
		for (int y = 0; y < bi.getHeight(); y++) {
			for (int x = 0; x < bi.getWidth(); x++) {
				// Get color from BufferedImage pixel getRGB
				// True; include alpha value
				color = new Color(bi.getRGB(x, y), true);

				// Check alpha code
				if (color.getAlpha() != 0) {
					// Not transparent, add to bounds
					ret.add(new Area(
						new Rectangle2D.Double(
							xOffset + x,
							yOffset + y,
							1, 1
						)
					));
				}
			}
		}
		return ret; // Return final area
	} /* End method exactBounds */


	/**
	 * Mirror-flip an image horizontally.
	 *
	 * This function was modified from the following blog post:
	 *
	 * <https://anilfilenet.wordpress.com/2011/01/22/flipping-an-image-horizontally-and-vertically-in-java/>
	 *
	 * @param bi BufferedImage to flip.
	 * @return BufferedImage flipped image.
	 */
	public static BufferedImage horizontalFlip(BufferedImage bi) {
		int w = bi.getWidth();
		int h = bi.getHeight();

		BufferedImage ret = new BufferedImage(w, h, bi.getType());

		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(bi, 0, 0, w, h, w, 0, 0, h, null);
		g2d.dispose();
		return ret;
	}

} /* End class Utilities */
