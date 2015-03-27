package engine.runtime;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import engine.space.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class EngineTest implements Listener, Renderer, Visible {
    private static final long serialVersionUID = 1L;
    private double angle, angle2, speed, speed2;
    private boolean faster, slower, messier, cleaner;
    private Matrix view;
    private boolean forward, backward, left, right, up, down;
    private double mouse_x, mouse_y;
    private final Color[][][] cols;
    private final Vertex[][][] points;
    public EngineTest () {
        angle = 0.0;
        angle2 = 0.0;
        speed = 90.0;
        speed2 = 15.0;
        faster = slower = messier = cleaner = false;
        view = new Matrix ();
        forward = backward = left = right = up = down = false;
        cols = new Color[2][2][2];
        cols[0][0][0] = Color.red;
        cols[1][0][0] = Color.orange;
        cols[1][1][0] = Color.yellow;
        cols[1][1][1] = Color.green;
        cols[1][0][1] = Color.cyan;
        cols[0][0][1] = Color.blue;
        cols[0][1][1] = new Color (0.5, 0.0, 1.0);
        cols[0][1][0] = Color.magenta;
        points = new Vertex[2][2][2];
        int i, j, k;
        for (i = 0;i < 2;i++) {
            for (j = 0;j < 2;j++) {
                for (k = 0;k < 2;k++) {
                    points[i][j][k] = new Vertex ((2.0 * i - 1.0), (2.0 * j - 1.0), (float)(2.0 * k - 1.0));
                }
            }
        }
    }
    public Renderer getDisplay () {
        return this;
    }
    private void draw (int x, int y, int z) {
        if ((y < 0) && (z < 0)) {
            Color.average ((new Color[] { cols[x][0][0], cols[x][0][1], cols[x][1][0], cols[x][1][1] })).draw ();
            Vertex.midpoint ((new Vertex[] { points[x][0][0], points[x][0][1], points[x][1][0], points[x][1][1] })).draw ();
        }
        else if ((x < 0) && (z < 0)) {
            Color.average ((new Color[] { cols[0][y][0], cols[0][y][1], cols[1][y][0], cols[1][y][1] })).draw ();
            Vertex.midpoint ((new Vertex[] { points[0][y][0], points[0][y][1], points[1][y][0], points[1][y][1] })).draw ();
        }
        else if ((x < 0) && (y < 0)) {
            Color.average ((new Color[] { cols[0][0][z], cols[1][0][z], cols[0][1][z], cols[1][1][z] })).draw ();
            Vertex.midpoint ((new Vertex[] { points[0][0][z], points[1][0][z], points[0][1][z], points[1][1][z] })).draw ();
        }
        else {
            cols[x][y][z].draw ();
            points[x][y][z].draw ();
        }
    }
    public void display () {
        
        view.transform ();
        GL11.glTranslated (0.0, 0.0, -5.0);
        GL11.glRotated (angle2, 1.0, 0.0, 0.0);
        GL11.glRotated (angle, 0.0, 1.0, 0.0);
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (0, -1, -1);
        draw (0, 0, 0);
        draw (0, 0, 1);
        draw (0, 1, 1);
        draw (0, 1, 0);
        draw (0, 0, 0);
        GL11.glEnd ();
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (-1, -1, 0);
        draw (0, 0, 0);
        draw (0, 1, 0);
        draw (1, 1, 0);
        draw (1, 0, 0);
        draw (0, 0, 0);
        GL11.glEnd ();
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (-1, 0, -1);
        draw (0, 0, 0);
        draw (1, 0, 0);
        draw (1, 0, 1);
        draw (0, 0, 1);
        draw (0, 0, 0);
        GL11.glEnd ();
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (1, -1, -1);
        draw (1, 1, 1);
        draw (1, 0, 1);
        draw (1, 0, 0);
        draw (1, 1, 0);
        draw (1, 1, 1);
        GL11.glEnd ();
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (-1, -1, 1);
        draw (1, 1, 1);
        draw (0, 1, 1);
        draw (0, 0, 1);
        draw (1, 0, 1);
        draw (1, 1, 1);
        GL11.glEnd ();
        
        GL11.glBegin (GL11.GL_TRIANGLE_FAN);
        draw (-1, 1, -1);
        draw (1, 1, 1);
        draw (1, 1, 0);
        draw (0, 1, 0);
        draw (0, 1, 1);
        draw (1, 1, 1);
        GL11.glEnd ();
    }
    public double priority () {
        return 0.0;
    }
    public void mouseEvent (int button, boolean state, double x, double y) {
        mouse_x = x;
        mouse_y = y;
    }
    public void keyEvent (int key, boolean state, char letter) {
        switch (key) {
            case Keyboard.KEY_UP: faster = state; break;
            case Keyboard.KEY_DOWN: slower = state; break;
            case Keyboard.KEY_RIGHT: messier = state; break;
            case Keyboard.KEY_LEFT: cleaner = state; break;
            case Keyboard.KEY_W: forward = state; break;
            case Keyboard.KEY_S: backward = state; break;
            case Keyboard.KEY_A: left = state; break;
            case Keyboard.KEY_D: right = state; break;
            case Keyboard.KEY_SPACE: up = state; break;
            case Keyboard.KEY_LSHIFT: down = state; break;
            case Keyboard.KEY_ESCAPE: Multiverse.stop (); break;
        }
    }
    public void loop (double time_rate) {
        if (faster) {
            speed += 30.0 / time_rate;
        }
        if (slower) {
            speed -= 30.0 / time_rate;
        }
        if (speed > 360.0) {
            speed = 360.0;
        }
        if (speed < -360.0) {
            speed = -360.0;
        }
        angle += speed / time_rate;
        while (angle > 360.0) {
            angle -= 360.0;
        }
        while (angle < 0.0) {
            angle += 360.0;
        }
        if (messier) {
            speed2 += 10.0 / time_rate;
        }
        if (cleaner) {
            speed2 -= 10.0 / time_rate;
        }
        if (speed2 > 360.0) {
            speed2 = 360.0;
        }
        if (speed2 < -360.0) {
            speed2 = -360.0;
        }
        angle2 += speed2 / time_rate;
        while (angle2 > 360.0) {
            angle2 -= 360.0;
        }
        while (angle2 < 0.0) {
            angle2 += 360.0;
        }
        if (forward) {
            view = view.translate (new Vertex (0.0, 0.0, (1.0 / time_rate)));
        }
        if (backward) {
            view = view.translate (new Vertex (0.0, 0.0, (-1.0 / time_rate)));
        }
        if (left) {
            view = view.translate (new Vertex ((1.0 / time_rate), 0.0, 0.0));
        }
        if (right) {
            view = view.translate (new Vertex ((-1.0 / time_rate), 0.0, 0.0));
        }
        if (up) {
            view = view.translate (new Vertex (0.0, (-1.0 / time_rate), 0.0));
        }
        if (down) {
            view = view.translate (new Vertex (0.0, (1.0 / time_rate), 0.0));
        }
        view = view.rotate (Quaternion.multiply ((new Quaternion ((new Vertex (0.0, 1.0, 0.0)), (mouse_x / time_rate))),
                (new Quaternion ((new Vertex (1.0, 0.0, 0.0)), (mouse_y / time_rate)))));
    }
    public boolean destroy (double time_rate) {
        return false;
    }
    public static void main (String[] args) {
        Universe world = new Universe (60.0);
        world.add (new EngineTest ());
        Multiverse.verses.add (world);
        try {
            DisplayMode[] modes = Display.getAvailableDisplayModes ();
            int i, best = -1;
            for (i = 0;i < modes.length;i++) {
                if ((best < 0) || (modes[i].getWidth () > modes[best].getWidth ())) {
                    best = i;
                }
            }
            if (best < 0) {
                System.err.println ("no available display modes");
                System.exit (-1);
            }
            Multiverse.initialize ("game engine test", modes[best], Color.black);
        }
        catch (LWJGLException ex) {
            ex.printStackTrace ();
        }
        world.start ();
        Multiverse.run ();
    }
}
