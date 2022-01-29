// ------------------------------------------------------------------------- //
// Represents an Enemy                                                       //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-20                                                   //
// Finish date: 2022-01-22                                                   //
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

	double yAccel = 0.09;
	int spriteCnt = 0;
	double speed = 1d;

	public Enemy(
		EntityType entity, double x, double y, SpriteHandler sh,
		Attribute[] attrs
	) {
		super(
			entity.hp,
			sh,
			new Rectangle2D.Double(x, y, Settings.SLIME_WIDTH*Settings.zoom(), Settings.SLIME_HEIGHT*Settings.zoom()),
			attrs
		);
		this.setX(x);
		this.setY(y);
		super.tickTime = 30d;
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
		super.update(diffT, bounds);

		/* Adjust velY for gravity */
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
		this.setVelX(0);
	} /* End method update */


	@Override
	public void updateTick() {
		if (spriteCnt > 1) {
			spriteCnt = 1;
		} else {
			spriteCnt++;
		}
		this.setVelX(this.right ? 5 : -5); // Move 5 per tick
	} /* End method updateTick */


	/**
	 * Walking animation for player is in series.
	 */
	@Override
	public BufferedImage getSprite() {
		if (super.isMovingRight()) {
			return costumes.getTile(spriteCnt, 0, false);
		} else if (super.isMovingLeft()) {
			return costumes.getTile(spriteCnt, 0, true);
		} else {
			return costumes.getTile(spriteCnt, 0);
		}
	} /* End method getSprite */

} /* End class Level */
