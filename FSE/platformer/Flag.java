// ------------------------------------------------------------------------- //
// The Flag class represents the endgame goal for a level.                   //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Flag.java                                                       //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

public class Flag extends Entity {

	/**
	 * Represents the game endgoal.
	 * 
	 * When the player reaches the animated flag, the game ends as victory.
	 */
	public Flag(double x, double y, SpriteHandler costumes) {
		super( // Use the constructor of base Entity for most setup
			costumes,       // SpriteHandler for costumes

			// Get hitbox based on flag costume
			(Rectangle2D) costumes.getBounds(0, 0).clone(),
			null // No attributes
		);
		super.tickTime = 30;
		super.stationary = true;
		this.setX(x); // Set X and Y of bounds
		this.setY(y);
	} /* End constructor */


	/**
	 * Update animation step of flag.
	 *
	 * The animation step of the flag is updated every tick.
	 */
	@Override
	public void updateTick() {
		/* Update sprite count (animation step) */
		if (spriteCnt > 0) {
			spriteCnt = 0;
		} else {
			spriteCnt++;
		}
	} /* End method updateTick */


	/**
	 * Get image representation of Flag's current animation step.
	 */
	@Override
	public BufferedImage getSprite() {
		return costumes.getTile(spriteCnt, 0, false);
	} /* End method getSprite */

} /* End class Flag */
