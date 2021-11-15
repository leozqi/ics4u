package pong;

import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

import static pong.Constants.*;

public class Ball {

	private Rectangle2D.Double shape;
	private double startX, startY; // store the starting pos of x, y
	private double angle; // degrees for clear calculations
	private double speed;
	private boolean toggle; // toggles whether ball has already recalculated

	private boolean facingUp;
	private boolean facingRight;

	private Random randGen = new Random();


	public Ball() {
		this.shape = new Rectangle2D.Double();
		reset();
	}


	public void reset() {
		this.startX = S_CENTRE_X - B_SIZE;
		this.startY = S_CENTRE_Y - B_SIZE;
		this.angle  = this.startAngle();
		
		this.facingUp    = this.randGen.nextBoolean();
		this.facingRight = this.randGen.nextBoolean();

		this.speed  = B_SPD;
		this.toggle = false;

		this.shape.setRect(
			this.startX,
			this.startY,
			B_SIZE,
			B_SIZE
		);
	}


	private double startAngle() {
		return this.randGen.nextInt(20) + 1;
	}


	public CalcObj calcAngle(
		boolean lTouch, boolean rTouch,
		Rectangle2D.Double lBox, Rectangle2D.Double rBox
	) {
		if (!(lTouch || rTouch
			|| (this.shape.getMaxX() >= S_WIDTH)
			|| (this.shape.getMinX() <= 0)
			|| (this.shape.getMaxY() >= S_HEIGHT)
			|| (this.shape.getMinY() <= 0))) {
			this.toggle = false;
			if (this.facingUp) {
				return new CalcObj(this.angle, VerticalD.UP);
			} else {
				return new CalcObj(this.angle, VerticalD.DOWN);
			}
		} else if (this.toggle) {
			return new CalcObj(0, VerticalD.NEUTRAL);
		}

		if (lTouch || rTouch || this.shape.getMaxX() >= S_WIDTH || this.shape.getMinX() <= 0) {
			this.facingRight = !this.facingRight;
		}

		if (this.shape.getMinY() <= 0 || this.shape.getMaxY() >= S_HEIGHT) {
			this.facingUp = !this.facingUp;
		}

		double calcVal = 0;
		if (lTouch) {
			calcVal = lBox.getCenterY();
		} else if (rTouch) {
			calcVal = rBox.getCenterY();
		}

		if (lTouch || rTouch) {
			this.angle = (
				Math.abs(calcVal - this.shape.getCenterY())
				/ (((double) P_HEIGHT) / 2)
				* 50
			);

			if (calcVal - this.shape.getCenterY() > 0) {
				// Going Up
				this.facingUp = true;
			} else if (calcVal - this.shape.getCenterY() < 0) {
				this.facingUp = false;
			}
		}

		this.speed *= 1.05;
		this.toggle = true;

		if (this.facingUp) {
			return new CalcObj(this.angle, VerticalD.UP);
		} else {
			return new CalcObj(this.angle, VerticalD.DOWN);
		}
	}


	public void move() {
		double x = Math.cos(Math.toRadians(this.angle)) * this.speed;
		double y = Math.sin(Math.toRadians(this.angle)) * this.speed;

		if (this.facingUp)     { y *= -1; }
		if (!this.facingRight) { x *= -1; } 

		this.shape.setRect(
			this.shape.getX()+x, this.shape.getY()+y,
			this.shape.getWidth(), this.shape.getHeight()
		);
	}


	public Rectangle2D.Double getBounds() {
		return (Rectangle2D.Double) this.shape.getBounds2D();
	}
}
