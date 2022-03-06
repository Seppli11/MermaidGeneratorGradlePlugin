package ch.zhaw.catan.model;

import ch.zhaw.catan.Config.Structure;

/**
 * This class represents a street.
 * 
 * @author Jonas Costa
 */
public class SiedlerStreet extends SiedlerStructure {
    /**
     * Creates a new street object.
     * 
     * @param owner Who owns this streets.
     */
    public SiedlerStreet(Player owner) {
        super(owner, Structure.ROAD);
    }
}
