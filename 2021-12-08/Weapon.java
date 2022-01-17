import java.awt.*;

import javax.swing.ImageIcon;
public class Weapon {
	
	int x,y;
	Image img;
	boolean bVis = true;
	
	public Weapon(int startX, int startY) {
		x = startX;
		y = startY;
		ImageIcon newShot = new ImageIcon("bullet.png");
		img = newShot.getImage();
		bVis = true;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getVisible() {
		return bVis;
	}
	
	public Image getImage() {
		return img;
	}
	
	public void move() {
		x += 2;
		if (x > 800) {
			bVis = false;
		}
	}
	
	
}
