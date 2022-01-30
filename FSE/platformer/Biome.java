// ------------------------------------------------------------------------- //
// The Biome enumeration represents different themes of a level              //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Biome.java                                                      //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

public enum Biome {
	GRASSY(1, 'g'), // Grass theme
	DIRTY(2, 'd'),  // Dirt theme
	ROCKY(3, 'r'),  // Underground rocky theme
	SANDY(4, 'a'),  // Desert theme
	SNOWY(5, 's');  // Snowy theme

	public final int  row;
	public final char rep;

	/**
	 * Represents a "biome" or theme of a level.
	 *
	 * The "row" of blocks of the theme in the spritesheet is stored in the
	 * `row` field. The character representation of the Biome in a save
	 * file is stored in `rep`.
	 */
	private Biome(int row, char rep) {
		this.row = row;
		this.rep = rep;
	} /* End constructor */

} /* End class Biome */
