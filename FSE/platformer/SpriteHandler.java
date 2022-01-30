// ------------------------------------------------------------------------- //
// The SpriteHandler class handles loading of sprite tiles from spritesheets //
// with a set tile width, height and padding.                                //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: SpriteHandler.java                                              //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.net.URL;
import java.io.IOException;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class SpriteHandler {

	private BufferedImage sheet;    // Store BufferedImage rep. of spritesheet
	private BufferedImage reversed; // Horizontally flipped spritesheet
	private int tileX;              // Width of one tile
	private int tileY;              // Height of one tile
	private int xOffset;            // Padding between tiles (X axis)
	private int yOffset;            // Padding between tiles (Y axis)

	private int rows; // Number of rows on spritesheet
	private int cols; // Number of columns on spritesheet

	private Rectangle2D.Double[][] tileBounds; // Collision-boxes of each tile
	private double zoom; // Zoom setting of spritesheet

	/**
	 * Create a handler managing the tiles of a spritesheet.
	 *
	 * SpriteHandlers take care of storing and managing the raw sheet as
	 * a single BufferedImage and dividing that sheet up into tiles as
	 * requested by data classes (Level, Player, Entity).
	 *
	 * This method is efficient; multiple entities of the same type are
	 * rendered using the same BufferedImage; multiple tiles from a single
	 * SpriteHandler share the same base array as storage.
	 *
	 * SpriteHandlers are most often created with the static createFromFile
	 * method, which also takes care of loading the initial file.
	 *
	 * @param sheet BufferedImage spritesheet.
	 * @param tileX the width of ONE tile.
	 * @param tileY the height of ONE tile.
	 * @param xOffset the padding between tiles, in the X direction.
	 * @param yOffset the padding between tiles, in the Y direction.
	 * @param zoom the zoom of the spritesheet (zoom is applied after creation)
	 * @param raw whether or not the spritesheet should be treated as already
	 *            at the zoom level specified.
	 */
	public SpriteHandler(
		BufferedImage sheet, int tileX, int tileY, int xOffset,
		int yOffset, double zoom, boolean raw
	) {
		this.sheet = sheet;    // Set fields based on parameters
		this.zoom = zoom;
		this.tileX = tileX;
		this.tileY = tileY;
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		double calcCol = this.tileX + this.xOffset; // Width of one column
		double calcRow = this.tileY + this.yOffset; // Width of one row

		if (raw) {
			calcCol *= this.zoom; // Adjust widths based on zoom
			calcRow *= this.zoom;
		}

		// Calculate number of rows and columns of spritesheet
		this.cols = this.sheet.getWidth()  / (int) calcCol;
		this.rows = this.sheet.getHeight() / (int) calcRow;

		// Create storage for the collision-boxes of tiles based on rows, cols
		this.tileBounds = new Rectangle2D.Double[rows][cols];

		if ((this.zoom != 1) && (!raw)) {
			// Zoom is not one and format should not be raw
			// Resize the spritesheet to conform with new zoom
			this.sheet = Utilities.resize(
				this.sheet,
				(int)(this.sheet.getWidth() * this.zoom),
				(int)(this.sheet.getHeight() * this.zoom)
			);
		}

		// Flip the sheet for reversed tiles (costumes in other direction)
		// For example, player facing left would be reverse of facing right
		this.reversed = Utilities.horizontalFlip(
			// Copy sheet as input so original is not modified
			Utilities.copy(this.sheet)
		);

		// Create shrunken rectangular boundaries for all tiles in sheet
		createExactBoundaries(rows, cols);
	} /* End constructor */


	/**
	 * Create shrunken rectangular boundaries for all tiles in sheet.
	 */
	public void createExactBoundaries(int rows, int cols) {
		// If sheet is not ready, do not continue with method
		if ((this.tileBounds == null) || (this.sheet == null)) { return; }

		// Get shrunken rectangle boundaries for all tiles:
		for (int row = 0; row < rows; row++) {
			// By row
			for (int col = 0; col < cols; col++) {
				// By col
				// [row][col] correspond with x y of getTile()
				this.tileBounds[row][col] =
					Utilities.exactRectBounds(
						getTile(col, row), 0, 0
				);
			}
		}
	} /* End method createExactBoundaries */


	/**
	 * Load a SpriteHandler from a file.
	 *
	 * Creates a SpriteHandler by loading an image from a file. Images should
	 * be able to be read by the ImageIO library and have transparency
	 * capabilities.
	 *
	 * Opens files relative to the `caller` parameter's classpath.
	 *
	 * @param caller class calling this method.
	 * @param relPath relative path to caller.
	 * @param tileX width of one tile.
	 * @param tileY height of one tile.
	 * @param xOffset padding of one tile in X axis.
	 * @param yOffset padding of one tile in Y axis.
	 * @param zoom zoom of created SpriteHandler.
	 * @param raw if raw is specified, image is not resized to match zoom.
	 *
	 * @return created SpriteHandler from file, or null if failure.
	 */
	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY,
		int xOffset, int yOffset, double zoom, boolean raw
	) {
		BufferedImage img = null;
		URL url = caller.getClass().getResource(relPath);

		// File does not exist; return null
		if (url == null) { return null; }

		try {
			img = ImageIO.read(url); // Try to read image
		} catch (IOException e) {
			return null; // Read of image failed; return null
		}
		// Create SpriteHandler from loaded BufferedImage and params.
		return new SpriteHandler(
			img, tileX, tileY, xOffset, yOffset, zoom, raw
		);
	} /* End static method createFromFile */


	/**
	 * Conveienence method of createFromFile without extra parameters.
	 *
	 * Paddings are both assumed to be equal to Settings.SEP (default padding)
	 * `raw` is assumed to be false.
	 */
	public static SpriteHandler createFromFile(
		Object caller, String relPath, int tileX, int tileY,
		double zoom
	) {
		return createFromFile(
			caller, relPath,
			tileX, tileY,
			Settings.SEP, Settings.SEP,
			zoom, false
		);
	}


	/**
	 * Gets a tile from the base spritesheet.
	 * 
	 * Assumes tiles are not offset BEFORE the first row/column.
	 *
	 * Throws IllegalArgumentException if gridX and gridY are not within
	 * spritesheet.
	 *
	 * @param gridX the column the tile is in; 0 is leftermost column.
	 * @param gridY the row the tile is in; 0 is leftermost row.
	 * @param reversed whether or not the tile should be flipped.
	 *
	 * @return BufferedImage of tile or null if error.
	 */
	public BufferedImage getTile(int gridX, int gridY, boolean reversed)
		throws IllegalArgumentException
	{
		if ((gridX < 0 || gridX > this.cols) || (gridY < 0 || gridY > this.rows)) {
			throw new IllegalArgumentException(
				"SpriteHandler.getTile -> Inputs cannot be negative."
			);
		}

		// Calculate X and Y position of origin of tile to extract
		// X is equal to width of one column * columns
		// Y is equal to height of one row * rows
		// Zoom factor of level, not global Settings, is applied
		int xPos   = (int)(( (tileX + xOffset) * gridX ) * this.zoom);
		int yPos   = (int)(( (tileY + yOffset) * gridY ) * this.zoom);

		// Calculate tile dimensions in same way as width and height
		int width  = (int)(this.tileX * this.zoom);
		int height = (int)(this.tileY * this.zoom);

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
		// Image is not reversed; get from normal sheet
		return this.sheet.getSubimage(xPos, yPos, width, height);
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
	 * 
	 * @param x Grid x position of tile.
	 * @param y Grid y position of tile.
	 */
	public Rectangle2D.Double getBounds(int x, int y) { return tileBounds[y][x]; }

} /* End class SpriteHandler */
