 
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
 
public class board2 extends JPanel implements ActionListener, Runnable {
    character2 guy;
    public Image img;
    Timer time;
    Thread Animator;
    enemy vil1,vil2;//create two enemies
 //   int vel = 280;//same value as guy.y     this is now obsolete after adding in bullets
    
    public board2() {
        guy = new character2();//nothing new here
        addKeyListener(new AL());
        setFocusable(true);
        ImageIcon i = new ImageIcon("bg.jpg");
        img = i.getImage();
        time = new Timer(5, this);
        time.start();
        vil1 = new enemy(guy.x + 600,280,"zubat.png");//creates the new images for enemies based on guys location
        vil2 = new enemy(guy.x + 700,280,"zubat.png");
    }

    /////////////////////////////LOAD THE GUN////////////////////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {//loading the magazine
    	 ArrayList magazine = guy.getShots(); //add new bullet objects into our clip
        
    	 for (int i = 0; i<magazine.size();i++) { //for each shot in the magazine we need to create a new image object
         	Weapon one = (Weapon) magazine.get(i); //stores a bullet object in each arraylist slot
         
         	if (one.getVisible())
         		one.move();//only moves the bullet if it is on screen
         	else{
         		magazine.remove(one);//otherwise it removes it from the ArrayList
         	}
         }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////         

         
        guy.move();//every action performed he will move and I will repaint
        if (guy.x > 400)//this only moves the enemies when our guy is moving too
        	vil1.move(guy.getDX());
        if (guy.x > 500)
        	vil2.move(guy.getDX());//we call the move method with getDX because their movement is dependant upon his (see method)
        repaint();
        
    }
 
boolean jumper = false;//when we jump we only want to jump once, double jump is not necessary but you can use similar code to figure that out
static Font font = new Font ("SanSerif",Font.BOLD,24);//ammo font




//////////////////////////////////////////////////////////PAINT//////////////////////////////////////////////////////////////
public void paint(Graphics g) {
    	if(guy.dy == 1 && jumper == false) {
    		jumper = true;//prevents double jumping
    		Animator = new Thread(this);
    		Animator.start();//starts the jumping thread
    	    	}
    	
        super.paint(g);
      
        if ((guy.getX() - 40)% 1600 == 0)//this is how we cycle the backgrounds multiple times
        	guy.nx =0;
        if ((guy.getX() - 840) % 1600 == 0)//I did % 1600 because my bg panel is 800 units long, so every 2 cycled images
        	guy.nx2 =0;//resets them to 0 and draws again
        Graphics2D g2d = (Graphics2D) g;
        	//The following makes only the background move instead of my guy
        
        g2d.drawImage(img, 785-guy.nx2, 0, null);//- sign makes it move left
        //if we left it as -guy.nx2 then the bg would be 685 units to the left of screen, the 785 - compensates for that
    
        if (guy.getX()>40)//where the bg ends on my screen
           g2d.drawImage(img, 785-guy.nx, 0, null);//- sign makes it move left
           g2d.drawImage(guy.getImage(), guy.mLeft, guy.y, null);//this draws my guy, if I draw his x at mLeft it prevents him from ever moving to the left of screen (old mario)

           
           
  ///////////////////////////////////////////////Bullets/////////////////////////////////////////////////////////////////////////////
        ArrayList magazine = guy.getShots();
        for (int i = 0; i<magazine.size();i++) {
        	Weapon one = (Weapon) magazine.get(i);//draws each bullet as it is fired
        	g2d.drawImage(one.getImage(),one.getX(),one.getY(),null);//draws each image object based on its initialization above
        }
        
    g2d.setFont(font);//ammo font being printed
    g2d.setColor(Color.white);
    g2d.drawString("Shots: "+guy.ammo, 20, 20);
    /////////////////////////////////////////Bad Guys///////////////////////////////////////////////////////////////////////////////
    if (guy.x>400)
    	if(vil1.getAlive() == true)//we don't have a condition yet for which it is false so this statement is currently useless, soon won't be though
    		g2d.drawImage(vil1.getImage(), vil1.getX(),vil1.getY(),null);
    	if(guy.x > 500)
    		if(vil1.getAlive() == true)
        		g2d.drawImage(vil2.getImage(), vil2.getX(),vil2.getY(),null);//x and y are defined above in the object initialization
        	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private class AL extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            guy.keyReleased(e);
        }
 
        public void keyPressed(KeyEvent e) {
            guy.keyPressed(e);
        }
    }
    
//////////////////////////////////Jumping Mechanic///////////////////////////////////////////////////////////////////// 
    boolean up = false;//is he at the top of his jump yet?
    boolean down = false;//has he landed yet?
 
    public void jumping() {
 
        if (up == false)//press jump? vel (y value decreases)
            guy.y--;
        if (guy.y == 230)//once it hits 230 it begins to fall back down
            up = true;
       
        if (up == true && guy.y <=280) {//if we have hit the peak and are ready to fall back down
            guy.y++;
            if (guy.y == 280) {
                down = true;
            }
        }
    }
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
////////////////////////JUMPING THREAD/////////////////////////////////////////////////////////////////////////////////////
    public void run() {
 
        long timeS, timeDiff, sleep;//each jump lasts x amount of time
 
        timeS = System.currentTimeMillis();//timeS is set to current time
 
        while (down == false) {//so long as down is false
 
            jumping();//call cycling function
 
            timeDiff = System.currentTimeMillis() - timeS;
            sleep = 10 - timeDiff;
 
            if (sleep < 0)
                sleep = 2;//by resetting sleep to 2 it keeps checking to see if the jump ends
            try {
                Thread.sleep(sleep);//suspends the current thread for the given amount of time
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
 
            timeS = System.currentTimeMillis();//resets the timeS to the new current time to prevent rechecking for jumps
        }
        down = false;//resets all variables to start values and conditions, ready to jump again
        up = false;
        jumper = false;
    }
    
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
