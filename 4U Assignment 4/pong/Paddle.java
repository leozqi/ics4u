package pong;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Random;

import static pong.Constants.*;

public class Paddle extends KeyAdapter {
	private Mode mode;
	private HorizontalD side;
	private VerticalD movingTo;
	private Rectangle2D.Double shape;
	private double yTarget; // For COMPUTER_SMART
	private Random randGen = new Random();
	private CalcObj prevCalc = new CalcObj(0, VerticalD.NEUTRAL);

	public Paddle(Mode mode, HorizontalD side) {
		super();

		this.mode = mode;
		this.side = side;
		this.shape = new Rectangle2D.Double();

		this.reset();
	} /* End constructor */

	public Paddle() {
		this(Mode.COMPUTER_SIMPLE, HorizontalD.LEFT);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if (this.mode != Mode.PLAYER) {
			return;
		}

		int code = e.getKeyCode();

		if (this.side == HorizontalD.LEFT) {
			switch (code) {
			case KeyEvent.VK_W:
				this.changeDirection(VerticalD.UP);
				break;
			case KeyEvent.VK_S:
				this.changeDirection(VerticalD.DOWN);
				break;
			}
		} else if (this.side == HorizontalD.RIGHT) {
			switch (code) {
			case KeyEvent.VK_UP:
				this.changeDirection(VerticalD.UP);
				break;
			case KeyEvent.VK_DOWN:
				this.changeDirection(VerticalD.DOWN);
				break;
			}
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (this.mode != Mode.PLAYER) {
			return;
		}

		int code = e.getKeyCode();
		
		if ((this.side == HorizontalD.LEFT && (code == KeyEvent.VK_W || code == KeyEvent.VK_S))
			|| (this.side == HorizontalD.RIGHT && (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN))) {
			// Set neutral
			this.changeDirection(VerticalD.NEUTRAL);
		}
	}


	public void reset() {
		this.movingTo = VerticalD.NEUTRAL;

		double xPos;
		if (this.side == HorizontalD.LEFT) {
			xPos = 0 + P_SPACE;
		} else {
			xPos = S_WIDTH - P_SPACE;
		}
		this.shape.setRect(
			xPos,
			S_CENTRE_Y - (P_HEIGHT / 2),
			P_WIDTH,
			P_HEIGHT
		);
	}


	public void calcDirection(Rectangle2D.Double ballBox, Rectangle2D.Double oppBox, boolean oppTouching, CalcObj calc) {
		switch (this.mode) {
		case PLAYER:
			return;
		case COMPUTER_SIMPLE:
			this.yTarget = ballBox.getCenterY();
			break;
		case COMPUTER_SMART:
			if (calc.dir != VerticalD.NEUTRAL) {
				this.calcSmart(ballBox, oppBox, calc);
			}
			break;
		}

		if (this.yTarget > this.shape.getCenterY()) {
			this.changeDirection(VerticalD.DOWN);
		} else if (this.yTarget < this.shape.getCenterY()) {
			this.changeDirection(VerticalD.UP);
		} else {
			this.changeDirection(VerticalD.NEUTRAL);
		}
	}


	private void calcSmart(Rectangle2D.Double ballBox, Rectangle2D.Double oppBox, CalcObj calc) {
		this.yTarget = ballBox.getCenterY();
		double dBallX = Math.abs(ballBox.getCenterX() - this.shape.getCenterX());
		double dBallY;

		double y = 0;

		double xCounter = 0;

		while (dBallX > 0) {
			dBallY = xCounter * Math.tan(Math.toRadians(calc.angle));

			switch (calc.dir) {
			case UP:
				y = ballBox.getCenterY() - dBallY;
				break;
			case DOWN:
				y = ballBox.getCenterY() + dBallY;
				break;
			}

			if (y <= 0 || y >= S_HEIGHT) {
				if (calc.dir == VerticalD.UP) { calc.dir = VerticalD.DOWN; }
				else { calc.dir = VerticalD.UP; }

				xCounter = 0;
			}
			xCounter += G_UNIT;
			dBallX -= G_UNIT;
		}

		this.yTarget = y;
	}


	public void move(double speed) {
		if (this.mode != Mode.PLAYER
			&& (Math.abs(this.yTarget - this.shape.getCenterY()) < 5)) {
			return; // within good enough threshold
		}

		switch (this.movingTo) {
		case UP:
			this.shape.setRect(
				this.shape.getX(),
				Math.max(0, this.shape.getY()-speed),
				this.shape.getWidth(),
				this.shape.getHeight()
			);
			break;
		case DOWN:
			this.shape.setRect(
				this.shape.getX(),
				Math.min(S_HEIGHT - P_HEIGHT, this.shape.getY()+speed),
				this.shape.getWidth(),
				this.shape.getHeight()
			);
			break;
		}
	}


	public void changeDirection(VerticalD direction) {
		if (direction == this.movingTo) { return; }
		this.movingTo = direction;
	}


	public Rectangle2D.Double getBounds() {
		return (Rectangle2D.Double) this.shape.getBounds2D();
	}


	public Mode getMode() {
		return this.mode;
	}


	public void setMode(Mode mode, HorizontalD side) {
		this.mode = mode;
		this.side = side;
		this.reset();
	}


	public String getName() {
		String s = "";
		switch (this.mode) {
		case PLAYER:
			s += "Player";
			break;
		case COMPUTER_SIMPLE:
			s += "Simple CPU";
			break;
		case COMPUTER_SMART:
			s += "Smart CPU";
			break;
		}
		
		switch (this.side) {
		case LEFT:
			s += ": Left";
			break;
		case RIGHT:
			s += ": Right";
			break;
		}

		return s;
	}

	public boolean touching(Rectangle2D.Double rect) {
		return this.shape.intersects(
			rect.getX(), rect.getY(),
			rect.getWidth(), rect.getHeight()
		);
	}
}
