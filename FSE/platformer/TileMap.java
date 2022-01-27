// ------------------------------------------------------------------------- //
// Provides an interface to spritesheet tiles via SpriteHandler              //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2021-12-09                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;


enum Biome {
	SWAMPY,
	SNOWY,
	SANDY,
	GRASSY
}

enum EntityType {
	NONE(0),
	PLAYER(5),
	SLIME(1);

	public final int hp;

	private EntityType(int hp) {
		this.hp = hp;
	} /* End constructor */
}

public class TileMap {

	private int gridX;
	private int gridY;
	private int xRange;
	private int yRange;
	private boolean empty = false;
	private boolean passable = false;
	private boolean itembox = false;
	private EntityType entityType = EntityType.NONE;

	TileMap(int gridX, int gridY, int xRange, int yRange) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.xRange = xRange;
		this.yRange = yRange;
	}

	TileMap(int gridX, int gridY) { this(gridX, gridY, 1, 1); }

	TileMap(int gridX, int gridY, boolean passable, boolean itembox) {
		this(gridX, gridY, 1, 1);
		this.passable = passable;
		this.itembox = itembox;
	}


	TileMap(EntityType type) {
		this(0, 0, 0, 0);
		this.empty = true;
		this.entityType = type;
	}

	TileMap() {
		this(0, 0, 0, 0);
		this.empty = true;
	}

	public BufferedImage getTile(SpriteHandler sheet) {
		if (this.empty) {
			return null;
		}
		try {
			return sheet.getTile(this.gridX, this.gridY);
		} catch (IllegalArgumentException e) {
			return null;
		}
	} /* End method getTile */


	public int getGridX()    { return this.gridX;  }
	public int getGridY()    { return this.gridY;  }
	public int getXRange()   { return this.xRange; }
	public int getYRange()   { return this.yRange; }
	public boolean isEmpty() { return this.empty;  }
	public boolean isPassable() {
		if (this.empty) {
			return true;
		} else {
			return this.passable;
		}
	}
	public boolean isItemBox() {
		if (this.empty) {
			return false;
		} else {
			return this.itembox;
		}
	}
	public EntityType getEntityType() {
		return this.entityType;
	}

	/* Layers: multiple TileMap arrays can form layers on top of existing textures */
	public static TileMap fromChar(Biome b, int c) {
		switch (c) {
		case 'P': // Player
			return new TileMap(EntityType.PLAYER);
		case 'S': // Slime
			return new TileMap(EntityType.SLIME);
		case 'b': // BLOCK
			return new TileMap(3, 0);
		case 'B': // BLOCK_BORDER_EMPTY
			return new TileMap(0, 6);
		case 'O': // BLOCK_BORDER_BUTTON
			return new TileMap(0, 8);
		case '#': // BLOCK_BORDER_WARNING
			return new TileMap(0, 5, false, true);
		case '%': // BLOCK_BORDER_EXCLAIM
			return new TileMap(0, 0, false, true);

		case 'X': // BOX_CROSSED
			return new TileMap(0, 11);
		case 'x': // BOX_SLASH
			return new TileMap(0, 12);
		case '?': // BOX_MYSTERY
			return new TileMap(1, 9, false, true);
		case '_': // OVERLAY_WINDOW
			return new TileMap(1, 0);
		case '^': // OVERLAY_TORCH
			return new TileMap(1, 1, 1, 3);
		case '{': // OVERLAY_LADDER_TOP
			return new TileMap(7, 1, true, false);
		case '[': // OVERLAY_LADDER
			return new TileMap(7, 2, true, false);
		case '-': // GROUND
			switch (b) {
			case SWAMPY:
				return new TileMap(1, 6);
			case SNOWY:
				return new TileMap(2, 10);
			case SANDY:
				return new TileMap(4, 8);
			}
		case '>': // GROUND_RIGHT_END
			switch (b) {
			case SWAMPY:
				return new TileMap(2, 3);
			case SNOWY:
				return new TileMap(3, 11);
			case SANDY:
				return new TileMap(4, 7);
			}
		case '<': // GROUND_LEFT_END
			switch (b) {
			case SWAMPY:
				return new TileMap(2, 5);
			case SNOWY:
				return new TileMap(4, 0);
			case SANDY:
				return new TileMap(4, 9);
			}
		case '=': // GROUND_DEPTH
			switch (b) {
			case SWAMPY:
				return new TileMap(2, 7);
			case SNOWY:
				return new TileMap(4, 1);
			case SANDY:
				return new TileMap(4, 1);
			case GRASSY:
				return new TileMap(8, 12);
			}
		case ')': // GROUND_SLOPE_DOWN
			switch (b) {
			case SWAMPY:
				return new TileMap(6, 2);
			case SNOWY:
				return new TileMap(3, 3);
			case SANDY:
				return new TileMap(5, 0);
			}
		case '(': // GROUND_SLOPE_UP
			switch (b) {
			case SWAMPY:
				return new TileMap(6, 3);
			case SNOWY:
				return new TileMap(3, 5);
			case SANDY:
				return new TileMap(5, 2);
			}
		case ',': // GROUND_SLOPE_DOWN_END
			switch (b) {
			case SWAMPY:
				return new TileMap(1, 10);
			case SNOWY:
				return new TileMap(3, 2);
			case SANDY:
				return new TileMap(4, 12);
			}
		case '.': // GROUND_SLOPE_UP_END
			switch (b) {
			case SWAMPY:
				return new TileMap(1, 11);
			case SNOWY:
				return new TileMap(3, 4);
			case SANDY:
				return new TileMap(5, 1);
			}
		case '~': // PLATFORM
			switch (b) {
			case SWAMPY:
				return new TileMap(2, 0);
			case SNOWY:
				return new TileMap(3, 7);
			case SANDY:
				return new TileMap(5, 4);
			}
		default: // AIR
			return new TileMap();
		}
	} /* End method fromChar */
} /* End class TileMap */
