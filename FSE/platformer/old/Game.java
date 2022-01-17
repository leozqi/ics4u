package platformer;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class Game extends JPanel {

	SpriteHandler tileHandle;
	Dimension res = new Dimension(
				Settings.getResolutionX(),
				Settings.getResolutionY()
	);
	Level lvl;

	public Game() {
		this.setPreferredSize(res);
		this.setFocusable(true);

		this.tileHandle = SpriteHandler.createFromFile(
			this, "/resources/tiles.png", 70, 2, 2
		);
		if (this.tileHandle == null) {
			System.out.println("Could not be loaded");
		}

		this.lvl = new Level("/resources/lvl1.lvl");
		if (this.lvl == null) {
			System.out.println("Level could not be loaded");
		}
	} /* End constructor */


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (this.tileHandle == null) { return; }

		Graphics2D g2d = (Graphics2D) g;
		int unit = Settings.getUnit();

		for (int col = 0; col < this.lvl.getColNum(); col++) {
			for (int row = 0; row < this.lvl.getRowNum(col); row++) {
				BufferedImage bi = this.lvl.getTile(
					row, col, this.tileHandle
				);
				if (bi == null) { continue; }
				g2d.drawImage(bi, row*unit, col*unit, null);
			}
		}
	}
}
