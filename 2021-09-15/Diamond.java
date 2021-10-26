/* Leo Qi
 *
 * This program creates a diamond with the user specifying the number of rows
 * of **half** of the diamond.
 */

import java.util.*;

public class Diamond{
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
			System.out.print("How many rows is half of the diamond? ");
			System.out.flush();
			try {
				rows = stdin.nextInt(); // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("Please give an integer for the row value!");
			}
		}

		// ----- Print the diamond ----- //
		int maxWidth = (rows * 2) - 1;

		// --- Print top half and middle --- //
		for (int r = 1; r <= rows; r++) {
			// For each row of the diamond:
			printSymbol(" ", (maxWidth - ((r*2)-1)) / 2, ""); // Will always be even
			printSymbol("*", (r*2)-1);
		}

		// --- Print bottom half --- //
		for (int r = rows-1; r >= 1; r--) {
			// For each row of the diamond:
			printSymbol(" ", (maxWidth - ((r*2)-1)) / 2, ""); // Will always be even
			printSymbol("*", (r*2)-1);
		}
	}
}
