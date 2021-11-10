import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class Ball {
	private Rectangle2D.Double shape;
	private double startX, startY; // store the starting pos of x, y
	private double angle; // degrees for clear calculations
	private double speed;
	private boolean toggle; // toggles whether ball has already recalculated

	private VerticalD vFace;
	private HorizontalD hFace;

	private Random random = new Random();

	public Ball(int x, int y, int size, double angle, double speed) {
		this.shape = new Rectangle2D.Double(x, y, size, size);
		this.startX = x;
		this.startY = y;
		this.angle  = angle;
		this.speed  = speed;
		this.toggle = false;

		if (random.nextBoolean()) {
			this.vFace = VerticalD.UP;
		} else {
			this.vFace = VerticalD.DOWN;
		}

		if (random.nextBoolean()) {
			this.hFace = HorizontalD.LEFT;
		} else {
			this.hFace = HorizontalD.RIGHT;
		}
	}

	public void calcTraj(boolean pTouch, boolean cTouch, double maxW, double maxH, Rectangle2D.Double player, Rectangle2D.Double computer) {
		if (!(pTouch || cTouch
			|| (this.shape.getMaxX() >= maxW)
			|| (this.shape.getMinX() <= 0)
			|| (this.shape.getMaxY() >= maxH)
			|| (this.shape.getMinY() <= 0))) {
			this.toggle = false;
			return;
		} else if (this.toggle) {
			return;
		}

		double yChange = this.shape.getY() - this.startY;
		double xChange = this.shape.getX() - this.startX;

		if (pTouch || cTouch || this.shape.getMaxX() >= maxW || this.shape.getMinX() <= 0) {
			// Bounced off vertical obstruction; only X is flipped
			if (xChange > 0) {
				// Flip from right to left
				this.hFace = HorizontalD.LEFT;
			} else if (xChange < 0) {
				this.hFace = HorizontalD.RIGHT;
			} else {
				this.hFace = HorizontalD.NEUTRAL;
			}
		}

		if (this.shape.getMinY() <= 0 || this.shape.getMaxY() >= maxH) {
			// Bounced off horizontal obstruction; only Y is flipped
			if (yChange > 0) {
				// Flip from down to up
				this.vFace = VerticalD.UP;
			} else if (yChange < 0) {
				this.vFace = VerticalD.DOWN;
			} else {
				this.vFace = VerticalD.NEUTRAL;
			}
		}

		double calcVal = 0;
		if (pTouch)      { calcVal = player.getCenterY();   }
		else if (cTouch) { calcVal = computer.getCenterY(); }

		if (pTouch || cTouch) {
			this.angle = (Math.abs(calcVal - this.shape.getCenterY()) / 37.5d) * 50;
		}

		this.startX = this.shape.getX();
		this.startY = this.shape.getY();

		this.speed *= 1.05;

		this.toggle = true;
		
		System.out.println(this.hFace);
		System.out.println(this.vFace);
		System.out.println(this.angle);
	}

	public void move(double maxW, double maxH) {
		double x = Math.cos(Math.toRadians(this.angle)) * this.speed;
		double y = Math.sin(Math.toRadians(this.angle)) * this.speed;

		if (this.vFace == VerticalD.UP) {
			y *= -1;
		}
		if (this.hFace == HorizontalD.LEFT) {
			x *= -1;
		}

		this.shape.setRect(
			this.shape.getX()+x, this.shape.getY()+y,
			this.shape.getWidth(), this.shape.getHeight()
		);
	}

	public Rectangle2D.Double getBounds() {
		return (Rectangle2D.Double) this.shape.getBounds2D();
	}
}
