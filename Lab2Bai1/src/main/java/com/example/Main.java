package com.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;

    public static class FlappyBird extends JPanel {
        // images
        Image backgroundImg;

        FlappyBird() throws IOException {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

            // load images
            backgroundImg = ImageIO.read(new File("images/flappybirdbg.png"));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw background
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        frame.setResizable(false); // non-resizable size

        // set background
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}