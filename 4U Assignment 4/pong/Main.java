package pong;

import javax.swing.*;

import static pong.Constants.*;

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
		UIManager.put("Panel.background", C_BACK);
		UIManager.put("Panel.foreground", C_FORE);

		UIManager.put("Button.background", C_BACK);
		UIManager.put("Button.foreground", C_FORE);

		UIManager.put("RadioButton.background", C_BACK);
		UIManager.put("RadioButton.foreground", C_FORE);

		UIManager.put("Label.background", C_BACK);
		UIManager.put("Label.foreground", C_FORE);

		Frame f = new Frame();
	}
}
