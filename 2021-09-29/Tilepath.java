import java.lang.Math;

public class Tilepath {
	static void paths(int xDest, int yDest, int steps, String path, int currX, int currY) {
		if (steps == 0) {
			if ((xDest == currX) && (yDest == currY)) {
				System.out.println(path);
			}
		} else {
			paths(xDest, yDest, steps-1, path + "U", currX, currY+1);
			paths(xDest, yDest, steps-1, path + "D", currX, currY-1);
			paths(xDest, yDest, steps-1, path + "R", currX+1, currY);
			paths(xDest, yDest, steps-1, path + "L", currX-1, currY);
		}
	}

	static void paths(int xDest, int yDest, int steps) {
		paths(xDest, yDest, steps, "", 0, 0);
	}

	public static void main(String[] args) {
		paths(1, 1, 12);
	}
}
