/* Leo Qi
 *
 * This program counts the number of digits a number has.
 */

import java.util.*;

public class CountingDigits{
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);

		// ----- Get number ----- //
		String num;               // Will store the amount of values entered.
		System.out.print("Enter a number: ");
		System.out.flush();

		int offset = 0;

		// ----- Find number of digits of number ----- //
		num = stdin.next().strip();
		if (!(num.matches("""
				^[0-9]+[.]*[0-9]+$"""))) {
			System.out.println(
				"You did not enter a number. I'm counting characters now.");
			System.out.println("The amount of characters was " + num.length());
		} else {
			if (num.matches("""
				^[0-9]+[.][0-9]+$""")) {
				offset = 1;
			}
			System.out.println("The amount of digits was " + (num.length() - offset));
		}
	}
}
