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

public class Flag extends Entity {

	/**
	 * Represents one consumable item that the player can take.
	 * 
	 * Items come in the form of coins for score and diamonds for effects.
	 * The sprite of an item and the type are both convienently indicated by
	 * the ItemType class.
	 */
	public Flag(double x, double y, SpriteHandler costumes) {
		super( // Use the constructor of base Entity for most setup
			0,              // Health points (none since it is item)
			costumes,       // SpriteHandler for costumes

			// Get how big the item is as a Rectangle

			// Deep-copy rectangle bounds or else when Item modifies
			// it all other items using that bound will also be
			// affected
			(Rectangle2D) costumes.getBounds(0, 0).clone(),
			null // No attributes
		);

		this.setX(x); // Set X and Y of bounds
		this.setY(y);
	} /* End constructor */


	@Override
	public void updateTick() {
		if (spriteCnt > 1) {
			spriteCnt = 0;
		} else {
			spriteCnt++;
		}
	} /* End method updateTick */


	/**
	 * Get image representation of item.
	 *
	 * For items, simply look like the object to get.
	 */
	@Override
	public BufferedImage getSprite() {
		return costumes.getTile(spriteCnt, 0, false);
	} /* End method getSprite */

} /* End class Item */
