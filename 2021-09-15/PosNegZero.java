/* Leo Qi
 *
 * This program checks if an inputted integer is negative, zero, or positive.
 */

import java.util.*;

public class PosNegZero{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get number ----- //
		int num = 0;               // Will store the amount of values entered.
		boolean isNotNum = true;    // If it is a number, stop the below loop.
		while (isNotNum) {          // Will repeat until user enters an integer.
			System.out.print("Enter an integer: ");
			System.out.flush();
			try {
				num = stdin.nextInt(); // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();           // Use up the incorrect value anyways.
				System.out.println("Please give an integer!");
			}
		}

		// ----- Calculate ----- //
		if (num > 0) {
			System.out.println("Your integer is positive.");
		} else if (num == 0) {
			System.out.println("Your integer is zero.");
		} else {
			System.out.println("Your integer is negative.");
		}
	}
}
