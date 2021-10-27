`Polygon` allows us to make a shape with an arbitary amount of (x, y) pairs connected and filled in together.

A Polygon takes in three parameters: `new Polygon(x, y, n)`

x: array of x coordinates
y: array of y coordinates (should match with x coordinates)
n: integer for how many coordinates should be in x and y (size of x & y)

Path2D:

Path2D.Double p = new Path2D.Double();

p.moveTo(x, y) -> starting coords
p.lineTo(x, y) -> draw a line to x, y
p.closePath() -> closes the path by drawing a line back to the start
Graphics2D g2d = (Graphics2D) g;

g2d.draw(p) or g2d.fill(p) to show p

```java
Path2D.Double curve = new Path2D.Double(); // draw a curve

curve.moveTo(250, 400);
curve.curveTo(350, 300, 500, 300, 600,400); // Bezier curve points

// the above coordinates represent two bezier handles.

g2d.draw(curve);
```

the g2d.translate(x, y) command moves the origin to the old x,y coordinates

`AffineTransform reset = g2d.getTransform();` can reset the origin with:

`g2d.setTransform(reset);

Math.toRadians(deg) converts degrees to radians

g2d.rotate(radians, x, y) rotates the canvas: you must reset the canvas afterwards
with g2d.setTransform(reset)
