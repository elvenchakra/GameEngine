package engine.runtime;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Kassandra Kaeck
 */
public class Phantom implements Energy, Visible {
    private Renderer render;
    public Phantom (Renderer renderer) {
        render = renderer;
    }
    public Renderer getDisplay () {
        return render;
    }
    public void loop (double time_rate) {
        if (render instanceof Energy) {
            ((Energy)render).loop (time_rate);
        }
    }
}
