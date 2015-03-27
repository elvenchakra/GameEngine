package engine.runtime;

/**
 *
 * @author Kassandra Kaeck
 */
public class Phenomena implements Substance {
    protected Phenomena () {}
    public Substance getSubstance () {
        return basic;
    }
    public boolean isInstance (Matter test) {
        return (test.getSubstance () instanceof Phenomena);
    }
    public boolean isSubtype (Substance type) {
        return ((this != basic) && (type == basic));
    }
    public boolean inPhase (double time_rate) {
        return on;
    }
    protected static final Phenomena basic = new Phenomena ();
    public static volatile boolean on = true;
}
