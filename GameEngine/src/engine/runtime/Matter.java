package engine.runtime;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Matter extends Energy {
    public Substance getSubstance ();
    /**
     * all access to mutable fields  (of any object) must be synchronized
     */
    public default boolean inPhase (double time_rate) {
        return true;
    }
    /**
     * all access to mutable fields  (of any object) must be synchronized
     */
    public boolean inPhase (Substance type);
    /**
     * all access to mutable fields  (of any object) must be synchronized
     */
    public void collide (Matter other, double time_rate);
}
