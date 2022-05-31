package Microproject;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Snake extends JFrame {
    public Snake(){
        this.add(new GamePanel());
        this.setTitle("SNAKE GAME");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to exit to MAIN MENU?", 
                        "Exit",
                        JOptionPane.YES_NO_OPTION
                    );
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new MainMenu();
                }
                else{
                    System.exit(0);
                }
            }
        }
        );

        this.addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                GamePanel.GAME_STATE = 2;
            }
            public void windowLostFocus(WindowEvent e) {
                GamePanel.GAME_STATE = 0;
            }
        }
        );
    }

}

class GamePanel extends JPanel implements ActionListener {
    static final int ACTUAL_SCREEN_WIDTH = 1800;
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 175;
    static int GAME_STATE = 0;

    final int snake_x[] = new int[GAME_UNITS];
    final int snake_y[] = new int[GAME_UNITS];


    int bodyParts = 6;
    int applesEaten;

    int LIMIT = 0;

    int appleX, appleY, boostersX, boostersY;

    char direction = 'R';

    boolean running = false;
    boolean snake_collision = false;

    int fruit_file_number, booster_file_number = 0;


    File fruit_images[] = {
            new File("assets/food/fruit_01.png"),
            new File("assets/food/fruit_02.png")
        };

    File boosters[] = {
            new File("assets/food/candy_01.png"),
            new File("assets/food/candy_02.png"),
            new File("assets/food/candy_03.png"),
    };

