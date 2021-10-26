/* Leo Qi
 *
 * This program creates a right-angle triangle with rows specified by the user
 * through a prompt.
 */

import java.util.*;

public class RightAngleTriangle{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get the amount of rows ----- //
		int rows = 0;               // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("How many rows should I draw? ");
			System.out.flush();
			try {
				rows = stdin.nextInt(); // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("Please give an integer for the row value!");
			}
		}

		// ----- Print the rows ----- //
		for (int r = 1; r <= rows; r++){
			for (int c = 1; c <= r; c++){   // Columns should start at 1
				System.out.print(c);        // Print each column.
			}
			System.out.print("\n");
		}
	}
}
