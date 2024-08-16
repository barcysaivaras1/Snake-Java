import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class Main{
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        //Title
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial",Font.BOLD,40));
        titleLabel.setText("Snake Game");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(600,100));
        titlePanel.setBackground(Color.green);
        titlePanel.add(titleLabel,BorderLayout.CENTER);

        //Main Panel
        JButton player1 = new JButton("1 Player");
        player1.setBackground(Color.green);
        player1.setFocusable(false);
        player1.setPreferredSize(new Dimension(200,100));



        JButton player2 = new JButton("2 Players");
        player2.setBackground(Color.CYAN);
        player2.setFocusable(false);
        player2.setPreferredSize(new Dimension(200,100));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50, 50));
        mainPanel.setPreferredSize(new Dimension(600,500));
        mainPanel.setBackground(Color.black);
        mainPanel.add(player1);
        mainPanel.add(player2);

        frame.add(titlePanel,BorderLayout.NORTH);
        frame.add(mainPanel);

        player1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Snake snake = new Snake(boardWidth,boardHeight,1);
                frame.remove(titlePanel);
                frame.remove(mainPanel);
                frame.add(snake);
                frame.pack();//Ensures that when a panel is added it will not count the title bar as part of the size
                snake.requestFocus();
            }
        });


        player2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Snake snake = new Snake(boardWidth,boardHeight,2);
                frame.remove(titlePanel);
                frame.remove(mainPanel);
                frame.add(snake);
                frame.pack();//Ensures that when a panel is added it will not count the title bar as part of the size
                snake.requestFocus();
            }
        });


        frame.setVisible(true);

    }
}