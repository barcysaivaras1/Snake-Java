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
    int num_players;


    //Player 1 Snake
    Tile snakeHead1;
    ArrayList<Tile> snakeBody1;

    //Player 2 Snake
    Tile snakeHead2;
    ArrayList<Tile> snakeBody2;


    //Food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    //Player1
    int velocityX1;
    int velocityY1;
    boolean hasMovedP1 = false;
    int inputBufferP1;  //Makes the game more responsive
    //Player2 OR Player AI
    int velocityX2;
    int velocityY2;
    boolean hasMovedP2 = false;
    int inputBufferP2;  //Makes the game more responsive

    boolean gameOver = false;

    Snake(int boardWidth,int boardHeight,int num_players){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.num_players = num_players;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        //Player 1
        snakeHead1 = new Tile(5,5);
        snakeBody1 = new ArrayList<Tile>();
        inputBufferP1 = -1;

        //Player 2
        if(num_players !=1) {
            snakeHead2 = new Tile(19, 19);
            snakeBody2 = new ArrayList<Tile>();

            velocityX2 = 0;
            velocityY2 = 0;
            inputBufferP2 = -1;
        }
        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX1 = 0;
        velocityY1 = 0;

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

        //Snake Head Player 1
        g.setColor(Color.green);
        g.fill3DRect(snakeHead1.x * tileSize, snakeHead1.y * tileSize,tileSize,tileSize,true);

        //Snake Head Player 2
        if (num_players!=1){
            g.setColor(Color.cyan);
            g.fill3DRect(snakeHead2.x * tileSize, snakeHead2.y * tileSize,tileSize,tileSize,true);
        }

        //Snake Body Player 1
        g.setColor(Color.green);
        for (int i =0; i<snakeBody1.size();i++){
            Tile snakePart = snakeBody1.get(i);
            g.fill3DRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize,true);
        }

        //Snake Body Player 2
        if(num_players != 1) {
            g.setColor(Color.cyan);
            for (int i = 0; i < snakeBody2.size(); i++) {
                Tile snakePart = snakeBody2.get(i);
                g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
            }
        }


        //Score
        if(gameOver) {
            //1 Player
            if (num_players == 1){
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                FontMetrics metrics = g.getFontMetrics();
                g.setColor(Color.red);
                int stringWidth =  metrics.stringWidth("Game Over : "+String.valueOf(snakeBody1.size()));
                g.drawString("Game Over : " + String.valueOf(snakeBody1.size()), (boardWidth/2)-(stringWidth/2) , boardHeight/4);
                }
            //2 Players
            else{
                g.setFont(new Font("Arial", Font.PLAIN, 80));

                //Game Over Text
                FontMetrics metrics = g.getFontMetrics();
                g.setColor(Color.red);
                int stringWidth =  metrics.stringWidth("Game Over");
                g.drawString("Game Over", (boardWidth/2)-(stringWidth/2) , boardHeight/4);

                g.setFont(new Font("Arial", Font.PLAIN, 40));
                metrics = g.getFontMetrics();
                //Player 1 Game Over Score
                stringWidth =  metrics.stringWidth("Player 1 : "+String.valueOf(snakeBody1.size()));
                g.setColor(Color.GREEN);
                g.drawString("Player 1 : " + String.valueOf(snakeBody1.size()), (boardWidth/2)-(stringWidth/2) , boardHeight/4 + 100);

                //Player 2 Game Over Score
                stringWidth =  metrics.stringWidth("Player 2 : "+snakeBody2.size());
                g.setColor(Color.CYAN);
                g.drawString("Player 2 : " + String.valueOf(snakeBody2.size()), (boardWidth/2)-(stringWidth/2) , boardHeight/4 + 200);
            }

            JButton restartButton = new JButton("Restart?");
            restartButton.setBounds(boardWidth/2 - 75,boardHeight/4 +250,150,75);
            restartButton.setBackground(Color.green);
            restartButton.setForeground(Color.black);
            restartButton.setFocusable(false);
            add(restartButton);
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
        }
        else{
            g.setColor(Color.green);
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.drawString("Score : " + String.valueOf(snakeBody1.size()),tileSize-16,tileSize);

            // 2 Player Mode
            if(num_players!=1){
                g.setColor(Color.CYAN);
                g.setFont(new Font("Arial",Font.BOLD,20));
                g.drawString("Score : " + String.valueOf(snakeBody2.size()),boardWidth-tileSize*4,tileSize);
            }
        }

    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(Tile snakeHead, ArrayList<Tile> snakeBody,int player){
        if(num_players == 0){
            aiMove();
        }
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
        if(player==1){
            snakeHead.x += velocityX1;
            snakeHead.y += velocityY1;
            hasMovedP1 = true;
//            if(inputBufferP1 != -1){
//                loadInputBuffer(inputBufferP1);
//            }
        }
        else{
            snakeHead.x += velocityX2;
            snakeHead.y += velocityY2;
            hasMovedP2 = true;
//            if(inputBufferP2 != -1){
//                loadInputBuffer(inputBufferP2);
//            }
        }

        //Game Over Conditions

        //Self Collision
        if (bodyCollision(snakeHead,snakeBody)){
            gameOver = true;
        }
        //Out of Bounds Collision
        if(isOutOfBounds(snakeHead)){
            gameOver = true;
        }

        //Player on Player Collisions
        if (playerOnPlayerCollision(snakeHead1,snakeBody1,snakeHead2,snakeBody2)){
            gameOver = true;
        }



    }

    public boolean playerOnPlayerCollision(Tile snakeHead1, ArrayList<Tile> snakeBody1, Tile snakeHead2, ArrayList<Tile> snakeBody2){
        if(num_players != 1){
            //Head To Head Collision
            if(snakeHead1.x == snakeHead2.x && snakeHead1.y == snakeHead2.y){
                return true;
            }

            //Player 1 Head collides with Player 2 Body
            if(bodyCollision(snakeHead1,snakeBody2)){
                return true;
            }

            //Player 2 Head collides with Player 1 Body
            if(bodyCollision(snakeHead2,snakeBody1)){
                return true;
            }
        }
        return false;
    }

    public boolean isOutOfBounds(Tile snakeHead){
        return  snakeHead.x*tileSize < 0 || snakeHead.x*tileSize>boardWidth ||
                snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight;
    }

    public boolean bodyCollision(Tile snakeHead, ArrayList<Tile> snakeBody){
        for(int i =0; i< snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            //Collides with body
            if (collision(snakeHead,snakePart)){
                return true;
            }
        }
        return false;
    }

    public void aiMove(){
        //Player vs AI

        //Possible directions
        boolean right = false;
        boolean left = false;
        boolean down = false;
        boolean up = false;

        if(hasMovedP2) {
            //Can we move left without collision
            if (!bodyCollision(new Tile(snakeHead2.x - 1, snakeHead2.y), snakeBody2) &&
                    !playerOnPlayerCollision(snakeHead1, snakeBody1, new Tile(snakeHead2.x - 1, snakeHead2.y), snakeBody2) &&
                    velocityX2 != 1 && !isOutOfBounds(new Tile(snakeHead2.x - 1, snakeHead2.y))) {
                left = true;
            }
            //Can we move right without collision
            if (!bodyCollision(new Tile(snakeHead2.x + 1, snakeHead2.y), snakeBody2) &&
                    !playerOnPlayerCollision(snakeHead1, snakeBody1, new Tile(snakeHead2.x + 1, snakeHead2.y), snakeBody2) &&
                    velocityX2 != -1 && !isOutOfBounds(new Tile(snakeHead2.x + 1, snakeHead2.y))) {
                right = true;
            }
            //Can we move up without collision
            if (!bodyCollision(new Tile(snakeHead2.x, snakeHead2.y - 1), snakeBody2) &&
                    !playerOnPlayerCollision(snakeHead1, snakeBody1, new Tile(snakeHead2.x, snakeHead2.y - 1), snakeBody2) &&
                    velocityY2 != 1 && !isOutOfBounds(new Tile(snakeHead2.x, snakeHead2.y - 1))) {
                up = true;
            }
            //Can we move down without collision
            if (!bodyCollision(new Tile(snakeHead2.x, snakeHead2.y + 1), snakeBody2) &&
                    !playerOnPlayerCollision(snakeHead1, snakeBody1, new Tile(snakeHead2.x, snakeHead2.y + 1), snakeBody2) &&
                    velocityY2 != -1 && !isOutOfBounds(new Tile(snakeHead2.x, snakeHead2.y + 1))) {
                down = true;
            }

            //Is there a SAFE move that we can make that gets us closer to the food
            if (food.x < snakeHead2.x && velocityX2 != 1 && left) {
                velocityY2 = 0;
                velocityX2 = -1;
            } else if (food.x > snakeHead2.x && velocityX2 != -1 && right) {
                velocityY2 = 0;
                velocityX2 = 1;

            } else if (food.y < snakeHead2.y && velocityY2 != 1 && up) {
                velocityX2 = 0;
                velocityY2 = -1;
            } else if (food.y > snakeHead2.y && velocityY2 != -1 && down) {
                velocityX2 = 0;
                velocityY2 = 1;
            }
            //No SAFE move that can get us closer to the food
            else {
                if (left && velocityX2 != 1) {
                    velocityY2 = 0;
                    velocityX2 = -1;
                } else if (right && velocityX2 != -1) {
                    velocityY2 = 0;
                    velocityX2 = 1;
                } else if (down && velocityY2 != -1) {
                    velocityX2 = 0;
                    velocityY2 = 1;
                } else if (up && velocityY2 != 1) {
                    velocityX2 = 0;
                    velocityY2 = -1;
                }
            }
            hasMovedP2 = false;
        }
    }


    public void loadInputBuffer(int inputBuffer){
        if (num_players == 1 || num_players == 0) {
                if ((inputBuffer == KeyEvent.VK_UP || inputBuffer == KeyEvent.VK_W) && velocityY1 != 1) {
                    velocityX1 = 0;
                    velocityY1 = -1;
                } else if ((inputBuffer == KeyEvent.VK_DOWN || inputBuffer == KeyEvent.VK_S) && velocityY1 != -1) {
                    velocityY1 = 1;
                    velocityX1 = 0;
                } else if ((inputBuffer == KeyEvent.VK_LEFT || inputBuffer == KeyEvent.VK_A) && velocityX1 != 1) {
                    velocityX1 = -1;
                    velocityY1 = 0;
                } else if ((inputBuffer == KeyEvent.VK_RIGHT || inputBuffer == KeyEvent.VK_D) && velocityX1 != -1) {
                    velocityX1 = 1;
                    velocityY1 = 0;
                }
                hasMovedP1 = false;
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(snakeHead1,snakeBody1,1);
        if (num_players != 1) {
            move(snakeHead2, snakeBody2, 2);
        }
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }
    //Checks if a key has been pressed
    @Override
    public void keyPressed(KeyEvent e) {

        if (num_players == 1 || num_players == 0) {
            inputBufferP1 = e.getKeyCode();
            if (hasMovedP1) {
                if ((inputBufferP1 == KeyEvent.VK_UP || inputBufferP1 == KeyEvent.VK_W) && velocityY1 != 1) {
                    velocityX1 = 0;
                    velocityY1 = -1;
                } else if ((inputBufferP1 == KeyEvent.VK_DOWN || inputBufferP1 == KeyEvent.VK_S) && velocityY1 != -1) {
                    velocityY1 = 1;
                    velocityX1 = 0;
                } else if ((inputBufferP1 == KeyEvent.VK_LEFT || inputBufferP1 == KeyEvent.VK_A) && velocityX1 != 1) {
                    velocityX1 = -1;
                    velocityY1 = 0;
                } else if ((inputBufferP1 == KeyEvent.VK_RIGHT || inputBufferP1 == KeyEvent.VK_D) && velocityX1 != -1) {
                    velocityX1 = 1;
                    velocityY1 = 0;
                }
                hasMovedP1 = false;
            }
        }

        else if (num_players == 2){
            if(hasMovedP1) {
                //Player 1 Movement
                if (e.getKeyCode() == KeyEvent.VK_W && velocityY1 != 1) {
                    velocityX1 = 0;
                    velocityY1 = -1;
                } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY1 != -1) {
                    velocityY1 = 1;
                    velocityX1 = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX1 != 1) {
                    velocityX1 = -1;
                    velocityY1 = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX1 != -1) {
                    velocityX1 = 1;
                    velocityY1 = 0;
                }
                hasMovedP1 = false;
            }
            if(hasMovedP2) {
                //Player 2 Movement
                if (e.getKeyCode() == KeyEvent.VK_UP && velocityY2 != 1) {
                    velocityX2 = 0;
                    velocityY2 = -1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY2 != -1) {
                    velocityX2 = 0;
                    velocityY2 = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX2 != 1) {
                    velocityX2 = -1;
                    velocityY2 = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX2 != -1) {
                    velocityX2 = 1;
                    velocityY2 = 0;
                }
                hasMovedP2 = false;
            }
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
