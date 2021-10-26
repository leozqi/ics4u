Graphics:

extends -> extends a class to add new methods, etc. "When we want to use a class but are not allowed to modify it"

Today we will be extending JComponent. JComponent is a pre-made class that is used to draw things.

It is a protected class so we cannot instantiate it.

In this example: my class graphics is an extended class of JComponent.

* Superclass - class being extended FROM
* Subclass - class being extend

```java
class Graphics extends JComponent {
// Graphics is subclass, JComponent is superclass
}
```

Object : JFrame -> the Java Frame is the location of all of our graphical work.

## Imports

The imports needed are:

```java
import javax.swing.*; // drawing canvas
import java.awt.*; // colours
import java.awt.geom.*; // shapes
```
In java (0,0) is the top left of your screen, most shapes are defined on the top left coordinates, width, and height.

RGB colours: new Color(r, g, b);

Go in order: if you draw something first it will be painted first.


You may create new classes to define objects to draw composed of many shapes.

```java
public class Cloud {
    private double x;
    private double y;
    private double size;
    private Color color;

    public Cloud (double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void drawCloud(Graphics2D g2d) {
        Ellipse2D.Double e1 = new Ellipse2D.Double(x, y, size, size);
        Ellipse2D.Double e1 = new Ellipse2D.Double(x+size*0.35, y-size*0.2, size*1.75, size*1.4);
        Ellipse2D.Double e1 = new Ellipse2D.Double(x+size*1.5, y-size*0.15, size*0.9, size*0.9);
        Ellipse2D.Double e1 = new Ellipse2D.Double(x+size*1.8, y-size*0.05, size*0.3, size*0.3);

        g2d.setColor(color);
        g2d.fill(e1);
        g2d.fill(e2);
        g2d.fill(e3);
        g2d.fill(e4);
    }
}
```

Anti-alaising:

* rendering is how we show objects
* anti-alaising smooths out the edges of your shapes
* Is a byproduct of 2D graphics.

```java
RenderingHints rh = new RenderingHints(
    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
)

g2d.setRenderingHints(rh);
```

## Assignments

* Create scenery: house, two clouds, and sun.
