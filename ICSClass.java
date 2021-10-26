/*
 * ICSClass.java
 *
 * By Leo Qi: 2021-09-18
 *
 * Describe the program here.
 */

import java.util.*;

public class ICSClass {
	/* Description */
	static final String CLASS_NAME = "ICSClass";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-09-17";
	static final String DESCRIPTION = "This is a template class.";
	static final String INSTRUCTIONS = """
		1. Do this
		2. Do this
		3. Okay, finish""";

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
	}

	static void printSymbol(String symbol, int amt, String end) {
		for (int i = 0; i < amt; i++){
			System.out.print(symbol);
		}
		System.out.print(end);
		System.out.flush();
	}

	static int lengthNoChars(String in) {
		return in.replaceAll("\\p{Punct}+", "").length();
	}

	/*
	 * Method main:
	 *
	 * Takes String[] as 'args' but does not use them, returns void.
	 *
	 * This method:
	 * 1. Asks the user via Scanner(System.in) to input a positive integer.
	 * 2. Makes sure that the input is positive and an integer before continuing.
	 * 3. Prints and calculates factorial.
	 */
	public static void main(String[] args) {
		introduction();
	}
}
