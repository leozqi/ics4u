// ------------------------------------------------------------------------- //
// Stores one single level of the game.                                      //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-02                                                   //
// Finish date: 2022-01-18                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.net.URL;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Level {

	/**
	 * `map` stores the tiles making up the obstacles of the level.
	 *
	 * Tiles are stored in the following format: map[row][col]
	 *
	 *       row
	 * col: | 0 | 1 | 2 | 3 |
	 *      | 1 |
	 *      | 2 |
	 *      | 3 |
	 *
	 * Tiles are NOT stored with images, only data.
	 */
	private TileMap[][] map = null;

	// Stores total rows and cols of the level
	private int rows = 0;
	private int cols = 0;

	// Starting coordinates of player
	private Point2D.Double pCoords = new Point2D.Double(0, 0);

	private Area bounds = new Area();            // Store normal bounds
	private SpecBounds boxes = new SpecBounds(); // Store special boxes
	private SpriteHandler tiles;                 // Store tile images
	private Biome biome;

	/**
	 * The Level class holds the data for one level.
	 *
	 * Use the loadFile method afterwards to load a file into the level.
	 *
	 * @param path  Path of level file (relative from class)
	 * @param tiles SpriteHandler holding image tiles.
	 * @param biome Biome code of the level.
	 */
	public Level(SpriteHandler tiles, Biome biome) {
		this.tiles = tiles;
		this.biome = biome;
	} /* End constructor */


	/**
	 * Load a level from a file.
	 *
	 * Iterates over the whole file first to determine number of rows
	 * and columns to make a storage array.
	 *
	 * Next, actually load the file into individual tiles.
	 *
	 * @param path Path of the level file (relative from class)
	 * @return true if success, false if error encountered (failed to load)
	 */
	public boolean loadFile(String path) {
		// Get the file path
		URL url = this.getClass().getResource(path);
		BufferedReader stdin;

		// Temporary container for each line in the file
		ArrayList<String> tmp = new ArrayList<String>();

		// Rows and columns
		int rowCnt = 0;
		int colCnt = 0;
		String ln; // Line in file
		try {
			// Read with the UTF-8 charset
			// Use InputStreamReader to specify charset:
			// InputStreamReader (InputStream in, Charset cs)
			//
			// Docs: <https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html>
			stdin = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File(url.toURI())
					),
					Charset.forName("UTF-8")
				)
			);

			// Read one line at a time
			ln = stdin.readLine();
			while (ln != null) {
				tmp.add(ln);
				if (ln.length() > colCnt) {
					// Find the maximum column size
					colCnt = ln.length();
				}
				ln = stdin.readLine();
				rowCnt++; // Maximum row size
			}
			stdin.close(); // Close the BufferedReader
		} catch (Exception e) {
			return false;
		}
		// No more chance of error, actually change class attributes
		this.rows = rowCnt;
		this.cols = colCnt;
		this.map = new TileMap[rowCnt][colCnt];

		// Create tile data for each tile
		for (int row = 0; row < this.rows; row++) {
			ln = tmp.get(row); // Re-get line

			// Use individual characters to build level
			for (int col = 0; col < ln.length(); col++) {
				this.setBlock(
					row, col,
					TileMap.fromChar(
						this.biome,
						ln.codePointAt(col)
					)
				);
			}
		}
		return true; // Success
	} /* End method loadFile */


	/**
	 * Set a block in the level as a specific type.
	 */
	public void setBlock(int row, int col, TileMap type) {
		if (this.map == null) { return; } // level is not loaded, exit

		synchronized(this.map) {
			if ((!type.isPassable()) || type.isItemBox()) {
				// Get exact boundaries of tile
				// The function strips all transparent space.
				Area tmp = Utilities.exactBounds(
					type.getTile(this.tiles),
					0 + (col * Settings.internUnit),
					0 + (row * Settings.internUnit)
				);

				if (!type.isPassable()) {
					this.bounds.add(tmp);
				}
				if (type.isItemBox()) {
					System.out.println("Item");
					this.boxes.add(tmp);
				}
			}
			EntityType entityType = type.getEntityType();
			switch (entityType) {
			case PLAYER:
				this.pCoords = new Point2D.Double(
					0 + (col * Settings.internUnit),
					0 + (row * Settings.internUnit)
				);
				return;
			}

			try {
				this.map[row][col] = type;
			} catch (ArrayIndexOutOfBoundsException e) {
				return; // row and column do not fit, exit.
			}
		}
	} /* End method setBlock */


	/**
	 * Get the data from one tile.
	 * 
	 * @param row the row of the tile.
	 * @param col the column of the tile.
	 */
	public TileMap getBlock(int row, int col) {
		if (this.map == null) { return new TileMap(); }
		synchronized(this.map) {
			return this.map[row][col];
		}
	} /* End method getBlock */


	/**
	 * Get number of rows (max height of the level)
	 */
	public int getRowNum() { return this.map.length; }


	/**
	 * Get number of columns (max width of the level)
	 */
	public int getColNum(int rowNum) { return this.map[rowNum].length; }


	/**
	 * Get the image representation of a tile.
	 *
	 * This function handles situations where no tile is created
	 * (no whitespace entered into the level file) or the tile created is
	 * empty (whitespace entered; air) in addition to normal tiles.
	 *
	 * @param row row of tile
	 * @param col column of tile
	 * @param th SpriteHandler containing spritesheet for tiles
	 * @return BufferedImage of tile or null for no image.
	 */
	public BufferedImage getTile(int row, int col, SpriteHandler th) {
		TileMap t = this.getBlock(row, col);

		if (t == null)   { return null; } // No tile is created
		if (t.isEmpty()) { return null; } // Tile is air / empty

		BufferedImage bi = t.getTile(th);

		if (bi == null) { return null; } // Failed to retrieve tile
		return bi;
	} /* End method getTile */


	/**
	 * Get the BufferedImage representation of the whole level.
	 *
	 * The camera renders the whole level with this one BufferedImage,
	 * meaning all loading cost is before the level is shown to the player,
	 * rather than an ongoing cost.
	 *
	 * @param th SpriteHandler to generate the level.
	 * @return BufferedImage representation of whole level.
	 */
	public BufferedImage getLevel(SpriteHandler th) {
		int u = Settings.internUnit; // Dimensions for one tile
		int xDim = this.cols * u;    // x-width of the whole image
		int yDim = this.rows * u;    // y-width of the whole image

		BufferedImage ret = new BufferedImage( // Image to be returned
			xDim, yDim,                    // Dimensions
			BufferedImage.TYPE_INT_ARGB    // Type
		);
		Graphics2D g2d = ret.createGraphics(); // Draw tiles onto image

		// Iterate for every row (y)
		for (int row = 0; row < this.getRowNum(); row++) {
			// Iterate for every column (x)
			for (int col = 0; col < this.getColNum(row); col++) {
				// Get image representation of tile
				BufferedImage bi = this.getTile(row, col, th);

				// If air, simply ignore!
				if (bi == null) { continue; }

				// Draw the tile onto level
				g2d.drawImage(bi, col*u, row*u, null);
			}
		}
		return ret; // Return finished representation
	} /* End method getLevel */


	/**
	 * Get ALL the boundaries of the level as one Area object.
	 * 
	 * This object contains the area of every unpassable object in the level
	 * for collision checking.
	 *
	 * @return area of every unpassable object.
	 */
	public Area getBounds() { return bounds; }


	/**
	 * Get the boundaries of special blocks as a SpecBounds object.
	 *
	 * @return area of every item block
	 */
	public SpecBounds getSpecBounds() { return boxes; }


	public Point2D.Double getPlayerStart() { return this.pCoords; }

} /* End class Level */
