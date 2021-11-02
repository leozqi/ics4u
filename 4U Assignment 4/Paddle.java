import java.awt.*;
import java.awt.geom.*;

public class Paddle {
	private boolean isPlayer;
	private HorizontalD side;
	private VerticalD movingTo;
	private Rectangle2D.Double shape;

	public Paddle(boolean isPlayer, HorizontalD side, int x, int y, int width, int height) {
		this.isPlayer = isPlayer;
		this.side = HorizontalD.LEFT;
		this.movingTo = VerticalD.NEUTRAL;

		this.shape = new Rectangle2D.Double(x, y, width, height);
	}

	public void move(double speed) {
		switch (this.movingTo) {
		case UP:
			this.shape.setRect(
				this.shape.getX(), this.shape.getY()-speed,
				this.shape.getWidth(), this.shape.getHeight()
			);
			break;
		case DOWN:
			this.shape.setRect(
				this.shape.getX(), this.shape.getY()+speed,
				this.shape.getWidth(), this.shape.getHeight()
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

	public boolean touching(Rectangle2D.Double rect) {
		return this.shape.intersects(
			rect.getX(), rect.getY(),
			rect.getWidth(), rect.getHeight()
		);
	}
}
