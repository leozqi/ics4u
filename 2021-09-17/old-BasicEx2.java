/*
 * BasicEx2.java
 *
 * By Leo Qi: 2021-09-17
 *
 * This program finds the sum of the terms of a series defined by the user. This
 * series must have a common ratio between each term.
 *
 * The sum of this series with terms x_1, x_2, x_3, x_4, ... x_n is defined in
 * class as the first term x_1 divided by (1 - common ratio).
 */

import java.util.*;
import java.util.Random;

public class BasicEx2 {
	/*
	 * Method main:
	 *
	 * Takes String[] args as parameter (not used), returns void.
	 *
	 * This method:
	 * 1. Displays an introductory message and how to use the program.
	 * 2. Prompts the user for each term of the series.
	 * 3. Calculates and displays the result.
	 */
	public static void main(String[] args) {
		/* Print an introductory message */
		System.out.println("\n--------------------------------------------------");
		System.out.println("BasicEx2.java\n");
		System.out.println("By Leo Qi: 2021-09-17.");
		System.out.println("Calculate the sum of _n_ terms of an infinite series inputted by the user.");
		System.out.println("Sum of series = first term divided by (1 - common ratio).\n");
		System.out.println("Instructions:");
		System.out.println("1. Enter three terms of the series with value descending: ");
		System.out.println("--------------------------------------------------\n");

		/* Get the user's input */
		double firstTerm = 0;   // store first term inside
		double ratio = 0;       // store ratio inside.
		int counter = 1;    // keep track of which term we are on; terms start at term one.
		double temp;        // store values here before we put them into firstTerm or ratio.
		double prev = 0;    // just to check if our ratio is still correct
		boolean isIncreasing = false;

		// Start a loop to make sure our input is correct.
		Scanner stdin = new Scanner(System.in); // create a Scanner input object (declare & initialize)
		while (true) {
			// The below is a prompt: print instead of println so the user can type right after the ':'
			System.out.print("Enter term " + counter + " of the series (or not-a-number to quit): ");

			// Make sure only numbers are entered.
			try {
				// Get numbers only; the below will throw errors that try catches
				temp = stdin.nextDouble();

				if (counter == 1) {
					firstTerm = temp;
				} else if (counter == 2) {
					if ((temp - firstTerm) > 0) {
						isIncreasing = true;
					} else {
						isIncreasing = false;
					}
					ratio = temp / firstTerm;
				} else if ((temp / prev) != ratio) {
					// The user did not enter a correct next term for the series defined in class
					System.out.println("Please enter a correct next term for this series!\n");
					continue; // Do not execute statements at the bottom of the loop for incorrect answers
				}
			} catch (Exception e) { // Catch a general error even though more are possible
				// Catch error: user has not entered a number! Calculate sum
				stdin.next(); // Get the incorrect value anyways
				break;
			}
			prev = temp;
			counter++;
		} // End input loop

		stdin.close(); // Make sure we close Scanner.

		/* Calculate the sum */
		double sum;
		sum = firstTerm / (1 - ratio);


		if (counter < 2) {
			// We do not have the first term, or first term and ratio stored.
			// So we can't calculate the sum: Sum = first term / (1 - ratio)
			// We say counter-1 terms provided because counter starts at one.
			System.out.println("There is insufficient data to calculate a series with " + (counter-1) + " terms provided.");
		} else {
			System.out.println("The sum of the series entered is: " + sum);
		}
	} // End main method
} // End class BasicEx2
