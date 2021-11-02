import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class Label implements MouseListener, MouseMotionListener{
	public static void main(String[] args) {
		ImageIcon iI = new ImageIcon(new ImageIcon("src/photo.jpeg").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
				//new ImageIcon

		Border border = BorderFactory.createLineBorder(Color.green,3);

		JLabel label = new JLabel();//create the label
		label.setText("Hello everyone and how are you today?");//adds text

		label.setIcon(iI);

		label.setHorizontalTextPosition(JLabel.CENTER);//puts text in the center
		label.setVerticalTextPosition(JLabel.TOP);//puts it at the top
		label.setForeground(Color.blue);//set font color of text
		label.setFont(new Font("MV Boli",Font.PLAIN,20));//set font of text

		label.setBackground(Color.black);//set background
		label.setOpaque(true); //displays the background color
		label.setBorder(border);//displays the border

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.add(label);
		frame.pack();

		// If you want a graphics element to interact with your listener,
		// you must "add" the listener:
		label.addMouseListener(this);
	}


}
