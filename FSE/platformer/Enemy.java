// ------------------------------------------------------------------------- //
// Represents an Enemy                                                       //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-20                                                   //
// Finish date: 2022-01-22                                                   //
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

public class Enemy extends Entity {

	double clock = 0d;
	double yAccel = 0.09;
	int spriteCnt = 0;
	double speed = 1d;

	public Enemy(
		EntityType entity, double x, double y, SpriteHandler sh,
		Attribute[] attrs
	) {
		super("enemy",
			entity.hp,
			sh,
			new Rectangle2D.Double(x, y, Settings.SLIME_WIDTH*Settings.zoom(), Settings.SLIME_HEIGHT*Settings.zoom()),
			attrs
		);
		this.setX(x);
		this.setY(y);
	} /* End constructor */


	/**
	 * Updates the Enemy's conditions for one frame.
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
		super.adjustVelY(diffT);
		if (this.right) {
			this.setAccelX(1);
		} else {
			this.setAccelX(-1);
		}
		super.boundedMove(
			diffT * this.xVel,
			diffT * this.yVel,
			bounds
		);

		if (!super.isMovingLeft() && !super.isMovingRight()) {
			clock = 0;
			spriteCnt = 0;
		} else {
			clock += diffT;

			if (clock > 30) {
				if (spriteCnt > 0) {
					spriteCnt = 0;
				} else {
					spriteCnt++;
				}
				this.setVelX(this.right ? 5 : -5);
				clock = 0;
			} else {
				this.setVelX(0);
			}
		}
	} /* End method update */


	/**
	 * Walking animation for player is in series.
	 */
	@Override
	public BufferedImage getSprite() {
		if (super.isMovingRight()) {
			return costumes.getTile(2 - spriteCnt, 0);
		} else if (super.isMovingLeft()) {
			return costumes.getReversedTile(spriteCnt, 0);
		} else {
			return costumes.getTile(spriteCnt, 0);
		}
	} /* End getSprite */

} /* End class Level */
