/*
 * MethodEx2.java
 *
 * By Leo Qi: 2021-09-30
 *
 * The novowel method takes a String and returns the same string with all vowels
 * removed.
 */

import java.util.*; // Import java.util.* for Scanner

public class MethodEx2 {
	/* Description */
	// Put all of these details here for the introduction() method in this class
	// to print. This makes printing everything easier.
	static final String CLASS_NAME = "MethodEx2";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-09-30";
	static final String DESCRIPTION = "Takes a string and removes all of its vowels.";
	static final String INSTRUCTIONS = """
		1. Input a string at the prompt.
		2. The program will output your string without vowels.
		    * Vowels are assumed as the following letters: a, e, i, o, and u.""";

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
	 * Method novowel:
	 *
	 * Takes String s as string to remove vowels from.
	 * Returns String as string without vowels.
	 */
	static String novowel(String s) {
		// Stores the characters added together to form a string sans vowels
		String ret = "";

		// Stores a single character of the string during iteration
		char c = ' ';

		// Stores whether or not a space has been immediately before another space (See below)
		boolean spaceBefore = false;

		// Iterate throughout the whole string
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);    // c contains the character we are on currently

			// If it IS a vowel, don't add it into our new string (default adds to string)
			switch (c) {
				// Use single quotes because these are CHARS not STRS
				// Add both capital and lowercase letters
				case 'a': break;
				case 'A': break;
				case 'e': break;
				case 'E': break;
				case 'i': break;
				case 'I': break;
				case 'o': break;
				case 'O': break;
				case 'u': break;
				case 'U': break;
				case ' ':
					// If it is a space: do something special

					// Normally if you enter a single vowel as a word: 'a', 'I',
					// there will be two spaces together left in the string together
					// which looks weird.
					// To avoid this, if there has already been a space ignore
					// the second space.

					// Before adding this: INPUT <look a giraffe> OUTPUTS <lk  grff>
					// After adding this:  INPUT <look a giraffe> OUTPUTS <lk grff>
					if (!spaceBefore) {
						ret += ("" + c);
						spaceBefore = true;
					}
					break;
				default:
					// Add any other characters into the returned string
					spaceBefore = false;
					ret += ("" + c);
					break;
			} // End switch (c)
		} // End iteration of string
		return ret;
	} // End method novowel(String s)

	/*
	 * Method main:
	 *
	 * Takes no arguments as parameters, returns void.
	 *
	 * This method:
	 * 1. Takes in a string as input.
	 * 2. Removes the vowels and prints it out.
	 */
	public static void main(String[] args) {
		/* Print introduction */
		introduction();

		/* Get string as input */
		Scanner stdin = new Scanner(System.in);
		System.out.println("Enter a string below to remove its vowels:");

		/* Output string without vowels */
		// Include result of method directly in output
		System.out.println("Your string without vowels is: <"
			+ novowel(stdin.nextLine())
			+ ">");

		stdin.close(); // Make sure we close Scanner.
	} // End method main(String[] args)
} // End class MethodEx2
