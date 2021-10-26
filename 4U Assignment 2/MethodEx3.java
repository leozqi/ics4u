/*
 * MethodEx3.java
 *
 * By Leo Qi: 2021-10-03
 *
 * The odds method takes an array of integers and returns an array of integers
 * with even numbers removed (ie. with only odd numbers)
 */

import java.util.*; // Import java.util.* for Scanner, Arrays

public class MethodEx3 {
	/* Description */
	// Put all of these details here for the introduction() method in this class
	// to print. This makes printing everything easier.
	static final String CLASS_NAME = "MethodEx3";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-10-03";
	static final String DESCRIPTION = "This program removes all even integers from input using the `odds` method";
	static final String INSTRUCTIONS = """
		1. Input integers at the prompt; press zero to end input.
		    * If more than one integer is entered per line, only the first will
		      be entered.
		2. The program will output your integers without any even ones.""";

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
	 * Method odds:
	 *
	 * Takes an array of integers 'intArr'.
	 * Returns an array of integers with even numbers removed
	 */
	static int[] odds(int[] intArr) {
		// Counnt stores how many odd numbers there are in intArr
		int count = 0;

		// Count all the odd numbers in intArr to get size of returned array
		for (int i = 0; i < intArr.length; i++) {
			// Remainder of divison by 2 is always non-zero for odd numbers
			if (intArr[i] % 2 != 0) {
				count++;
			}
		}

		// Make a new array to return based on amount of odd numbers counted
		int[] ret = new int[count];
		count = 0;  // Reset count; this time use it as position of ret

		// Go through intArr adding odd numbers to position count of ret.
		for (int j = 0; j < intArr.length; j++) {
			if (intArr[j] % 2 != 0) {
				// When we add a new odd number to ret increase count (the position)
				ret[count] = intArr[j];
				count++;
			}
		}
		// Return finished array
		return ret;
	} // End method odds(int[] intArr)

	/*
	 * Method main:
	 *
	 * Takes no arguments as parameters, returns void.
	 *
	 * This method:
	 * 1. Takes in integers as input
	 * 2. Removes the even integers through the 'odds' method.
	 * 3. Prints out the array of odd integers as a string.
	 */
	public static void main(String[] args) {
		/* Print introduction */
		introduction();

		ArrayList<Integer> list = new ArrayList<Integer>(); // list will store user-inputted ints

		/* Get user input */
		Scanner stdin = new Scanner(System.in);
		int temp = 0; // Stores user inputted ints temporarily
		// Start a loop to input numbers.
		while (true) {
			// print instead of println so the user can type right after the ':'
			System.out.print("Enter integers (0 to finish): ");

			// Make sure only integers are entered
			try {
				temp = stdin.nextInt();

				stdin.nextLine(); // Use up extra inputs

				if (temp == 0) {
					break; // exit the loop if user inputs zero
				} else {
					// Otherwise store temp in list
					list.add(temp);
					System.out.println("-> You entered " + temp); // Give confirmation
				}
			} catch (Exception e) {
				// Value was not an integer!
				stdin.nextLine(); // Get the incorrect value anyways
				System.out.println("Please enter an integer!"); // Print an error message
			}
		} // End input
		stdin.close(); // Make sure we close Scanner.

		// Make an array to pass the integers of the user to method odds
		int[] pass = new int[list.size()];

		// Transfer ints from ArrayList to array
		for (int i = 0; i < list.size(); i++) {
			pass[i] = list.get(i);
		}

		/* Get returned values from odds function and print them */
		int[] ret = odds(pass);
		System.out.println("\nThe odd integers returned from the 'odds' method are: "
			+ Arrays.toString(ret));
	} // End method main(String[] args)
} // End class MethodEx3
