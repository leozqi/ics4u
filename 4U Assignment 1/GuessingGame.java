/*
 * GuessingGame.java
 *
 * By Leo Qi: 2021-09-17
 *
 * This program is a game where the player attempts to guess a number that the
 * computer picks. The computer will pick a number from 1 to 30 and the game
 * continues until the user correctly guesses what the number is.
 *
 * When the player guesses an incorrect number, the program will output whether
 * or not the correct number is lower, or higher.
 */

import java.util.*;
import java.util.Random;

public class GuessingGame {
	/*
	 * Method main:
	 *
	 * Takes String[] as 'args' but does not use them, returns void.
	 *
	 * This method:
	 * 1. Generates a random integer between 1 and 30 inclusive.
	 * 2. Creates a loop where the user can guess a number.
	 * 3. Continue the loop until the user guesses right.
	 * 4. If the user does guess right, show a message.
	 */
	public static void main(String[] args) {
		/* Print introductory message */
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.println("GuessingGame.java\n");
		System.out.println("By Leo Qi: 2021-09-17.");
		System.out.println("The player attempts to guess a number between 1 and 30 inclusive.\n");
		System.out.println("Instructions:");
		System.out.println("1. Enter your guess as an integer.");
		System.out.println("2. The computer will tell you if you're right, or if the number is lower or higher.");
		System.out.println("3. Repeat until you get it!");
		System.out.println("--------------------------------------------------------------------------------\n");

		/* Create a random number */
		Random rand = new Random(); // Create a random number generator
		double n = rand.nextInt(30)+1;     // Generate a random number to put in n

		/* Get the user's input */
		Scanner stdin = new Scanner(System.in); // Create a new input Scanner
		int counter = 1; // counter stores number of guesses
		double guess = 0;
		// Start a loop: break to end when user guesses right.
		while (true) {
			// print instead of println so the user can type right after the ':'
			System.out.print("Enter your number " + counter + " guess! ");
			System.out.flush();
			try {
				// Make sure numbers
				guess = stdin.nextDouble();

				// Use 'nextLine()' instead of 'next()' to get a whole line of
				// extra input, so that if a user types in spaces between inputs
				// they are not considered
				stdin.nextLine(); // Use up extra inputs

				if ((guess < 1) || (guess > 30)) {
					// Integer was not between 1 and 30 inclusive
					System.out.println("Yeah, the number *is* between 1 and 30 inclusive, right?\n");
				} else if (Double.isNaN(guess)) {
					// Special case: The user may enter NaN and have it be
					// considered as a double value. This would lead to errors
					// when comparing NaN to n
					System.out.println("That is a number, right? We're guessing numbers ;)\n");
				} else if (guess == n) {
					System.out.println("Got it! Yep, the number was " + n + "!\n");
					break;
				} else if (guess < n) {
					System.out.println("The number is *higher* than " + guess + "!\n");
				} else if (guess > n) {
					System.out.println("The number is *lower* than " + guess + "!\n");
				}
			} catch (Exception e) {
				// Catch error: user has not entered an integer!
				stdin.nextLine(); // Get the incorrect value anyways (whole line so that if a user types in spaces between inputs, they are not considered)
				System.out.println("That is a number, right? We're guessing numbers ;)\n");
			}
			counter++;
		} // End input
		stdin.close(); // Make sure we close Scanner.

		// Give an extra tip to the player depending on counter's value
		if (counter == 1) {
			System.out.println("-> Genius (or lucky)! You got it the first time!");
		} else if (counter <= 4) {
			System.out.println("-> Nice! You got it within four guesses!");
		} else if (counter <= 8) {
			System.out.println("-> Pretty good! You got it within eight guesses.");
		} else {
			System.out.println("-> Okay, well you got it at least!");
		}
	} // End method main
} // End class Factorial
