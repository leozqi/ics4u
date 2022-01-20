// ------------------------------------------------------------------------- //
// Represents a game entity.                                                 //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-10                                                   //
// Finish date: 2022-01-20                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.concurrent.*;

enum Attribute {
	DOUBLE_JUMP,
	COIN_1,
	COIN_10,
	COIN_100,
	HP_1
}

public class Entity {

	/* Identifiers */
	String name; // Name of the entity

	/* Properties */
	SpriteHandler costumes; // Sprites and image animations for the entity
	Rectangle2D.Double bounds; // Bounding-box (collision box) for entity
	int hp; // Health points of the entity
	int coin = 0; // Coins of the entity

	// DelayQueue for effects
	// The current effect is the element at head that is being peeked
	// When the delay is over and the element is removed, it CEASES effect
	DelayQueue<Effect> effects = new DelayQueue<Effect>();

	/* Movement */
	double xVel = 0; // X velocity of entity
	double yVel = 0; // Y velocity of entity
	double xAccel = 0; // X acceleration of entity
	double yAccel = Settings.E_GRAVITY; // Y acceleration of entity
	int jumpCnt = 0; // Number of times entity has already jumped
                         // (before hitting the ground)
	int jumpLimit = 1; // Limit of jumps before entity cannot jump

	/**
	 * Create a new entity.
	 *
	 * Entities are the parts of the game that do not have a fixed position
	 * within the level. They should usually be created through a subclass
	 * of Entity, like Player or Item.
	 */
	public Entity(
		String name, int hp, SpriteHandler costumes, Rectangle2D bounds,
		Attribute[] attrs
	) {
		this.name = name;
		this.hp = hp;
		this.costumes = costumes;
		this.bounds = (Rectangle2D.Double) bounds;

		// Process each attribute, if there are any
		if (attrs == null) { return; }
		for (int i = 0; i < attrs.length; i++) {
			applyAttribute(attrs[i]);
		}
	} /* End constructor */


	/**
	 * Get the current health of the entity.
	 */
	public int getHealth() { return hp; }


	/**
	 * Apply a status effect to the entity.
	 */
	public void applyStatus(Effect e) { effects.add(e); }


	/**
	 * Apply an attribute to the entity.
	 * 
	 * Attributes make it easier to specify base properties of entities
	 * without making a new class. In effect, they are *effects* applied
	 * instantly. This is used, for example, with the Item class.
	 */
	public void applyAttribute(Attribute a) {
		switch (a) {
		case DOUBLE_JUMP:
			jumpLimit = 2;
			break;
		case COIN_1:
			coin++;
			break;
		case COIN_10:
			coin += 10;
			break;
		case COIN_100:
			coin += 100;
			break;
		case HP_1:
			hp += 1;
			break;
		}
	} /* End method applyAttribute */


	/**
	 * Updates the entity's conditions for one frame.
	 *
	 * The main game loop calls this method to update an entity's status,
	 * including coordinate position, etc.
	 *
	 * Updated conditions are constant regardless of time between frames
	 * due to use of a difference in time diffT.
	 *
	 * Classes inheriting Entity should override this method to implement
	 * their own functionality.
	 *
	 * @param diffT difference in time
	 * @param bounds shape representing collision boxes entity should be
	 *               aware of.
	 */
	public void update(double diffT, Shape bounds) {}


	/**
	 * Adjusts velocity based on acceleration.
	 *
	 * @param diffT time adjustment between frames.
	 */
	public void adjustVelocity(double diffT) {
		if (this.xAccel > 0) {
			if (this.xVel < 0.5 && this.xVel > 0) {
				this.xVel = 0.5;
			} else if (this.xVel < Settings.P_MAX_SPD) {
				this.xVel += (diffT*this.xAccel);
			}
		} else if (this.xAccel < 0) {
			if (this.xVel > -0.5 && this.xVel < 0) {
				this.xVel = -0.5;
			} else if (this.xVel > -Settings.P_MAX_SPD) {
				this.xVel += (diffT*this.xAccel);
			}
		} else {
			if (Math.abs(this.xVel) < 0.5) {
				this.xVel = 0;
			}
			if (this.xVel > 0) {
				this.xVel -= (diffT*0.1);
			} else if (this.xVel < 0) {
				this.xVel += (diffT*0.1);
			}
		}

		this.yVel += (diffT*this.yAccel);
	} /* End method adjustVelocity */


	/**
	 * Move the entity (x, y) units from its current position.
	 *
	 * Entities' origins are at the top-left corner, as is the screen's
	 * origin.
	 *
	 * (0, 0) ------------ + x
	 * |
	 * |
	 * |
	 * | +y
	 *
	 */
	public void move(double x, double y) {
		synchronized (this.bounds) {
			this.bounds.setRect(
				this.bounds.getX() + x,
				this.bounds.getY() + y,
				this.bounds.getWidth(),
				this.bounds.getHeight()
			);
		}
	} /* End method move */


