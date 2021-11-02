import javax.swing.*;//drawing canvas
import java.awt.Color;
import java.awt.*;//colors
import java.awt.geom.*;//shapes
import java.awt.geom.Rectangle2D.Double;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

public class Panel extends JPanel implements ActionListener {
	int x, y, velX, velY;
	char direction = 'R';
	static int GRID=25; // amount of squares in each dimension (total squares = GRID*GRID)
	static int SIZE=20;
	static int STARTSIZE = 10;
	ArrayList<Integer> snakeX = new ArrayList<Integer>(); // store where the snake is at right now
	ArrayList<Integer> snakeY = new ArrayList<Integer>(); // specifically, the number of squares AWAY from the top left square (in X and Y)
	JLabel label; // declare before initialize

	private Timer tm = new Timer(200, this);//this this is for action listener

	public Panel() {

		this.setSize(500,500);

		label = new JLabel();//creates the label
		label.setBackground(Color.lightGray);
		label.setOpaque(true);

		this.add(label);
		this.setVisible(true);

		tm.start();

		keyAdapter k = new keyAdapter();

		// the grid will be the size of the amount of GRID times the size of each square
		setPreferredSize(new Dimension(GRID*SIZE, GRID*SIZE));
		setBackground(Color.black); //set the background
		addKeyListener(k);  //this refers to keylistener and tells the program to watch for keys
		setFocusable(true);  //Enables keyListener
		setFocusTraversalKeysEnabled(true);  //means we will use shift or tab keys

		// Start the game
		Startgame();
	}

	public class keyAdapter extends KeyAdapter { // Subclass in the class of JPanel
		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode(); // gets the key that was keyPressed

			switch ( code ) {
				// Can only go in one direction at a time
				case KeyEvent.VK_LEFT:  //Left Arrow Key
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

	public void Startgame() {
		int centre = GRID / 2 + 1;

		for (int x = 0; x < STARTSIZE; x++) {
		snakeX.add(centre - x);
		snakeY.add(centre);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;


		// draw a grid
		// lets say each square is 20x20
		g2d.setColor(Color.lightGray);
		Path2D.Double p = new Path2D.Double();
		// Draw vertically
		for (int x = 1; x < GRID; x++) {
			p.moveTo(x*SIZE, 0);
			p.lineTo(x*SIZE, GRID*20);
			p.closePath();
			g2d.draw(p);
		}

		// Draw horizontally
		for (int y = 1; y < GRID; y++) {
			p.moveTo(0, y*SIZE);
			p.lineTo(GRID*20, y*SIZE);
			p.closePath();
			g2d.draw(p);
		}

		// Draw snake
		for (int b = 0; b < snakeX.size(); b++) {
		if (b == 0) {
			g2d.setColor(Color.green); // snake head
		} else {
			g2d.setColor(Color.red); // snake body
		}
		g2d.fillRect(snakeX.get(b)*SIZE, snakeY.get(b)*SIZE, SIZE,SIZE);
		}
	}

	public void Draw(Graphics g) {}

	public void newFood(Graphics2D g2d) {
		Random generator = new Random();
		double x = generator.nextInt(GRID * SIZE); // how big the gird is times the size of each square
		double y=generator.nextInt(GRID * SIZE);  // how big the gird is times the size of each square
		double size=10;
		Color color;
	}

	public void move() {

	}

	public void checkFood() {

		label.setText("SCORE: ");
		label.setHorizontalTextPosition(JLabel.TOP); //puts text in the center
		label.setVerticalTextPosition(JLabel.LEFT);  //puts text in the center
		label.setForeground(Color.red); //sets font color of text
		label.setFont(new Font("MV Boli",Font.PLAIN,20) ); //set font of text
	}

	public void collisions() {

	}

	public void gameOver(Graphics g) {

		label.setText("GAME OVER");
		label.setHorizontalTextPosition(JLabel.CENTER); //puts text in the center
		label.setVerticalTextPosition(JLabel.CENTER);  //puts text in the center
		label.setForeground(Color.white); //sets font color of text
		label.setFont(new Font("MV Boli",Font.PLAIN,20) ); //set font of text

	}

	public void actionPerformed(ActionEvent e) {
		for (int i = snakeX.size()-1; i > 0; i--) {
			snakeX.set(i, snakeX.get(i-1));
			snakeY.set(i, snakeY.get(i-1));
		}

		switch (direction) {
			case 'L':
				snakeX.set(0, snakeX.get(0)-1);
				break;
			case 'R':
				snakeX.set(0, snakeX.get(0)+1);
				break;
			case 'U':
				snakeY.set(0, snakeY.get(0)-1);
				break;
			case 'D':
				snakeY.set(0, snakeY.get(0)+1);
				break;
		}

		repaint();
	}
}
