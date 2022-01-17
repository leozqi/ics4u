import java.awt.Image;

import javax.swing.ImageIcon;

public class enemy {//standard characteristics we have used for other classes so far
	Image img;
	int x,y;
	boolean alive = true;//always check if alive, not super important yet, but will be once our bullets work
	
	public enemy(int startX, int startY, String spot) {
		x = startX;
		y = startY;
		ImageIcon l = new ImageIcon(spot);
		img = l.getImage();//initialize all field variables in the constructor
		
	}
	
	public int getX() {
		return x;//these methods are super helpful when we want to use them in other classes
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getAlive() {//check enemy status
		return alive;
	}
	
	public Image getImage() {//will help us to create the enemies in the other classes
		return img;
	}
	
	public void move(int dx) {//only going to move if guy is
		x = x - dx;//moving left at same speed as guy
	}
	
}
