// ------------------------------------------------------------------------- //
// Represents a player.                                                      //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-17                                                   //
// Finish date: 2022-01-20                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.net.URL;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.awt.geom.*;

public class Player extends Entity implements KeyListener {

	boolean lastLeft = false;
	boolean climbing = false;

	public Player(
		String name, int hp, SpriteHandler sh, Rectangle2D bounds,
		Attribute[] attrs
	) {
		super(name, hp, sh, bounds, attrs);
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
	 */
	public void update(double diffT, Shape bounds, Shape climbable) {
		super.update(diffT, bounds);

		/* Adjust velX for friction, slip */
		super.adjustVelX(diffT);
		/* Adjust velY for gravity */

		if (this.alive) {
		if (climbable.intersects(this.bounds)) {
			if (this.climbing != true) {
				this.climbing = true;
				super.setVelY(0);
				super.setAccelY(0);
				this.jumpCnt = 0;
			}
		} else {
			this.climbing = false;
			super.setAccelY(Settings.E_GRAVITY);
		}
		}
		super.adjustVelY(diffT);

		/* Bounded move takes obstacles into account */
		if (this.alive) {
			super.boundedMove(
				diffT * this.xVel * Settings.zoom(), // Take into account zoom
				diffT * this.yVel * Settings.zoom(),
				bounds
			);
		} else {
			super.move(
				diffT * this.xVel * Settings.zoom(),
				diffT * this.yVel * Settings.zoom()
			);
		}
	} /* End method update */


	@Override
	public void updateTick() {
		if (spriteCnt > 9) {
			spriteCnt = 0;
		} else {
			spriteCnt++;
		}
	} /* End method updateSprite */


	/**
	 * Walking animation for player is in series.
	 */
	@Override
	public BufferedImage getSprite() {
		if (super.isMovingRight()) {
			lastLeft = false;
			return costumes.getTile(spriteCnt, 0);
		} else if (super.isMovingLeft()) {
			lastLeft = true;
			return costumes.getTile(spriteCnt, 0, true);
		} else {
			return costumes.getTile(spriteCnt, 0, lastLeft);
		}
	} /* End getSprite */


	@Override
	public void keyPressed(KeyEvent e) {
		if (!this.alive) { return; }
		int code = e.getKeyCode();

		switch (code) {
		case KeyEvent.VK_W:
			if (!climbing) {
				super.jump(-Settings.P_JUMP);
			} else {
				super.setVelY(-Settings.P_SPD);
			}
			break;
		case KeyEvent.VK_S:
			if (climbing) {
				super.setVelY(Settings.P_SPD);
			}
			break;
		case KeyEvent.VK_A:
			setAccelX(-Settings.P_SPD);
			break;
		case KeyEvent.VK_D:
			setAccelX(Settings.P_SPD);
			break;
		}
	} /* End method keyPressed */


	@Override
	public void keyReleased(KeyEvent e) {
		if (!this.alive) { return; }
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_A:
			setAccelX(0); // Stop moving 
			break;
		case KeyEvent.VK_D:
			setAccelX(0);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

} /* End class Player */
