/*
 * Challenge.java
 *
 * By Leo Qi: 2021-09-30
 *
 * Picks x winners from y people with ArrayList
 */

import java.util.*;
import java.lang.Math;

public class Challenge {
	/* Description */
	static final String CLASS_NAME = "Challenge";
	static final String AUTHOR = "Leo Qi";
	static final String DATE_CREATED = "2021-09-30";
	static final String DESCRIPTION = "Prize draw: Picks 'x' winners from 'y' people.";
	static final String INSTRUCTIONS = """
		1. Enter the amount people participating in the draw.
		2. Enter the amount of winners
		3. Enter the names of the people.""";

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
	 * Takes no arguments as parameters, returns void.
	 *
	 */
	public static void main(String[] args) {
		introduction();
	}
}
