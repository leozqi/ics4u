/*
 * Mode.java
 *
 * 4U Assignment 4
 *
 * By Leo Qi: 2021-11-23
 *
 * This file provides the "Mode" public class as part of the "pong" package,
 * which recreates the game of Pong in Java.
 *
 * The "Mode" class represents the different modes possible for a Paddle object.
 */


package pong;


/**
 * Provides the modes possible for a Paddle.
 * 
 * A paddle's mode determines how it moves: whether it waits for keyboard input
 * from the player or by calculating its own direction. The Paddle class uses
 * these modes to define how it moves, and the Game class uses these modes to
 * determine how many RadioButtons it should create for the user to choose
 * from: one for each mode on both the left and right sides.
 *
 * In the Mode enum, each possible mode has a defined toString function.
 * Displaying the mode of a paddle can be done through getting the toString of
 * its mode.
 */
public enum Mode {
	PLAYER {
		public String toString() {
			return "Player";
		}
	},
	COMPUTER_SIMPLE {
		public String toString() {
			return "Simple CPU";
		}
	},
	COMPUTER_SMART {
		public String toString() {
			return "Smart CPU";
		}
	}
}
