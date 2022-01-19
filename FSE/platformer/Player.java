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

	double clock = 0d;
	double yAccel = 0.10;
	int spriteCnt = 0;

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
	 */
	@Override
	public void update(double diffT, Shape bounds) {
		super.adjustVelocity(diffT);
		super.boundedMove(diffT*this.xVel, diffT*this.yVel, bounds);

		if (!super.isMovingLeft() && !super.isMovingRight()) {
			clock = 0;
			spriteCnt = 0;
		} else {
			clock += diffT;

			if (clock > 10) {
				if (spriteCnt > 9) {
					spriteCnt = 0;
				} else {
					spriteCnt++;
				}
				clock = 0;
			}
		}
	} /* End method update */


	/**
	 * Walking animation for player is in series.
	 */
	@Override
	public BufferedImage getSprite() {
		if (super.isMovingRight()) {
			return sh.getTile(spriteCnt, 0);
		} else if (super.isMovingLeft()) {
			return sh.getReversedTile(10 - spriteCnt, 0);
		} else {
			return sh.getTile(spriteCnt, 0);
		}
	} /* End getSprite */


	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		switch (code) {
		case KeyEvent.VK_W:
			jump(-4);
			break;
		case KeyEvent.VK_S:
			break;
		case KeyEvent.VK_A:
			setAccelX(-speed);
			break;
		case KeyEvent.VK_D:
			setAccelX(speed);
			break;
		}
	} /* End method keyPressed */


	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_W:
			break;
		case KeyEvent.VK_S:
			break;
		case KeyEvent.VK_A:
			setAccelX(0);
			break;
		case KeyEvent.VK_D:
			setAccelX(0);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
} /* End class Level */
