package engine.runtime;

/**
 *
 * @author Kassandra Kaeck
 */
public interface Visible extends Energy {
    /**
     * Renderer objects returned by this method
     * may only have access to their own fields,
     * the fields of other Renderer objects
     * created from within the same Universe,
     * immutable fields, static final fields,
     * and static, client side maintained fields.
     */
    public Renderer getDisplay ();
}
