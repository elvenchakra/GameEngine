package engine.runtime;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Energy {
    public void loop (double time_rate);
    public default Collection<Energy> create (double time_rate) {
        return Collections.<Energy>emptyList ();
    }
    public default boolean destroy (double time_rate) {
        return  false;
    }
}
