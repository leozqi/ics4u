// ------------------------------------------------------------------------- //
// The Attribute enumeration represents properties of entities or tiles.     //
//                                                                           //
// Package:  platformer                                                      //
// Filename: Attribute.java                                                  //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

/**
 * Properties of entities or tiles that may be applied or given on creation.
 *
 * Entities and tiles may choose to interpret Attributes differently; they are
 * simply flexible markers that can be applied to signify specific properties.
 *
 * Each Attribute's interpretation is stated next to its definition below.
 */
public enum Attribute {

	NONE,

	/* Entity Attributes */
		DOUBLE_JUMP, // Apply double jump
		LIGHTING,    // Entity gives off light
		COIN_1,      // Give one coin
		COIN_10,     // Give ten coins
		COIN_100,    // Give one hundred coins
		HP_1,        // Give one health

	/* Tile Attributes */
		EMPTY,       // Tile represents empty space
		PASSABLE,    // Tile is passable (not blocking) for Entities
		ITEMBOX,     // Tile provides gems
		NOT_SQUARE,  // Tile is not square and not rectangular
		RECTANGULAR, // Tile is rectangular but not square
		CLIMBABLE,   // Tile is climbable by the player
		DEADLY       // Tile is deadly to the player

} /* End enum Attribute */
