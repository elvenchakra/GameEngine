package engine.space;

/**
 *
 * @author Kassandra Kaeck
 */
public class Quaternion {
    double w, x, y, z;
    public Quaternion (double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Quaternion (Vertex dir, double angle) {
        angle *= 0.5;
        w = Math.cos (angle);
        angle = Math.sin (angle);
        x = dir.x * angle;
        y = dir.y * angle;
        z = dir.z * angle;
    }
    public Quaternion (Vertex vector) {
        double mag = vector.magSq ();
        if (mag > 0.03125) {
            mag = Math.sqrt (mag);
            w = Math.cos (mag);
            mag = Math.sin (mag) / mag;
            x = vector.x * mag;
            y = vector.y * mag;
            z = vector.z * mag;
        }
        else {
            double temp = Math.sqrt (1.0 - mag);
            w = Math.sqrt (0.5 + 0.5 * temp);
            temp = Math.sqrt ((0.5 - 0.5 * temp) / mag);
            x = vector.x * temp;
            y = vector.y * temp;
            z = vector.z * temp;
        }
    }
    public Quaternion () {
        w = 1.0;
        x = y = z = 0.0;
    }
    private Quaternion (boolean init) {
        if (init) {
            w = x = y = z = 0.0;
        }
    }
    public Quaternion unit () {
        double mag = Math.sqrt (w * w + x * x + y * y + z * z);
        return (new Quaternion ((w / mag), (x / mag), (y / mag), (z / mag)));
    }
    public Quaternion multiply (Quaternion other) {
        Quaternion ret = new Quaternion (false);
        ret.w = w * other.w - x * other.x - y * other.y - z * other.z;
        ret.x = w * other.x + x * other.w + y * other.z - z * other.y;
        ret.y = w * other.y - x * other.z + y * other.w + z * other.x;
        ret.z = w * other.z + x * other.y - y * other.x + z * other.w;
        return ret;
    }
    public static Quaternion multiply (Quaternion one, Quaternion two) {
        Quaternion ret = new Quaternion (false);
        ret.w = one.w * two.w - one.x * two.x - one.y * two.y - one.z * two.z;
        ret.x = one.w * two.x + one.x * two.w + one.y * two.z - one.z * two.y;
        ret.y = one.w * two.y - one.x * two.z + one.y * two.w + one.z * two.x;
        ret.z = one.w * two.z + one.x * two.y - one.y * two.x + one.z * two.w;
        return ret;
    }
    public static final Quaternion identity = new Quaternion ();
}
