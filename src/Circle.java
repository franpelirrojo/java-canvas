import java.awt.*;

public class Circle {
    double [] position = new double[2];
    int[] displayPosition = new int[2];
    int r;

    public Circle(int x, int y, int r) {
        this.r = r;
        position[0] = x;
        position[1] = y;
    }

    public void render(Graphics g) {
        saveDisplay();
        g.setColor(Color.green);
        g.fillOval(displayPosition[0], displayPosition[1], r/2, r/2);
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
