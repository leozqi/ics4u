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
		EMPTY,
		PASSABLE,
		ITEMBOX,
		NOT_SQUARE,
		CLIMBABLE,
		DEADLY

} /* End enum Attribute */
