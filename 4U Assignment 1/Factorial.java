/*
 * Factorial.java
 *
 * By Leo Qi: 2021-09-16
 *
 * This program takes in an integer as user input through a prompt, and then
 * calculates its factorial. It then displays a visual calculation and final
 * value.
 */

import java.util.*;

public class Factorial {
	/*
	 * Method main:
	 *
	 * Takes String[] as 'args' but does not use them, returns void.
	 *
	 * This method:
	 * 1. Asks the user via Scanner(System.in) to input a positive integer.
	 * 2. Makes sure that the input is positive and an integer before continuing.
	 * 3. Prints and calculates factorial.
	 */
	public static void main(String[] args) {
		/* Get the user's input */
		Scanner stdin = new Scanner(System.in); // Create a new scanner

		int n = 0; // We store our number in n: we say n! for factorials

		// Print introductory message
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.println("Factorial.java\n");
		System.out.println("By Leo Qi: 2021-09-16.");
		System.out.println("Calculate the factorial of a positive integer.\n");
		System.out.println("Instructions:");
		System.out.println("1. Enter a positive integer at the prompt.");
		System.out.println("--------------------------------------------------------------------------------\n");

		// Start a loop to make sure our input is correct.
		while (true) {
			// print instead of println so the user can type right after the ':'
			System.out.print("Enter a positive integer to calculate its factorial: ");
			try {
				n = stdin.nextInt();

				// Use 'nextLine()' instead of 'next()' to get a whole line of
				// extra input, so that if a user types in spaces between inputs
				// they are not considered
				stdin.nextLine(); // Use up extra inputs

				// Make sure only POSITIVE integers are entered
				if (n < 0) {
					// Print error message with two newlines for separation
					System.out.println("Please enter a positive integer!\n");
				} else {
					break; // Exit the loop
				}
			} catch (Exception e) {
				// Catch error: user has not entered an integer!
				stdin.nextLine(); // Get the incorrect value anyways
				System.out.println("Please enter a valid integer!\n");
			}
		} // End input
		stdin.close(); // Make sure we close Scanner.

		/* Calculate and display factorial */
		// Use a double to represent very large numbers without errors.
		// Set it to one so that we can multiply from the start.
		double result = 1;

		System.out.print(n + "! = "); // Print the beginning: "n! = "

		// Count down from n while multiplying result by the current number i
		// This way we can get the factorial relatively easily; also one of the
		// accepted results of 0! works too: 0! = 1 = 1
		for (int i = n; i > 1; i--) {
			System.out.print(i + "*"); // Display "i*" over and over again
			result *= i; // Multiply result by i (the factorial part)
		}

		// Print out one separately because it doesn't have a star after it.
		System.out.println("1 = " + result);

		// If result is equal to infinity, explain that it probably isn't.
		if (result == Double.POSITIVE_INFINITY) {
			System.out.println("The result is probably not infinity, just so large the 'double' type can't hold it!");
		}
	} // End method main
} // End class Factorial
