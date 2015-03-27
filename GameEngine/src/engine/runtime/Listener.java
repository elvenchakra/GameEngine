package engine.runtime;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Listener extends Energy {
    public void mouseEvent (int button, boolean state, double x, double y);
    public void keyEvent (int key, boolean state, char letter);
}
