// ------------------------------------------------------------------------- //
// Represents an in-game item                                                //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-10                                                   //
// Finish date: 2022-01-20                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

enum ItemType {
	B_COIN(0, Attribute.COIN_1),   // Bronze coin
	S_COIN(1, Attribute.COIN_10),  // Silver coin
	G_COIN(2, Attribute.COIN_100), // Gold coin
	HEALTH(5, Attribute.HP_1);     // One health-point (red diamond)

	public final int spriteNum;  // Costume number in spritesheet
	public final Attribute attr; // The attribute on the player

	/**
	 * Represents the type of an Item.
	 *
	 * The `spriteNum` field allows easy access for Items to determine their
	 * costume number on a spritesheet.
	 */
	private ItemType(int spriteNum, Attribute attr) {
		this.spriteNum = spriteNum;
		this.attr = attr;
	} /* End constructor */

} /* End enum ItemType */


public class Item extends Entity {

	ItemType type; // Stores the type of the item.

	/**
	 * Represents one consumable item that the player can take.
	 * 
	 * Items come in the form of coins for score and diamonds for effects.
	 * The sprite of an item and the type are both convienently indicated by
	 * the ItemType class.
	 */
	public Item(double x, double y, SpriteHandler costumes, ItemType type) {
		super( // Use the constructor of base Entity for most setup
			"item",         // Name of entity
			0,              // Health points (none since it is item)
			costumes,       // SpriteHandler for costumes

			// Get how big the item is as a Rectangle

			// Deep-copy rectangle bounds or else when Item modifies
			// it all other items using that bound will also be
			// affected
			(Rectangle2D) costumes.getBounds(type.spriteNum, 0).clone(),
			null // No attributes
		);

		this.setX(x); // Set X and Y of bounds
		this.setY(y);

		this.type = type; // Type in Enum also efficiently determines costume.
	} /* End constructor */


	/**
	 * Get image representation of item.
	 *
	 * For items, simply look like the object to get.
	 */
	@Override
	public BufferedImage getSprite() {
		return costumes.getTile(type.spriteNum, 0);
	} /* End method getSprite */


	/**
	 * Get one time effect of item.
	 *
	 * This method is so that the Player can know what to apply
	 * when coming into contact with item.
	 */
	public Attribute getAttribute() { return this.type.attr; }

} /* End class Item */
