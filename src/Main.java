import java.awt.*;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class Main {
    private static Window window;
    private static int [][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private static void grid(Graphics g) {
        int cellSize = 50;
        int gap = 2;
        int x, y, offsetY, offsetX;

        offsetY = 0;
        for (int i = 0; i < map.length; i++) {
            y = i*cellSize + offsetY;
            offsetY+=gap;
            offsetX= 0;
            for (int k = 0; k < map[i].length; k++) {
                x = k*cellSize + offsetX;
                offsetX+=gap;
                if (map[i][k] == 1) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    private static void player(Graphics g) {
        g.setColor(new Color(253, 225, 90));
        g.fillOval(window.getWidth()/2, window.getHeight()/2, 10, 10);
    }

    private static void mouse(Graphics g) {
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, window);

        int padding = 5;
        int mouseX = p.x;
        int mouseY = p.y;

        int renderX = mouseX + padding;
        int renderY = mouseY - padding;

        String text = String.format("[x: %d, y: %d]", mouseX, mouseY);
        g.setColor(Color.green);
        g.drawString(text, renderX, renderY);
    }

    public static void main(String[] args) {
        Window parentWindow = new Window(null);
        GraphicsConfiguration gc =  parentWindow.getGraphicsConfiguration();
        Rectangle bound = gc.getBounds();
        parentWindow.setSize(bound.getSize());
        parentWindow.setVisible(true);

        window = new Window(parentWindow);
        window.setBackground(new Color(50, 50,50));
        window.setSize(468, 468);
        window.setLocation(0, 0);
        window.setVisible(true);
        window.createBufferStrategy(2);

        BufferStrategy strategy = window.getBufferStrategy();
        while (true) {
            do {
                do {
                    Graphics g = strategy.getDrawGraphics();
                    g.clearRect(0, 0, window.getWidth(), window.getHeight());
                    grid(g);
                    player(g);
                    mouse(g);
                    g.dispose();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (strategy.contentsRestored());

                strategy.show();
            } while (strategy.contentsLost());
        }
    }
}
