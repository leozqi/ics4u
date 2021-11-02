import javax.swing.*;

class Frame extends JFrame {
	public Frame() {
		Game g = new Game();
		this.add(g);
		this.setTitle("Pong");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}

public class Main {
	public static void main(String[] args) {
		Frame f = new Frame();
	}
}
