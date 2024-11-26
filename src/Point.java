import java.awt.*;

public class Point {
    double [] position = new double[2];
    int[] displayPosition = new int[2];
    int size;

    public Point(int x, int y, int size) {
       this.size = size;
       position[0] = x;
       position[1] = y;
    }

    public void render(Graphics g) {
        saveDisplay();
       g.fillOval(displayPosition[0], displayPosition[1], size, size);
    }

    public void saveDisplay() {
       displayPosition[0]  = (int) Math.round(position[0]);
       displayPosition[1]  = (int) Math.round(position[1]);
    }

    public void moveHorizontal(int m) {
        position[0] += m;
    }

    public void moveVertical(int m) {
        position[1] += m;
    }
}
