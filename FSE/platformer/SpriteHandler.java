// ------------------------------------------------------------------------- //
// The SpriteHandler class handles loading of sprite images within           //
// spritesheets arranged in tile fashion.                                    //
//                                                                           //
// Author:      Leo Qi                                                       //
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
	private int tileX;
	private int tileY;
	private int xOffset;
	private int yOffset;

	private int rows;
	private int cols;

	private Rectangle2D.Double[][] tileBounds;
	private double zoom;

	public SpriteHandler(
		BufferedImage sheet, int tileX, int tileY, int xOffset,
		int yOffset, double zoom, boolean raw
	) {
		this.sheet = sheet;
		this.zoom = zoom;

		this.tileX = tileX;
		this.tileY = tileY;

		this.xOffset = xOffset;
		this.yOffset = yOffset;

		double calcCol = this.tileX + this.xOffset;
		double calcRow = this.tileY + this.xOffset;

		if (raw) {
			calcCol *= this.zoom;
			calcRow *= this.zoom;
		}

		this.cols = this.sheet.getWidth()  / (int) calcCol;
		this.rows = this.sheet.getHeight() / (int) calcRow;

		this.tileBounds = new Rectangle2D.Double[rows][cols];

		if ((this.zoom != 1) && (!raw)) {
			this.sheet = Utilities.resize(
				this.sheet,
				(int)(this.sheet.getWidth() * this.zoom),
				(int)(this.sheet.getHeight() * this.zoom)
			);
		}

		this.reversed = Utilities.horizontalFlip(
			Utilities.copy(this.sheet)
		);
		createExactBoundaries(rows, cols);
	} /* End constructor */


	public void createExactBoundaries(int rows, int cols) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				this.tileBounds[row][col] =
					Utilities.exactRectBounds(
						getTile(col, row), 0, 0
				);
			}
		}
	} /* End method createExactBoundaries */


	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY,
		int xOffset, int yOffset, double zoom, boolean raw
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
			img, tileX, tileY, xOffset, yOffset, zoom, raw
		);
	} /* End static method createFromFile */


	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY,
		double zoom
	) {
		return createFromFile(caller, relPath, tileX, tileY,
				Settings.SEP, Settings.SEP, zoom, false);
	}


	/**
	 * Gets a tile from the base spritesheet.
	 * 
	 * Assumes tiles are not offset BEFORE the first row/column.
	 * 
	 * @param gridX the column the tile is in; 0 is leftermost column.
	 * @param gridY the row the tile is in; 0 is leftermost row.
	 * @param reversed whether or not the tile should be flipped.
	 */
	public BufferedImage getTile(int gridX, int gridY, boolean reversed)
		throws IllegalArgumentException
	{
		if ((gridX < 0 || gridX > this.cols) || (gridY < 0 || gridY > this.rows)) {
			throw new IllegalArgumentException(
				"SpriteHandler.getTile -> Inputs cannot be negative."
			);
		}

		int xPos   = (int)(( (tileX + xOffset) * gridX ) * this.zoom);
		int yPos   = (int)(( (tileY + yOffset) * gridY ) * this.zoom);
		int width  = (int)(this.tileX * this.zoom);
		int height = (int)(this.tileY * this.zoom);

		try {
		if (reversed) {
			// Image is reversed so get from reversed sheet.
			// Start from end (WIDTH) instead of 0 because sheet
			// is reversed
			return this.reversed.getSubimage(
				this.reversed.getWidth() - xPos - width,
				this.reversed.getHeight() - yPos - height,
				width, height
			);
		}
		return this.sheet.getSubimage(xPos, yPos, width, height);
		} catch (Exception e) {
			System.out.println("" + gridX + " " + gridY + " " + reversed);
			return null;
		}
	} /* End method getTile */


	/**
	 * Convenience method where tiles are not reversed by default.
	 */
	public BufferedImage getTile(int gridX, int gridY) {
		return this.getTile(gridX, gridY, false);
	} /* End method getTile */


	/**
	 * Get x width of spritesheet.
	 */
	public int getXSize() { return (int)(this.tileX * this.zoom); }


	/**
	 * Get y height of spritesheet.
	 */
	public int getYSize() { return (int)(this.tileY * this.zoom); }


	/**
	 * Get Rectangular area boundary for a particular tile (with transparent
	 * pixels removed).
	 */
	public Rectangle2D.Double getBounds(int x, int y) { return tileBounds[y][x]; }

} /* End class SpriteHandler */
