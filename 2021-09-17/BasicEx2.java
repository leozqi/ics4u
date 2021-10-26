/*
 * BasicEx2.java
 *
 * By Leo Qi: 2021-09-17
 *
 * This program finds the sum of an infinite series defined by the user. This
 * series must have a common ratio between each term.
 *
 * The user will enter three terms, each smaller than the previous one, and the
 * program will calculate the sum of the infinite series based on those three terms.
 */

import java.util.*;

public class BasicEx2 {
	/*
	 * Method main:
	 *
	 * Takes String[] args as parameter (not used), returns void.
	 *
	 * This method:
	 * 1. Displays an introductory message and how to use the program.
	 * 2. Prompts the user for each term out of 3 for the series.
	 * 3. Calculates and displays the result.
	 */
	public static void main(String[] args) {
		/* Print an introductory message */
		System.out.println("\n--------------------------------------------------");
		System.out.println("BasicEx2.java\n");
		System.out.println("By Leo Qi: 2021-09-17.");
		System.out.println("Calculate the sum of 3 terms of an infinite series inputted by the user.");
		System.out.println("Sum of series = first term divided by (1 - common ratio).\n");
		System.out.println("Instructions:");
		System.out.println("1. Enter three numbers representing three terms of the series.");
		System.out.println("--------------------------------------------------\n");

		/* Get the user's input */
		double firstTerm = 0;   // store first term inside
		double ratio = 0;       // store ratio inside.

		// Stores the previous value to check the correctness of the user's inputs.
		// Since we store it to check for whether the next inputted number is
		// smaller (to make sure it is descending), it has to start as big as
		// possible.
		double prev = Double.MAX_VALUE;

		// temp stores the user's inputted value as a placeholder before we
		// assign other variables to it.
		double temp = 0;

		// Create a Scanner input object (declare & initialize)
		Scanner stdin = new Scanner(System.in);

		// Get three terms
		for (int term = 1; term <= 3; term++) {
			// Prompt: use print so the user can type right after the ':'
			System.out.print("Enter term " + term + " of the series: ");
			System.out.flush(); // Flush output so that the above does not become input

			// Make sure only numbers are entered.
			try {
				// Store a number by the user: if it isn't a number go to catch { . . . }
				temp = stdin.nextDouble();

				if (temp > prev) {
					// If the entered 'temp' value is greater than the previously
					// entered value, it is not valid.
					System.out.println("\nPlease enter terms in descending value!\n");

					term--;   // Roll back the loop one value so that it will ask for the same term again.
					continue; // Skip the final statement that sets previous to temp as temp is not valid.
				} else if (term == 1) {
					// Store first term as it is needed for our calculation.
					firstTerm = temp;
				} else if (term == 2) {
					// Store ratio of first to second term as it is needed for our calculation.
					ratio = temp / firstTerm;
				} else if ((temp / prev) != ratio) {
					// Check to make sure the third term fits within the ratio of the first and second terms
					// temp (the value entered) divided by the previous value entered must equal ratio
					// t_3 / t_2 = ratio as t_2 * ratio = t_3

					// If it isn't, tell the user that the entered value is wrong:
					System.out.println("\nThe entered value isn't the expected next term!");
					System.out.println("Ratio: " + ratio);

					// Expected value is given as t_2 (stored in previous) * ratio
					// Show it so that the user knows what is wrong
					System.out.println("Expected third term value: " + (prev * ratio));
					System.out.println("If the value is a long decimal, you may need"
						+ " to enter a few more digits for it to be considered equivalent.\n");

					term--;   // Roll back the loop one value so that it will ask for the same term again.
					continue; // Skip the final statement that sets previous to temp as temp is not valid.
				}
			} catch (Exception e) {
				// User did not enter a number! Show warning
				stdin.next(); // Take whatever the user entered so that it doesn't become input next
				System.out.println("\nPlease enter a number!\n");
				term--;   // Roll back the loop one value so that it will ask for the same term again.
				continue; // Skip the final statement that sets previous to temp as temp is not valid.
			}
			prev = temp;
		} // End input loop
		stdin.close(); // Make sure we close Scanner.

		/* Calculate the sum and print */
		// Extra new lines before and after for visuals
		System.out.println("\nThe sum of the infinite series entered is: " + (firstTerm / (1 - ratio)) + "\n");
	} // End main method
} // End class BasicEx2
