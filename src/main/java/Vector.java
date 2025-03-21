public class Vector {
    private double[] p = new double[4];

    public Vector(double x1, double y1, double x2, double y2) {
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
