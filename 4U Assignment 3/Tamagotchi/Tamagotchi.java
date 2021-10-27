/*
 * Tamagotchi.java
 *
 * By Leo Qi: 2021-10-27
 *
 * This program provides the "Tamagotchi" public class to simulate a simple
 * Tamagotchi pet.
 */

/**
 * Simulates a Tamagotchi pet with 4 methods representing different actions to
 * take to the Tamagotchi.
 *
 * Has four fields: hunger, happiness, discipline, and age. Each field contains
 * a value from 0-100 inclusive, except for age which has no boundaries.
 *
 * hunger and discipline cannot go too low (<=0) or too high (>=100) or else a
 * special status message is shown and no further changes to the Tamagotchi's
 * fields can occur.
 */
public class Tamagotchi {
	private int hunger;     // Stores hunger (0-100): 0 is full, 100 is dead.
	private int happiness;  // Stores happiness (0-100): 100 is happiest
	private int discipline; // Stores discipline (0-100): 100 is most disciplined.
	private int age;        // Stores time passed in "Tamagotchi minutes"

	private String name;    // Stores name of the Tamagotchi


	/**
	 * Private method to ensure that value is within the boundaries low-high
	 * inclusive; if it is not either return highest or lowest bound.
	 *
	 * @param value value to guarantee within bounds
	 * @param low lowest possible value
	 * @param high highest possible value
	 *
	 * @return
	 */
	private int bound(int value, int low, int high) {
		if (value > high) {
			return high;    // value is over bound so return highest
		} else if (value < low) {
			return low;     // value is lower bound so return lowest
		} else {
			return value;   // value is okay; is in between bound
		}
	} /* End private method bound */


	/**
	 * Create a new Tamagotchi: hunger, happiness, and discipline will be
	 * set at 50 while age is 0.
	 *
	 * @param name Name of the Tamagotchi
	 */
	public Tamagotchi(String name) {
		this.name = name;

		this.hunger = 50;
		this.happiness = 50;
		this.discipline = 50;
		this.age = 0;
	} /* End constructor */


	/**
	 * Passes time for the created Tamagotchi: age will increase by one
	 * Tamagotchi minute; hunger increases by one while happiness and
	 * discipline each decrease by one.
	 */
	public void age() {
		// If the Tamagotchi is not alive/has been taken away, do not modify values
		if (this.hunger >= 100 || this.discipline <= 0) { return; }

		// Increase age
		this.age += 1;

		// Change other fields without going over the range (0-100) inclusive
		this.happiness  = bound(this.happiness  -1, 0, 100);
		this.discipline = bound(this.discipline -1, 0, 100);
		this.hunger     = bound(this.hunger     +1, 0, 100);
	} /* End method age */


	/**
	 * Decreases hunger of the Tamagotchi by 10.
	 */
	public void feed() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.hunger = bound(this.hunger-10, 0, 100);
	} /* End method feed */


	/**
	 * Increases the happiness of the Tamagotchi by 10.
	 */
	public void play() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.happiness = bound(this.happiness+10, 0, 100);
	} /* End method play */


	/**
	 * Increases the discipline of the Tamagotchi by 10.
	 */
	public void scold() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.discipline = bound(this.discipline+10, 0, 100);
	} /* End method scold */


	/**
	 * Returns information and the status of the Tamagotchi.
	 * Five descriptors are used to describe the status of the Tamagotchi
	 * for each value of hunger, happiness, and discipline.
	 */
	@Override
	public String toString() {
		// Returned message format is the following:

		// Name: <name>
		// Age: <age> Tamagotchi minute(s)
		// Status:
		// * <Message 1>
		// * <Message 2>
		// * - - -

		// "- - -" stands for no message (if the value is too close to
		// default of 50)

		/* Check if Tamagotchi is dead or carried away by Protective services */
		if (this.hunger >= 100) {
			// Dead; return message of pet being dead
			// DO NOT return status messages afterwards; only maxage
			return "Oh no! " + this.name + " is dead from hunger!\n"
				+ this.name + " lived to an age of "
				+ this.age + " Tamagotchi minutes.";
		} else if (this.discipline <= 0) {
			// Taken away by Protective Services
			// DO NOT return status messages afterwards; only max age
			return "Oh no! " + this.name + " has been taken away by"
				+ "the Tamagotchi protective services for being"
				+ "out of control!";
				+ this.name + " was yours for " + this.age
				+ " Tamagotchi minutes."
		}

		// First store basic name and age in a single string; add on to
		// it for other fields
		String ret = "Name: " + this.name
			+ "\nAge: " + age + " Tamagotchi minute(s)"
			+ "\nStatus:\n* ";

		// Add on a descriptor for hunger
		if (this.hunger > 85) {
			ret += this.name + " has fainted from lack of food!";
		} else if (this.hunger > 60) {
			ret += this.name + " stares at you hungrily . . .";
		} else if (this.hunger <= 60 && this.hunger >= 40) {
			ret += "- - -";
		} else if (this.hunger > 35) {
			ret += this.name + " looks a bit hungry.";
		} else if (this.hunger > 10) {
			ret += this.name + " looks active and alert.";
		} else {
			ret += "Burp . . . " + this.name + " looks full.";
		}

		// Add on a descriptor for happiness
		ret += "\n* ";
		if (this.happiness > 85) {
			ret += this.name + " is as happy as can be!";
		} else if (this.happiness > 60) {
			ret += this.name + " is feeling happy!";
		} else if (this.happiness <= 60 && this.happiness >= 40) {
			ret += "- - -";
		} else if (this.happiness > 35) {
			ret += this.name + " is feeling gloomy.";
		} else if (this.happiness > 10) {
			ret += this.name + " is feeling sad.";
		} else {
			ret += this.name + " looks deflated and depressed.";
		}
		ret += "\n* ";

		// Add on a descriptor for discipline
		if (this.discipline > 85) {
			ret += this.name + " can do tricks at your command!";
		} else if (this.discipline > 60) {
			ret += this.name + " looks at you obediently.";
		} else if (this.discipline <= 60 && this.discipline >= 40) {
			ret += "- - -";
		} else if (this.discipline > 35) {
			ret += this.name + " looks smug.";
		} else if (this.discipline > 10) {
			ret += this.name + " ignores you completely.";
		} else {
			ret += (this.name + " is in a destructive frenzy!");
		}

		// Add newline for display purposes and return
		ret += "\n";
		return ret;
	} /* End method toString */
} /* End class Tamagotchi */
