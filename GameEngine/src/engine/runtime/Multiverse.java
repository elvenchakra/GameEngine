package engine.runtime;
import java.util.ArrayList;
import gnu.trove.set.hash.THashSet;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import engine.space.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class Multiverse {
    private Multiverse () {}
    private static double aspect = -1.0;
    private static boolean running = false;
    private static boolean alt = false;
    private static void loop () {
        THashSet<Universe> iter;
        while (Keyboard.next ()) {
            final int key = Keyboard.getEventKey ();
            final boolean state = Keyboard.getEventKeyState ();
            final char letter = Keyboard.getEventCharacter ();
            synchronized (verses) {
                iter = new THashSet<> (verses);
            }
            iter.forEach ((Universe n) -> n.keyEvent (key, state, letter));
            if (key == Keyboard.KEY_LMENU) {
                alt = state;
            }
            if ((key == Keyboard.KEY_F4) && alt && state) {
                stop ();
            }
        }
        double width = 0.5 * Display.getWidth (), height = 0.5 * Display.getHeight ();
        synchronized (verses) {
            aspect = width / height;
        }
        while (Mouse.next ()) {
            final int button = Mouse.getEventButton ();
            final boolean state = Mouse.getEventButtonState ();
            final double x = Mouse.getEventX () / height - aspect;
            final double y = Mouse.getEventY () / height - 1.0;
            synchronized (verses) {
                iter = new THashSet<> (verses);
            }
            iter.forEach ((Universe n) -> n.mouseEvent (button, state, x, y));
        }
    }
    /**
     * must have called initialize
     */
    public static double getAspectRatio () {
        synchronized (verses) {
            if (aspect < 0.0) {
                throw (new IllegalStateException ("not initialized"));
            }
            else {
                return aspect;
            }
        }
    }
    /**
     * shade model = smooth
     * front face = clockwise
     * culls back faces
     * uses the whole screen
     * perspective projection
     */
    public static void initialize (String title, DisplayMode mode, Color background) throws LWJGLException {
        Display.setTitle (title);
        Display.setDisplayMode (mode);
        Display.setFullscreen (true);
        Display.setVSyncEnabled (false);
        Display.setInitialBackground (background.getRed (), background.getGreen (), background.getBlue ());
        Display.create ();
        GL11.glShadeModel (GL11.GL_SMOOTH);
        GL11.glCullFace (GL11.GL_BACK);
        GL11.glFrontFace (GL11.GL_CCW);
        GL11.glEnable (GL11.GL_CULL_FACE);
        GL11.glEnable (GL11.GL_DEPTH_TEST);
        int width = Display.getWidth (), height = Display.getHeight ();
        GL11.glViewport (Display.getX (), Display.getY (), width, height);
        GL11.glMatrixMode (GL11.GL_PROJECTION);
        GL11.glLoadIdentity ();
        aspect = (double)width / (double)height;
        GL11.glFrustum (-aspect, aspect, -1.0, 1.0, 1.0, 1000.0);
    }
    /**
     * blocks the current thread, so call last
     */
    public static void run () {
        if (running == true) {
            throw (new IllegalStateException ("alread running the multiverse"));
        }
        running = true;
        THashSet<Universe> iter;
        while (running) {
            loop ();
            GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            synchronized (verses) {
                iter = new THashSet<> (verses);
            }
            iter.forEach ((Universe n) -> n.display ());
            Display.update ();
            if (Display.isCloseRequested ()) {
                running = false;
            }
        }
        Display.destroy ();
        System.exit (0);
    }
    public static void stop () {
        synchronized (verses) {
            running = false;
        }
    }
    /**
     * always synchronize on this field before modifying it
     */
    public static final ArrayList<Universe> verses = new ArrayList<> ();
}