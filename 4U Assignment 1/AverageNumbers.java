/*
 * AverageNumbers.java
 *
 * By Leo Qi: 2021-09-17
 *
 * This program adds any real numbers from the user except zero into a variable
 * called 'sum', until the user enters zero. It then calculates and displays the
 * average of all the numbers entered.
 */

import java.util.*;

public class AverageNumbers {
	/*
	 * Method main:
	 *
	 * Takes String[] as 'args' but does not use them, returns void.
	 *
	 * This method loops in the following manner:
	 * 1. Asks the user via Scanner(System.in) to input a number.
	 * 2. Verify that the number is a number.
	 * 3. If number is zero, exit the loop.
	 * 4. Else, add the number to a variable 'sum' and increment a variable
	 *    counter by one.
	 *
	 * Then, the method calculates and displays the average by dividing sum by
	 * counter.
	 */
	public static void main(String[] args) {
		/* Get the user's input */
		Scanner stdin = new Scanner(System.in); // Create a new scanner

		int counter = 0; // Counter may be int.
		double sum = 0; // Store a sum
		double temp = 0; // store numbers temporarily here

		// Print introductory message
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.println("AverageNumbers.java\n");
		System.out.println("By Leo Qi: 2021-09-17.");
		System.out.println("Calculate the average of the inputted numbers.\n");
		System.out.println("Instructions:");
		System.out.println("1. Continue entering numbers at the prompt EXCEPT zero.");
		System.out.println("2. Enter zero at the prompt to submit.");
		System.out.println("Note: If more than 1 numbers are entered per line, only the 1st number will be used.");
		System.out.println("--------------------------------------------------------------------------------\n");

		// Start a loop to input numbers.
		while (true) {
			// print instead of println so the user can type right after the ':'
			System.out.print("Enter number(s) to calculate average (0 to finish): ");

			// Make sure only numbers are entered
			try {

				temp = stdin.nextDouble();

				// Use 'nextLine()' instead of 'next()' to get a whole line of
				// extra input, so that if a user types in spaces between inputs
				// they are not considered
				stdin.nextLine(); // Use up extra inputs

				if (temp == 0) {
					break;
				} else if (Double.isNaN(temp)) {
					// Print an error message
					// Special case: The user may enter NaN and have it be
					// considered as a double value. This can't be allowed for
					// the rest of our calculations, even if there are no errors.
					System.out.println("Please enter a number!\n");
				} else if ((temp == Double.POSITIVE_INFINITY)
					|| (temp == Double.NEGATIVE_INFINITY)) {
					// Print an error message
					// The user cannot enter positive infinity or negative infinity
					// because it does not make any sense as an average.
					// However, the main reason is because entering +Infinity and
					// then -Infinity causes NaN.
					System.out.println("Please enter a number other than infinity!\n");
				} else {
					sum += temp;
					counter++;
				}
			} catch (Exception e) {
				stdin.nextLine(); // Get the incorrect value anyways
				System.out.println("Please enter a number!\n"); // Print an error message
			}
			System.out.println("-> Current sum: " + sum);
		} // End input
		stdin.close(); // Make sure we close Scanner.

		/* Calculate and print out average */
		// If sum is equal to positive infinity, a LOT of numbers were entered and
		// the average cannot possibly be correct.
		if (sum == Double.POSITIVE_INFINITY) {
			System.out.println(
					"\nThe inputted numbers are so large that `double` registered INFINITY: the resulting average will be incorrect.");
		}

		if (counter == 0) {
			// Special case: if user presses 0 right after entry, zero/zero error occurs
			// resulting in NaN. Instead, tell the user that zero numbers have been entered.
			System.out.println("\nYou did not input any numbers, so no average can be given.");
		} else {
			System.out.println("\nThe sum of the inputted numbers is:\t" + sum);
			System.out.println("The # of valid inputted numbers was:\t" + counter);
			System.out.println("The average of the inputted numbers is:\t" + (sum / counter));
		}
	} // End method main
} // End class AverageNumbers
