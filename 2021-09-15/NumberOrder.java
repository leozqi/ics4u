/* Leo Qi
 *
 * This program keeps track of if the numbers entered by the user are in
 * increasing, decreasing, or neither increasing nor decreasing order.
 */

import java.util.*;

public class NumberOrder{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get values ----- //
		boolean increasing = true;
		boolean decreasing = true;
		double prev = 0;
		double temp = 0;
		int count = 0;

		while (true){
			System.out.print("Input a number, or character(s) to finish: ");
			System.out.flush();     // Print the output right away so `stdin`
			                        // doesn't mistake it for user input.
			try {
				temp = stdin.nextDouble();  // Get our number of inputs expected.
			} catch (Exception e){
				stdin.next();               // Use up the incorrect value anyways.
				break;
			}
			if (count == 0){
				prev = temp;
			} else if (temp > prev){
				decreasing = false;
			} else if (temp < prev){
				increasing = false;
			}
			count++;
		}

		// ----- Give results ----- //
		if ((increasing) && (!decreasing)){
			System.out.println("The number sequence is increasing.");
		} else if ((decreasing) && (!increasing)){
			System.out.println("The number sequence is decreasing.");
		} else{
			System.out.println("The number sequence is neither increasing nor decreasing.");
		}

	}
}
