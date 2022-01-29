// ------------------------------------------------------------------------- //
// The Level class loads levels from text files and stores level data        //
// separate from graphics data.                                              //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Level.java                                                      //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.net.URL;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.*;
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

	// Store entities contained within the level
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	private Area bounds = new Area();            // Store normal bounds
	private Area climbable = new Area();         // Store climbable locations
	private SpecBounds boxes = new SpecBounds(); // Store special boxes
	private SpriteHandler tiles;                 // Store tile images

	// Store sprites of entities that are not the player within the level
	private SpriteHandler[] entityCostumes;

	// Biome (general appearance) of the level
	private Biome biome = Biome.GRASSY;

	private RenderingHints enableAntiAlias = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON
	);
	private RenderingHints enableHighQuality = new RenderingHints(
		RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY
	);

	private double zoom; // Zoom of the level (x1 etc)

	/**
	 * The Level class holds the data for one level.
	 *
	 * Use the loadFile method afterwards to load a file into the level.
	 *
	 * @param tiles SpriteHandler holding image tiles.
	 * @param enemies SpriteHandler[] array for all enemy costumes
	 * @param biome Biome constant of the level.
	 * @param zoom the zoom level of the level, with 1 being no zoom.
	 */
	public Level(
		SpriteHandler tiles, SpriteHandler[] enemies, double zoom
	) {
		this.tiles = tiles;
		this.entityCostumes = enemies;
		this.zoom = zoom;
	} /* End constructor */


	/**
	 * Load a level from a file.
	 *
	 * Iterates over the whole file first to determine number of rows
	 * and columns to make a storage array.
	 *
	 * Next, actually load the file into individual tiles. Files are read
	 * with the UTF-8 charset via `InputStreamReader`. I referenced
	 * these docs but did not directly take any code:
	 *
	 * <https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html>
	 *
	 * The Level class scans the first character of the first line as the
	 * indicator for which theme to use:
	 *
	 * 'g': Grassy
	 * 'd': Dirty (dirt)
	 * 'r': Rocky
	 * 'a': Sandy
	 * 's': Snowy
	 *
	 * See the Biome enumeration in Biome.java for more details.
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
			stdin = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File(url.toURI())
					),
					Charset.forName("UTF-8")
				)
			);

			// Read one line at a time
			// Load biome
			ln = stdin.readLine();
			this.loadBiome((char)ln.codePointAt(0));
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


	public void loadBiome(char biomeCode) {
		for (Biome biome : Biome.values()) {
			if (biome.rep == biomeCode) {
				this.biome = biome;
			}
		}
	} /* End method loadBiome */


	/**
	 * Set a block in the level as a specific type.
	 *
	 * @param row row of the block
	 * @param col column of the block
	 * @param type TileMap to set the block as
	 */
	public void setBlock(int row, int col, TileMap type) {
		if (this.map == null) { return; } // level is not loaded, exit

		/* Calculate `x` and `y` coordinates of block */
		int x = (int)(0 + (col * Settings.UNIT) * this.zoom);
		int y = (int)(0 + (row * Settings.UNIT) * this.zoom);

		/* Set entity information if tile represents an entity */
		if (type.getEntityType() != EntityType.NONE) {
			this.setEntity(type, x, y);
			return;
		}

		/* It is a tile; add to map of all tiles */
		try {
			this.map[row][col] = type;
		} catch (ArrayIndexOutOfBoundsException e) {
			return; // row and column do not fit, exit.
		}

		Area area; // Area covered by the tile
		ArrayList<Attribute> typeAttrs = type.getAttributes();

		if (typeAttrs.contains(Attribute.EMPTY)) {
			// Tile is empty, nothing else to do
			return;
		}
		if (typeAttrs.contains(Attribute.NOT_SQUARE)) {
			// Create exact boundaries for not-square tiles
			area = Utilities.exactBounds(
				type.getTile(this.tiles), x, y
			);
		} else {
			// Is a square; use simple rectangle boundary
			area = new Area(new Rectangle2D.Double(
				x, y,
				Settings.UNIT * this.zoom,
				Settings.UNIT * this.zoom
			));
		}
		if (!typeAttrs.contains(Attribute.PASSABLE)) {
			// Not passable; add area to overall bounding-box
			this.bounds.add(area);
		}
		if (typeAttrs.contains(Attribute.ITEMBOX)) {
			// Is an itembox; add area to special bounding-boxes
			this.boxes.add(area);
		}
		if (typeAttrs.contains(Attribute.CLIMBABLE)) {
			// Climbable; add to climbable area
			this.climbable.add(area);
		}
	} /* End method setBlock */


	/**
	 * Set entities based on tile start locations.
	 *
	 * @param type tile containing data on entity's type.
	 * @param x real x position of entity based on tile (pixels).
	 * @param y real y position of entity based on tile (pixels).
	 */
	public void setEntity(TileMap type, int x, int y) {
		EntityType entityType = type.getEntityType();

		switch (entityType) {
		case FLAG:
			this.entities.add(new Flag(
				x, y, this.entityCostumes[1]
			));
			break;
		case PLAYER:
			this.pCoords = new Point2D.Double(x, y);
			break;
		case SLIME:
			this.entities.add(new Enemy(
				EntityType.SLIME, x, y,
				this.entityCostumes[0], null
			));
			break;
		case TORCH:
			this.entities.add(new Torch(
				x, y, this.entityCostumes[3]
			));
			break;
		case B_COIN:
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[2],
				entityType
			));
			break;
		case S_COIN:
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[2],
				entityType
			));
			break;
		case G_COIN:
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[2],
				entityType
			));
		}
	} /* End method setEntity */


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
		if (t.getAttributes().contains(Attribute.EMPTY)) {
			return null; // Tile is air / empty
		}

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
	public BufferedImage getLevel(SpriteHandler th, double zoom) {
		int u = (int)(Settings.UNIT*zoom); // Dimensions for one tile
		int xDim = this.cols * u;    // x-width of the whole image
		int yDim = this.rows * u;    // y-width of the whole image

		BufferedImage ret = new BufferedImage( // Image to be returned
			xDim, yDim,                    // Dimensions
			BufferedImage.TYPE_INT_ARGB    // Type
		);
		Graphics2D g2d = ret.createGraphics(); // Draw tiles onto image
		g2d.setRenderingHints(this.enableAntiAlias);
		g2d.setRenderingHints(this.enableHighQuality);

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


	/**
	 *
	 */
	public Area getClimbable() { return this.climbable; }

	public Point2D.Double getPlayerStart() { return this.pCoords; }

	public ArrayList<Entity> getEntities() { return this.entities; }

	public double getWidth() { return this.cols * Settings.UNIT * this.zoom; }
	public double getHeight() { return this.rows * Settings.UNIT * this.zoom; }


	public Biome getBiome() { return this.biome; }

} /* End class Level */
