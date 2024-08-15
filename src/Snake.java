import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends JPanel implements ActionListener, KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x,int y){
            this.x = x;
            this.y = y;
        }
    }


    int boardWidth;
    int boardHeight;
    int tileSize = 25;


    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;


    //Food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    Snake(int boardWidth,int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();

        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100,this);
        gameLoop.start();


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
//        //Grid
//        for (int i = 0; i < boardWidth/tileSize; i++) {
//            g.setColor(Color.LIGHT_GRAY);
//            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
//            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
//        }

        //Food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize,food.y*tileSize, tileSize, tileSize,true);

        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize,tileSize,tileSize,true);

        //Snake Body
        for (int i =0; i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize,true);
        }

        //Score
        if(gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(Color.red);
            int stringWidth =  metrics.stringWidth("Game Over : ");
            g.drawString("Game Over : " + String.valueOf(snakeBody.size()), (boardWidth/2)-(stringWidth/2) , boardHeight/4);
        }
        else{
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.drawString("Score : " + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        //Eat food
        if (collision(snakeHead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }

        //Snake body
        for(int i =snakeBody.size()-1; i >=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //SnakeHead
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Game Over Conditions
        //Collides with body
        for(int i =0; i< snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            //Collides with body
            if (collision(snakeHead,snakePart)){
                gameOver = true;
            }
        }
        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize>boardWidth ||
            snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    //Checks if a key has been pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && velocityY != -1){
            velocityY = 1;
            velocityX = 0;
        }
        else if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }
    //Not needed
    @Override
    public void keyTyped(KeyEvent e) {
    }
    //Not needed
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
