import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Canvas");

        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelLateral = new JPanel();
        Render renderPanel = new Render();
        renderPanel.setPreferredSize(new Dimension((int)(frame.getWidth()*0.8), frame.getHeight()));
        panelLateral.setPreferredSize(new Dimension((int)(frame.getWidth()*0.2), frame.getHeight()));

        frame.add(renderPanel, BorderLayout.CENTER);
        //frame.add(panelLateral, BorderLayout.EAST);

        frame.setVisible(true);

        renderPanel.createBufferStrategy(2);
        renderPanel.start();
    }
}

class Line {
    double[] p = new double[4];

    public Line(double x1, double y1, double x2, double y2) {
        p[0] = x1;
        p[1] = y1;
        p[2] = x2;
        p[3] = y2;
    }

    public double[] line() {
        return p;
    }

    public double getM(){
        return (p[3]-p[1])/(p[2]-p[1]);
    }

    public double[] computeNormalicedVector() {
        double[] vector = new double[2];
        double horizontalX = p[2] - p[0];
        double horizontalY = p[3] - p[1];

        double magnitude = Math.sqrt(horizontalX * horizontalX + horizontalY * horizontalY);
        horizontalX /= magnitude;
        horizontalY /= magnitude;

        vector[0] = horizontalX;
        vector[1] = horizontalY;
        return vector;
    }
}

class Scare {
    Component context;
    int size;
    int pointSize;

    double[][] originalVertices = new double[2][4];
    int[][] displayVertices = new int[2][4];
    Line horizon;

    public Scare(Component context, int positionX, int positionY, int size, int pointSize) {
        this.context = context;
        this.size = size;
        this.pointSize = pointSize;

        originalVertices[0][0] = positionX;
        originalVertices[1][0] = positionY;

        originalVertices[0][1] = positionX + size;
        originalVertices[1][1] = positionY;

        originalVertices[0][2] = positionX;
        originalVertices[1][2] = positionY + size;

        originalVertices[0][3] = positionX + size;
        originalVertices[1][3] = positionY + size;

        saveDisplayVertex();

        double[] centro = centro();
        horizon = new Line(centro[0], centro[1], originalVertices[0][3], (originalVertices[1][1] + originalVertices[1][3])/2);
    }

    public Line getHorizon() {
        return horizon;
    }

    public double[] centro() {
        double[] centro =  new double[2];
        centro[0] = (originalVertices[0][0] + originalVertices[0][3])/2;
        centro[1] = (originalVertices[1][0] + originalVertices[1][3])/2;

        return centro;
    }

    private void saveDisplayVertex() {
        for (int i = 0; i < originalVertices[0].length; i++) {
            displayVertices[0][i] = (int) Math.round(originalVertices[0][i]);
            displayVertices[1][i] = (int) Math.round(originalVertices[1][i]);
        }
    }

    public void rotate(double angle) {
        if (angle >= 360) angle = 0;

        double centroX = (originalVertices[0][0] + originalVertices[0][3])/2;
        double centroY = (originalVertices[1][0] + originalVertices[1][3])/2;

        double radians = Math.toRadians(angle);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);

        for (int i = 0; i < originalVertices[0].length; i++) {
            double x = originalVertices[0][i] - centroX;
            double y = originalVertices[1][i] - centroY;

            originalVertices[0][i] = (x * cos) - (y * sin) + centroX;
            originalVertices[1][i] = (x * sin) + (y * cos) + centroY;

            displayVertices[0][i] = (int) Math.round(originalVertices[0][i]);
            displayVertices[1][i] = (int) Math.round(originalVertices[1][i]);
        }

        double[] points = horizon.line();
        for (int i = 0; i < points.length; i+=2) {
            double x = points[i] - centroX;
            double y = points[i+1] - centroY;

            points[i] = (x * cos) - (y * sin) + centroX;
            points[i+1] = (x * sin) + (y * cos) + centroY;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        for (int i = 0; i < displayVertices[0].length; i++) {
            g.fillOval(displayVertices[0][i] - pointSize/2, displayVertices[1][i] - pointSize/2, pointSize, pointSize);
            g.drawString(new StringBuilder().append(displayVertices[0][i] - pointSize / 2).append(" , ").append(displayVertices[1][i] - pointSize/2).toString(), displayVertices[0][i] - pointSize/2, displayVertices[1][i] - pointSize/2);
        }

        for (int i = 1; i < 3; i++) {
            g.drawLine(displayVertices[0][0], displayVertices[1][0], displayVertices[0][i], displayVertices[1][i]);
            g.drawLine(displayVertices[0][3], displayVertices[1][3], displayVertices[0][i], displayVertices[1][i]);
        }


        g.setColor(Color.RED);
        double[] line = horizon.line();
        g.drawLine((int) Math.round(line[0]), (int) Math.round(line[1]), (int) Math.round(line[2]), (int) Math.round(line[3]));
    }
}

class Render extends Canvas {
    private Scare scare = new Scare(this, 200, 200, 200, 10);
    private double angle = 0;

    public Render() {
        setBackground(Color.BLACK);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    scare.rotate(angle);
                    System.out.println("Rotado " + angle + " grados");
                }
            }
        });
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

    private void renderMouseinfo(Graphics g) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);

        int padding = 5;
        int mouseX = p.x;
        int mouseY = p.y;

        int renderX = mouseX + padding;
        int renderY = mouseY - padding;

        String text = String.format("[x: %d, y: %d]", mouseX, mouseY);
        g.setColor(Color.green);
        g.drawString(text, renderX, renderY);

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

        this.angle = angle;

        g.setColor(Color.YELLOW);
        g.drawLine((int) Math.round(horizontal[0]), (int) Math.round(horizontal[1]), mouseX, mouseY);
        g.drawString(String.format("Angle: %.2f°", this.angle), (int) (horizontal[0] + padding), (int) (horizontal[1] - padding));
    }

    private void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            return;
        }

        Graphics g = bufferStrategy.getDrawGraphics();
        try {
            g.clearRect(0, 0, getWidth(), getHeight());
            scare.render(g);
            renderMouseinfo(g);
            scare.rotate(0.3);
        } finally {
            g.dispose();
        }

        bufferStrategy.show();
    }
}