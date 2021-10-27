/*
 * CardDeck.java
 *
 * 4U Assignment 3, Question 3
 *
 * By Leo Qi: 2021-10-27
 *
 * This program provides the "CardDeck" public class to simulate a standard deck
 * of cards, and three methods, shuffle, Deal, and toString, for question 3.
 *
 * The methods that answer question 3 are the instance method shuffle(), the
 * STATIC toString method, and the Deal() instance method.
 *
 * Everything is implemented with Java arrays, ArrayList, and the java.util.Arrays
 * class.
 */

import java.util.*;

/**
 * Simulates a playing card deck with 52 cards in four suits.
 *
 * There are four cards of each type in four suits: spades, hearts, diamonds,
 * and clubs. The types are "Ace", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen and
 * King.
 *
 * When an object of this class is initialized, it will automatically have cards in
 * order from Ace to numbered cards to face cards, from suit Spades to Hearts to
 * Diamonds to Clubs.
 *
 * Public instance methods this class provides:
 *
 *    * `void shuffle()`: shuffles all the cards into a new random order.
 *
 *    * `String toString()`: Displays the entire deck in its current state, whether
 *      shuffled or not.
 *
 *    * `void Deal(int handSize (optional), boolean showSymbols (optional))`:
 *      Deals out handSize cards to two hands and prints them on the screen.
 *        * handSize: how many cards to deal to each of the two hands. 5 is default.
 *          If handSize is greater than can be dealt with the deck's cards, an error
 *          will be printed out. `false` is default.
 *        * showSymbols: If showSymbols is set to true, will show Unicode card symbols.
 *          when displaying each hand.
 *          This is not the default because some terminals do not support displaying
 *          these playing card characters, but can be called on a terminal that
 *          supports it like Replit's online Java terminal.
 *
 * Static methods this class provides:
 *
 *    * `static String toString(CardDeck d)`: same as toString but shuffles first.
 *      Takes a CardDeck instance as input, shuffles it, and then displays its
 *      new shuffled order.
 */
public class CardDeck {
	private final String[] TYPES = { // Stores all the possible types of cards
		"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10",
		"Jack", "Queen", "King"
	};
	private final String[] SUITS = { // Stores all the possible suits of cards
		"Spades", "Hearts", "Diamonds", "Clubs"
	};

	// Private Arraylist storing cards: position 1 is the "top of the deck"
	private ArrayList<String> deck;


	/**
	 * Initializes a deck of 52 standard cards. (See the standardDeck method)
	 */
	public CardDeck() {
		this.deck = this.standardDeck();
	} /* End constructor */


	/**
	 * Returns an ArrayList deck with the 52 standard cards. Suit order is from
	 * Spades to Hearts to Diamonds to Clubs; card order is Ace, then numbered
	 * cards, finally face cards.
	 */
	private ArrayList<String> standardDeck() {
		// Create new deck with ArrayList instead of Array like in Question 2
		ArrayList<String> d = new ArrayList<String>();

		// Iterate over four suits starting with "Spades"
		for (int suit = 0; suit < 4; suit++) {
			// Iterate over 13 types starting with "Ace" for each suit
			for (int type = 0; type < 13; type++) {
				// Each "card" string will be made of a word from
				// TYPES, a word from SUITS, and "of" in the middle
				// Increment index by 1 after it is used to store
				// next card in next place
				d.add(this.TYPES[type] + " of " + this.SUITS[suit]);
			}
		}
		return d; // Return filled deck
	} /* End method standardDeck */


	/**
	 * Shuffles the internal deck to a new random order.
	 *
	 * The method works by:
	 * (1) taking a card at random from the internal ArrayList this.deck
	 *     and adding it to a string ArrayList, newDeck.
	 * (2) removing that card from this.deck
	 *
	 * By repeating these two steps a newDeck with random order can be created
	 * from the original deck.
	 */
	public void shuffle() {
		Random rand = new Random(); // initialize to create random ints

		ArrayList<String> newDeck = new ArrayList<String>(); // new deck

		int r; // Stores random position in old deck to replace
		int oldSize = this.deck.size(); // Stores old size of this.deck

		// Iterate over this.deck
		for (int pos = 0; pos < oldSize; pos++) {
			 // Pick random card: range from 0 to (this.deck.length - 1)
			r = rand.nextInt(this.deck.size());

			// Add that card to newDeck while removing it from original deck
			newDeck.add(this.deck.get(r));
			this.deck.remove(r);
		}

		this.deck = newDeck; // Set internal deck to new order
	} /* End method shuffle */


	/**
	 * Returns the string representation of the current deck, in order from
	 * first card to last card, in its current state WITHOUT shuffling.
	 */
	@Override
	public String toString() {
		// Convert ArrayList to string
		return this.deck.toString();
	}


