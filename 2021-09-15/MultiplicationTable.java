/* Leo Qi
 *
 * This program displays the multiplication table to a given amount of rows, of
 * a given integer.
 */

import java.util.*;

public class MultiplicationTable{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get values ----- //
		int num = 0;

		boolean isNotNum = true;
		while (isNotNum){
			System.out.print("Input the number to calculate for: ");
			System.out.flush();     // Print the output right away so `stdin`
			                        // doesn't mistake it for user input.
			try {
				num = stdin.nextInt();  // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();               // Use up the incorrect value anyways.
				System.out.println("Please enter an integer value!");
				break;
			}
		}

		int rows = 0;
		isNotNum = true;
		while (isNotNum){
			System.out.print("Input the number of rows to calculate: ");
			System.out.flush();     // Print the output right away so `stdin`
									// doesn't mistake it for user input.
			try {
				rows = stdin.nextInt();  // Get our number of inputs expected.
				isNotNum = false;
			} catch (Exception e){
				stdin.next();               // Use up the incorrect value anyways.
				System.out.println("Please enter an integer value!");
				break;
			}
		}

		for (int r = 1; r <= rows; r++){
			System.out.println(num + " X " + r + "\t= " + (num*r));
		}
	}
}
