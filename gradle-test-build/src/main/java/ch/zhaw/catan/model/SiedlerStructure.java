package ch.zhaw.catan.model;

import java.util.Objects;

import ch.zhaw.catan.Config.Structure;

/**
 * This abstract class contains basic functions and fields for all types of
 * structures.
 * 
 * @author Jonas Costa
 */
public abstract class SiedlerStructure {
    /**
     * Specifies the owner of this structure.
     */
    private Player owner;

    /**
     * The kind of the structure.
     */
    private Structure structure;

    /**
     * Creates a new structure.
     * 
     * @param owner     Who owns this structure.
     * @param structure The kind of the structure.
     */
    public SiedlerStructure(Player owner, Structure structure) {
        this.owner = owner;
        this.structure = structure;
    }

    /**
     * Get this structure's owner.
     * 
     * @return Which {@link Player} owns this structure.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Get the kind of this structure.
     * 
     * @return The kind of the structure.
     */
    public Structure getStructure() {
        return structure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SiedlerStructure)) {
            return false;
        }

        SiedlerStructure other = (SiedlerStructure) obj;
        return Objects.equals(owner, other.owner);
    }

}