	/**
	 * Move restricting movement that intersects with shape `bounds`.
	 *
	 * Bounds can be any costumesape, but for this program is the Area class:
	 *     java.awt.geom.Area.
	 *
	 * It works by gradually moving the entity one coordinate in the x and
	 * y directions at a time until one of those dimensions hits a bound
	 * or the specified distance has been covered.
	 *
	 * Then, it continues moving one coordinate in the other direction until
	 * it too hits a bound.
	 *
	 * This method is inefficient, but is the simplest method of collision
	 * detection *and* is more efficient than checking MANY costumesapes (i.e.
	 * each tile) individually.
	 */
	public void boundedMove(double x, double y, Shape bounds) {
		boolean xReachedLim = false; // hit boundary in X dimension
		boolean yReachedLim = false; // hit boundary in Y dimension

		int xInc = (x > 0) ? 1 : -1; // set x increment by value
		int yInc = (y > 0) ? 1 : -1; // set y increment by value

		int cnt = 0; // how many increments have passed

		while (true) {
			// Boundary not reached; requested x distance not reached
			if (!xReachedLim && cnt < Math.abs(x)) {
				// Increment player Rectangle x
				this.bounds.x += xInc;

				// Check for collision
				if (bounds.intersects(this.bounds)) {
					// Go back so object is *almost*
					// colliding, but not actually
					this.bounds.x -= xInc;

					xReachedLim = true;
				}
			}
			// Boundary not reached; requested y distance not reached
			if (!yReachedLim && cnt < Math.abs(y)) {
				// Increment player Rectangle y
				this.bounds.y += yInc;

				// Check for collision
				if (bounds.intersects(this.bounds)) {
					this.bounds.y -= yInc;
					yReachedLim = true;

					// Reached boundary while falling
					// Decrement jump counter
					// Stop falling
					if (y > 0) {
						if (this.jumpCnt > 0) {
							this.jumpCnt--;
						}
						this.yVel = 0;
					}
				}
			}
			cnt++; // Increment movement

			// Both boundaries have been reached OR distance moved
			// is satisfied
			if (
				(xReachedLim && yReachedLim)
				|| (cnt >= Math.abs(x) && cnt >= Math.abs(y))
			) {
				break; // Exit loop
			}
		} /* End while loop */
	} /* End method boundedMove */


	/**
	 * Return the sprite of the current animation step.
	 *
	 * Classes inheriting the Entity class costumesould change this to a different
	 * return value reflecting the SpriteHandler and Spritesheet they use.
	 */
	public BufferedImage getSprite() { return costumes.getTile(0, 0); }


	/**
	 * Return X/Y coordinates at entity origin.
	 */
	public Point2D.Double getPoint() {
		// Package X and Y into a Point2D object to return both at once
		return new Point2D.Double(this.bounds.getX(), this.bounds.getY());
	} /* End method getPoint */


	/**
	 * Return centre x position of the sprite.
	 *
	 * Conveinence method to get centre x of entity; all it does is call
	 * Utilities.midpoint with the entity's coordinates and width.
	 */ 
	public int getCentreX() {
		return (int) Utilities.midpoint(
			this.bounds.getX(), this.bounds.getWidth()
		);
	} /* End method getCentreX */


	/**
	 * Return centre y position of the sprite.
	 */
	public int getCentreY() {
		return (int) Utilities.midpoint(
			this.bounds.getY(), this.bounds.getHeight()
		);
	} /* End method getCentreY */


	/**
	 * Set the acceleration and velocities of the entity manually.
	 */
	public synchronized void setAccelX(double accel) { this.xAccel = accel; }
	public synchronized void setAccelY(double accel) { this.yAccel = accel; }
	public synchronized void setVelX(double vel) { this.xVel = vel; }
	public synchronized void setVelY(double vel) { this.yVel = vel; }
	public synchronized void setX(double x) { this.bounds.x = x; }
	public synchronized void setY(double y) { this.bounds.y = y; }


	public Rectangle2D.Double getBounds() { return this.bounds; }
	public boolean isMovingLeft() { return (this.xAccel < 0); }
	public boolean isMovingRight() { return (this.xAccel > 0); }
	public boolean isMovingUp() { return (this.yAccel < 0); }
	public boolean isMovingDown() { return (this.yAccel > 0); }
	public boolean isTouchingGround() { return (this.yVel == 0); }

	/**
	 * Jump with a starting velocity of `vel`.
	 *
	 * This method will consume one "jump". Players can be configured to
	 * have more than one "jump" before landing.
	 */
	public synchronized void jump(double vel) {
		if (jumpCnt < jumpLimit) {
			this.yVel = vel;
			jumpCnt++;
		}
	} /* End method jump */


	public boolean isTouching(Rectangle2D.Double bounds) {
		if (this.bounds.intersects(bounds)) {
			return true;
		}
		return false;
	} /* End method isTouching */

} /* End class Level */
