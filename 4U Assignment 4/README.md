## Pong

Pong in Java for Assignment 4U #4!

## Assignment information

Pong Assignment:

Your goal will be to recreate the game “pong” using java. You may add extra aesthetics if you would like, but most important is making sure the game runs and runs efficiently.

Here are the requirements:

1. Must be 2 player (each paddle is controlled by a person)
2. There must be a ball and a score at the top, with a midline down the middle for division
3. The score must be kept active all game and appear at the top of the screen. There does NOT need to be a win condition, two players may continue to play until they are bored.
4. At the start of the game, the ball picks a random direction to go (upleft up right downleft down right)
5. When the ball reaches one side, the player on the opposite side of the table scores (ie- ball hits left wall, player on right gets a point).
6. When a player scores, the score is increased accordingly, and the ball and paddles are reset to their original locations.

Here is some helpful code to help you with running this program, you do not have to use it if you do not want to but I find this useful in keeping it 60 ish fps. With comments to aid in understanding. You have enough knowledge to complete this project to 90%, the other 10 % must come from research and self-learning.

```java
public void run() {//creates a basic game loop so it keeps playing
	long lastTime = System.nanoTime();//time in nanoseconds at start
	double ticks = 60.0;//fps
	double nanos = 1000000000/ticks;//unit of time to keep the game running at 60 fps, this is not needed for the code to work
	double diff = 0;//so if you do not have it, that is
	while(true) {
		long now = System.nanoTime();//gets the current time in nanoseconds
		diff += (now-lastTime)/nanos;//finds the difference and then divides by nanoseconds to get time in seconds
		lastTime = now; //updates the time to now to calculate again

		if (diff >=1) { //as long as the time difference is greater than or equal to 1 the game will call these methods
			move();
			checkCollision();
			repaint();
			diff--;//and then decrease the difference by 1 to see if it ends the game. When the screen is closer and this updates one final time, the difference will be less than or equal to 1, thus closing the game
		}
	}
}
```

Helpful Hints:

I have a class for everything, ball, paddles, gameFrame , JPanel. Most of the methods for running the game (ie- run, newBall, move, checkcollision, are in the gamePanel class.) This class also extends “runnable” and allows me to use “Threads.” We will talk about what those are in another week, they may be of use to you.

Grading Criteria:

1. Do you have all necessary items to appear as a playable game of pong?(IE – ball, paddle, score,etc…) 14 marks
2. Is your code functional and concise? 14 marks
3. Do you have comments on all necessary materials? 2 marks

## Making a JAR

Go to root directory and make a build subdirectory:

```bash
mkdir build
```

Compile all source files (no need to change directory into `build`):

```bash
javac -d ./build/ ./pong/*.java
```

Create a `.jar`:

```bash
cd build
jar cvfm Pong.jar ../manifest.mf ./pong/
``` 

Run:

```bash
java -jar Pong.jar
```
