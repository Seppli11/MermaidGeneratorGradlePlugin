package ch.zhaw.catan.model;
/**
 * Represents a player
 *
 * @author Sebastian Zumbrunn
 */
public class Player extends ch.zhaw.catan.model.ResourceHolder {
    /**
     * the faction of the player
     */
    private ch.zhaw.catan.Config.Faction faction;

    /**
     * Constructor
     *
     * @param faction
     * 		the faction of the player
     */
    public Player(ch.zhaw.catan.Config.Faction faction) {
        this.faction = faction;
    }

    /**
     * Constructor
     *
     * @param faction
     * 		the faction of the player
     * @param resources
     * 		the initial inventory of the player
     */
    public Player(ch.zhaw.catan.Config.Faction faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> resources) {
        super(resources);
        this.faction = faction;
    }

    /**
     *
     *
     * @return the faction
     */
    public ch.zhaw.catan.Config.Faction getFaction() {
        return faction;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        return (prime * super.hashCode()) + java.util.Objects.hash(faction);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!super.equals(obj))
            return false;

        if (!(obj instanceof ch.zhaw.catan.model.Player))
            return false;

        ch.zhaw.catan.model.Player other = ((ch.zhaw.catan.model.Player) (obj));
        return faction == other.faction;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Player [ faction=" + faction) + ", inventory=") + getResourceMap()) + "]";
    }
}