Event: a segment of code that happens at a certain time (asynchronously)

```java
import java.awt.event.*;
```

When you want an event to happen, you must implement a Listener. a Listener *listens* for events to happen: they capture events.

For example: the `MouseListener` and `MouseMotionListener` listen for mouse events.

When implementing a listener, we must implement all of its methods (with `@Override`). These methods can be blank, but must be there in your extended class. For example:

```java
@Override
public void mousePressed(MouseEvent e) {
    System.out.println("You pressed the mouse button!");
    e.consume(); // the event has been processed; frees memory
}

@Override
public void mouseDragged(MouseEvent e) {

}

@Override
public void mouseMoved(MouseEvent e) {

}
```

A **press** and a **release** make a ***click***.
