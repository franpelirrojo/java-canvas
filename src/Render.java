import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Render extends Canvas{
    private Scare scare = new Scare(this, 600, 400, 200, 10);
    private double angle = 0;

    public Render() {
        setBackground(Color.GRAY);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                scare.rotate(-angle);
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

        if (Double.isNaN(angle)) {
            System.out.println(dot);
            System.out.println(angle);
            return;
        }else {
            this.angle = angle;
        }

        //g.setColor(Color.YELLOW);
        //g.drawLine((int) Math.round(horizontal[0]), (int) Math.round(horizontal[1]), mouseX, mouseY);
        //g.drawString(String.format("Angle: %.2f°", this.angle), (int) (horizontal[0] + padding), (int) (horizontal[1] - padding));
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
        } finally {
            g.dispose();
        }

        bufferStrategy.show();
    }
}
