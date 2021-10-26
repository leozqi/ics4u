/* Leo Qi
 *
 * This program displays up to n natural numbers and their cubes.
 */

import java.util.*;

public class DisplayNNaturalNumbers{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get the value of n ----- //
		int n = 0;                  // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("Print up to *n* numbers? ");
			System.out.flush();     // Print the output right away so `stdin`
			                        // doesn't mistake it for user input.
			try {
				n = stdin.nextInt();  // Get our number of inputs expected.
				if (n <= 1) {
					System.out.println("You must enter a number greater than or equal to 1!");
					continue;
				}
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("You must enter an integer!");
			}
		}

		// ----- Print up to n natural numbers ----- //
		for (int i = 1; i <= n; i++){      // We loop to get every number added.
			System.out.println("Number is : " + i + " and cube of " + i + " is : " + (i*i));
		}
	}
}
