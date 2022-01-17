// ------------------------------------------------------------------------- //
// Stores one single level of the game.                                      //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-02                                                   //
// Finish date: 2022-01-04                                                   //
// ------------------------------------------------------------------------- //

package platformer;

import java.net.URL;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class Level {

	/**
	 * Each overlay is of the form below.
	 *
	 *       row
	 * col: | 0 | 1 | 2 | 3 |
	 *      | 1 |
	 *      | 2 |
	 *      | 3 |
	 */
	private TileMap[][] map;
	private int rows, columns;
	private Area bounds;

	/**
	 * The Level class holds the data for one level.
	 *
	 * @param path Path of a file
	 */
	public Level(String path) {
		URL url = this.getClass().getResource(path);
		BufferedReader stdin;
		this.rows = 0;
		this.columns = 0;
		ArrayList<String> tmp = new ArrayList<String>();
		this.bounds = new Area();
		try {
			stdin = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File(url.toURI())
					),
					Charset.forName("UTF-8")
				)
			);

			String ln = stdin.readLine();
			while (ln != null) {
				tmp.add(ln);
				if (ln.length() > this.columns) {
					this.columns = ln.length();
				}
				ln = stdin.readLine();
				this.rows++;
			}
			stdin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.map = new TileMap[this.rows][this.columns];
		String s;
		for (int row = 0; row < this.rows; row++) {
			s = tmp.get(row);
			for (int col = 0; col < s.length(); col++) {
				this.setBlock(
					row, col,
					TileMap.fromChar(
						Biome.SANDY,
						s.codePointAt(col)
					)
				);
			}
		}
	} /* End constructor */


	public void setBlock(int row, int col, TileMap type) {
		if (this.map == null) { return; }
		synchronized(this.map) {
			this.map[row][col] = type;
			if (!type.isPassable()) {
				this.bounds.add(new Area(new Rectangle2D.Double(
					0 + (row*Settings.internUnit),
					0+(col*Settings.internUnit),
					Settings.internUnit,
					Settings.internUnit)
				));
			}
		}
	} /* End method setBlock */


	public TileMap getBlock(int row, int col) {
		if (this.map == null) { return new TileMap(); }
		synchronized(this.map) {
			return this.map[row][col];
		}
	} /* End method getBlock */


	public int getRowNum() { return this.map.length; }
	public int getColNum(int rowNum) { return this.map[rowNum].length; }


	public BufferedImage getTile(int row, int col, SpriteHandler th) {
		TileMap t = this.getBlock(row, col);

		if (t == null)   { return null; }
		if (t.isEmpty()) { return null; }

		BufferedImage bi = t.getTile(th);

		if (bi == null) { return null; }
		return bi;
	} /* End method getTile */


	public BufferedImage getLevel(SpriteHandler th) {
		int u = Settings.internUnit;
		int xDim = this.columns * u;
		int yDim = this.rows * u;

		BufferedImage ret = new BufferedImage(
			xDim, yDim,
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D g2d = ret.createGraphics();

		for (int row = 0; row < this.getRowNum(); row++) {
			for (int col = 0; col < this.getColNum(row); col++) {
				BufferedImage bi = this.getTile(row, col, th);
				if (bi == null) { continue; }
				g2d.drawImage(bi, col*u, row*u, null);
			}
		}
		return ret;
	} /* End method getLevel */


	public Area getBounds() {
		return this.bounds;
	}

} /* End class Level */
