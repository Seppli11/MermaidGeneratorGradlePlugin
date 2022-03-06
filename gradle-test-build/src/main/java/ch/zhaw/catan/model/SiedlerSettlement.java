package ch.zhaw.catan.model;

import ch.zhaw.catan.Config.Structure;

/**
 * This class represents a settlement and how many victory points it rewards.
 * 
 * @author Jonas Costa
 */
public class SiedlerSettlement extends SiedlerStructure {
    /**
     * Specifies the amount of victory points this structure rewards.
     */
    private static final int VICTORY_POINTS = 1;

    /**
     * Specifies the amount of resource card this structure rewards.
     */
    private static final int RESOURCE_COUNT = 1;

    /**
     * Creates a new city.
     * 
     * @param owner Who owns this city.
     */
    public SiedlerSettlement(Player owner) {
        this(owner, Structure.SETTLEMENT);
    }

    /**
     * Creates a new city.
     * 
     * @param owner     Who owns this city.
     * @param structure The kind of the structure.
     */
    public SiedlerSettlement(Player owner, Structure structure) {
        super(owner, structure);
    }

    /**
     * Get the amount of victory points this structure rewards.
     * 
     * @returns The amount of victory points this structure rewards.
     */
    public int getVictoryPoints() {
        return VICTORY_POINTS;
    }

    /**
     * Get the amount of resource cards this structure rewards.
     * 
     * @returns The amount of resource cards this structure rewards.
     */
    public int getResourceCount() {
        return RESOURCE_COUNT;
    }
}
