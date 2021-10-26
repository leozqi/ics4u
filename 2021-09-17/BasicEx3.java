/*
 * BasicEx3.java
 *
 * By Leo Qi: 2021-09-17
 *
 * Asks the user for an integer between 1 and 50: outputs all of that integer's
 * factors.
 */

import java.util.*;

public class BasicEx3 {
	/*
	 * Method main:
	 *
	 * Takes String[] args as parameter (not used), returns void.
	 *
	 * This method:
	 * 1. Displays an introductory message and how to use the program.
	 * 2. Prompts the user for an integer between one and 50.
	 * 3. Calculates all of its factors.
	 */
	public static void main(String[] args) {
		/* Print an introductory message */
		System.out.println("\n--------------------------------------------------");
		System.out.println("BasicEx3.java\n");
		System.out.println("By Leo Qi: 2021-09-17.");
		System.out.println("Calculate the factors of an integer.\n");
		System.out.println("Instructions:");
		System.out.println("1. Enter an integer between 1 and 50 inclusive to calculate its factors.");
		System.out.println("--------------------------------------------------\n");

		/* Get the user's input */
		int n = 0; // Store the user's input as n.
		Scanner stdin = new Scanner(System.in); // create a Scanner input object (declare & initialize)

		// Use a loop to ask over and over again for a number if the user does not input one.
		while (true) {
			// Prompt: use print so the user can type right after the ':'
			System.out.print("Enter an integer (1-50): ");
			System.out.flush(); // Flush output so that the above does not become input next

			// Only integers should be entered
			try {
				// Store a number by the user: if it isn't a number go to catch { . . . }
				n = stdin.nextInt();
				if ((n < 1) || (n > 50)) {
					// Integer was not between 1 and 50 inclusive
					System.out.println("\nPlease enter an integer between 1 and 50 inclusive!\n");
				} else {
					// Integer was between 1 and 50 inclusive
					break; // Exit the loop -> a VALID number has been provided.
				}
			} catch (Exception e) {
				// User did not enter an integer!
				// Take the wrong value anyways or it will be used as input next loop
				stdin.next();
				System.out.println("\nPlease enter an integer!\n");
			}
		} // End input loop
		stdin.close(); // Make sure we close Scanner.

		System.out.println("\nThe factors of " + n + " are:");

		/* Calculate the factors */

		// Only integers from 1 to 50 could be factors of an integer between 1 and 50.
		for (int i = 1; i <= 50; i++) {
			if ((n % i) == 0) {
				// i is a factor because n can be evenly divided by i (with remainder of zero)
				System.out.println(i + " is a factor of " + n);
			}
		}
	} // End main method
} // End class BasicEx2
