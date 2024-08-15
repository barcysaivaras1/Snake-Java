import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Snake snake = new Snake(boardWidth,boardHeight);
        frame.add(snake);
        frame.pack();//Ensures that when a panel is added it will not count the title bar as part of the size
        snake.requestFocus();
    }
}