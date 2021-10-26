import java.lang.String;
import java.lang.Math;

class Main {
  public static void main(String[] args) {
    MyRobot robo = new MyRobot(50, 20, 90);

		robo.advance(50);
		System.out.println(robo);
		robo.turn(-600);
		robo.advance(50);
		System.out.println(robo);
  }
}

class MyRobot {
  double x;
  double y;
  double heading;

  public MyRobot() {
    // Make a robot with position (0, 0) heading 0deg
    this.x = 0.0;
    this.y = 0.0;
    this.heading = 0.0;
  }

  public MyRobot(double x, double y, double heading){
    this.x = x;
    this.y = y;
    this.heading = heading % 360;
  }

  public void turn(double angle) {
    // if angle is greater than 360, then it rotates multiple times
    double actual = angle % 360;

    this.heading += actual;
    while (this.heading <=0 || this.heading >= 360){
      if (this.heading > 360) {
        // So if we go over to 500 or something,
        // It'll go back to zero and then add 500-360 = 270
        this.heading = 0 + (this.heading - 360);
      }
      else if (this.heading < 0 && this.heading >= -360){
        this.heading = 360 + (this.heading);
      }
      else if (this.heading < -360) {
        // -600 = 360 + (-600 + 360) = 120
        this.heading = 360 + (this.heading + 360);
      }
    }
  }

  public void advance(double distance){
    /*
              ^ 90 deg
    < 180 deg ROBO > 0/360 deg
              _ 270 deg
    */

//ohhhhh that sucks alright imma change up the code in another tab and see if anything changes
//yh imma see what we can change as well
    double rad_heading = Math.toRadians(this.heading);
  // aight didn't need those ifs after all
  // ours is correct now
    this.x += Math.cos(rad_heading) * distance;
    this.y += Math.sin(rad_heading) * distance;
  }

  public String toString() {
    return ("Coords: (" + Math.round(this.x) + ", " + Math.round(this.y) + "); Heading: [" + Math.round(this.heading) + "]");
  }
}
