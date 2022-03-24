package ch.zhaw.catan.model;
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
    private ch.zhaw.catan.model.Player owner;

    /**
     * The kind of the structure.
     */
    private ch.zhaw.catan.Config.Structure structure;

    /**
     * Creates a new structure.
     *
     * @param owner
     * 		Who owns this structure.
     * @param structure
     * 		The kind of the structure.
     */
    public SiedlerStructure(ch.zhaw.catan.model.Player owner, ch.zhaw.catan.Config.Structure structure) {
        this.owner = owner;
        this.structure = structure;
    }

    /**
     * Get this structure's owner.
     *
     * @return Which {@link Player} owns this structure.
     */
    public ch.zhaw.catan.model.Player getOwner() {
        return owner;
    }

    /**
     * Get the kind of this structure.
     *
     * @return The kind of the structure.
     */
    public ch.zhaw.catan.Config.Structure getStructure() {
        return structure;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(owner);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ch.zhaw.catan.model.SiedlerStructure)) {
            return false;
        }
        ch.zhaw.catan.model.SiedlerStructure other = ((ch.zhaw.catan.model.SiedlerStructure) (obj));
        return java.util.Objects.equals(owner, other.owner);
    }
}