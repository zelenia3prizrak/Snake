import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 200;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int snake_length = 6;
    int foodEaten = 0;
    int snakeFoodX;
    int snakeFoodY;
    char dir = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {

        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {


            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(snakeFoodX, snakeFoodY, UNIT_SIZE, UNIT_SIZE);


            for (int i = 0; i < snake_length; i++) {

                if (i == 0) {
                    g.setColor(Color.cyan);

                } else {

                    if (i % 2 == 0) {
                        g.setColor(Color.black);
                    } else {
                        g.setColor(Color.white);
                    }

                }
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }


            g.setColor(Color.GREEN);
            g.setFont(new Font("Serif", Font.ITALIC, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }

    }


    public void newFood() {
        snakeFoodX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        snakeFoodY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }


    public void move() {
        for (int i = snake_length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }


        switch (dir) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }


    public void checkFood() {
        if ((x[0] == snakeFoodX) && (y[0] == snakeFoodY)) {
            snake_length++;
            foodEaten++;
            newFood();
        }
    }


    public void checkCollisions() {

        for (int i = snake_length; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }


    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Serif", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + foodEaten)) / 2, g.getFont().getSize());


        g.setColor(Color.red);
        g.setFont(new Font("Serif", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (dir != 'R') {
                        dir = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dir != 'L') {
                        dir = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (dir != 'D') {
                        dir = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dir != 'U') {
                        dir = 'D';
                    }
                    break;
            }
        }
    }
}