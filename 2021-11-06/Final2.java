import javax.swing.*;
 
public class Final2 {
 
    public Final2(){
        JFrame frame = new JFrame();
        frame.add(new board2());
        frame.setTitle("I wanna Be Mr. S");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void main(String[] args){
        new Final2();
    }
}