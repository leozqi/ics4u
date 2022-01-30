// ------------------------------------------------------------------------- //
// The Player class handles the velocity and acceleration adjustment of the  //
// Rectangle bounding box representing the player itself, the animation of   //
// the player when being drawn onto the screen, and keyboard input for       //
// player control in addition to base functionality provided by Entity.      //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Player.java                                                     //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.awt.geom.*;

public class Player extends Entity implements KeyListener {

	boolean lastLeft = false; // Stores last-facing direction of Player
	boolean climbing = false; // Stores whether or not the player is climbing

	// For control purposes only
	boolean leftKeyPressed  = false;
	boolean rightKeyPressed = false;
	boolean upKeyPressed    = false;
	boolean downKeyPressed  = false;

	/**
	 * Create a player.
	 *
	 * The player hitbox is automatically determined by the bounding box
	 * of the SpriteHandler costumes used for the player.
	 *
	 * The Player has the Lighting attribute applied automatically; this
	 * gives the Player a small circle of light in dark levels.
	 */
	public Player(double x, double y, SpriteHandler costumes, Attribute[] attrs) {
		// Use the Entity constructor to fulfill most creation
		// functionality
		super(
			costumes, // SpriteHandler holding image resources

			// Create collision box using dimension of costumes
			new Rectangle2D.Double(
				x, y,                // Starting position
				costumes.getXSize(), // Width
				costumes.getYSize()  // Height
			),
			attrs     // Additional applied attributes
		);
		// Apply additional attribute; lighting (player glows in dark
		// environment)
		super.applyAttribute(Attribute.LIGHTING);
		super.tickTime = 10;
	} /* End constructor */


	/**
	 * Updates the Player's conditions for one frame.
	 *
	 * The main game loop calls this method to update the player status,
	 * including coordinate position, etc.
	 *
	 * @param diffT difference in time
	 * @param bounds Shape representing collision boxes entity should be
	 *               aware of.
	 * @param climbable Shape representing climbable areas the entity
	 *                  should be aware of.
	 * @param deadly Shape representing areas fatal to the player.
	 */
	public void update(double diffT, Shape bounds, Shape climbable, Shape deadly) {
		super.update(diffT, bounds); // Update ticks through Entity

		/* Adjust velX for friction, slip */
		super.setVelX(super.adjustVel(diffT, super.xAccel, super.xVel));

		if (!this.alive) {
			// Not alive; don't adjust velY except for gravity
			super.adjustVelY(diffT);

			// Do not take into account obstacles if dead
			super.move(
				(int)(diffT * this.xVel * Settings.zoom()),
				(int)(diffT * this.yVel * Settings.zoom())
			);
			return; // Don't do rest of method
		}

		/* Check if player is hitting deadly object */
		if (deadly.intersects(this.bounds)) {
			// Play sizzle sound in contact with lava if possible
			SoundHandler.get().playSound("SOUND_SIZZLE", false);
			this.die(); // die
			return;
		}

		/* Is alive; adjust Y velocity based on if character is climbing */
		if (climbable.intersects(this.bounds) && (!this.climbing)) {
			// Only set once when touching climbing object
			this.climbing = true;
			this.jumpCnt = 0;
			super.setAccelY(0);
		} else if (climbable.intersects(this.bounds)) {
			// If climbing, adjust y velocity for friction as well
			super.setVelY(super.adjustVel(diffT, super.yAccel, super.yVel));
		} else {
			// Not climbing; apply gravity
			this.climbing = false;
			super.setAccelY(Settings.E_GRAVITY);
			super.adjustVelY(diffT);
		}

		/* Bounded move takes obstacles into account */
		super.boundedMove(
			(int)(diffT * this.xVel * Settings.zoom()), // Take into account zoom
			(int)(diffT * this.yVel * Settings.zoom()),
			bounds
		);
	} /* End method update */


