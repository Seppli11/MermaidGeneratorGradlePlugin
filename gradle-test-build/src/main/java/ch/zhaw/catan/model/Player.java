package ch.zhaw.catan.model;

import java.util.Map;
import java.util.Objects;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

/**
 * Represents a player
 * 
 * @author Sebastian Zumbrunn
 */
public class Player extends ResourceHolder {
    /**
     * the faction of the player
     */
    private Faction faction;

    /**
     * Constructor
     * 
     * @param faction the faction of the player
     */
    public Player(Faction faction) {
        this.faction = faction;
    }

    /**
     * Constructor
     * 
     * @param faction   the faction of the player
     * @param resources the initial inventory of the player
     */
    public Player(Faction faction, Map<Resource, Integer> resources) {
        super(resources);
        this.faction = faction;
    }

    /**
     * @return the faction
     */
    public Faction getFaction() {
        return faction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * super.hashCode() + Objects.hash(faction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        return faction == other.faction;
    }

    @Override
    public String toString() {
        return "Player [ faction=" + faction + ", inventory=" + getResourceMap() + "]";
    }
}
