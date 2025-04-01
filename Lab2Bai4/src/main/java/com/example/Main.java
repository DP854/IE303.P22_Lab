package com.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;

    public static class FlappyBird extends JPanel implements ActionListener, KeyListener {
        // images
        Image backgroundImg;
        Image birdImg;
        Image topPipeImg;
        Image bottomPipeImg;

        // bird class
        int birdX = BOARD_WIDTH/8;
        int birdY = BOARD_HEIGHT/2;
        int birdWidth = 34;
        int birdHeight = 24;

        class Bird {
            int x = birdX;
            int y = birdY;
            int width = birdWidth;
            int height = birdHeight;
            Image img;

            Bird(Image img) {
                this.img = img;
            }
        }

        // pipe class
        int pipeX = BOARD_WIDTH;
        int pipeY = 0;
        int pipeWidth = 64;
        int pipeHeight = 512;

        class Pipe {
            int x = pipeX;
            int y = pipeY;
            int width = pipeWidth;
            int height = pipeHeight;
            Image img;
            boolean passed = false;

            Pipe(Image img) {
                this.img = img;
            }
        }

        // game logic
        Bird bird;
        int velocityX = -4; // move pipes left speed, move bird right speed
        int velocityYBird = 0; // move bird up/down speed
        int gravity = 1;

        ArrayList<Pipe> pipes;

        Timer gameLoop;
        Timer placePipeTimer;
        boolean gameOver = false;
        int score = 0;

        // restart button
        JButton restartButton;

        FlappyBird() throws IOException {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
            setFocusable(true); // allow keyboard event
            addKeyListener(this);

            // load images
            backgroundImg = ImageIO.read(new File("images/flappybirdbg.png"));
            birdImg = ImageIO.read(new File("images/flappybird.png"));
            topPipeImg = ImageIO.read(new File("images/toppipe.png"));
            bottomPipeImg = ImageIO.read(new File("images/bottompipe.png"));

            // bird
            bird = new Bird(birdImg);
            pipes = new ArrayList<>();

            // place pipes timer
            placePipeTimer = new Timer(1500, _ -> placePipes());
            placePipeTimer.start();

            // game timer
            gameLoop = new Timer(1000/60, this); // 60 fps
            gameLoop.start();

            // init restart button
            restartButton = new JButton("Restart");
            restartButton.setBounds(BOARD_WIDTH/2 - 75, BOARD_HEIGHT/2 - 25, 150, 50);
            restartButton.setFont(new Font("Arial", Font.BOLD, 18));
            restartButton.setBackground(new Color(0x1DA1F2));
            restartButton.setForeground(Color.WHITE);
            restartButton.setFocusPainted(false); // turn off focus border effect
            restartButton.setVisible(false);

            restartButton.addActionListener(_ -> restart());

            setLayout(null);
            add(restartButton);
        }

        void placePipes() {
            int randomPipeY = (int) (pipeY - (double) pipeHeight/4 - Math.random()*((double) pipeHeight/2));
            int openingSpace = BOARD_HEIGHT/4;

            Pipe topPipe = new Pipe(topPipeImg);
            topPipe.y = randomPipeY;
            topPipe.passed = true; // prevent score twice
            pipes.add(topPipe);

            Pipe bottomPipe = new Pipe(bottomPipeImg);
            bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
            pipes.add(bottomPipe);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw background
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);

            // draw bird
            g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

            // draw pipes
            for (Pipe pipe : pipes) {
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            // draw score
            g.setColor(Color.white);

            g.setFont(new Font("Arial", Font.PLAIN, 32));
            if(gameOver) {
                g.drawString("Game Over: " + score, 10, 35);
            } else {
                g.drawString("Score: " + score, 10, 35);
            }
        }

        public void move() {
            // bird
            velocityYBird += gravity;
            bird.y += velocityYBird; // apply gravity to bird
            bird.y = Math.max(bird.y, 0); // limit bird to top

            // pipes
            for (Pipe pipe : pipes) {
                pipe.x += velocityX;

                if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                    score++;
                    pipe.passed = true;
                }

                if(collision(bird, pipe)) {
                    gameOver = true;
                }
            }

            if(bird.y > BOARD_HEIGHT) {
                gameOver = true;
            }
        }

        boolean collision(Bird b, Pipe p) {
            return  b.x < p.x + p.width &&  // bird's left side is on the left of pipe's right side
                    b.x + b.width > p.x &&  // bird's right side is on the right of pipe's left side
                    b.y < p.y + p.height && // bird's top side is on the top of pipe's bottom side
                    b.y + b.height > p.y;   // bird's bottom side is on the bottom of pipe's top side
        }

        // restart
        public void restart() {
            if(gameOver) {
                bird.y = birdY;
                velocityYBird = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                restartButton.setVisible(false);
                gameLoop.start();
                placePipeTimer.start();
                requestFocus(); // return focus to the game panel
            }
        }

        // implement ActionListener
        @Override
        public void actionPerformed(ActionEvent e) { // gameLoop events
            move();
            repaint(); // re-render
            if(gameOver) {
                placePipeTimer.stop();
                gameLoop.stop();
                restartButton.setVisible(true);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                velocityYBird = -9;

                // restart
                restart();
            }
        }

        // implement KeyListener
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        frame.setResizable(false); // non-resizable size

        // set background
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);

        // set icon
        frame.setIconImage(flappyBird.birdImg);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}