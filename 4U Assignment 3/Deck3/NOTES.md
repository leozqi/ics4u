 * Public methods:
 *     age() passes time for the created Tamagotchi: age will increase by one
 *     Tamagotchi minute, hunger will increase by 1, other fields will decrease by 1.
 *
 *     feed() decreases the hunger of the Tamagotchi by 10.
 *     play() increases the happiness of the Tamagotchi by 10.
 *     scold() increases the discipline of the Tamagotchi by 10.
 *
 *     toString() returns information about the Tamagotchi's status based on the
 *     values of each individual field.
 
 // For each of the 13 types (ace, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, jack, queen, king)
 
 	public static String toString(Deck deck) {
		return Arrays.toString(deck.shuffle());
	}
