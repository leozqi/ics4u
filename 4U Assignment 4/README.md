## Pong

Name: Leo Qi
Date: Nov. 23, 2021.

The `pong` package creates a game of Pong in Java for 4U Assignment #4.

Features:

* Paddles can be controlled the W/S keys for the left paddle, and Up arrow/Down arrow keys for the right paddle.
* A paddle can also be a "CPU player". The "Simple CPU" only follows the ball but the "Smart CPU" tries to calculate where the ball will end up.
* A start menu is available to pick what type each paddle will be.
* Matches are up to 10 rounds.

Details:

* All source files are located in the `pong` directory.
* The main class and entry point of the game is the Main class (Main.java)
* The Constants class holds common constants used by most of the other classes. They include the height and width of the game field, initial speed of a Ball, and size of the ball/paddle.
* The Game class displays the actual game and a start menu built with Swing components. It uses a thread to continuously update the game like the sample code shown in the Pong Assignment file, but with some modifications.
* The Ball class and the Paddle class define both the ball and the paddle and have methods to make them move used by the Game class.
* The CalcObj class provides a way to pass a CPU Paddle angle information of the ball so that the Paddle can calculate where it should go. 
* The HorizontalD, VerticalD, and Mode enums allow the other classes to specify horizontal directions, vertical directions, and the mode of a paddle without using strings.

## Running the code

To run the program without creating a JAR file, I started in the root directory of the project and compiled the source files into a build directory with [javac](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javac.html):

```bash
mkdir build
javac -d ./build/ ./pong/*.java
```

Then I could run the program from the entry class:

```bash
cd build
java pong.Main
```

Changing the name of the Main class should not affect anything as long as it
remains in the same package:

```bash
java pong.NewName
```

## The manifest.mf file

I found that without a manifest file, my JAR file would not know which initial file to execute. I followed the instructions on [the Java docs](https://docs.oracle.com/javase/tutorial/deployment/jar/appman.html) to write a manifest file with the single key `Main-class` of value `pong.Main`.

Including this manifest allows the JAR file to execute my Main class when it is first started.

## Making a JAR

Go to root directory and make a build subdirectory:

```bash
mkdir build
```

Compile all source files into build:

```bash
javac -d ./build/ ./pong/*.java
```

Create a `.jar` with a manifest: I used [this section of the Java docs](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html) to learn to use the `jar` command.

```bash
cd build
jar cfvm Pong.jar ../manifest.mf ./pong/
```

Run:

```bash
java -jar Pong.jar
```
