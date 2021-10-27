/*
 * Deck.java
 *
 * 4U Assignment 3, Question 2
 *
 * By Leo Qi: 2021-10-27
 *
 * This program provides the "Deck" public class to simulate a standard deck of
 * cards, and two methods, shuffle and toString, for question 2.
 *
 * The methods that answer question 2 are the instance method shuffle() and the
 * STATIC toString method.
 *
 * Everything is implemented with Java arrays and the java.util.Arrays class.
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
 * Public methods this class provides:
 *
 *    * `String[] shuffle()`: shuffles all the cards in the instance randomly.
 *      Returns array with the cards in the new order; the instance will also store
 *      the cards in the new order.
 *
 *    * `String toString()`: Displays the entire deck in its current state, whether
 *      shuffled or not.
 *
 * Static methods this class provides:
 *
 *    * `static String toString(Deck d)`: same as toString but shuffles the deck.
 *      Takes a Deck in as input, shuffles it, and then displays its new shuffled
 *      order.
 */
public class Deck {
	private final String[] TYPES = { // Stores all the possible types of cards
		"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10",
		"Jack", "Queen", "King"
	};
	private final String[] SUITS = { // Stores all the possible suits of cards
		"Spades", "Hearts", "Diamonds", "Clubs"
	};

	private String[] deck; // Private field storing cards


	/**
	 * Initializes a deck of 52 standard cards. (See the standardDeck method)
	 */
	public Deck() {
		this.deck = this.standardDeck();
	} /* End constructor */


	/**
	 * Returns a String[] deck with the 52 standard cards. Suit order is from
	 * Spades to Hearts to Diamonds to Clubs; card order is Ace, then numbered
	 * cards, finally face cards.
	 */
	private String[] standardDeck() {
		String[] d = new String[52]; // Store 52 cards in a standard deck

		// Iterate over four suits starting with "Spades"
		// Index stores current position in d for iteration
		for (int suit = 0, index = 0; suit < 4; suit++) {
			// Iterate over 13 types starting with "Ace" for each suit
			for (int type = 0; type < 13; type++) {
				// Each "card" string will be made of a word from
				// TYPES, a word from SUITS, and "of" in the middle
				// Increment index by 1 after it is used to store next card in next place
				d[index++] = this.TYPES[type] + " of " + this.SUITS[suit];
			}
		}
		return d; // Return filled deck
	} /* End method standardDeck */


	/**
	 * Shuffles the internal deck with any amount of elements. Returns a copy
	 * of the final array, but this does not need to be stored: the instance
	 * of the final array, but this does not need to be stored: the instance
	 * deck will also be modified.
	 *
	 * The method works by:
	 * (1) taking a card at random from the internal string array this.deck
	 *     and adding it to a string array, newDeck.
	 * (2) removing that card from this.deck, whose length decreases by 1.
	 *
	 * By repeating these two steps a newDeck with random order can be created
	 * from the original deck.
	 */
	public String[] shuffle() {
		Random rand = new Random(); // initialize to create random ints

		String[] newDeck = new String[this.deck.length]; // array containing new order of cards

		int r; // Stores random position in old deck to replace
		int oldSize = this.deck.length; // Stores old size of this.deck

		// Iterate over this.deck
		for (int pos = 0; pos < oldSize; pos++) {
			 // Pick random card: range from 0 to (this.deck.length - 1)
			r = rand.nextInt(this.deck.length);

			// Add that card to newDeck while removing it from original deck
			newDeck[pos] = this.deck[r];
			this.removeCard(r);
		}

		// Set internal deck to new order; also return this array
		this.deck = newDeck;
		return newDeck;
	} /* End method shuffle */


	/**
	 * Used by the shuffle method.
	 *
	 * Removes a card at a specific position from the internal deck with arrays.
	 * The length of the internal deck will decrease by one and all elements
	 * after pos will shift to the left by one.
	 *
	 * @param pos position of the card to remove
	 */
	private void removeCard(int pos) {
		// Create a new deck with one element to be removed
		String[] newDeck = new String[this.deck.length - 1];

		// Iterate over newDeck
		// oldPos stores position in this.deck at each loop; newPos
		// does the same for newDeck.
		for (int oldPos = 0, newPos = 0; newPos < newDeck.length; oldPos++) {
			if (oldPos != pos) {
				// Transfer elements from the old deck to the new
				// deck IF they are not the element to remove (pos)
				newDeck[newPos++] = this.deck[oldPos];
			}
		}

		// Set deck to equal deck with card removed
		this.deck = newDeck;
	} /* End method removeCard */


	/**
	 * Returns the string representation of the current deck, in order from
	 * first card to last card, in its current state WITHOUT shuffling.
	 */
	@Override
	public String toString() { return Arrays.toString(this.deck); }


	/**
	 * Static version of toString() that takes in a Deck and returns its
	 * string representation AFTER shuffling it first.
	 */
	public static String toString(Deck d) {
		d.shuffle();
		return d.toString();
	}

} /* End class Deck */
