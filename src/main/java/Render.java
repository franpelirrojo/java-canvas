import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferStrategy;

import javax.swing.SwingUtilities;

public class Render extends Canvas{
    private static final boolean DEBUG = true;
    private int mouseX;
    private int mouseY;

    public Render() {
        setBackground(new Color(255, 245, 243));
    }

    public void start() {
        Thread renderThread = new Thread(this::runRenderLoop);
        renderThread.start();
    }

    private void runRenderLoop() {
        while (true) {
            render();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            return;
        }

        Graphics g = bufferStrategy.getDrawGraphics();
        try {
            g.clearRect(0, 0, getWidth(), getHeight());
            renderMouseinfo(g);
        } finally {
            g.dispose();
        }

        bufferStrategy.show();
    }

    private void renderMouseinfo(Graphics g) {
        java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);

        int padding = 5;
        mouseX = p.x;
        mouseY = p.y;

        int renderX = mouseX + padding;
        int renderY = mouseY - padding;

        String text = String.format("[x: %d, y: %d]", mouseX, mouseY);
        g.setColor(Color.green);
        g.drawString(text, renderX, renderY);

        /*
        double[] horizontal = scare.getHorizon().line();
        double[] horizontalVector = scare.getHorizon().computeNormalicedVector();

        double mouseVectorX = p.x - horizontal[0];
        double mouseVectorY = p.y - horizontal[1];
        double mouseMag = Math.sqrt(mouseVectorX * mouseVectorX + mouseVectorY * mouseVectorY);
        double normMouseX = mouseVectorX / mouseMag;
        double normMouseY = mouseVectorY / mouseMag;

        double dot = (horizontalVector[0] * normMouseX) + (horizontalVector[1] * normMouseY);
        double angle = Math.acos(dot);

        double crossProduct = horizontalVector[0] * normMouseY - horizontalVector[1] * normMouseX;
        if (crossProduct > 0) {
            angle = -angle;
        }

        angle = Math.toDegrees(angle);

        if (angle < 0) {
            angle += 360;
        }

        if (Double.isNaN(angle)) {
            System.out.println(dot);
            System.out.println(angle);
        }else {
            this.angle = angle;
        }
        */
    }

    private void renderFixedGrid(Graphics g) {
        int cellSize = 80;
        int cols = (int) Math.floor((double) getWidth() / cellSize);
        int rows = (int) Math.floor((double) getHeight() / cellSize);
        int ofsetCols = getWidth()%cellSize;
        int ofsetRows = getHeight()%cellSize;
        int cellWidth  = cellSize + ofsetCols / cols;
        int cellHeight = cellSize + ofsetRows / rows;
        int moduleWidth  = ofsetCols%cols;
        int moduleHeight = ofsetRows%rows;

        g.setColor(new Color(49, 49, 49));
        for (int i = 0; i <= cols; i++){
            int x = i * cellWidth;
            g.drawLine(x, 0, x, getHeight());
            g.drawLine(x+1, 0, x+1, getHeight());
            g.drawString(String.valueOf(i+1), x + 15, 15);
        }
        for (int i = 0; i <= rows; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, getWidth(), y);
            g.drawLine(0, y+1, getWidth(), y+1);
            g.drawString(String.valueOf(i+1), 15, y + 15);
        }

        if (DEBUG) {
            String[] logs = new String[6];
            logs[0] = String.format("Cols: %s | Rows: %s", cols, rows);
            logs[1] = String.format("Width: %s | Height: %s", getWidth(), getHeight());
            logs[2] = String.format("Width Ofset: %s | Height Ofset: %s", ofsetCols, ofsetRows);
            logs[3] = String.format("Cell size: %s", cellSize);
            logs[4] = String.format("Cell Width: %s | Cell Height: %s", cellWidth, cellHeight);
            logs[5] = String.format("Width module: %s | Height module: %s", moduleWidth, moduleHeight);

            g.setColor(Color.black);
            g.fillRect(getWidth()-200, getHeight()-200, 200, 200);
            g.setColor(Color.magenta);
            for (int i = 0; i < logs.length; i++) {
                g.drawString(logs[i], getWidth()-200, getHeight()-185+(15*i));
            }
        }
    }
}
