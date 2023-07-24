package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    
    //
    private int score = 0;
    private int highScore = 0;
    
    public Board() {
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        
        //
        loadHighScore();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("com/img/g2.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("com/img/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("com/img/g1.png");
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = new ImageIcon("com/img/bg2.png").getImage();
        g.drawImage(backgroundImage,0,0, getWidth(), getHeight(),this);
        doDrawing(g);
        
    }
    
    private void doDrawing(Graphics g) {
        //
        String scoreText = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        String highscoreText = "Highscore: " + highScore;

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(highscoreText, 10, 20);
        g.drawString(scoreText, 10, 40);
        
        if (inGame) {
    
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            
            gameOver(g);
        }    
    }
    
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        String msg1 = "Score: "+ score;
        String msg2 = "HighScore: " + highScore;
        
        Font small = new Font("Helvetica", Font.BOLD, 25);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 4);
        g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2);
        g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 3);

        //
        JButton Game = new JButton("Try Again");
        Game.setBounds(170,300,150,40);
        Font game = new Font("Helvetica", Font.BOLD, 20);
        Game.setFont(game);
        Game.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){       
                restartGame();
            }
        });
        add(Game);
    
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
        
    }
    //
    private void loadHighScore() {
        try {
            FileReader file = new FileReader("com/highscore.txt");
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
            reader.close();
        } catch (IOException | NumberFormatException ex) {
            System.err.println("ERROR reading score");
        }
    }
    //
    private void saveHighScore() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("com/highscore.txt"))) {
            writer.println(highScore);
        } catch (IOException ex) {
            System.err.println("ERROR reading score");
        }
    }
    //
    public void restartGame(){
        setVisible(false);
        new GameFrame();
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
        dots++;
        score++; 
        locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
    
        repaint();
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}

class GameFrame extends JFrame {

    public GameFrame() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JFrame ex = new Snake();
        ex.setVisible(true);
    }
}