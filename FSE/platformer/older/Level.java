// ------------------------------------------------------------------------- //
// Stores one single level of the game.                                      //
//                                                                           //
// Author:      Leo Qi                                                       //
// Start date:  2022-01-02                                                   //
// Finish date:                                                              //
// ------------------------------------------------------------------------- //

package platformer;

import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;

public class Level {

	/*
	 *      y
	 * x: | 0 | 1 | 2 | 3 |
	 *    | 1 |
	 *    | 2 |
	 *    | 3 |
	 */
	private TileMap[][] map; // [x][y]
	private int rows;
	private int columns;

	public Level(String filePath) {
		URL url = this.getClass().getResource(filePath);

		BufferedReader stdin;
		int gRow = 0;
		int gCol = 0;
		Charset chartype = Charset.forName("UTF-8");
		ArrayList<String> temp = new ArrayList<String>();

		try {
			stdin = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(
						new File(url.toURI())
					),
					chartype
				)
			);

			String ln = stdin.readLine();
			while (ln != null) {
				temp.add(ln);
				if (ln.length() > gCol) {
					gCol = ln.length();
				}
				ln = stdin.readLine();
				gRow++;
			}
			stdin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.map = new TileMap[gCol][gRow];
		this.rows = gRow;
		this.columns = gCol;

		String s;
		for (int row = 0; row < this.rows; row++) {
			s = temp.get(row);
			for (int col = 0; col < s.length(); col++) {
				this.setBlock(
					col, row,
					TileMap.fromChar(
						Biome.SWAMPY,
						s.codePointAt(col)
					)
				);
			}
		}
	} /* End constructor */


	public void setBlock(int x, int y, TileMap type) {
		if (this.map == null) { return; }
		synchronized(this.map) {
			this.map[x][y] = type;
		}
	} /* End method setBlock */


	public TileMap getBlock(int x, int y) {
		if (this.map == null) { return new TileMap(); }
		synchronized(this.map) {
			return this.map[x][y];
		}
	} /* End method getBlock */


	public int getColNum() { return this.map.length; }
	public int getRowNum(int colNum) { return this.map[colNum].length; }


	public BufferedImage getTile(int x, int y, SpriteHandler th) {
		TileMap t = this.getBlock(x, y);

		if (t == null)   { return null; }
		if (t.isEmpty()) { return null; }

		BufferedImage bi = t.getTile(th);
		
		if (bi == null) { return null; }

		int unit = Settings.getUnit();
		if ( (bi.getWidth() != unit) || (bi.getHeight() != unit) ) {
			return resize(bi, unit, unit);
		}
		return bi;
	} /* End method getTile */


	public BufferedImage resize(BufferedImage img, int w, int h) {
		Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage ret = new BufferedImage(w, h, img.getType());

		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return ret;
	} /* End method resize */

} /* End class Level */
