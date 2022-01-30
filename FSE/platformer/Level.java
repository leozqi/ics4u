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
	private Area deadly = new Area();            // Store deadly locations
	private SpecBounds boxes = new SpecBounds(); // Store special boxes
	private SpriteHandler tiles;                 // Store tile images

	// Store sprites of entities that are not the player within the level
	private SpriteHandler[] entityCostumes;

	// Biome (general appearance) of the level
	private Biome biome = Biome.GRASSY; // Default biome is GRASSY

	// Levels can be rendered in high quality because their image is
	// only processed once.

	// Enable antialiasing for Graphics2D object
	private RenderingHints enableAntiAlias = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON
	);
	// Enable high-quality rendering for levels
	private RenderingHints enableHighQuality = new RenderingHints(
		RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY
	);

	private double zoom; // Zoom of the level (determined at creation)

	/**
	 * The Level class holds the data for one level.
	 *
	 * Use the loadFile method afterwards to load a file into the level.
	 *
	 * @param tiles SpriteHandler holding image tiles.
	 * @param handleEntities SpriteHandler[] array for all entity costumes.
	 * @param zoom the zoom level of the level, with 1 being no zoom.
	 */
	public Level(
		SpriteHandler tiles, SpriteHandler[] handleEntities, double zoom
	) {
		this.tiles = tiles;                   // Spritesheet for all tiles
		this.entityCostumes = handleEntities; // Costumes for all entities
		this.zoom = zoom;                     // Zoom of level
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
	 * Scans the first character of the first line as the indicator for
	 * which theme or "biome" to use for the level:
	 *
	 *     'g': Grassy
	 *     'd': Dirty (dirt)
	 *     'r': Rocky
	 *     'a': Sandy
	 *     's': Snowy
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
		ArrayList<String> lines = new ArrayList<String>();

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

			// Iterate over all lines in document
			while (ln != null) {
				lines.add(ln); // Store line for further processing later
				if (ln.length() > colCnt) {
					// Find the maximum column size
					colCnt = ln.length();
				}
				ln = stdin.readLine();
				rowCnt++; // Maximum row size
			}
			stdin.close(); // Close the BufferedReader
		} catch (Exception e) {
			return false; // In event of error, return error boolean
		}
		// No more chance of error, actually change class attributes
		this.rows = rowCnt; // Record number of rows
		this.cols = colCnt; // Record number of columns

		// Create single array representing all tiles in level.
		this.map = new TileMap[rowCnt][colCnt];

		// Create tile data for each tile
		for (int row = 0; row < this.rows; row++) {
			ln = lines.get(row); // Re-get line

			// Use individual characters to build level
			for (int col = 0; col < ln.length(); col++) {
				this.setBlock(
					row, col,

					// Generate tile info based on character.
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
	 * Load biome from character.
	 *
	 * The character representing each biome is stored as part of the Biome
	 * enumeration's possible values for flexibility. Changing the Biome
	 * enumeration will change this method's behaviour.
	 *
	 * If the character does not match any biomes, no changes are made.
	 * 
	 * @param biomeCode character representing biome.
	 */
	public void loadBiome(char biomeCode) {
		// Iterate over all values of the biome enumeration
		for (Biome biome : Biome.values()) {
			if (biome.rep == biomeCode) {
				// biomeCode matches Biome's specified character
				// Therefore level is this biome
				this.biome = biome;
			}
		}
	} /* End method loadBiome */


	/**
	 * Set a block in the level as a specific type.
	 *
	 * Processes tiles in the level into blocks with boundaries that
	 * Entities take into account, or start positions for Entities.
	 *
	 * Tiles in the level have different attributes representing their
	 * special properties.
	 *
	 * Silently fails if row and column are not valid for this level.
	 *
	 * @param row row of the block
	 * @param col column of the block
	 * @param tile TileMap to set the block as
	 */
	public void setBlock(int row, int col, TileMap tile) {
		if (this.map == null) { return; } // level is not loaded, exit

		// Get the attributes for each tile
		ArrayList<Attribute> tileAttrs = tile.getAttributes();

		/* Calculate `x` and `y` coordinates of block based on tile position */
		// Settings.UNIT is the dimension of one tile at default zoom.
		int x = (int)(0 + (col * Settings.UNIT) * this.zoom);
		int y = (int)(0 + (row * Settings.UNIT) * this.zoom);

		/* Set entity information if tile represents an entity */
		if (tile.getEntityType() != EntityType.NONE) {
			this.setEntity(tile, x, y);
			return; // No need to set tile information
		}

		// Check if tile is empty; if it is, no need to do anything.
		if (tileAttrs.contains(Attribute.EMPTY)) { return; }

		/* It is a tile; add to map of all tiles */
		try {
			this.map[row][col] = tile;
		} catch (ArrayIndexOutOfBoundsException e) {
			return; // row and column do not fit; silently fail.
		}

		Area area; // Store area covered by tile

		/* Calculate the area taken up by a tile */
		if (tileAttrs.contains(Attribute.NOT_SQUARE)) {
			// Create exact boundaries for curved tiles
			area = Utilities.exactBounds(
				tile.getTile(this.tiles), x, y
			);
		} else if (tileAttrs.contains(Attribute.RECTANGULAR)) {
			// Create shrunken rectangular boundaries (not square)
			Rectangle2D tmp = (Rectangle2D.Double)(
				this.tiles.getBounds(
					tile.getX(), // X of tile
					tile.getY()  // Y of tile
				)
			).clone(); // Clone so that modifications are private

			// Change position to correspond with coordinates of tile
			tmp.setRect(x, y, tmp.getWidth(), tmp.getHeight());

			// Create area with rectangle
			area = new Area(tmp);
		} else {
			// Is a square; use simple square boundary
			area = new Area(new Rectangle2D.Double(
				x, y,
				Settings.UNIT * this.zoom,
				Settings.UNIT * this.zoom
			));
		}

		if (!tileAttrs.contains(Attribute.PASSABLE)) {
			// Not passable; add area to overall bounding-box
			this.bounds.add(area);
		}
		if (tileAttrs.contains(Attribute.ITEMBOX)) {
			// Is an itembox; add area to special bounding-boxes
			this.boxes.add(area);
		}
		if (tileAttrs.contains(Attribute.CLIMBABLE)) {
			// Climbable; add to climbable area
			this.climbable.add(area);
		}
		if (tileAttrs.contains(Attribute.DEADLY)) {
			// Deadly; add to deadly area
			this.deadly.add(area);
		}
	} /* End method setBlock */


	/**
	 * Create entities based on tile start locations.
	 *
	 * Tiles specify the starting positions of entities with a particular
	 * EntityType. This method generates an entity based on a tile's type,
	 * correct starting location, and adds it to the level's store of all
	 * Entities.
	 *
	 * Uses the Level's SpriteHandler bank, `entityCostumes`, to create
	 * resulting Entities. The index of each entity's SpriteHandler is given
	 * by their EntityType enumeration.
	 *
	 * @param tile tile containing data on entity's type.
	 * @param x real x position of entity based on tile (pixels).
	 * @param y real y position of entity based on tile (pixels).
	 */
	public void setEntity(TileMap tile, int x, int y) {
		EntityType entityType = tile.getEntityType();

		switch (entityType) {
		case FLAG:   // Entity is a flag
			this.entities.add(new Flag(
				x, y, this.entityCostumes[entityType.costume]
			));
			break;
		case PLAYER: // Entity is a player
			// Players are generated in the Game, so only a
			// starting position is needed
			this.pCoords = new Point2D.Double(x, y);
			break;
		case SLIME:  // Entity is a slime (ground enemy)
			this.entities.add(new Enemy(
				EntityType.SLIME, x, y,
				this.entityCostumes[entityType.costume], null
			));
			break;
		case FLY:    // Entity is a fly (flying enemy)
			this.entities.add(new Enemy(
				EntityType.FLY, x, y,
				this.entityCostumes[entityType.costume], null
			));
			break;
		case TORCH:  // Entity is a torch
			this.entities.add(new Torch(
				x, y, this.entityCostumes[entityType.costume]
			));
			break;
		case B_COIN: // Entity is an coin Item
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[entityType.costume],
				entityType
			));
			break;
		case S_COIN: // Entity is a coin Item
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[entityType.costume],
				entityType
			));
			break;
		case G_COIN: // Entity is a coin Item
			this.entities.add(new Item(
				x, y,
				this.entityCostumes[entityType.costume],
				entityType
			));
			break;
		}
	} /* End method setEntity */


	/**
	 * Get the data from one tile.
	 * 
	 * @param row the row of the tile.
	 * @param col the column of the tile.
	 */
	public TileMap getBlock(int row, int col) {
		// Level has not yet been loaded, return no data
		if (this.map == null) { return new TileMap(); }

		synchronized(this.map) {
			// Get and return data
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
	 * This method handles situations where no tile is created
	 * (no whitespace entered into the level file) or the tile created is
	 * empty (whitespace entered; air) in addition to normal tiles.
	 *
	 * @param row row of tile
	 * @param col column of tile
	 * @param th SpriteHandler containing spritesheet for tiles
	 * @return BufferedImage of tile or null for no image.
	 */
	public BufferedImage getTile(int row, int col, SpriteHandler th) {
		TileMap tile = this.getBlock(row, col);

		if (tile == null)   { return null; } // No tile is created
		if (tile.getAttributes().contains(Attribute.EMPTY)) {
			return null; // Tile is air / empty
		}

		BufferedImage bi = tile.getTile(th); // get image of tile

		if (bi == null) { return null; } // Failed to retrieve tile
		return bi; // Return image representation
	} /* End method getTile */


	/**
	 * Get the BufferedImage representation of the whole level.
	 *
	 * The camera renders the whole level with this one BufferedImage,
	 * meaning all loading cost is before the level is shown to the player,
	 * rather than an ongoing cost.
	 *
	 * @param th SpriteHandler to generate the level.
	 * @param zoom Zoom of the level (for the tile representation)
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
	 * Get the impassable boundaries of the level as one Area object.
	 * 
	 * This object contains the area of every impassable object in the level
	 * for collision checking.
	 *
	 * @return area of every impassable object.
	 */
	public Area getBounds() { return bounds; }


	/**
	 * Get the boundaries of special blocks as a SpecBounds object.
	 *
	 * @return area of every item block.
	 */
	public SpecBounds getSpecBounds() { return boxes; }


	/**
	 * Get the climbable area of the level.
	 *
	 * @return area that is climbable for the player.
	 */
	public Area getClimbable() { return this.climbable; }


	/**
	 * Get a "fatal zone" of the Player.
	 *
	 * @return area fatal to the Player within the level.
	 */
	public Area getDeadly() { return this.deadly; }


	/**
	 * Get the starting position of the player within the level.
	 *
	 * @return Point2D of player position.
	 */
	public Point2D.Double getPlayerStart() { return this.pCoords; }


	/**
	 * Get list of all entities currently within the level.
	 *
	 * @return ArrayList<Entity> of all entities within the level.
	 */
	public ArrayList<Entity> getEntities() { return this.entities; }


	/**
	 * Get width of level in pixels.
	 */
	public double getWidth() { return this.cols * Settings.UNIT * this.zoom; }


	/**
	 * Get height of level in pixels.
	 */
	public double getHeight() { return this.rows * Settings.UNIT * this.zoom; }


	/**
	 * Get biome of level.
	 *
	 * @return biome of the level as Biome enumeration value.
	 */
	public Biome getBiome() { return this.biome; }

} /* End class Level */
