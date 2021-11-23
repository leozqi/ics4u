## Pong

Name: Leo Qi
Date: Nov. 23, 2021.

The `pong` package creates a game of Pong in Java for 4U Assignment #4.

Features:

* Paddles can be controlled the W/S keys for the left paddle, and Up arrow/Down arrow keys for the right paddle.
* A paddle can also be a "CPU player". The "Simple CPU" only follows the ball but the "Smart CPU" tries to calculate where the ball will end up.
* A start menu is available to pick what type each paddle will be.
* Matches are up to 10 rounds.
* The movement animations are smooth, but there may be slight lag on keyboard input due to the difference in time being different for each frame and/or a lot of CPU usage due to the continuous updating and drawing of the game elements: Ball, Paddle, etc.

Details:

* All source files are located in the `pong` directory.
* The main class and entry point of the game is the Main class (Main.java)
* The Constants class holds common constants used by most of the other classes. They include the height and width of the game field, initial speed of a Ball, and size of the ball/paddle.
* The Game class displays the actual game and a start menu built with Swing components. It uses a thread to continuously update the game as shown in the Pong Assignment description and copied in this file, with some adjustments.
* The Ball class and the Paddle class define both the ball and the paddle and have methods to make them move used by the Game class.
* The CalcObj class provides a way to pass a CPU Paddle angle information of the ball so that the Paddle can calculate where it should go. 
* The HorizontalD, VerticalD, and Mode enums allow the other classes to specify horizontal directions, vertical directions, and the mode of a paddle without using strings.

## Assignment information

*Copied from Pong Assignment:*

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

## Running the code

To run the program without creating a JAR file, I started in the root directory of the project and compiled the source files into a build directory:

```bash
mkdir build
javac -d ./build/ ./pong/*.java
```

Then I could run the program from the entry class:

```bash
cd build
java pong.Main
```

## The manifest.mf file

I found that without a manifest file, my JAR file would not know which initial file to execute. I followed the instructions on [the Java docs](https://docs.oracle.com/javase/tutorial/deployment/jar/appman.html) to write a manifest file with the single key `Main-class` of value `pong.Main`.

By including this manifest into my JAR file, my JAR file was able to start my program at launch.

## Making a JAR

Go to root directory and make a build subdirectory:

```bash
mkdir build
```

Compile all source files into build:

```bash
javac -d ./build/ ./pong/*.java
```

Create a `.jar` with a manifest: I used [this section of the Java docs](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html)

```bash
cd build
jar cfvm Pong.jar ../manifest.mf ./pong/
```

Run:

```bash
java -jar Pong.jar
```
