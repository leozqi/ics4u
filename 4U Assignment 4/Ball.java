import java.awt.*;
import java.awt.geom.*;

public class Ball {
	public HorizontalD facing;
	private Rectangle2D.Double shape;
	private double startX, startY; // store the starting pos of x, y
	private double angle; // in radians

	public Ball(HorizontalD facing, int x, int y, int size, double angleDeg) {
		this.facing = facing;
		this.shape = new Rectangle2D.Double(x, y, size, size);
		this.startX = x;
		this.startY = y;
		this.angle = Math.toRadians(angle);
	}

	public void calcTraj() {
		if (Math.hypot(this.startX - this.shape.getX(), this.startY - this.shape.getY()) < 100) {
			return;
		}
		double newRise = this.shape.getY() + (this.shape.getY() - this.startY);
		double newRun = Math.abs(this.shape.getX() - this.startX);

		this.angle = Math.atan(newRise / newRun);
		System.out.println(angle);
		this.startX = this.shape.getX();
		this.startY = this.shape.getY();

		switch (this.facing) {
		case LEFT:
			this.facing = HorizontalD.RIGHT; break;
		case RIGHT:
			this.facing = HorizontalD.LEFT; break;
		}
	}

	public void move(double speed) {
		double x = Math.cos(this.angle) * speed;
		double y = Math.sin(this.angle) * speed;

		switch (this.facing) {
		case LEFT:
			this.shape.setRect(
				this.shape.getX()-x, this.shape.getY()-y,
				this.shape.getWidth(), this.shape.getHeight()
			);
			break;
		case RIGHT:
			this.shape.setRect(
				this.shape.getX()+x, this.shape.getY()+y,
				this.shape.getWidth(), this.shape.getHeight()
			);
			break;
		}
	}

	public Rectangle2D.Double getBounds() {
		return (Rectangle2D.Double) this.shape.getBounds2D();
	}
}
