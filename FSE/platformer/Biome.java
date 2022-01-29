// ------------------------------------------------------------------------- //
// The Biome enumeration represents different themes of a level              //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Biome.java                                                      //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

public enum Biome {
	GRASSY(1), // Grass theme
	DIRTY(2),
	ROCKY(3),
	SANDY(4),
	SNOWY(5);

	public final int row;

	/**
	 * Represents a "biome" or theme of a level.
	 *
	 * The "row" of blocks of the theme in the spritesheet is stored in the
	 * `row` field.
	 */
	private Biome(int row) {
		this.row = row;
	} /* End constructor */

} /* End class Biome */

