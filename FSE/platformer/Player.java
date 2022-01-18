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

	boolean up    = false;
	boolean down  = false;
	boolean left  = false;
	boolean right = false;

	public Player(String name, int hp, SpriteHandler sh, Rectangle2D bounds, Attribute[] attrs) {
		super(name, hp, sh, bounds, attrs);
	} /* End constructor */


	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		switch (code) {
		case KeyEvent.VK_W:
			jump(-5);
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
