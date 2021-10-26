import java.util.*;

public class Age {
	public static void main(String[] args) {
		int age;
		Scanner stdin = new Scanner(System.in);

		System.out.print("Enter your age: ");
		age = stdin.nextInt();

		if (age > 65) {
			System.out.println("You are old . . .");
		} else if ((age >= 20) && (age < 65)) {
			System.out.println("You're an adult!");
		} else if ((age < 20) && (age > 0)) {
			System.out.println("You're a teen!");
		} else {
			System.out.println("Are you sure you're typing this right now?");
		}
	}
}
