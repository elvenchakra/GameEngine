package engine.runtime;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import gnu.trove.set.hash.THashSet;
import gnu.trove.map.hash.THashMap;
import java.util.concurrent.TimeUnit;
import org.lwjgl.opengl.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class Universe {
    private final THashSet<Energy> ideas;
    private final THashSet<Listener> listeners;
    private final THashMap<Substance,THashSet<Matter>> stuff;
    private final THashSet<Visible> drawn;
    private double time_rate;
    private boolean running, displaying;
    private final ForkJoinPool threads;
    public Universe (double time_rate) {
        ideas = new THashSet<> ();
        listeners = new THashSet<> ();
        stuff = new THashMap<> ();
        drawn = new THashSet<> ();
        this.time_rate = time_rate;
        running = false;
        displaying = true;
        threads = new ForkJoinPool ();
    }
    public synchronized void setTimeRate (double rate) {
        time_rate = rate;
    }
    public synchronized double getTimeRate () {
        return time_rate;
    }
    private void addImpl (Energy next) {
        ideas.add (next);
        if (next instanceof Matter) {
            Matter thing = (Matter)next;
            Substance type = thing.getSubstance ();
            THashSet<Matter> things = stuff.get (type);
            if (things == null) {
                things = new THashSet<> ();
                stuff.put (type, things);
            }
            things.add (thing);
        }
        if (next instanceof Listener) {
            listeners.add ((Listener)next);
        }
        if (next instanceof Visible) {
            drawn.add ((Visible)next);
        }
    }
    public synchronized void add (Energy next) {
        if (running) {
            throw (new IllegalStateException ("use method Energy.create to add new objects while the Universe is running"));
        }
        addImpl (next);
    }
    /**
     * starts a new thread
     */
    public synchronized void start () {
        if (running == true) {
            throw (new IllegalStateException ("already running"));
        }
        running = true;
        new Thread (this::run).start ();
    }
    public synchronized void stop () {
        running = false;
    }
    public synchronized void setVisible (boolean visible) {
        displaying = visible;
    }
    public synchronized boolean isRunning () {
        return running;
    }
    public synchronized boolean isVisible () {
        return displaying;
    }
    protected synchronized void loop (double time_rate) {
        THashSet<Energy> create = new THashSet<> ();
        ideas.forEach ((Energy n) -> create.addAll (n.create (time_rate)));
        create.forEach ((Energy n) -> addImpl (n));
        ideas.forEach ((Energy n) -> n.loop (time_rate));
        stuff.entrySet ().stream ().filter ((e) -> e.getKey ().inPhase (time_rate)).forEach ((e) -> {
            e.getValue ().forEach ((n1) -> {
                if (n1.inPhase (time_rate)) {
                    threads.submit (() -> {
                        stuff.forEachEntry ((k, v) -> {
                            if (n1.inPhase (k)) {
                                v.forEach ((Matter n2) -> n1.collide (n2, time_rate));
                            }
                            return true;
                        });
                    });
                }
            });
        });
        int num = 0;
        while (!threads.awaitQuiescence (2L, TimeUnit.SECONDS)) {
            num++;
            if (num >= 15) {
                running = false;
                threads.shutdown ();
                return;
            }
            try {
                wait (100L);
            }
            catch (InterruptedException ex) {}
        }
        Iterator<Energy> iter = ideas.iterator ();
        Energy next;
        while (iter.hasNext ()) {
            next = iter.next ();
            if (next.destroy (time_rate)) {
                iter.remove ();
                if (next instanceof Matter) {
                    stuff.get (((Matter)next).getSubstance ()).remove ((Matter)next);
                }
                if (next instanceof Listener) {
                    listeners.remove ((Listener)next);
                }
                if (next instanceof Visible) {
                    drawn.remove ((Visible)next);
                }
            }
        }
    }
    public void display () {
        if (isVisible ()) {
            TreeSet<Renderer> temp = new TreeSet<> (Universe::compare);
            synchronized (this) {
                drawn.forEach ((Visible n) -> temp.add (n.getDisplay ()));
            }
            GL11.glMatrixMode (GL11.GL_MODELVIEW);
            GL11.glLoadIdentity ();
            for (Renderer next : temp) {
                next.display ();
            }
        }
    }
    public synchronized void mouseEvent (int button, boolean state, double x, double y) {
        listeners.forEach ((Listener n) -> n.mouseEvent (button, state, x, y));
    }
    public synchronized void keyEvent (int key, boolean state, char letter) {
        listeners.forEach ((Listener n) -> n.keyEvent (key, state, letter));
    }
    private void run () {
        long time = System.nanoTime ();
        long step;
        while (running) {
            synchronized (this) {
                step = (long)(1000000000.0 / time_rate);
            }
            if ((time + step - System.nanoTime ()) >= 1000000L) {
                Display.sync ((int)(1000000000L / (time + step - System.nanoTime ())) + 1);
            }
            loop (time_rate);
            time += step;
        }
    }
    public static int compare (Renderer one, Renderer two) {
        if (one.equals (two)) {
            return 0;
        }
        if (one.priority () > two.priority ()) {
            return -1;
        }
        if (one.priority () < two.priority ()) {
            return 1;
        }
        if (one.hashCode () < two.hashCode ()) {
            return -1;
        }
        if (one.hashCode () > two.hashCode ()) {
            return 1;
        }
        if (System.identityHashCode (one) < System.identityHashCode (two)) {
            return -1;
        }
        if (System.identityHashCode (one) > System.identityHashCode (two)) {
            return 1;
        }
        return 0;
    }
}
