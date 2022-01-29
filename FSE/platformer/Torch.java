// ------------------------------------------------------------------------- //
// The Torch class represents a flickering torch which provides a light      //
// effect in "dark" biomes.                                                  //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Torch.java                                                      //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

public class Torch extends Entity {

	/**
	 * Represents a flickering light source.
	 */
	public Torch(double x, double y, SpriteHandler costumes) {
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
		super.applyAttribute(Attribute.LIGHTING);

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
	 * Get image representation of torch.
	 */
	@Override
	public BufferedImage getSprite() {
		return costumes.getTile(spriteCnt, 0, false);
	} /* End method getSprite */

} /* End class Item */
