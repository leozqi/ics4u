import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

class Main {
//	public static void main(String[] args) {
//		MouseLabel m = new MouseLabel();
//	}

public static void main(String[] args) {
	movement m = new movement();
	JFrame jf = new JFrame();
	jf.setTitle("Tutorial on Keys");
	jf.setSize(500,500);
	jf.setVisible(true);
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.add(m);
}
}
