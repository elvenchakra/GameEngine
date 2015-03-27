package engine.runtime;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Substance {
    public boolean isInstance (Matter test);
    public boolean isSubtype (Substance test);
    public default boolean inPhase (double time_rate) {
        return true;
    }
}
