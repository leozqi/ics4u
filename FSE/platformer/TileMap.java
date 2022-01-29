// ------------------------------------------------------------------------- //
// The TileMap class stores "per-tile" data of a current level.              //
//                                                                           //
// Package:  platformer                                                      //
// Filename: TileMap.java                                                    //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.util.Collections;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class TileMap {

	/* Tile location */
	private int gridX; // Grid position (x or y). NOT real pixel position.
	private int gridY;

	// Each tile has a list of attributes on how it affects the player
	private ArrayList<Attribute> attrs = new ArrayList<Attribute>();

	// A tile may represent the starting location of an entity
	private EntityType entityType = EntityType.NONE;

	/**
	 * Create tile representing location in levelmap with attributes.
	 */
	TileMap(int gridX, int gridY, Attribute... attrs) {
		this.gridX = gridX;
		this.gridY = gridY;

		if (attrs != null) {
			// Add all of array into ArrayList
			Collections.addAll(this.attrs, attrs);
		}
	} /* End constructor */


	/**
	 * Create empty tile.
	 * 
	 * @param gridX column position of tile.
	 * @param gridY row position of tile.
	 */
	TileMap() { this(0, 0, Attribute.EMPTY); }


	/**
	 * Create tile representing entity start.
	 * 
	 * @param type type of Entity.
	 */
	TileMap(EntityType type) {
		this();
		this.entityType = type;
	} /* End constructor */


	/**
	 * Get the image of the tile this TileMap represents from sheet.
	 *
	 * TileMap stores the location of the image of the TileMap within
	 * *a* sheet. The sheet specified can differ to create different
	 * effects.
	 * 
	 * @param sheet SpriteHandler representing spritesheet to use.
	 * @return BufferedImage of tile, or null if no tile.
	 */
	public BufferedImage getTile(SpriteHandler sheet) {
		// If the tile does not represent a tile
		if (this.attrs.contains(Attribute.EMPTY)) { return null; }

		try {
			return sheet.getTile(this.gridX, this.gridY);
		} catch (IllegalArgumentException e) {
			return null;
		}
	} /* End method getTile */


	/**
	 * Get X position of tile.
	 */
	public int getX() { return this.gridX; }


	/**
	 * Get Y position of tile.
	 */
	public int getY() { return this.gridY; }


	/**
	 * Get entity type of TileMap.
	 *
	 * @return EntityType of tile, or EntityType.NONE if it is not an entity.
	 */
	public EntityType getEntityType() { return this.entityType; }


	/**
	 * Get tile attributes of TileMap.
	 *
	 * @return Attributes as ArrayList
	 */
	public ArrayList<Attribute> getAttributes() {
		return this.attrs;
	} /* End method getAttributes */


	/**
	 * Create a TileMap from a character.
	 *
	 * This is used to load levels from text files.
	 * 
	 * @param biome Biome of the level to use.
	 * @param c Character.
	 * @return TileMap of resulting tile or TileMap with EMPTY attribute
	 *         for no tile / air in that position.
	 */
	public static TileMap fromChar(Biome biome, int c) {
		switch (c) {
		/* ENTITIES */
			case 'P': // Player
				return new TileMap(EntityType.PLAYER);
			case 'S': // Slime
				return new TileMap(EntityType.SLIME);
			case '@': // Flag
				return new TileMap(EntityType.FLAG);
			case 'c': // Bronze coin
				return new TileMap(EntityType.B_COIN);
			case 'C': // Silver coin
				return new TileMap(EntityType.S_COIN);
			case '$': // Gold coin
				return new TileMap(EntityType.G_COIN);
		/* SPECIAL EFFECTS */
			case 'x': // BOX
				return new TileMap(0, 0);
			case 'X': // CROSSED_BOX
				return new TileMap(1, 0);
			case '!': // EXCLAMATION_BOX
				return new TileMap(2, 0, Attribute.ITEMBOX);
			case '?': // DISABLED_BOX
				return new TileMap(3, 0);
			case '+': // BLOCK
				return new TileMap(4, 0);
			case 'B': // BRIDGE
				return new TileMap(5, 0, Attribute.NOT_SQUARE);
			case 'b': // BRIDGE_HORIZONTAL
				return new TileMap(6, 0, Attribute.NOT_SQUARE);
			case 'F': // FENCE
				return new TileMap(7, 0, Attribute.PASSABLE);
			case 'f': // FENCE_BROKEN
				return new TileMap(8, 0, Attribute.PASSABLE);
			case '[': // LADDER
				return new TileMap(9, 0, Attribute.PASSABLE,
					Attribute.CLIMBABLE
				);
			case '{': // LADDER_TOP
				return new TileMap(10, 0,
					Attribute.PASSABLE,
					Attribute.CLIMBABLE
				);
			case 'L': // LAVA
				return new TileMap(11, 0,
					Attribute.PASSABLE,
					Attribute.DEADLY
				);
			case 'l': // LAVA_TOP
				return new TileMap(12, 0,
					Attribute.PASSABLE,
					Attribute.DEADLY
				);
			case 'W': // WATER
				return new TileMap(13, 0,
					Attribute.PASSABLE,
					Attribute.CLIMBABLE
				);
			case 'w': // WATER_TOP
				return new TileMap(14, 0,
					Attribute.PASSABLE,
					Attribute.CLIMBABLE,
					Attribute.NOT_SQUARE
				);
			case '~': // SIGN_LEFT
				return new TileMap(15, 0,
					Attribute.PASSABLE
				);
			case '`': // SIGN_RIGHT
				return new TileMap(16, 0,
					Attribute.PASSABLE
				);
			case 'T': // TORCH
				return new TileMap(17, 0, Attribute.PASSABLE);
			case 'I': // TORCH_OUT
				return new TileMap(19, 0, Attribute.PASSABLE);
			case '_': // WINDOW
				return new TileMap(20, 0, Attribute.PASSABLE);
		/* BLOCK ELEMENTS */
			case 'R': // BLOCK_ROUNDED
				return new TileMap(0, biome.row);
			case '=': // BLOCK_DEPTH
				return new TileMap(1, biome.row);
			case 'r': // BLOCK_DEPTH_ROUNDED
				return new TileMap(2, biome.row);
			case '(': // BLOCK_LEDGE_ROUNDED_LEFT
				return new TileMap(3, biome.row, Attribute.NOT_SQUARE);
			case '<': // BLOCK_LEDGE_SHARP_LEFT
				return new TileMap(4, biome.row, Attribute.NOT_SQUARE);
			case ')': // BLOCK_LEDGE_ROUNDED_RIGHT
				return new TileMap(5, biome.row, Attribute.NOT_SQUARE);
			case '>': // BLOCK_LEDGE_SHARP_RIGHT
				return new TileMap(6, biome.row, Attribute.NOT_SQUARE);
			case '.': // PLATFORM_ROUNDED
				return new TileMap(7, biome.row, Attribute.NOT_SQUARE);
			case ':': // PLATFORM_ROUNDED_LEFT
				return new TileMap(8, biome.row, Attribute.NOT_SQUARE);
			case '^': // PLATFORM_CENTER
				return new TileMap(9, biome.row, Attribute.NOT_SQUARE);
			case ';': // PLATFORM_ROUNDED_RIGHT
				return new TileMap(10, biome.row, Attribute.NOT_SQUARE);
			case '/': // SLOPE_LEFT
				return new TileMap(11, biome.row, Attribute.NOT_SQUARE);
			case '\'': // SLOPE_CORNER_LEFT
				return new TileMap(12, biome.row);
			case '|': // SLOPE_RIGHT
				return new TileMap(13, biome.row, Attribute.NOT_SQUARE);
			case '"': // SLOPE_CORNER_RIGHT
				return new TileMap(14, biome.row);
			case '#': // BLOCK_ROUNDED_LEFT
				return new TileMap(15, biome.row);
			case '-': // BLOCK
				return new TileMap(16, biome.row);
			case '*': // BLOCK_ROUNDED_RIGHT
				return new TileMap(17, biome.row);
		/* DEFAULT */
			default: // AIR
				return new TileMap();
			}
	} /* End method fromChar */

} /* End class TileMap */
