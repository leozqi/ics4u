// BasicInput1.java
// Gets the amount of money the user has and converts that to Egyptian pounds
// (1 CAD = 5.25282 EGP)
import java.util.*;

// Object initialization:
// * Declare: you prepare the object to use
// * Initialize: we create the object
// * Invoke: we use the methods of the object

public class BasicInput1 {
	public static void main(String [] args)
	{
		final double CAD_TO_EGP = 5.25282;
		double can, egp;
		Scanner stdin = new Scanner(System.in); // create scanner object

		System.out.print("How many Canadian dollars do you have? ");
		can = stdin.nextDouble(); // retrieves next entered double
		egp = can * CAD_TO_EGP;
		System.out.printf("That is %.2f Egyptian pounds\n", egp);
	}
}
