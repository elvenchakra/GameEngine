package engine.space;
import org.lwjgl.opengl.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class Vertex {
    double x, y, z;
    public Vertex (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    Vertex () {}
    public double getX () {
        return x;
    }
    public double getY () {
        return y;
    }
    public double getZ () {
        return z;
    }
    public void draw () {
        GL11.glVertex3d (x, y, z);
    }
    public double magSq () {
        return (x * x + y * y + z * z);
    }
    public boolean isOutFacing (Vertex one, Vertex two, Vertex three) {
        return (dot (this, cross (subtract (two, one), subtract (three, one))) > 0.0);
    }
    public void triangle (Vertex v1, Vertex v2, Vertex v3, Color c1, Color c2, Color c3) {
        c1.draw ();
        v1.draw ();
        if (isOutFacing (v1, v2, v3)) {
            c2.draw ();
            v2.draw ();
            c3.draw ();
            v3.draw ();
        }
        else {
            c3.draw ();
            v3.draw ();
            c2.draw ();
            v2.draw ();
        }
    }
    public void triangle (Vertex one, Vertex two, Vertex three, Color col) {
        triangle (one, two, three, col, col, col);
    }
    public static Vertex add (Vertex one, Vertex two) {
        return (new Vertex ((one.x + two.x), (one.y + two.y), (one.z + two.z)));
    }
    public static Vertex subtract (Vertex one, Vertex two) {
        return (new Vertex ((one.x - two.x), (one.y - two.y), (one.z - two.z)));
    }
    public static Vertex midpoint (Vertex[] points) {
        double x = 0.0, y = 0.0, z = 0.0;
        int i;
        for (i = 0;i < points.length;i++) {
            x += points[i].x;
            y += points[i].y;
            z += points[i].z;
        }
        return (new Vertex ((x / (double)points.length), (y / (double)points.length), (z / (double)points.length)));
    }
    public static double dot (Vertex one, Vertex two) {
        return (one.x * two.x + one.y * two.y + one.z * two.z);
    }
    public static Vertex cross (Vertex one, Vertex two) {
        return (new Vertex ((one.y * two.z - one.z * two.y), (one.z * two.x - one.x * two.z), (one.x * two.y - one.y * two.x)));
    }
    public static final Vertex identity = new Vertex ();
}
