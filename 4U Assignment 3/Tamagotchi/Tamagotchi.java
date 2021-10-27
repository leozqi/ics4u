/*
 * Tamagotchi.java
 *
 * 4U Assignment 3, Question 1
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
 * hunger cannot go too high (>=100) and discipline too low (<=0) or else a
 * special status message is shown on toString() and no further changes to the
 * Tamagotchi's fields can occur: if the Tamagotchi is dead or taken away the
 * player can no longer influence its fields.
 *
 * Public instance methods this class provides:
 *
 *    * `void age()`: ages the Tamagotchi by one Tamagotchi minute.
 *       decreases happiness and discipline by 1; increases hunger by 1.
 *
 *    * `void feed()`: decreases Tamagotchi hunger by 10 within range (0-100)
 *    * `void play()`: increases Tamagotchi happiness by 10 within range (0-100)
 *    * `void scold`: increases Tamagotchi discipline by 10 within range (0-100)
 *
 *    * `String toString()`: Displays the status of the Tamagotchi.
 */
public class Tamagotchi {
	private int hunger;     // Stores hunger (0-100): 0 is full, 100 is dead.
	private int happiness;  // Stores happiness (0-100): 100 is happiest
	private int discipline; // Stores discipline (0-100): 100 is most disciplined.

	// Use long to store larger whole number age values than integer
	private long age;       // Stores time passed in "Tamagotchi minutes"

	private String name;    // Stores name of the Tamagotchi


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
	 * Private method to ensure that value is within the boundaries low-high
	 * inclusive; if it is not either return highest or lowest bound.
	 *
	 * @param value value to guarantee within bounds
	 * @param low lowest possible value
	 * @param high highest possible value
	 *
	 * @return value if within bounds or either lowest/highest bound
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
	 * Passes time for the created Tamagotchi: age will increase by one
	 * Tamagotchi minute; hunger increases by one while happiness and
	 * discipline each decrease by one.
	 */
	public void age() {
		// If the Tamagotchi is not alive/has been taken away, do not modify values
		if (this.hunger >= 100 || this.discipline <= 0) { return; }

		// Increase age
		this.age += 1;

		// Change other fields without going out of the range (0-100) inclusive
		this.happiness  = bound(this.happiness  -1, 0, 100);
		this.discipline = bound(this.discipline -1, 0, 100);
		this.hunger     = bound(this.hunger     +1, 0, 100);
	} /* End method age */


	/**
	 * Decreases hunger of the Tamagotchi by 10 unless Tamagotchi is dead
	 * or taken away by protective services, because in those cases the
	 * Tamagotchi can no longer be fed by the owner.
	 */
	public void feed() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.hunger = bound(this.hunger-10, 0, 100);
	} /* End method feed */


	/**
	 * Increases the happiness of the Tamagotchi by 10 unless Tamagotchi is
	 * dead or taken away by protective services, because in those cases
	 * the user can no longer play with the Tamagotchi.
	 */
	public void play() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.happiness = bound(this.happiness+10, 0, 100);
	} /* End method play */


	/**
	 * Increases the discipline of the Tamagotchi by 10 unless Tamagotchi is
	 * dead or taken away by protective services, because in those cases
	 * the user can no longer scold the Tamagotchi.
	 */
	public void scold() {
		if (this.hunger >= 100 || this.discipline <= 0) { return; }
		this.discipline = bound(this.discipline+10, 0, 100);
	} /* End method scold */


	/**
	 * Returns information and the status of the Tamagotchi.
	 * Five descriptors are used to describe the status of the Tamagotchi
	 * for each value of hunger, happiness, and discipline.
	 *
	 * If the Tamagotchi is dead from hunger or taken away by Protective
	 * Services due to poor discipline, do not show rest of stats (happiness
	 * etc as they are not applicable.
	 *
	 * @return status of the Tamagotchi
	 */
	@Override
	public String toString() {
		// Returned message format is the following:

		// Name: <name>
		// Age: <days> days <hours> hours <minutes> minutes old
		// Status:
		// * <Message 1>
		// * <Message 2>

		// "- - -" stands for no message. No message is displayed if the
		// value is 10 or less units away from 50

		String ret; // stores the returned string

		// If dead or taken away, give a special message:
		if ((this.hunger >= 100) || (this.discipline <= 0)) {
			ret = "Name: " + this.name + "\nStatus:\n* ";

			if (this.hunger >= 100) {
				// Dead; return formatted message of pet being dead
				// DO NOT return status messages afterwards, only age reached
				ret += String.format(
					"Oh no! %s is dead from hunger!\n* %s"
					+ " lived to an age of %s.\n",

					// Add name and formatted age
					this.name, this.name, formatAge(this.age)
				);
			} else if (this.discipline <= 0) {
				// Taken away by Protective Services
				ret += String.format(
					"Oh no! %s has been taken away by the"
					+ " Tamagotchi protective services for being"
					+ " out of control!\n* %s was yours for %s.\n",

					// Mention how long pet was players
					this.name, this.name, formatAge(this.age)
				);
			}
			return ret; // Return unique dead/taken away message
		}

		// Otherwise, give a normal status message of all conditions
		ret = String.format(
			"Name: %s\nAge:  %s\nStatus:\n* ",
			this.name,
			formatAge(this.age)
		);

		// Since all descriptors must be unique and conform to a unique
		// range, AND all our field values are in different integers,
		// an if-else ladder is the best way to set descriptors.

		// Add on a descriptor for hunger (choose from 5)
		if ((this.hunger > 85)) {
			ret += this.name + " is faint from lack of food!";
		} else if (this.hunger > 80) {
			ret += this.name + " is starving!";
		} else if (this.hunger > 60) {
			ret += this.name + " drools hungrily.";
		} else if (this.hunger <= 60 && this.hunger >= 40) {
			ret += "- - -";
		} else if (this.hunger > 10) {
			ret += this.name + " looks full.";
		} else {
			ret += "Burp . . . " + this.name + " looks bloated.";
		}

		// Add on a descriptor for happiness (choose from 5)
		ret += "\n* ";
		if (this.happiness > 85) {
			ret += this.name + " is as happy as can be!";
		} else if (this.happiness > 60) {
			ret += this.name + " is feeling happy!";
		} else if (this.happiness <= 60 && this.happiness >= 40) {
			ret += "- - -";
		} else if (this.happiness > 25) {
			ret += this.name + " is feeling gloomy.";
		} else if (this.happiness > 10) {
			ret += this.name + " is feeling sad.";
		} else {
			ret += this.name + " looks deflated and depressed.";
		}

		// Add on a descriptor for discipline (choose from 5)
		ret += "\n* ";
		if (this.discipline > 85) {
			ret += this.name + " can do tricks at your command!";
		} else if (this.discipline > 60) {
			ret += this.name + " looks at you obediently.";
		} else if (this.discipline <= 60 && this.discipline >= 40) {
			ret += "- - -";
		} else if (this.discipline > 25) {
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


	/**
	 * Method to format ages for display in toString.
	 * @param age the age of the Tamagotchi in minutes
	 * @return String with formatted day, hour, minute values.
	 */
	private String formatAge(long age) {
		// Hold days passed, hours passed, minutes passed.

		// Division without remainder for integers give integer # of days
		long d = age / 1440l; // <x number>l is a "long literal"

		// Dividing remainder of getting days gives hours
		long h = (age % 1440l) / 60l;

		// Remainder of getting hours gives minutes
		long m = (age % 1440l) % 60l;

		// Format everything nicely as a string
		return String.format("%d days %d hours %d minutes old", d, h, m);
	} /* End method formatAge */
} /* End class Tamagotchi */
