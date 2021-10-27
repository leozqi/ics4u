javax.swing.*;

Label: labels are very similar to frames, except they are mostly for text and images.

JLabel label = new JLabel();

We have to make sure that we add our label to our frame, we do this by using frame.add(label).

To add text we use: label.setText("");

Text is centered left-middle.

## Image icons

ImageIcon iI = new ImageIcon(new ImageIcon("src/photo.jpeg").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
