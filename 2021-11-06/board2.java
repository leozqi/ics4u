 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class board2 extends JPanel implements ActionListener, Runnable {
    character2 guy;
    public Image img;
    Timer time;
    Thread Animator;
    int vel = 280;//same value as guy.y
    
    public board2() {
        guy = new character2();//nothing new here
        addKeyListener(new AL());
        setFocusable(true);
        ImageIcon i = new ImageIcon("bg.jpg");
        img = i.getImage();
        time = new Timer(5, this);
        time.start();
    }
 
    public void actionPerformed(ActionEvent e) {
        guy.move();//every action performed he will move and I will repaint
        repaint();
    }
 
boolean jumper = false;//when we jump we only want to jump once, double jump is not necessary but you can use similar code to figure that out

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
        
        ///////////////////////////////////////////////////////////////////////////////
        g2d.drawImage(img, 785-guy.nx2, 0, null);//- sign makes it move left
        //if we left it as -guy.nx2 then the bg would be 685 units to the left of screen, the 785 - compensates for that
       if (guy.getX()>40)//where the bg ends on my screen
           g2d.drawImage(img, 785-guy.nx, 0, null);//- sign makes it move left
///////////////////////////////////////////////////////////////////////////////////////
System.out.println(guy.getX());
        
        g2d.drawImage(guy.getImage(), guy.mLeft, vel, null);//this draws my guy, if I draw his x at mLeft it prevents him from ever moving to the left of screen (old mario)
    }//now I draw him at a y value of vel instead .getY();
 
    private class AL extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            guy.keyReleased(e);
        }
 
        public void keyPressed(KeyEvent e) {
            guy.keyPressed(e);
        }
    }
    
    
    boolean up = false;//is he at the top of his jump yet?
    boolean down = false;//has he landed yet?
 
    public void jumping() {
 
        if (up == false)//press jump? vel (y value decreases)
            vel--;
        if (vel == 230)//once it hits 230 it begins to fall back down
            up = true;
       
        if (up == true && vel <=280) {//if we have hit the peak and are ready to fall back down
            vel++;
            if (vel == 280) {
                down = true;
            }
        }
    }
 
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
