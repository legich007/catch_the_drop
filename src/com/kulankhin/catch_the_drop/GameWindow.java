package com.kulankhin.catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame
{
    private static GameWindow gameWindow;
    private static Image background;
    private static Image drop;
    private static Image gameOver;
    private static int width_game_window = 900;
    private static int height_game_window = 700;
    private static float drop_top = -100;
    private static float drop_v = 200;
    private static float drop_left = 200;
    private static long last_frame_time;
    private static int score;

    public static void main(String[] args) throws IOException
    {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream(("drop.jpg")));
        gameOver = ImageIO.read((GameWindow.class.getResourceAsStream("gameOver.jpg")));
        background = background.getScaledInstance(width_game_window,height_game_window,Image.SCALE_AREA_AVERAGING);
        drop = drop.getScaledInstance(50,50,Image.SCALE_AREA_AVERAGING);
        gameOver = gameOver.getScaledInstance(width_game_window,height_game_window,Image.SCALE_AREA_AVERAGING);
        last_frame_time = System.nanoTime();
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(200,150);
        gameWindow.setSize(width_game_window,height_game_window);
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        GameField gameField = new GameField();
        gameWindow.add(gameField);
        gameField.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop)
                {
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (gameWindow.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 20;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }
            }
        });
    }


    private static void onRepaint(Graphics g)
    {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time)*0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;


        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int) drop_left, (int) drop_top, null);
        if (drop_top > gameWindow.getHeight()) g.drawImage(gameOver, 0, 0, null);




    }
    private static class GameField extends JPanel
    {
        @Override
        protected void paintComponent (Graphics g)
        {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }

    }

}
