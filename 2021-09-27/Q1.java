/*
 * Q1.java
 *
 * By Leo Qi: 2021-09-27
 *
 * Write a Java program to compare two strings lexicographically, ignoring case
 * differences and states whether the strings are the same, one is less than the
 * other or one is more than the other.
 */

import java.util.*;

public class Q1 {
	/* Description */
	static final String CLASS_NAME = "Q1";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-09-27";
	static final String DESCRIPTION = "Compares length of strings ignoring case";
	static final String INSTRUCTIONS = """
		1. Enter a first string.
		2. Enter a second string.
		3. The program will compare the strings and print out a statement.
		""";

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

	/*
	 * Method main:
	 *
	 * Takes no arguments as parameters, returns void.
	 *
	 */
	public static void main(String[] args) {
		introduction();

		Scanner stdin = new Scanner(System.in);

		System.out.println("Enter a first string:");
		String input1 = stdin.nextLine();

		System.out.println("Enter a second string:");
		String input2 = stdin.nextLine();

		int comp = input1.compareToIgnoreCase(input2);

		if (comp > 0) {
			System.out.println("First string greater than second string.");
		} else if (comp < 0) {
			System.out.println("First string lesser than second string.");
		} else {
			System.out.println("First string equal to second string.");
		}

		stdin.close();
	}
}