    Timer timer;
    Random random;
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(ACTUAL_SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(176, 229, 124));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if(e.getButton() == MouseEvent.BUTTON1){
                   if(!running){
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                    frame.dispose();
                    new GameFrame();
                   }
                   else{
                       return;
                   }
                }
                // now get right click to pause
                if(e.getButton() == MouseEvent.BUTTON3){
                    if(!running){
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                    frame.dispose();
                    new MainMenu();
                   }
                   else{
                       return;
                   }
                }
            }
            
        });

        startGame();
    }

    public void startGame() {
        newApple();
        newBooster();
        running = true;
        GAME_STATE = 1;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running && GAME_STATE == 1) {
            side_panel(g);
            
            

            Image fruit_image = null;
            Image boosters_image = null;


            try {
                fruit_image = ImageIO.read(fruit_images[fruit_file_number]);
                boosters_image = ImageIO.read(boosters[booster_file_number]);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
            

            g.drawImage(fruit_image, appleX, appleY, this);
            g.drawImage(boosters_image, boostersX, boostersY, this);

            drawSnake(g);


        } 

        else if(running && GAME_STATE == 0){
            g.setColor(Color.red);
            g.setFont(new Font("Luckiest Guy", Font.BOLD, 80));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("GAME PAUSED !",
                        (SCREEN_WIDTH - metrics.stringWidth("GAME PAUSED !")) / 2,
                        (SCREEN_HEIGHT - metrics.getHeight()) / 2
            );
            g.setColor(new Color(82, 88, 97));
            g.setFont(new Font("Luckiest Guy", Font.BOLD, 30));
            String the_text = "Press R to resume";
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString(the_text, (SCREEN_WIDTH - metrics3.stringWidth(the_text)) / 2, SCREEN_HEIGHT / 2 + metrics3.getHeight());
            side_panel(g);

        }
        else if(running && GAME_STATE == 2){
            g.setColor(Color.red);
            g.setFont(new Font("Luckiest Guy", Font.BOLD, 80));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("RESUMED!",
                    (SCREEN_WIDTH - metrics.stringWidth("RESUMED!")) / 2,
                    (SCREEN_HEIGHT - metrics.getHeight()) / 2);
            GAME_STATE = 1;
        }
        
        else {
            gameOver(g);
        }

    }

    public void drawSnake(Graphics g){
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                Image head = null;
                try {
                    head = ImageIO.read(new File("assets/sprite_sheet/snake_" + direction + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(head, snake_x[i], snake_y[i], this);
            } 
            else if (i == bodyParts - 1) {
                Image tail = null;
                int prevX = snake_x[i - 1];
                int prevY = snake_y[i - 1];

                try {
                    if (prevY < snake_y[i]) {
                        tail = ImageIO.read(new File("assets/sprite_sheet/snake_u_tail.png"));
                    } else if (prevX > snake_x[i]) {
                        tail = ImageIO.read(new File("assets/sprite_sheet/snake_r_tail.png"));
                    } else if (prevY > snake_y[i]) {
                        tail = ImageIO.read(new File("assets/sprite_sheet/snake_d_tail.png"));
                    } else if (prevX < snake_x[i]) {
                        tail = ImageIO.read(new File("assets/sprite_sheet/snake_l_tail.png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(tail, snake_x[i], snake_y[i], this);
            } 
            else {
                int nextX = snake_x[i + 1];
                int nextY = snake_y[i + 1];

                int prevX = snake_x[i - 1];
                int prevY = snake_y[i - 1];

                Image body = null;
                try {
                    if (prevX < snake_x[i] && nextX > snake_x[i] ||
                            nextX < snake_x[i] && prevX > snake_x[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/snake_lr.png"));
                    }

                    else if (prevY < snake_y[i] && nextY > snake_y[i] ||
                            nextY < snake_y[i] && prevY > snake_y[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/snake_v.png"));
                    }

                    else if (prevX < snake_x[i] && nextY > snake_y[i] ||
                            nextX < snake_x[i] && prevY > snake_y[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/tile002.png"));
                    }

                    else if (prevY < snake_y[i] && nextX < snake_x[i] ||
                            nextY < snake_y[i] && prevX < snake_x[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/tile012.png"));
                    } 
                    else if (prevX > snake_x[i] && nextY < snake_y[i] ||
                            nextX > snake_x[i] && prevY < snake_y[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/tile005.png"));
                    } 
                    else if (prevY > snake_y[i] && nextX > snake_x[i] ||
                            nextY > snake_y[i] && prevX > snake_x[i]) {
                        body = ImageIO.read(new File("assets/sprite_sheet/tile000.png"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g.drawImage(body, snake_x[i], snake_y[i], this);
            }
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        fruit_file_number = random.nextInt(fruit_images.length);
    }

    public void newBooster() {
        if (random.nextInt(5) < 4 | LIMIT > 5) {
            LIMIT = 0;  
            boostersX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            boostersY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            booster_file_number = random.nextInt(boosters.length);
        }
        else{
            boostersX = -100;
            boostersY = -100;
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            snake_x[i] = snake_x[i - 1];
            snake_y[i] = snake_y[i - 1];
        }

        switch (direction) {
            case 'U':
                snake_y[0] = snake_y[0] - UNIT_SIZE;
                break;
            case 'D':
                snake_y[0] = snake_y[0] + UNIT_SIZE;
                break;
            case 'L':
                snake_x[0] = snake_x[0] - UNIT_SIZE;
                break;
            case 'R':
                snake_x[0] = snake_x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {
        if ((snake_x[0] == appleX) && (snake_y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            LIMIT++;
            if(LIMIT == 5){
                LIMIT = 0;
                newBooster();
            }
        }
    }

    public void checkBooster() {
        if ((snake_x[0] == boostersX) && (snake_y[0] == boostersY)) {
            if(booster_file_number == 0){
                if (applesEaten < 3){
                    applesEaten = 0;
                    bodyParts = bodyParts - 3;
                }
                else{
                    applesEaten = applesEaten - 3;
                    bodyParts = bodyParts - 3;
                }
                newBooster();
            }
            else{
                bodyParts = bodyParts + 2;
                newBooster();
            }
            
           
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((snake_x[0] == snake_x[i]) && (snake_y[0] == snake_y[i])) {
                running = false;
            }
        }
        if(bodyParts <= 1){
            running = false;
        }
        if (snake_x[0] < 0) {
            running = false;
        }
        if (snake_x[0] >= SCREEN_WIDTH) {
            running = false;
        }
        if (snake_y[0] < 0) {
            running = false;
        }
        if (snake_y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        side_panel(g);
        drawSnake(g);
        g.setColor(Color.red);
        g.setFont(new Font("Luckiest Guy", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(new Color(82, 88, 97));
        g.setFont(new Font("Luckiest Guy", Font.BOLD, 30));
        String the_text = "Left click to Restart | Right click for Main Menu";
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString(the_text, (SCREEN_WIDTH -  metrics3.stringWidth(the_text)) / 2, SCREEN_HEIGHT / 2 + metrics2.getHeight());
    }

    public void side_panel(Graphics g){
        g.setColor(new Color(106, 143, 70));
        g.fillRect(1300, 0, 10, 750);
        g.setColor(Color.red);
        g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        int width = (SCREEN_WIDTH + (SCREEN_WIDTH + metrics.stringWidth("Score: " + applesEaten))) / 2;
        g.drawString("Score: " + applesEaten, width, g.getFont().getSize());
        g.setColor(new Color(106, 143, 70));
        g.fillRect(SCREEN_WIDTH, g.getFont().getSize() + 20, ACTUAL_SCREEN_WIDTH - SCREEN_WIDTH, 5);

        String information = "- Control snake with the W A S and D\n\n" +
                             "- Some candies can be dangerous for snake\n\n" +
                             "- Boosters will get you more score and length\n\n" +
                             "- Pause the game by pressing the P key\n\n" +
                             "- Resume the game by pressing the R key\n\n"+
                             "- Main Menu by pressing the M key\n\n"+
                             "- Some Magical potions which do stuff.";


        String[] lines = information.split("\n");

        g.setFont(new Font("Luckiest Guy", Font.ITALIC, 20));

        int lineHeight = g.getFontMetrics().getHeight();
        for (int lineCount = 0; lineCount < lines.length; lineCount++) {
            int xPos = 1320;
            int yPos = 100 + lineCount * lineHeight;
            String line = lines[lineCount];
            g.drawString(line, xPos, yPos);
            if(lineCount == lines.length - 1){
                g.setFont(new Font("Luckiest Guy", Font.ITALIC, 20));
                g.fillRect(1300, yPos + lineHeight, ACTUAL_SCREEN_WIDTH - SCREEN_WIDTH, 5);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            if(GAME_STATE == 1){
                move();
                checkApple();
                checkBooster();
                checkCollisions();
            }
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    GAME_STATE =0;
                    break;
                case KeyEvent.VK_P:
                    GAME_STATE = 0;
                    break;
                case KeyEvent.VK_R:
                    if(GAME_STATE == 0){
                        GAME_STATE = 1;
                    }
                    break;
            }
        }
    }
}