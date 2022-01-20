// ------------------------------------------------------------------------- //
// Stores different utilities used throughout the program.                   //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-18                                                   //
// Finish date: 2022-01-18                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D;

import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;

public class Utilities {

	/**
	 * Provides a set of static helper methods.
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
	 * Same as exactBounds, but returns a Rectangle2D bounding box
	 * instead of an exact Area.
	 *
	 * An area is stationary, so it cannot be used with entities.
	 * Thus, a rectangle bounding box that is smaller than the original
	 * approximates a smaller boundary.
	 */
	public static Rectangle2D.Double exactRectBounds(
		BufferedImage bi, int xOffset, int yOffset
	) {
		return (Rectangle2D.Double) exactBounds(
			bi, xOffset, yOffset
		).getBounds2D();
	} /* End method exactRectBounds */


	/**
	 * Mirror-flip an image horizontally.
	 *
	 * This function was taken from the following blog post (with minor
	 * modifications):
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
	} /* End method horizontalFlip */


	/**
	 * Resize a BufferedImage to a new dimension.
	 *
	 * If the display ratio of the two ratios is different, the resulting
	 * image will look shrunk / stretched.
	 *
	 * The code for this method was taken from this StackOverflow answer
	 * with minor variable name modifications and the addition of
	 * img.getType() instead of assuming a type of A_RGB.
	 *
	 * <https://stackoverflow.com/a/9417836>
	 *
	 * @param img image to resize
	 * @param w width of resized image
	 * @param h height of resized image
	 * @return resized BufferedImage.
	 */
	public static BufferedImage resize(BufferedImage img, int w, int h) {
		// Create a new temporary image of input img scaled to the correct
		// width and height
		Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);

		// Create a new BufferedImage to store this scaled image
		BufferedImage ret = new BufferedImage(w, h, img.getType());

		// Transfer temporary image to new BufferedImage
		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return ret;
	} /* End method resize */


	/**
	 * "Deep copy" a BufferedImage.
	 *
	 * Modifying a BufferedImage by drawing on it with a Graphics2D also
	 * modifies other references of the (same) BufferedImage. This method
	 * creates a deep copy so that subsequent modifications do not affect
	 * previous references.
	 *
	 * The code for this method was taken from this StackOverflow answer:
	 *
	 * <https://stackoverflow.com/a/9417836>
	 *
	 * @param img BufferedImage to copy
	 * @return copied BufferedImage.
	 */
	public static BufferedImage copy(BufferedImage img) {
		// Get ColorModel, essential raw data directly from BufferedImage.
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);

		// Create new BufferedImage with the same data.
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	} /* End method copy */


	/**
	 * Find files in a directory with a specified file extension.
	 *
	 * Taken from this blog post on walking through a directory for paths
	 * with a specified file extension with Java NIO:
	 *
	 * <https://mkyong.com/java/how-to-find-files-with-certain-extension-only/>
	 *
	 * Slight modification: changed final walk function to simply return
	 * the Path object as opposed to a string for further processing.
	 */
	public static List<Path> findFiles(
		Path path, String extension) throws IOException {
		// Cannot look through a file for files!
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException(
				"Path must be a directory."
			);
		}

		List<Path> ret;

		try (Stream<Path> walk = Files.walk(path)) {
			ret = walk
				// No directories
				.filter(p -> !Files.isDirectory(p))
				// Make sure file ends with extension
				.filter(f -> f.toString().toLowerCase().endsWith(extension))
				// Collect matches
				.collect(Collectors.toList());
		}

		return ret;
	} /* End method findFiles */


	/**
	 * Calculate the midpoint for two points in one dimension.
	 *
	 * Useful for calculating the midpoint X or Y for entities.
	 * 
	 * @param originPos position of the origin (within a larger coordinate
	 *                  system)
	 * @param length length from origin to second point.
	 * @return Centre midpoint
	 */
	public static double midpoint(double originPos, double length) {
		// Midpoint formula: (m1 + m2) / 2
		return ((originPos + length) + originPos) / 2;
	} /* End method midpoint */

} /* End class Utilities */
