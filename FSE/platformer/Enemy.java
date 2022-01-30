// ------------------------------------------------------------------------- //
// The Enemy class represents various enemies as Entities on the map.        //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Enemy.java                                                      //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.awt.geom.*;

public class Enemy extends Entity {

	double speed = 1d; // Default speed of entity as constant velocity.
	EntityType type;

	/**
	 * Create an enemy at real coordinates X and Y.
	 *
	 * Enemies are deadly to the player, who only has one health. Currently
	 * two have been implemented, slime and fly, using this same class.
	 * The slime is a ground-moving enemy affected by gravity. The fly is
	 * a flying enemy not affected by gravity. Both move side to side.
	 *
	 * Enemies cannot be killed by the player due to gameplay reasons; it
	 * would make the game easier. However, if they drop below level limits
	 * or come into contact with deadly objects, they are marked dead and
	 * removed from the level.
	 *
	 * @param entity type of the entity
	 * @param x real-x position of the entity on the level map.
	 * @param y real-y position of the entity on the level map.
	 * @param costumes the different sprite animation steps of the enemy.
	 * @param attrs additional attributes of the entity applied on creation.l
	 *              See Attribute.java for more details.
	 */
	public Enemy(
		EntityType entity, double x, double y, SpriteHandler costumes,
		Attribute[] attrs
	) {
		// Use the Entity constructor to fulfill most creation
		// functionality.
		super(
			costumes,  // SpriteHandler holding image resources

			// Create bounding box using dimensions of costumes:
			new Rectangle2D.Double(
				x, y,                // Starting position
				costumes.getXSize(), // Width
				costumes.getYSize()  // Height
			),
			attrs      // Additional applied attributes
		);
		this.type = entity; // Set type of enemy

		// Change speed and tick-time depending on enemy type
		switch(this.type) {
		case SLIME:
			this.speed = 2d;
			super.tickTime = 30d;
			break;
		case FLY:
			this.speed = 5d;
			super.tickTime = 15d;
			break;
		}
	} /* End constructor */


	/**
	 * Updates the Enemy's conditions for one frame.
	 *
	 * The main game loop calls this method to update enemies' status,
	 * including coordinate position and collisions.
	 *
	 * @param diffT difference in time
	 * @param bounds Shape representing collision boxes entity should be
	 *               aware of and cannot go into.
	 */
	@Override
	public void update(double diffT, Shape bounds) {
		super.update(diffT, bounds); // Update tick time

		/* Adjust velY for gravity (if not flying) */
		switch(this.type) {
		case SLIME:
			super.adjustVelY(diffT);
			break;
		}

		if (this.right) {
			// Moving right
			this.setAccelX(1); // Accel represents direction as
			                   // enemy velocity is constant
		} else {
			// Moving left
			this.setAccelX(-1);
		}

		// Move taking into account collision boxes provided by bounds.
		// This method is common to all entities.
		// Apply an adjustment of time per frame and Zoom factor.
		super.boundedMove(
			(int)(diffT * this.xVel * Settings.zoom()),
			(int)(diffT * this.yVel * Settings.zoom()),
			bounds
		);

		this.setVelX(0); // Set X velocity to zero.
	} /* End method update */


	/**
	 * Update enemy conditions per constant tick.
	 *
	 * Enemy sprite costumes are updated during this method. Since enemies
	 * all move at a constant velocity, speeds are updated here too.
	 */
	@Override
	public void updateTick() {
		// Adjust enemy's costumes differently depending on type
		// Each type has a different spritesheet.
		int toggleLim = 1; // Sprite number to switch to beginning of sheet
		int start = 0;     // Sprite number that is beginning of sheet
		switch (type) {
		case SLIME:
			toggleLim = 1;
			start = 0;
			break;
		case FLY:
			toggleLim = 0;
			start = 0;
			break;
		}

		// Change current costume number based on above
		if (spriteCnt > toggleLim) {
			spriteCnt = start;
		} else {
			spriteCnt++;
		}

		// Constant movement is also done per tick
		// Each tick, move `speed` steps (for only that tick) at certain speed
		this.setVelX(this.right ? this.speed : -this.speed);
	} /* End method updateTick */


	/**
	 * Get current sprite of enemy.
	 *
	 * @return costume of enemy based on enemy's current direction (facing
	 *         left or right).
	 */
	@Override
	public BufferedImage getSprite() {
		// Default direction for sprites is facing right.
		// costumes.getTile(SpriteHandler column, row, isReversed)
		return costumes.getTile(spriteCnt, 0, super.isMovingLeft());
	} /* End method getSprite */

} /* End class Enemy */
