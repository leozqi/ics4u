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
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class SpriteHandler {
	private BufferedImage sheet;
	private BufferedImage reversed;
	private int xPixels;
	private int yPixels;
	private int tileX;
	private int tileY;
	private int xOffset;
	private int yOffset;

	private int rows;
	private int cols;
	private double zoom = 1;

	private Rectangle2D.Double[][] tileBounds;


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels,
		int tileX, int tileY, int xOffset, int yOffset, double zoom
	) {
		this.sheet = sheet;
		this.xPixels = xPixels;
		this.yPixels = yPixels;

		this.tileX = tileX;
		this.tileY = tileY;

		this.xOffset = xOffset;
		this.yOffset = yOffset;

		this.cols = this.sheet.getWidth() / (this.tileX + this.xOffset);
		this.rows = this.sheet.getHeight() / (this.tileY + this.yOffset);

		this.tileBounds = new Rectangle2D.Double[rows][cols];
		this.zoom = zoom;

		if (this.zoom != 1) {
			this.sheet = Utilities.resize(
				this.sheet,
				(int)(this.sheet.getWidth() * this.zoom),
				(int)(this.sheet.getHeight() * this.zoom)
			);
		}
		this.reversed = Utilities.horizontalFlip(
			Utilities.copy(this.sheet));
		createExactBoundaries(rows, cols);
	} /* End constructor */


	public void createExactBoundaries(int rows, int cols) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.tileBounds[row][col] = Utilities.exactRectBounds(getTile(col, row), 0, 0);
			}
		}
	};


	public SpriteHandler(
		BufferedImage sheet, int xPixels, int yPixels,
		int tileX, int tileY, double zoom
	) {
		this(sheet, xPixels, yPixels, tileX, tileY, 0, 0, zoom);
	} /* End constructor */


	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY,
		int xOffset, int yOffset, double zoom
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
			tileX, tileY, xOffset, yOffset, zoom
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
			(int)(((this.tileX + this.xOffset) * gridX) * this.zoom), // x
			(int)(((this.tileY + this.yOffset) * gridY) * this.zoom), // y
			(int)(this.tileX * this.zoom),
			(int)(this.tileY * this.zoom)
		);
	} /* End method getTile */


	public BufferedImage getReversedTile(int gridX, int gridY) throws IllegalArgumentException {
		if (gridX < 0 || gridY < 0) {
			throw new IllegalArgumentException(
				"SpriteHandler.getReversedTile -> Inputs cannot be negative."
			);
		}
		return this.reversed.getSubimage(
			(int)(((this.tileX + this.xOffset) * gridX) * this.zoom),
			(int)(((this.tileY + this.yOffset) * gridY) * this.zoom),
			(int)(this.tileX * this.zoom),
			(int)(this.tileY * this.zoom)
		);
	} /* End method getReversedTile */


	public int getXSize() { return (int)(this.tileX * this.zoom); }
	public int getYSize() { return (int)(this.tileY * this.zoom); }

	public Rectangle2D.Double getBounds(int x, int y) { return tileBounds[y][x]; } 

} /* End class SpriteHandler */
