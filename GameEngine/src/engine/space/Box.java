package engine.space;

/**
 *
 * @author Kassandra Kaeck
 */
public class Box {
    private double x, y, z, width, height, depth;
    public Box (double x, double y, double z, double width, double height, double depth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
    public Box (double x, double y, double z, double size) {
        this.x = x;
        this.y = y;
        this.z = z;
        width = height = depth = size;
    }
    public double getX () {
        return x;
    }
    public double getY () {
        return y;
    }
    public double getZ () {
        return z;
    }
    public double getWidth () {
        return width;
    }
    public double getHeight () {
        return height;
    }
    public double getDepth () {
        return depth;
    }
    public double getX2 () {
        return (x + width);
    }
    public double getY2 () {
        return (y + height);
    }
    public double getZ2 () {
        return (z + depth);
    }
    public Vertex getCorner (int x, int y, int z) {
        return (new Vertex ((this.x + x * width), (this.y + y * height), (this.z + z * depth)));
    }
    public Vertex getMidpoint () {
        return (new Vertex ((x + 0.5 * width), (y + 0.5 * height), (z + 0.5 * depth)));
    }
    public static Box intersect (Box one, Box two) {
        double[] array = new double[] { one.x, one.y, one.z, one.width, one.height, one.depth,
            two.x, two.y, two.z, two.width, two.height, two.depth
        };
        int i;
        for (i = 0;i < 3;i++) {
            array[i + 3] += array[i];
            array[i + 9] += array[i + 6];
            if (array[i] < array[i + 6]) {
                array[i] = array[i + 6];
            }
            if (array[i + 9] < array[i + 3]) {
                array[i + 3] = array[i + 9];
            }
            array[i + 3] -= array[i];
            if (array[i + 3] < 0.0) {
                return null;
            }
        }
        return (new Box (array[0], array[1], array[2], array[3], array[4], array[5]));
    }
}
