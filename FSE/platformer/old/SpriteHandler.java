// ------------------------------------------------------------------------- //
// Handle sprite loading within the game.                                    //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.net.URL;
import java.io.IOException;

public class SpriteHandler {
	private BufferedImage sheet;
	private int xPixels;
	private int yPixels;
	private int tilePixels;
	private int xOffset;
	private int yOffset;


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels,
		int tilePixels, int xOffset, int yOffset
	) {
		this.sheet = sheet;
		this.xPixels = xPixels;
		this.yPixels = yPixels;
		this.tilePixels = tilePixels;

		this.xOffset = xOffset;
		this.yOffset = yOffset;
	} /* End constructor */


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels, int tilePixels
	) {
		this(sheet, xPixels, yPixels, tilePixels, 0, 0);
	} /* End constructor */


	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tilePixels, int xOffset, int yOffset
	) {
		BufferedImage img = null;
		URL url = caller.getClass().getResource(relPath);

		if (url == null) {
			return null;
		}

		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			return null;
		}
		return new SpriteHandler(
			img, img.getWidth(), img.getHeight(),
			tilePixels, xOffset, yOffset
		);
	} /* End static method createFromFile */


	/**
	 * Gets a tile from the base spritesheet.
	 * 
	 * Assumes tiles are not offset BEFORE the first row/column.
	 * 
	 * @param gridX the column the tile is in; 0 is leftermost column.
	 * @param gridY the row the tile is in; 0 is leftermost row.
	 */
	public BufferedImage getTile(int gridX, int gridY) throws IllegalArgumentException {
		if (gridX < 0 || gridY < 0 ) {
			throw new IllegalArgumentException(
				"SpriteHandler.getTile -> Inputs cannot be negative."
			);
		}

		return this.sheet.getSubimage(
			(gridX * this.tilePixels) + (gridX * this.xOffset), // x
			(gridY * this.tilePixels) + (gridY * this.yOffset),
			this.tilePixels, this.tilePixels
		);
	} /* End method getTile */

} /* End class SpriteHandler */
