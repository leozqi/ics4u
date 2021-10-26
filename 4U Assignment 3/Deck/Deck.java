import java.util.*;

public class Deck {
	private String[] TYPES = {
		"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10",
		"Jack", "Queen", "King"
	};
	private String[] SUITS = {
		"Hearts", "Diamonds", "Clubs", "Spades"
	};
	private String[] deck;


	/**
	 * Initializes a deck of 52 standard cards. (See the standardDeck method)
	 */
	public Deck() {
		this.deck = this.standardDeck();
	} /* End constructor */


	/**
	 * Returns a String[] deck with the 52 standard cards. Suit order is from
	 * Hearts to Diamonds to Clubs to Spades; card order is Ace, then numbered
	 * cards, finally face cards.
	 */
	private String[] standardDeck() {
		String[] d = new String[52];

		int index = 0;
		for (int suit = 0; suit < 4; suit++) {
			for (int type = 0; type < 13; type++) {
				d[index] = this.TYPES[type] + " of " + this.SUITS[suit];
				index++;
			}
		}
		return d;
	} /* End method standardDeck */


	/**
	 * Shuffles the internal deck with any amount of elements. Returns a copy
	 * of the final array, but this does not need to be stored: the internal
	 * deck will also become this new value.
	 */
	public String[] shuffle() {
		Random rand = new Random();

		ArrayList<String> oldDeck = new ArrayList<String>(Arrays.asList(this.deck));
		String[] newDeck = new String[this.deck.length];

		int r;
		for (int i = 0; i < this.deck.length; i++) {
			r = rand.nextInt(oldDeck.size()); // 0 to deck.length-1
			newDeck[i] = oldDeck.get(r);
			oldDeck.remove(r);
		}

		this.deck = newDeck;
		return newDeck;
	}


	/**
	 * Returns the string representation of the current deck, in order from
	 * first card to last card, with Arrays.toString();
	 */
	@Override
	public String toString() { return Arrays.toString(this.deck); }


	/**
	 * Static version of toString() that takes in a Deck and returns its
	 * string representation.
	 */
	public static String toString(Deck deck) { return deck.toString(); };


	/**
	 * "Deals" the deck by printing out the hands of two players of size `n`.
	 * Cards are dealt from the first card in the internal deck to the last
	 * card if needed.
	 *
	 * Specifying a hand size greater than possible will output a printed error.
	 */
	public void Deal(int handSize) {
		if (handSize * 2 > this.deck.length) {
			System.out.println("Specified hand size is too large.");
			return;
		}

		String[] hand1 = new String[handSize];
		String[] hand2 = new String[handSize];

		int index = 0;
		for (int i = 0; i < handSize; i++) {
			hand1[i] = this.deck[index];
			index++;
			hand2[i] = this.deck[index];
			index++;
		}

		printDecks(hand1, hand2, 100);
	} /* End method Deal */


	/**
	 * Overload Deal for a default hand size of five.
	 */
	public void Deal() { Deal(5); }


	private void printDecks(String[] hand1, String[] hand2, int delay) {
		System.out.printf("|%-20s%-3s|%-20s%-3s|\n", "Player 1 hand", "", "Player 2 hand", "");

		for (int i = 0; i < hand1.length; i++) {
			System.out.printf("|%-20s%-4s", hand1[i], getUCard(hand1[i]));

			this.wait(delay);

			if (i < hand2.length) {
				System.out.printf("|%-20s%-4s", hand2[i], getUCard(hand2[i]));
			}
			System.out.println("|");

			this.wait(delay);
		}
	} /* End method printDecks */


	private void wait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	} /* End method wait */


	private String getUCard(String card) {
		String[] construct = card.split(" ");
		int code = 0x1F0A1;

		for (int i = 0; i < this.SUITS.length; i++) {
			if (construct[2].equals(this.SUITS[i])) {
				code += (i * 0x10);
				break;
			}
		}

		for (int i = 0; i < this.TYPES.length; i++) {
			if (construct[0].equals(this.TYPES[i])) {
				code += (i * 0x1);
				break;
			}
		}

		return String.valueOf(Character.toChars(code));
	} /* End method getUCard */
}
