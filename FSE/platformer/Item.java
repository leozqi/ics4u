// ------------------------------------------------------------------------- //
// The Item class represents an in-game item that applies a one-time effect  //
// to the player (or potentially entities).                                  //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Item.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Item extends Entity {

	EntityType type; // Stores the type of the item.

	/**
	 * Represents one consumable item that the player can take.
	 * 
	 * Items come in the form of coins for score and diamonds for effects.
	 * The sprite of an item and the type are both convienently indicated by
	 * the EntityType class.
	 */
	public Item(double x, double y, SpriteHandler costumes, EntityType type) {
		super( // Use the constructor of base Entity for most setup
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
		return costumes.getTile(type.spriteNum, 0, false);
	} /* End method getSprite */


	/**
	 * Create a random coin for itemboxes.
	 *
	 * There is a ~10% chance for a gold coin, ~20% chance for a silver coin,
	 * and ~70% chance for a bronze coin to be returned.
	 *
	 * @param x real-x position of the coin to appear.
	 * @param y real-y position of coin to appear.
	 * @param costumes SpriteHandler with image resources to use.
	 * @return Item created.
	 */
	public static Item randomCoin(double x, double y, SpriteHandler costumes) {
		Random rand = new Random();

		// Use random integer to determine coin
		int choice = rand.nextInt(100);

		EntityType type;
		if (choice > 89) {
			// 10% chance for gold
			type = EntityType.G_COIN;
		} else if (choice < 20) {
			// 20% chance for silver
			type = EntityType.S_COIN;
		} else {
			// 70% chance for bronze
			type = EntityType.B_COIN;
		}

		return new Item(x, y, costumes, type);

	} /* End method randomCoin */


	/**
	 * Get one time effect of item.
	 *
	 * This method is so that the Player can know what to apply
	 * when coming into contact with item.
	 */
	public Attribute getAttribute() { return this.type.attr; }

} /* End class Item */
