/* Leo Qi
 *
 * This program creates a pyramid with the user specifying the number of rows
 * of **half** of the pyramid.
 */

import java.util.*;

public class Pyramid{
	static void printSymbol(String symbol, int amt, String end){
		for (int i = 0; i < amt; i++){
			System.out.print(symbol);
		}
		System.out.print(end);
		System.out.flush();
	}

	static void printSymbol(String symbol, int amt){
		printSymbol(symbol, amt, "\n");
	}

	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get the amount of rows ----- //
		int rows = 0;               // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("How many rows is the pyramid? ");
			System.out.flush();
			try {
				rows = stdin.nextInt(); // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("Please give an integer for the row value!");
			}
		}

		// ----- Print the pyramid ----- //
		int maxWidth = (rows * 4) - 1;

		for (int r = 1; r <= rows; r++) {
			// For each row of the pyramid:
			printSymbol(" ", (maxWidth - ((r * 4) - 1)) / 2, "");
			printSymbol("* ", (r * 2) - 1);
		}
	}
}
