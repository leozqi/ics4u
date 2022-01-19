// ------------------------------------------------------------------------- //
// Handle sprite loading within the game.                                    //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date: 2021-12-16                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.net.URL;
import java.io.IOException;

public class SpriteHandler {
	private BufferedImage sheet;
	private BufferedImage reversed;
	private int xPixels;
	private int yPixels;
	private int tileX;
	private int tileY;
	private int xOffset;
	private int yOffset;


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels,
		int tileX, int tileY, int xOffset, int yOffset
	) {
		this.sheet = sheet;
		this.reversed = Utilities.horizontalFlip(sheet);
		this.xPixels = xPixels;
		this.yPixels = yPixels;

		this.tileX = tileX;
		this.tileY = tileY;

		this.xOffset = xOffset;
		this.yOffset = yOffset;
	} /* End constructor */


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels, int tileX, int tileY
	) {
		this(sheet, xPixels, yPixels, tileX, tileY, 0, 0);
	} /* End constructor */


	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY, int xOffset, int yOffset
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
			tileX, tileY, xOffset, yOffset
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
			(gridX * this.tileX) + (gridX * this.xOffset), // x
			(gridY * this.tileY) + (gridY * this.yOffset),
			this.tileX, this.tileY
		);
	} /* End method getTile */


	public BufferedImage getReversedTile(int gridX, int gridY) throws IllegalArgumentException {
		if (gridX < 0 || gridY < 0) {
			throw new IllegalArgumentException(
				"SpriteHandler.getReversedTile -> Inputs cannot be negative."
			);
		}

		return this.reversed.getSubimage(
			(gridX * this.tileX) + (gridX * this.xOffset),
			(gridY * this.tileY) + (gridY * this.yOffset),
			this.tileX, this.tileY
		);
	} /* End method getReversedTile */


	public int getXSize() { return this.tileX; }
	public int getYSize() { return this.tileY; }

} /* End class SpriteHandler */
