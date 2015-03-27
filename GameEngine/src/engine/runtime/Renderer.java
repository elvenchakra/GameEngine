package engine.runtime;
import java.io.Serializable;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Renderer extends Serializable {
    public void display ();
    public default double priority () {
        return 0.0;
    }
}
