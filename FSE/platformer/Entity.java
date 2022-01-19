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
	DOUBLE_JUMP
}

public class Entity {

	/* Identifiers */
	String name;

	/* Properties */
	SpriteHandler sh;
	Rectangle2D.Double coords;
	int hp;
	double speed = 0.05;
	DelayQueue<Effect> effects = new DelayQueue<Effect>();

	/* Movement */
	double xVel = 0;
	double yVel = 0;
	double xAccel = 0;
	double yAccel = 0.09;
	int jumpCnt = 0;
	int jumpLimit = 1;

	/**
	 * Create a new entity.
	 *
	 * Entities should usually be created through a subclass.
	 */
	public Entity(
		String name, int hp, SpriteHandler sh, Rectangle2D bounds,
		Attribute[] attrs
	) {
		this.name = name;
		this.hp = hp;
		this.sh = sh;
		this.coords = (Rectangle2D.Double) bounds;

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
	 * without making a new class.
	 */
	public void applyAttribute(Attribute a) {
		switch (a) {
		case DOUBLE_JUMP:
			jumpLimit = 2;
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
	 * @param bounds Shape representing collision boxes entity should be
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
		synchronized (this.coords) {
			this.coords.setRect(
				this.coords.getX() + x,
				this.coords.getY() + y,
				this.coords.getWidth(),
				this.coords.getHeight()
			);
		}
	} /* End method move */


	/**
	 * Move restricting movement that intersects with Shape `bounds`.
	 *
	 * Bounds can be any shape, but for this program is the Area class:
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
	 * detection *and* is more efficient than checking MANY shapes (i.e.
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
				this.coords.x += xInc;

				// Check for collision
				if (bounds.intersects(this.coords)) {
					// Go back so object is *almost*
					// colliding, but not actually
					this.coords.x -= xInc;

					xReachedLim = true;
				}
			}
			if (!yReachedLim && cnt < Math.abs(y)) {
				// Increment player Rectangle y
				this.coords.y += yInc;

				// Check for collision
				if (bounds.intersects(this.coords)) {
					this.coords.y -= yInc;
					yReachedLim = true;

					// Reached boundary while falling
					// Decrement jump counter
					// Stop falling
					if (y > 0) {
						if (this.jumpCnt > 0) {
							this.jumpCnt--;
						}
						this.yVel = 0;
						//this.yAccel = 0;
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
	 * Classes inheriting the Entity class should change this to a different
	 * return value reflecting the SpriteHandler and SpriteSheet they use.
	 */
	public BufferedImage getSprite() { return sh.getTile(0, 0); }


	/**
	 * Return X/Y coordinates at entity origin.
	 */
	public Point2D.Double getPoint() {
		return new Point2D.Double(this.coords.getX(), this.coords.getY());
	} /* End method getPoint */


	/**
	 * Return centre x position of the sprite.
	 */ 
	public int getCentreX() {
		return (int)(
			(
			 	(this.coords.getX() + this.coords.getWidth())
				+ this.coords.getX()
			) / 2
		);
	} /* End method getCentreX */


	/**
	 * Return centre y position of the sprite.
	 */
	public int getCentreY() {
		return (int)(
			(
			 	(this.coords.getY() + this.coords.getHeight())
				+ this.coords.getY()
			) / 2
		);
	} /* End method getCentreY */


	/**
	 * Set the acceleration and velocities of the entity manually.
	 */
	public synchronized void setAccelX(double accel) { this.xAccel = accel; }
	public synchronized void setAccelY(double accel) { this.yAccel = accel; }
	public synchronized void setVelX(double vel) { this.xVel = vel; }
	public synchronized void setVelY(double vel) { this.yVel = vel; }


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

} /* End class Level */
