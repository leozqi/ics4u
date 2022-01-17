 
import java.awt.*;
import java.awt.event.KeyEvent;
 
import javax.swing.ImageIcon;
 
public class character2 {
    int x, dx, y, nx2,nx,mLeft,dy;
    Image still;
    ImageIcon i = new ImageIcon("guy.png");
    ImageIcon l = new ImageIcon("GuyL.png");
    
    public character2() {
        
        still = i.getImage();//this is my image to start I am facing right
        mLeft = 25;
        x = 25;
        y = 280;
        nx2 = 785;//length of our frame, we will do some math later
        nx = 0;//second bg drawn after first
        dy = 0;//jump movement
    }
 
    public void move() {
        if (dx != -1) {//so long as he is not moving left
        	if (mLeft + dx <=25)
        		mLeft = mLeft + dx;
        	else {
    	x = x + dx;//add value to move right
        nx2 = nx2 + dx; //moves at same speed as guy
        nx = nx + dx;//scroll bg 2 as well
        }
        }
        else {
        	if (mLeft + dx > 0)
        		mLeft = mLeft +dx;
        }
    }
 
    public int getX() {
        return x;
    }
 
    public int getY() {
        return y;
    }
 
    public Image getImage() {
        return still;
    }
 
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        	still = l.getImage();//push left he moves left and it switches the image
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
            still = i.getImage();
    }
        if (key == KeyEvent.VK_UP) {
            dy = 1;
            still = i.getImage();//this is where you would displpay that jump image
     
    }
           
        
        
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
 
        if (key == KeyEvent.VK_LEFT)
            dx = 0;//stop moving when let go
        if (key == KeyEvent.VK_RIGHT)
            dx = 0;
        if (key == KeyEvent.VK_UP) 
            dy = 0;
    }
 
}