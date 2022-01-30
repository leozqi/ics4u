// ------------------------------------------------------------------------- //
// The EntityType enumeration represents the different entities possible.    //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: EntityType.java                                                 //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

public enum EntityType {
	NONE(-1),   // No type
	FLAG(1),    // Endgoal flag
	PLAYER(-1), // Player
	SLIME(0),   // Slime (moving ground enemy)
	FLY(4),     // Fly (flying enemy)

	B_COIN(2, 0, Attribute.COIN_1),   // Bronze coin
	S_COIN(2, 1, Attribute.COIN_10),  // Silver coin
	G_COIN(2, 2, Attribute.COIN_100), // Gold coin
	HEALTH(2, 5, Attribute.HP_1),     // Health

	TORCH(3, Attribute.LIGHTING); // Torch

	public final int costume;    // index of SpriteHandler to draw Entity
	public final int spriteNum;  // index of tile within SpriteHandler (optional)
	public final Attribute attr; // One starting attribute of the Entity

	/**
	 * Create an EntityType.
	 *
	 * The EntityType stores Entity data to be used during an Entity's
	 * creation.
	 *
	 * The `spriteNum` field holds the Entity's spriteNum if it shares a
	 * SpriteHandler with other Entities, providing finer control. It is
	 * not used by default.
	 *
	 * @param costume the index of the SpriteHandler that the Entity should
	 *                use to draw itself, from an array of SpriteHandlers
	 *                created by the Game class on game start. -1 should be
	 *                used if no index need be specified.
	 * @param spriteNum index of tile within SpriteHandler of Entity if it
	 *                  shares with other Entities, providing finer control
	 *                  over selection.
	 * @param attr one attribute the Entity should have on start.
	 */
	private EntityType(int costume, int spriteNum, Attribute attr) {
		this.costume = costume; // Set fields
		this.spriteNum = spriteNum;
		this.attr = attr;
	} /* End constructor */


	/**
	 * Create an EntityType.
	 */
	private EntityType(int costume) {
		this.costume = costume;
		this.spriteNum = 0;
		this.attr = Attribute.NONE;
	} /* End constructor */


	/**
	 * Create an EntityType.
	 */
	private EntityType(int costume, Attribute attr) {
		this.costume = costume;
		this.spriteNum = 0;
		this.attr = attr;
	} /* End constructor */

} /* End class EntityType */
