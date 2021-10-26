/* Leo Qi
 *
 * Program designed to show off the Scanner in Java
 */

import java.util.*;

public class Name {
	public static void main(String [] args) {
		String Name;
		int Grade;

		Scanner stdin = new Scanner(System.in);

		System.out.println("What is your name?");
		Name = stdin.nextLine();

		System.out.println("What is your grade?");
		Grade = stdin.nextInt();

		System.out.println("Your name is " + Name
			+ " and your grade is " + Grade)
	}
}