	/**
	 * Update sprite animation step per tick.
	 */
	@Override
	public void updateTick() {
		if (spriteCnt > 9) { // Reached last step; loop back to first
			spriteCnt = 0;
		} else {
			spriteCnt++; // Else increment step
		}
	} /* End method updateSprite */


	/**
	 * Get current animation step of Player.
	 *
	 * The player's animation step is in series. Additionally, the "lastLeft"
	 * variable holds if the player was last moving left or right before
	 * stopping, to face the right direction when stationary.
	 */
	@Override
	public BufferedImage getSprite() {
		if (super.isMoving()) {
			// Default orientation is movingRight; therefore
			// use super.isMovingLeft as boolean to whether or not
			// the tile should be flipped.
			lastLeft = super.isMovingLeft();
			return costumes.getTile(spriteCnt, 0, super.isMovingLeft());
		} else {
			// Stationary, get last facing direction
			return costumes.getTile(spriteCnt, 0, lastLeft);
		}
	} /* End method getSprite */


	/**
	 * Get input events to control Player motion.
	 *
	 * The keybindings used for the Player are stored as constants in the
	 * global Settings object.
	 *
	 * Settings.KEY_UP: Jump if not climbing, otherwise move up.
	 * Settings.KEY_DOWN: Descend if climbing.
	 * Settings.KEY_LEFT: Accelerate leftwards.
	 * Settings.KEY_RIGHT: Accelerate rightwards.
	 *
	 * This is for extensibility; a menu could potentially be used to create
	 * new keybindings; this however has not yet been implemented.
	 *
	 * @param e key pressed.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// If not alive, do not accept controls
		if (!this.alive) { return; }
		int code = e.getKeyCode();

		switch (code) {
		case Settings.KEY_UP:
			if (!climbing) {
				// Jump if not climbing
				// (negative y is towards top of screen)
				this.jump(-Settings.P_JUMP);
			} else {
				// Accelerate upwards when climbing
				super.setAccelY(-Settings.P_SPD);
			}
			upKeyPressed = true;
			break;
		case Settings.KEY_DOWN:
			if (climbing) {
				// Move downwards if climbing
				super.setAccelY(Settings.P_SPD);
			}
			downKeyPressed = true;
			break;
		case Settings.KEY_LEFT:
			// Move left (negative x direction)
			setAccelX(-Settings.P_SPD);
			leftKeyPressed = true;
			break;
		case Settings.KEY_RIGHT:
			// Move right (positive x direction)
			setAccelX(Settings.P_SPD);
			rightKeyPressed = true;
			break;
		}
	} /* End method keyPressed */


	/**
	 * Check for keys being released to stop applying their effects.
	 *
	 * Uses the same keybindings as those set for the `keyPressed` method.
	 *
	 * @param e key pressed.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// If not alive, do not accept controls
		if (!this.alive) { return; }
		int code = e.getKeyCode();

		// For all key codes, check to make sure key in opposite direction
		// has also not been pressed. This makes sure there is no case
		// where acceleration is set to zero in both directions
		// because one key has been released.
		switch (code) {
		case Settings.KEY_UP:
			if (climbing && !downKeyPressed) {
				// Stop moving in Y direction if climbing
				setAccelY(0);
			}
			upKeyPressed = false;
			break;
		case Settings.KEY_DOWN:
			if (climbing && !upKeyPressed) {
				// Stop moving in the Y direction if climbing
				setAccelY(0);
			}
			downKeyPressed = false;
			break;
		case Settings.KEY_LEFT:
			if (!rightKeyPressed) {
				setAccelX(0); // Stop moving in the X direction
			}
			leftKeyPressed = false;
			break;
		case Settings.KEY_RIGHT:
			if (!leftKeyPressed) {
				setAccelX(0); // Stop moving in the X direction
			}
			rightKeyPressed = false;
			break;
		}
	} /* End method keyReleased */

	@Override
	public void keyTyped(KeyEvent e) {}

} /* End class Player */
