import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Panel  extends JPanel implements ActionListener{
	
	static final int Screen_Width = 600;//sets dimensions of the game screen
	static final int Screen_Height = 600;
	static final int UnitSize = 25;//size of each block
	static final int gameUnits = (Screen_Width*Screen_Height)/UnitSize;//snake block size
	static final int Delay = 75; //nice size delay for the game, runs at good speed
	final int x[] = new int[gameUnits];
	final int y[] = new int[gameUnits];//holds all the x and y coordinates of the snake
	int bodyParts = 6; //start with 6 parts
	int foodEaten;//How may have been eaten?
	int foodX;//Where does the food spawn at?
	int foodY;
	char direction = 'R';//snake starts going right
	boolean running = false;
	Timer timer;
	Random random;//sets up the timer and random aspecs such as fruit placement
	
	
	Panel(){//sets the screen size
		random = new Random();//new random instance
		this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new keyAdapter());
		startGame();
	
	}
	
	
	public void startGame() {//all the basics of starting the game
		newFood();
		running = true;
		timer = new Timer(Delay,this);
		timer.start();
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);//draws the screen
		draw(g);
	}
	
	public void draw(Graphics g){
		if (running) {
		
			for (int i=0;i<Screen_Height/UnitSize;i++) {
				g.drawLine(i*UnitSize, 0, i*UnitSize, Screen_Height);//creates a grid to make life easier
				g.drawLine(0, i*UnitSize, Screen_Width, i*UnitSize);
			}
			g.setColor(Color.blue);//creates the food at a random spot
			g.fillOval(foodX,foodY,UnitSize,UnitSize);
		
			for (int i=0;i<bodyParts;i++) {//this is the snakes head
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UnitSize, UnitSize);
				}
			
				else {//this is the body
					g.setColor(Color.red);
					g.fillRect(x[i], y[i], UnitSize, UnitSize);
				}
			
			}
			g.setColor(Color.red);//text written on screen for the score
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ foodEaten, (Screen_Width-metrics.stringWidth("\"Score: \"+ foodEaten"))/2,g.getFont().getSize());
		
		}
			
		else {
			gameOver(g);//if the game isn't running, it's game over!
		}
	
	
	}
	public void newFood() {//creation of new food in random spots
		foodX = random.nextInt((int)(Screen_Width/UnitSize))*UnitSize;
		foodY = random.nextInt((int)(Screen_Height/UnitSize))*UnitSize;
		
	}
	
	
	public void move() {
		for(int i = bodyParts; i>0; i--) {//this creates a for loop to maintain the lenth of the snake
			x[i] = x[i - 1];//shifts all coordinates by one spot in the index
			y[i]=  y[i - 1];
		}
			switch(direction) {//adds a new location to the head of the snake 
			case 'U':
				y[0] = y[0] - UnitSize;//Goes to the next position
				break;
			case 'D':
				y[0] = y[0] + UnitSize;//moves one grid square down
				break;
			case 'L':
				x[0] = x[0] - UnitSize;
				break;
			case 'R':
				x[0] = x[0] + UnitSize;
				break;
			
		}
	}
	
	public void checkFruit() {//checks if you've eaten fruit
		if ((x[0] == foodX && y[0] == foodY)) {
			bodyParts++;//increase in parts, score and creates another new food
			foodEaten++;
			newFood();
		}
	}
	
	public void Collisions() {
		for(int i = bodyParts; i > 0; i--) {//does the head hit the body?
			if ((x[0] == x[i]) && (y[0] == y[i])){//this checks if it does
				running = false;//if it collides its game over
			}
		}
		if (x[0] < 0) {
			running = false;
		}
		if (x[0] > Screen_Width) {//checks left and right walls
			running = false;
		}
		
		if (y[0] < 0) {
			running = false;
		}
		if (y[0] > Screen_Height){//checks top and bottom walls
			running = false;
		}
		
		if (!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//This displays when the game ends, GAME OVER! text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (Screen_Width-metrics.stringWidth("Game Over!"))/2,Screen_Height/2);
	
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ foodEaten, (Screen_Width-metrics2.stringWidth("\"Score: \"+ foodEaten"))/2,g.getFont().getSize());
	
	}
	
	
	
	
	public void actionPerformed(ActionEvent e){
		if(running) {//as long as the game is running, check movement, food and collisions
			move();
			checkFruit();
			Collisions();
		}
		repaint();//always repaint!
	}

	public class keyAdapter extends KeyAdapter{
	
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT://just need to make sure that if we are heading Right they can't go Left on an Left arrow push, otherwise they'de run into themselves
				if (direction != 'R') {
					direction = 'L';
					break;
				}
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
					break;
				}
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
					break;
				}
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
					break;
				}
			
			
			}
		}
	}

}
