// ------------------------------------------------------------------------- //
// The EntityType enumeration represents enemy types possible.               //
//                                                                           //
// Package:  platformer                                                      //
// Filename: EntityType.java                                                 //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

public enum EntityType {
	NONE(0),    // No type
	PLAYER(5),  // Player
	SLIME(1);   // Slime (moving ground enemy)

	public final int hp;

	/**
	 * Represents the type of an entity.
	 *
	 * The entity's starting HP level is stored in the `hp` field.
	 */
	private EntityType(int hp) {
		this.hp = hp;
	} /* End constructor */

} /* End class EntityType */
