/*
 * ArrayEx1.java
 *
 * By Leo Qi: 2021-10-03
 *
 * This program takes 8 marks from the user, displays them in order and tells them
 * how much above or below average each mark is.
 */

import java.util.*;     // Import java.util.* for Scanner
import java.lang.Math;  // Import java.lang.Math for absolute value method Math.abs()

public class MethodEx2 {
	/* Description */
	// Put all of these details here for the introduction() method in this class
	// to print. This makes printing everything easier.
	static final String CLASS_NAME = "ArrayEx1";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-10-03";
	static final String DESCRIPTION = "Calculates an average mark from eight provided marks.";
	static final String INSTRUCTIONS = """
		1. Input eight of your marks from your previous year.
		    * Marks should be entered as PERCENTAGES WITHOUT THE PERCENT sign.
		2. The program will calculate your average and compare each of your
		   grades to that average.""";

	/* Prints introductory message */
	static void introduction() {
		System.out.flush();
		System.out.println("\n--------------------------------------------------------------------------------");
		System.out.println(CLASS_NAME + ".java\n");
		System.out.println("By: " + AUTHOR + "\tDate: " + DATE_CREATED + "\n");
		System.out.println(DESCRIPTION + "\n");
		System.out.println("Instructions: ");
		System.out.println(INSTRUCTIONS);
		System.out.println("--------------------------------------------------------------------------------\n");
		System.out.flush();
	} // End method introduction()

	/*
	 * Method main:
	 *
	 * Takes String[] as 'args' but does not use them, returns void.
	 *
	 * This method:
	 * 1. Prints an introductory message.
	 * 2. Asks the user for eight marks.
	 * 3. Prints mark average.
	 * 4. For each mark, compares that mark to the average.
	 */
	public static void main(String[] args) {
		/* Print out introduction */
		introduction();

		// Array 'marks' will store the marks inputted by user. Give it a size of
		// 8: 0-7 spots are available.
		double[] marks = new double[8];

		// 'markSum' will store the sum of all marks, so there is no need to go
		// through the array again.
		double markSum = 0;

		/* Get user inputs */

		// Create new Scanner for input
		Scanner stdin = new Scanner(System.in);

		// Get the eight marks: i < 8 because i should go from 0-7
		for (int i = 0; i < 8; i++) {
			// Say (i + 1) so that the user sees "number 1" instead of "number 0"
			System.out.print("Enter your number " + (i + 1) + " mark: ");

			// Make sure a double comes back as a mark from the user.
			try {
				// Store double into array as one of our marks.
				// No need to check if it isn't NaN or Infinity; those are still
				// values that won't error that can be overwritten next loop
				marks[i] = stdin.nextDouble();

				// Take other inputs on the same line
				// ex. if numbers are entered with spaces separating.
				stdin.nextLine();

				if (Double.isNaN(marks[i])) {
					// Print an error message: No NaN because that's not a mark.
					System.out.println("Please enter a number!");

					// Error so reset i so that we can take that number again.
					i--;
				} else if ((marks[i] == Double.POSITIVE_INFINITY)
					|| (marks[i] == Double.NEGATIVE_INFINITY)) {
					// Print an error message: Infinity is not a realistic mark!
					System.out.println("Please enter a number other than infinity!");
					i--;
				} else {
					// There's nothing wrong with the input
					markSum += marks[i]; // add the number to the sum

					// Print out a confirmation message
					System.out.println("-> You entered " + marks[i]
						+ "% as mark " + (i + 1));
				} // End if
			} catch (Exception e) {
				// Error: That wasn't a double!!!
				stdin.nextLine(); // Use up incorrect input
				System.out.println("Please enter a percentage without a percent sign!");
				i--;
			}
		} // End input loop; Now eight numbers have all been stored
		stdin.close(); // Make sure we close Scanner.

		/* Calculate average: average = sum / number of items */
		double avg = markSum / marks.length;

		/* Print out information */
		// Print out average
		System.out.println("\nYour average was: " + avg + "%\n");

		// Print a comparison between each individual grade and the average in order entered:
		for (int i = 0; i < marks.length; i++) {
			// Print out each grade first in same line
			System.out.print("Your grade number " + (i + 1) + " was " + marks[i] + "%");

			if (marks[i] > avg) {
				// Above average message
				System.out.print(", above your average by ");
			} else if (marks[i] < avg) {
				// Below average message
				System.out.print(", below your average by ");
			} else {
				// Equal to average message
				System.out.println(", equal to the average.");
				continue; // No need to print out last fragment of sentence
			}
			// Print out difference in average
			System.out.println(Math.abs(avg - marks[i]) + "%");
		} // End for each grade print comparison

		/* Print out marks from least to greatest */
		Arrays.sort(marks); // sort marks

		System.out.print("\nFrom least to greatest, your marks were ");

		// Print each mark
		for (int i = 0; i < marks.length - 1; i++) {
			// Print out each grade first in same line
			System.out.print(marks[i] + "%, ");
		} // End printing each mark sorted

		// Print last grade without comma
		System.out.println("and " + marks[marks.length-1] + "%.");
	} // End method main(String[] args)
} // End class ArrayEx1
