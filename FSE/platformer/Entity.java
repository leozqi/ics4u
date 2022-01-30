// ------------------------------------------------------------------------- //
// The Entity class stores base functionality for moving, animated sprites   //
// displayed in levels, including the player.                                //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Entity.java                                                     //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Entity {

	/* Properties */
	Rectangle2D.Double bounds; // Bounding-box (collision box) for entity
	boolean alive = true; // Whether entitiy is alive or not
	int coin = 0; // Coins of the entity
	ArrayList<Attribute> attrs = new ArrayList<Attribute>();

	// DelayQueue for effects
	// The current effect is the element at head that is being peeked
	// When the delay is over and the element is removed, it CEASES effect
	// Effects have not currently been implemented but are present for
	// extensibility.
	DelayQueue<Effect> effects = new DelayQueue<Effect>();

	/* Movement */
	double xVel   = 0;                  // X velocity of entity
	double yVel   = 0;                  // Y velocity of entity
	double xAccel = 0;                  // X acceleration of entity
	double yAccel = Settings.E_GRAVITY; // Y acceleration of entity
	double maxVel = Settings.E_MAX_SPD; // Max velocity
	double minVel = Settings.E_MIN_SPD; // Min velocity

	int jumpCnt = 0;   // Number of times entity has already jumped
                           // (before hitting the ground)
	int jumpLimit = 1; // Limit of jumps before entity cannot jump
	boolean right = false; // Horizontal direction of sprite (left or right)
	boolean stationary = false; // Stationary Entities update ticks when not moving

	/* Sprites */
	SpriteHandler costumes; // Sprite animations for the entity
	int spriteCnt;          // Current costume of sprite

	/* Time */
	double clock = 0d;      // Clock for sprite change cycles
	double tickTime = 30d;  // Time to update one tick cycle

	/**
	 * Create a new entity.
	 *
	 * Entities represent components of the game that must be able to change
	 * after the level is loaded. They may display animated sprites that
	 * are able to move within the level.
	 *
	 * The base entity class provides common functionality for all subclasses
	 * including movement with collision detection and sprite management.
	 *
	 * Actual types of entities should be created through a subclass like
	 * the Player or Item classes.
	 */
	public Entity(
		SpriteHandler costumes, Rectangle2D bounds, Attribute[] attrs
	) {
		this.costumes = costumes;                  // Store costumes of entity
		this.bounds = (Rectangle2D.Double) bounds; // Store coordinates of entity

		// Process and attributes
		if (attrs == null) { return; }
		for (int index = 0; index < attrs.length; index++) {
			// Loop over attributes and apply attributes
			this.applyAttribute(attrs[index]);
		}
	} /* End constructor */


	/**
	 * Apply a status effect to the entity.
	 *
	 * @param effect Effect to apply.
	 */
	public void applyStatus(Effect effect) { effects.add(effect); }


	/**
	 * Set the entity as being "dead".
	 *
	 * A dead entity will stop moving except to drop out of the screen.
	 */
	public void die() {
		// If already dead, do not reset this method
		if (!this.alive) { return; }

		this.alive = false; // "Kill" the entity.

		this.setAccelX(0);  // Stop moving in both axis.
		this.setVelX(0);
		this.setVelY(-Settings.P_JUMP);
		this.setAccelY(Settings.E_GRAVITY);
	} /* End method die */


	/**
	 * Apply an attribute to the entity.
	 * 
	 * Attributes make it easier to specify base properties of entities
	 * without making a new class. In effect, they are *effects* applied
	 * instantly. This is used, for example, with the Item class.
	 *
	 * @param attr attribute to apply.
	 */
	public void applyAttribute(Attribute attr) {
		switch (attr) {
		case DOUBLE_JUMP: // Entity may double-jump
			jumpLimit = 2;
			break;
		case COIN_1:      // Add one coin to Entity counter
			coin++;
			break;
		case COIN_10:     // Add ten coins to Entity counter
			coin += 10;
			break;
		case COIN_100:    // Add one hundred coins to Entity
			coin += 100;
			break;
		default:
			this.attrs.add(attr);
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
	 * their own functionality, and call super() to update common functions
	 * of all entities.
	 *
	 * @param diffT difference in time
	 * @param bounds shape representing collision boxes entity should be
	 *               aware of.
	 */
	public void update(double diffT, Shape bounds) {
		/* Update the animation tick */
		if (this.isMovingLeft() || this.isMovingRight() || this.stationary) {
			// Update animation tick
			this.clock += diffT;

			// Reached needed ticks to update costume?
			if (this.clock > this.tickTime) {
				updateTick(); // Per-tick constant time update.
				clock = 0; // Reset clock
			}
		} else {
			// Don't update animation tick as entity is not moving
			// OR entity is not stationary
			this.clock = 0;
		}

		/* Check for death */
		this.checkDeath();
	} /* End method update */


	/**
	 * Adjusts velocity based on acceleration and friction for variable.
	 *
	 * Adds "friction" to reduce specified velocity gradually and a max
	 * velocity as a limit in both directions.
	 *
	 * @param diffT time ajustment between frames.
	 * @param accel acceleration value to use for adjustments.
	 * @param vel current velocity value.
	 * @return velocity after adjustment.
	 */
	public double adjustVel(double diffT, double accel, double vel) {
		// Positive acceleration
		if (accel > 0) {
			// If velocity is too low to be measurable,
			// set it equal to MIN_SPD
			if (vel < this.minVel && vel > 0) {
				vel = this.minVel;

			// If velocity is too high
			} else if (vel < this.maxVel) {
				vel += (diffT * accel);
			}
		// Negative acceleration
		} else if (accel < 0) {
			// Set very small velocities equal to MIN_SPD
			if (vel > -this.minVel && vel < 0) {
				vel = -this.minVel;

			// If velocity is too high
			} else if (vel > -this.maxVel) {
				vel += (diffT * accel);
			}
		// No acceleration; use friction
		} else {
			// Less than minVel; friction should stop velocity.
			if (Math.abs(vel) < this.minVel) {
				vel = 0;
			}
			// Apply friction
			if (vel > 0) {
				vel -= (diffT * 0.1);
			} else if (vel < 0) {
				vel += (diffT * 0.1);
			}
		}
		return vel;
	} /* End method adjustVel */


	/**
	 * Called whenever a "tick" for the entity has passed.
	 *
	 * Unlike the update function per frame, this "per tick" method is
	 * controlled with `tickTime` which can be set by inheriting classes
	 * with the `super.tickTime` field. It updates constantly; about the
	 * same time passes per tick.
	 *
	 * This is perfect for sprite updating.
	 */
	public void updateTick() {}


	/**
	 * Adjusts velocity based on acceleration for the Y axis.
	 *
	 * Applies gravity to y velocity with time adjustment.
	 *
	 * @param diffT time adjustment between frames.
	 */
	public void adjustVelY(double diffT) {
		this.yVel += ( (diffT * this.yAccel) );
	} /* End method adjustVelY */


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
	 * @param x integer number of pixels to move in x axis
	 * @param y integer number of pixels to move in y axis
	 */
	public void move(int x, int y) {
		synchronized (this.bounds) {
			this.bounds.setRect(
				this.bounds.getX() + x, // Set X Pos
				this.bounds.getY() + y, // Set Y Pos
				this.bounds.getWidth(), // Set X width
				this.bounds.getHeight() // Set Y height
			);
		}
	} /* End method move */


	/**
	 * Move restricting movement that intersects with shape `bounds`.
	 *
	 * Bounds can be any Shape, but for this program is the Area class:
	 *     java.awt.geom.Area.
	 *
	 * Works by gradually moving the entity one coordinate in the x and
	 * y directions at a time until one of those dimensions hits a bound
	 * or the specified distance has been covered.
	 *
	 * Then, it continues moving one coordinate in the other direction until
	 * it too hits a bound. This method takes into account horizontal
	 * boundaries of the level as "walls". Passing the lower vertical bound
	 * of the level (max Y) will mark the entity as being "dead" for collection.
	 *
	 * This method is the simplest method of collision detection *and* is
	 * more efficient than checking MANY Shapes (or each tile) individually.
	 *
	 * @param x integer number of pixels to move in x axis
	 * @param y integer number of pixels to move in y axis
	 * @param bounds collision box entity should not enter
	 */
	public void boundedMove(int x, int y, Shape bounds) {
		// No movement; return
		if ((x == 0) && (y == 0)) { return; }

		boolean xReachedLim = (x == 0); // hit boundary in X dimension
		boolean yReachedLim = (y == 0); // hit boundary in Y dimension

		int xInc = (x > 0) ? 1 : -1; // set x increment by value
		int yInc = (y > 0) ? 1 : -1; // set y increment by value

		int cnt = 0; // how many increments have passed

		// If count is greater than arbitrarily large number, exit:
		// error in finding boundary
		while (cnt < 1000) {
			// Boundary not reached; requested x distance not reached
			if (!xReachedLim && cnt < Math.abs(x)) {
				// Increment player Rectangle x
				this.bounds.x += xInc;

				// Check for collision
				if (
					(bounds.intersects(this.bounds))
					|| (this.bounds.getMinX() < 0) // X bounds:
					|| (this.bounds.getMaxX() > Settings.levelWidth())
				) {
					// Go back so object is *almost*
					// colliding, but not actually
					this.bounds.x -= xInc;

					// Have reached x limit; stop incrementing
					// on next loop
					xReachedLim = true;

					// Entity should now be moving in the
					// opposite direction.
					this.right = !this.right;
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
					}
					this.yVel = 0;
				}
			}
			cnt++; // Increment movement

			// Both boundaries have been reached OR distance moved
			// is satisfied
			if (
				(xReachedLim && yReachedLim)
				|| ((cnt >= Math.abs(x)) && (cnt >= Math.abs(y)))
			) {
				return; // Exit loop
			}
		} /* End while loop */
	} /* End method boundedMove */


	/**
	 * Check if the entity should be "dead".
	 *
	 * Dead entities are marked to be removed at a specified point in the
	 * main game loop.
	 */
	public void checkDeath() {
		// If already dead, do not recheck
		if (!this.alive) { return; }

		/* Death due to falling out of the world */
		if (this.bounds.getMinY() > Settings.levelHeight()) {
			this.die();
			return;
		}
	} /* End method checkDeath */


	/**
	 * Return the sprite of the current animation step.
	 *
	 * Classes inheriting the Entity class costumesould change this to a different
	 * return value reflecting the SpriteHandler and Spritesheet they use.
	 */
	public BufferedImage getSprite() { return costumes.getTile(0, 0, false); }


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
	public int getCentreX() { return (int) this.bounds.getCenterX(); }


	/**
	 * Return centre y position of the sprite.
	 */
	public int getCentreY() { return (int) this.bounds.getCenterY(); }


	/**
	 * Set the acceleration and velocities of the entity manually.
	 *
	 * Used for an Entity with constant velocity or acceleration.
	 */
	public synchronized void setAccelX(double accel) { this.xAccel = accel; }
	public synchronized void setAccelY(double accel) { this.yAccel = accel; }
	public synchronized void setVelX(double vel) { this.xVel = vel; }
	public synchronized void setVelY(double vel) { this.yVel = vel; }
	public synchronized void setX(double x) { this.bounds.x = x; }
	public synchronized void setY(double y) { this.bounds.y = y; }


	/**
	 * Status indicators.
	 *
	 * Directions are based on acceleration. Some Entities do not use
	 * acceleration but modify the acceleration variable to give accurate
	 * directions.
	 */
	public Rectangle2D.Double getBounds() { return this.bounds;       }
	public ArrayList<Attribute> getAttributes() { return this.attrs;  }
	public boolean isMovingLeft()         { return (this.xAccel < 0); }
	public boolean isMovingRight()        { return (this.xAccel > 0); }
	public boolean isMovingUp()           { return (this.yAccel < 0); }
	public boolean isMovingDown()         { return (this.yAccel > 0); }
	public boolean isTouchingGround()     { return (this.yVel == 0);  }
	public boolean isAlive()              { return this.alive;        }
	public int getCoins()                 { return this.coin;         }


	/**
	 * Returns whether or not the Entity is moving at all.
	 *
	 * This method takes into account velocity, not acceleration, to
	 * determine if an Entity is moving or not.
	 *
	 * @return true if Entity is moving; otherwise false
	 */
	public boolean isMoving() {
		// If NOT both velocities are zero, is moving
		return !((this.xVel == 0) && (this.yVel == 0));
	} /* End method isMoving */


	/**
	 * Jump with a starting velocity of `vel`.
	 *
	 * This method will consume one "jump." Players can be configured to
	 * have more than one "jump" before landing.
	 */
	public synchronized void jump(double vel) {
		// Allow jumping if jumpLimit has not been reached
		if (jumpCnt < jumpLimit) {
			// Play jump sound
			// Designed to silently fail if sound does not exist
			SoundHandler.get().playSound("SOUND_JUMP", false);

			this.setVelY(vel);
			jumpCnt++; // Increment jump count
		}
	} /* End method jump */


	/**
	 * Check if entity touching a rectangle bounds-box.
	 * 
	 * @return true if touching, else false.
	 */
	public boolean isTouching(Rectangle2D.Double bounds) {
		if (this.bounds.intersects(bounds)) {
			return true;
		}
		return false;
	} /* End method isTouching */

} /* End class Entity */