	/**
	 * Static version of toString() that takes in a Deck and returns its
	 * string representation AFTER shuffling it first.
	 */
	public static String toString(CardDeck d) {
		d.shuffle();
		return d.toString();
	}

	/**
	 * "Deals" the deck by printing out the hands of two players of size `n`.
	 * Cards are dealt from the first card in the internal deck to the last
	 * card if needed. Deal will NOT shuffle the cards.
	 *
	 * Specifying a hand size greater than possible will output a printed error.
	 *
	 * @param handSize size of each player's hand.
	 * @param showSymbols if showSymbols is true, prints out Unicode card
	 *        symbols as well as their names like (🂡) for Ace of Spades.
	 *        If the card symbol in the brackets above is not rendered, the
	 *        editor font may not support the playing card UTF-8 code block.
	 */
	public void Deal(int handSize, boolean showSymbols) {
		// Clear the terminal screen before printing anything
		clearTerminal();

		// Hand size for two players is greater than length; print error and exit
		if (handSize * 2 > this.deck.size()) {
			System.out.println("\n\nSpecified hand size is too large.\n\n");
			return;
		}

		// Create two arrays to hold each hand
		String[] hand1 = new String[handSize];
		String[] hand2 = new String[handSize];

		// Take turns giving each hand a card until both hands have size
		// handSize.
		for (int oldPos = 0, newPos = 0; newPos < handSize; newPos++) {
			// Both hands' newPos values are the same each loop.
			// oldPos is incremented after each hand gets a card from
			// this.deck.
			hand1[newPos] = this.deck.get(oldPos++);
			hand2[newPos] = this.deck.get(oldPos++);
		}

		// Print hands with a method separate from Deal function to
		// allow different ways of showing the cards if needed.

		// IF showSymbols is true, use the printHandsU method to display
		// Unicode card symbols along with card Strings; otherwise
		// just display the strings with printHands.
		if (showSymbols) {
			printHandsU(hand1, hand2, 200);
		} else {
			printHands(hand1, hand2, 200);
		}
	} /* End method Deal */


	/**
	 * Overload Deal for a default hand size of five for two players.
	 * Also turn off Unicode card symbols because support is not guaranteed
	 * on terminals/IDEs/consoles.
	 */
	public void Deal() { Deal(5, false); }


	/**
	 * Overload Deal for a variable hand size, but turning off Unicode card
	 * symbols by default.
	 */
	public void Deal(int handSize) { Deal(handSize, false); }


	/**
	 * Overload Deal to show symbols
	 */
	public void Deal(boolean showSymbols) { Deal(5, showSymbols); }


	/**
	 * Print the hands of two players as a table with a delay between each
	 * card, with Unicode card symbols.
	 *
	 * @param hand1 player 1's hand
	 * @param hand2 player 2's hand
	 * @param delay delay in milliseconds between each card printed
	 */
	private void printHandsU(String[] hand1, String[] hand2, int delay) {
		// Use formatted print to make pretty tables
		// %-23s : <-> left aligned, <23> minimum width of 23, <s> string.
		System.out.printf("\n\n|%-23s|%-22s|\n", "Player 1 hand", "Player 2 hand");

		// Print out a card for hand1, then for hand2, and repeat
		// Loop hand1.length times because only hand1 possibly has an extra card:
		// * Even card # means same hand # for both;
		// * Odd card # means hand1 has 1 extra card
		for (int i = 0; i < hand1.length; i++) {
			// 3 character wide Unicode card character display "%-3s"
			// 20 character wide card name display
			System.out.printf("|%-3s%-20s\t",
				this.getUCard(hand1[i]),
				hand1[i]
			);

			// Wait for `delay` ms (as an animation)
			this.wait(delay);

			// Make sure hand2 also has cards to print
			if (i < hand2.length) {
				System.out.printf("|%-3s%-20s",
					this.getUCard(hand2[i]),
					hand2[i]
				);
			} else {
				// Print a blank space for column 2
				// Because the column is blank, next column only
				// has 19 spaces
				System.out.printf("|%-3s%-19s", " ", " ");
			}
			System.out.println("|");

			this.wait(delay);
		}
		// Add two newlines for separation.
		System.out.print("\n\n");
	} /* End method printHandsU */


