package ch.zhaw.catan.model;
/**
 * This class represents a city and how many victory points it rewards.
 *
 * @author Jonas Costa
 */
public class SiedlerCity extends ch.zhaw.catan.model.SiedlerSettlement {
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
     * @param owner
     * 		Who owns this city.
     */
    public SiedlerCity(ch.zhaw.catan.model.Player owner) {
        super(owner);
    }

    /**
     * Get the amount of victory points this structure rewards.
     *
     * @returns The amount of victory points this structure rewards.
     */
    @java.lang.Override
    public int getVictoryPoints() {
        return ch.zhaw.catan.model.SiedlerCity.VICTORY_POINTS;
    }

    /**
     * Get the amount of resource cards this structure rewards.
     *
     * @returns The amount of resource cards this structure rewards.
     */
    @java.lang.Override
    public int getResourceCount() {
        return ch.zhaw.catan.model.SiedlerCity.RESOURCE_COUNT;
    }
}