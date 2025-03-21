import java.awt.*;

public class Scare {
    Component context;
    int size;
    int pointSize;

    double[][] originalVertices = new double[2][4];
    int[][] displayVertices = new int[2][4];
    Vector horizon;

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
        horizon = new Vector(centro[0], centro[1], originalVertices[0][3], (originalVertices[1][1] + originalVertices[1][3])/2);
    }

    public Vector getHorizon() {
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

    public void moveHorizontal(int m){
       for (int i = 0; i < originalVertices[0].length; i++) {
           originalVertices[0][i] += m;
       }
       saveDisplayVertex();
    }

    public void moveVertical(int m){
        for (int i = 0; i < originalVertices[0].length; i++) {
            originalVertices[1][i] += m;
        }
        saveDisplayVertex();
    }
}
