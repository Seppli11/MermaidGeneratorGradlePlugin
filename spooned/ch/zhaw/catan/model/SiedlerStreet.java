package ch.zhaw.catan.model;
/**
 * This class represents a street.
 *
 * @author Jonas Costa
 */
public class SiedlerStreet extends ch.zhaw.catan.model.SiedlerStructure {
    /**
     * Creates a new street object.
     *
     * @param owner
     * 		Who owns this streets.
     */
    public SiedlerStreet(ch.zhaw.catan.model.Player owner) {
        super(owner, ch.zhaw.catan.Config.Structure.ROAD);
    }
}