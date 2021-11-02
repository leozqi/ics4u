import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class MouseLabel extends JFrame implements MouseListener, MouseMotionListener {
	private JLabel label;

	public MouseLabel() {
		super();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);

		label = new JLabel("Click!");

		label.setBounds(0, 0, 100, 100);
		label.setBackground(Color.red);
		label.setOpaque(true);
		label.addMouseListener(this);

		this.add(label);

		this.setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.label.setText("Pressing mouse!");
		e.consume(); // the event has been processed; frees memory
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.label.setText("Not pressing mouse!");
		e.consume();
	}


	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}


}
