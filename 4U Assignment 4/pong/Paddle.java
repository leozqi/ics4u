package pong;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import static pong.Constants.*;

public class Paddle extends KeyAdapter {
	private Mode mode;
	private HorizontalD side;
	private VerticalD movingTo;
	private Rectangle2D.Double shape;


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
			Constants.centreY() - (P_HEIGHT / 2),
			P_WIDTH,
			P_HEIGHT
		);
	}


	public void calculateDirection(Rectangle2D.Double ballBox) {
		if (this.mode == Mode.PLAYER) {
			return;
		}

		switch (this.mode) {
		case COMPUTER_SIMPLE:
			if (ballBox.getCenterY() > this.shape.getCenterY()) {
				this.changeDirection(VerticalD.DOWN);
			} else if (ballBox.getCenterY() < this.shape.getCenterY()) {
				this.changeDirection(VerticalD.UP);
			} else {
				this.changeDirection(VerticalD.NEUTRAL);
			}
			break;
		}
	}

	public void move(double speed) {
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


	public boolean touching(Rectangle2D.Double rect) {
		return this.shape.intersects(
			rect.getX(), rect.getY(),
			rect.getWidth(), rect.getHeight()
		);
	}
}