	/**
	 * Print the hands of two players as a table with a delay between each
	 * card, with NO Unicode card symbols.
	 *
	 * @param hand1 player 1's hand
	 * @param hand2 player 2's hand
	 * @param delay delay in milliseconds between each card printed
	 */
	private void printHands(String[] hand1, String[] hand2, int delay) {
		// Use formatted print to make pretty tables
		// %-23s : <-> left aligned, <23> minimum width of 23, <s> string.
		System.out.printf("\n\n|%-20s\t|%-20s|\n", "Player 1 hand", "Player 2 hand");

		// Print out a card for hand1, then for hand2, and repeat
		// Loop hand1.length times because only hand1 possibly has an extra card:
		// * Even card # means same hand # for both;
		// * Odd card # means hand1 has 1 extra card
		for (int i = 0; i < hand1.length; i++) {
			// 20 character wide card name display
			System.out.printf("|%-20s\t", hand1[i]);

			// Wait for `delay` ms (as an animation)
			this.wait(delay);

			// Make sure hand2 also has cards to print
			if (i < hand2.length) {
				System.out.printf("|%-20s", hand2[i]);
			} else {
				// Print a blank space for column 2
				System.out.printf("|%-19s", " ", " ");
			}
			System.out.println("|");

			this.wait(delay);
		}
		// Add two newlines for separation.
		System.out.print("\n\n");
	} /* End method printHands */


	/**
	 * Prints ANSI escape codes to return cursor to top and clear screen.
	 * Static as it doesn't use any fields.
	 */
	private static void clearTerminal() {
		// Gets the current OS (we need this for comparisons)
		String os = System.getProperty("os.name"); // Gets current OS

		// This must only be done in terminals that support it which are
		// not on by default on Windows
		// If on Windows, just exit the function as clearing the terminal
		// is optional
		if (!os.contains("Windows")) {
			// "\033[" starts all ANSI escape code sequences
			// ANSI code reference for the below at URL:
			// https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_(Control_Sequence_Introducer)_sequences
			System.out.print(
				"\033[H"        // "H" moves cursor to top corner by default
				+ "\033[2J"     // "2J" clears the entire screen
			);

			System.out.flush();     // Make changes happen right away
		}
	} /* End method clearTerminal */


	/**
	 * Use Java's Thread.sleep() method to wait for `ms` seconds.
	 *
	 * All this method adds is a way to catch any "InterruptedException"s
	 * by doing nothing if such an exception occurs (which will stop the
	 * waiting immediately).
	 *
	 * It also checks to make sure ms is greater than zero; otherwise it exits.
	 */
	private void wait(int ms) {
		if (ms <= 0) {
			// Exit: less than or equal to 0 means no waiting
			return;
		}
		try {
			// wait for ms milliseconds by "pausing" or "sleeping"
			// https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#sleep-long-
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// just exit method if interrupted
			return;
		}
	} /* End method wait */


	/**
	 * Get unicode character representing a playing card of form
	 * "<type> of <suit>" where type is in this.TYPES and suit is in
	 * this.SUITS. If passed String is not of form <type> of <suit>
	 * Ace of Spades is the default.
	 *
	 * @param card a playing card string of form "<type> of <suit>"
	 * @return string with single unicode playing card (codePoint)
	 */
	private String getUCard(String card) {
		// Split the string representing a card into three components:
		// <type>, "of", and <suit>
		// through cutting them off at the spaces.
		String[] construct = card.split(" ");

		// Card symbols reference in hexadecimal for Unicode:
		// https://en.wikipedia.org/wiki/Playing_cards_in_Unicode#Playing_Cards_(block)
		// Set code to first card codepoint (Ace of Spades)
		int code = 0x1F0A1;

		// Iterate over each suit to find one that matches the card
		for (int i = 0; i < this.SUITS.length; i++) {
			// construct[2] gives suit for string of order <type> of <suit>
			if (construct[2].equals(this.SUITS[i])) {
				// The suits in this.SUITS are in order with their
				// Unicode codepoints. Each suit increases by 0x10

				// Add 0x10 * amount of suits passed
				code += (i * 0x10);
				break; // Found correct suit so finish loop
			}
		} // If no match is found we remain at default regardless

		// Iterate over each type to find one that matches the card
		for (int i = 0; i < this.TYPES.length; i++) {
			// construct[0] gives type for string of order <type> of <suit>
			if (construct[0].equals(this.TYPES[i])) {
				// The types in this.TYPES are in order with their
				// Unicode codepoints, EXCEPT Unicode also has the
				// "knight". Each type increases by 0x1 but don't
				// include the knight (at position 11 from ace)
				if (i > 10) {
					// Add 0x10 * amount of types passed
					// SKIP the "knight" (plus one)
					code += ( (i + 1) * 0x1);
				} else {
					// Add 0x10 * amount of types passed
					code += (i * 0x1);
				}
				break; // Found correct type so finish loop
			}
		}

		// Convert the Unicode code from an int to multiple characters,
		// (A Unicode codepoint can be multiple chars)
		// then to a String to use easily.
		return String.valueOf(Character.toChars(code));
	} /* End method getUCard */
} /* End class CardDeck */
