/* Leo Qi
 *
 * This program calculates the sum and average of a list of numbers, which the
 * user inputs through prompts on the command line.
 */

import java.util.*;

public class SumAndAverage{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get the amount of values to sum ----- //
		int amt = 0;                // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("How many numbers would you like to input: ");
			System.out.flush();     // Print the output right away so `stdin`
			                        // doesn't mistake it for user input.
			try {
				amt = stdin.nextInt();  // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("You must enter an integer!");
			}
		}

		// ----- Get each value and store it in sum ----- //
		double sum = 0;                     // Store the sum of all those values.
		for (int i = 0; i < amt; i++){      // We loop to get every number added.
			System.out.print("Enter number " + (i+1) + ": ");
			try {
				sum += stdin.nextDouble();  // Add our double value to the sum
			} catch (Exception e){
				stdin.next();   // As with before, consume incorrect value
				System.out.println("Please only enter numbers!");
				i--;            // Allow the user to enter the number again.
			}
		}

		// ----- Print our sums and averages ----- //
		System.out.println("\nThe sum of the numbers was " + sum);
		System.out.println("The average of the numbers was " + (sum/amt));
	}
}
