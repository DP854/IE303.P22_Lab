package com.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;

    public static class FlappyBird extends JPanel implements ActionListener, KeyListener {
        // images
        Image backgroundImg;
        Image birdImg;

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

        // game logic
        Bird bird;
        int velocityYBird = 0; // move bird up/down speed
        int gravity = 1;

        Timer gameLoop;

        FlappyBird() throws IOException {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
            setFocusable(true); // allow keyboard event
            addKeyListener(this);

            // load images
            backgroundImg = ImageIO.read(new File("images/flappybirdbg.png"));
            birdImg = ImageIO.read(new File("images/flappybird.png"));

            // bird
            bird = new Bird(birdImg);

            // game timer
            gameLoop = new Timer(1000/60, this); // 60 fps
            gameLoop.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw background
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);

            // draw bird
            g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);
        }

        public void move() {
            // bird
            velocityYBird += gravity;
            bird.y += velocityYBird; // apply gravity to bird
            bird.y = Math.max(bird.y, 0); // limit bird to top
        }

        // implement ActionListener
        @Override
        public void actionPerformed(ActionEvent e) { // gameLoop events
            move();
            repaint(); // re-render
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                velocityYBird = -9;
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