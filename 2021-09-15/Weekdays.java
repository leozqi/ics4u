/* Leo Qi
 *
 * This program displays a name for a weekday for each number from 1-7
 * 1 is Monday, 7 is Sunday.
 */

import java.util.*;

public class Weekdays{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get the value of day ----- //
		int day = 0;                // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("Enter a number to represent a weekday (1-7): ");
			System.out.flush();     // Print the output right away so `stdin`
			                        // doesn't mistake it for user input.
			try {
				day = stdin.nextInt();  // Get our number of inputs expected.
				if ((day < 1) || (day > 7)) {
					System.out.println("Enter a number between 1 and 7 inclusive!");
					continue;
				}
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("You must enter an integer!");
			}
		}

		// ----- Switch depending on day of the week ----- //
		String dayAsString = "";
		switch (day) {
			case 1:
				dayAsString = "Monday";
				break;
			case 2:
				dayAsString = "Tuesday";
				break;
			case 3:
				dayAsString = "Wednesday";
				break;
			case 4:
				dayAsString = "Thursday";
				break;
			case 5:
				dayAsString = "Friday";
				break;
			case 6:
				dayAsString = "Saturday";
				break;
			case 7:
				dayAsString = "Sunday";
		}

		System.out.println("That's " + dayAsString + "!");
	}
}
