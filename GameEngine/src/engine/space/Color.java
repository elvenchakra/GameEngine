package engine.space;
import org.lwjgl.opengl.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class Color {
    private float r, g, b;
    public Color (double red, double green, double blue) {
        r = (float)red;
        g = (float)green;
        b = (float)blue;
    }
    public Color (int rgb, int bits) {
        int mask = (1 << bits) - 1;
        int red = rgb & mask, green = (rgb >> bits) & mask, blue = (rgb >> (2 * bits)) & mask;
        r = (float)red / (float)mask;
        g = (float)green / (float)mask;
        b = (float)blue / (float)mask;
    }
    public float getRed () {
        return r;
    }
    public float getGreen () {
        return g;
    }
    public float getBlue () {
        return b;
    }
    public int getRGB (int bits) {
        int mask = (1 << bits) - 1;
        return ((int)(r * mask) + ((int)(g * mask) << bits) + ((int)(b * mask) << (2 * bits)));
    }
    public void draw () {
        GL11.glColor3f (r, g, b);
    }
    public boolean equals (Object other) {
        if (other instanceof Color) {
            Color test = (Color)other;
            return (((int)(r * 0x10000) == (int)(test.r * 0x10000)) &&
                    ((int)(g * 0x10000) == (int)(test.g * 0x10000)) &&
                    ((int)(b * 0x10000) == (int)(test.b * 0x10000)));
        }
        else {
            return false;
        }
    }
    public int hashCode () {
        return ((int)(r * 0x10000) ^ ((int)(g * 0x10000) << 4) ^ ((int)(b * 0x10000) << 8));
    }
    public static Color average (Color[] cols) {
        float r = 0.0f, g = 0.0f, b = 0.0f;
        int i;
        for (i = 0;i < cols.length;i++) {
            r += cols[i].r;
            g += cols[i].g;
            b += cols[i].b;
        }
        return (new Color ((r / (double)cols.length), (g / (double)cols.length), (b / (double)cols.length)));
    }
    public static int getRGB (double r, double g, double b, int bits) {
        int mask = (1 << bits) - 1;
        return ((int)(r * mask) + ((int)(g * mask) << bits) + ((int)(b * mask) << (2 * bits)));
    }
    public static Color
            white = new Color (1.0, 1.0, 1.0), black = new Color (0.0, 0.0, 0.0),
            red = new Color (1.0, 0.0, 0.0), orange = new Color (1.0, 0.5, 0.0),
            yellow = new Color (1.0, 1.0, 0.0), green = new Color (0.0, 1.0, 0.0),
            cyan = new Color (0.0, 1.0, 1.0), blue = new Color (0.0, 0.0, 1.0),
            magenta = new Color (1.0, 0.0, 1.0), brown = new Color (0.5, 0.25, 0.0),
            gray = new Color (0.5, 0.5, 0.5), light_gray = new Color (0.75, 0.75, 0.75),
            dark_gray = new Color (0.25, 0.25, 0.25);
}
