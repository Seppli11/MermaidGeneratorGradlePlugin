package ch.zhaw.catan.model;

/**
 * This class represents a city and how many victory points it rewards.
 * 
 * @author Jonas Costa
 */
public class SiedlerCity extends SiedlerSettlement {
    /**
     * Specifies the amount of victory points this structure rewards.
     */
    private static final int VICTORY_POINTS = 2;

    /**
     * Specifies the amount of resource card this structure rewards.
     */
    private static final int RESOURCE_COUNT = 2;

    /**
     * Creates a new city.
     * 
     * @param owner Who owns this city.
     */
    public SiedlerCity(Player owner) {
        super(owner);
    }

    /**
     * Get the amount of victory points this structure rewards.
     * 
     * @returns The amount of victory points this structure rewards.
     */
    @Override
    public int getVictoryPoints() {
        return VICTORY_POINTS;
    }

    /**
     * Get the amount of resource cards this structure rewards.
     * 
     * @returns The amount of resource cards this structure rewards.
     */
    @Override
    public int getResourceCount() {
        return RESOURCE_COUNT;
    }

}
