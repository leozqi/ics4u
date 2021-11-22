package pong;

import java.awt.*;

public final class Constants {

	private Constants() {}

	/* Game constants */

	/* Coordinate system
	 *
	 * 0 ---------------- S_WIDTH pixels
	 * |
	 * |
	 * |
	 * |
	 * S_HEIGHT pixels
	 */

	public static final int    G_UNIT     = 20;
	public static final double FRAMES_SEC = 120d;

	public static final int S_WIDTH    = 57 * G_UNIT;
	public static final int S_HEIGHT   = 35 * G_UNIT;
	public static final int S_CENTRE_X = (S_WIDTH / 2) + 1;
	public static final int S_CENTRE_Y = (S_HEIGHT / 2) + 1;

	/* Paddle constants */

	public static final int    P_WIDTH  = 1 * G_UNIT;
	public static final int    P_HEIGHT = 5 * G_UNIT;
	public static final int    P_SPACE  = 2 * G_UNIT;
	public static final double P_SPD    = (20 * G_UNIT) / FRAMES_SEC;

	/* Ball constants */
	
	public static final int    B_SIZE = 1 * G_UNIT;
	public static final double B_SPD  = (20 * G_UNIT) / FRAMES_SEC;

	/* Midsection */
	
	public static final int M_SIZE = (G_UNIT / 3) * 2;

	/* Colours */

	public static final Color C_FORE = Color.white;
	public static final Color C_BACK = Color.black;
	public static final int F_SIZE = 5 * G_UNIT;
	public static final int F_PAD = G_UNIT;
}

