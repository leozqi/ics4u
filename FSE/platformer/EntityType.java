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
	FLAG(0),    // Endgoal flag
	PLAYER(5),  // Player
	SLIME(1),   // Slime (moving ground enemy)

	B_COIN(0, Attribute.COIN_1),
	S_COIN(1, Attribute.COIN_10),
	G_COIN(2, Attribute.COIN_100),
	HEALTH(5, Attribute.HP_1);

	public final int hp;
	public final int spriteNum;
	public final Attribute attr;

	/**
	 * Represents the type of an entity.
	 *
	 * The entity's starting HP level is stored in the `hp` field.
	 */
	private EntityType(int hp) {
		this.hp = hp;
		this.spriteNum = 0;
		this.attr = Attribute.NONE;
	} /* End constructor */


	private EntityType(int spriteNum, Attribute attr) {
		this.hp = 0;
		this.spriteNum = spriteNum;
		this.attr = attr;
	} /* End constructor */

} /* End class EntityType */
