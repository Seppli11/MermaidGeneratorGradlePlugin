package ch.zhaw.catan.model;
/**
 * This class represents a settlement and how many victory points it rewards.
 *
 * @author Jonas Costa
 */
public class SiedlerSettlement extends ch.zhaw.catan.model.SiedlerStructure {
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
     * @param owner
     * 		Who owns this city.
     */
    public SiedlerSettlement(ch.zhaw.catan.model.Player owner) {
        this(owner, ch.zhaw.catan.Config.Structure.SETTLEMENT);
    }

    /**
     * Creates a new city.
     *
     * @param owner
     * 		Who owns this city.
     * @param structure
     * 		The kind of the structure.
     */
    public SiedlerSettlement(ch.zhaw.catan.model.Player owner, ch.zhaw.catan.Config.Structure structure) {
        super(owner, structure);
    }

    /**
     * Get the amount of victory points this structure rewards.
     *
     * @returns The amount of victory points this structure rewards.
     */
    public int getVictoryPoints() {
        return ch.zhaw.catan.model.SiedlerSettlement.VICTORY_POINTS;
    }

    /**
     * Get the amount of resource cards this structure rewards.
     *
     * @returns The amount of resource cards this structure rewards.
     */
    public int getResourceCount() {
        return ch.zhaw.catan.model.SiedlerSettlement.RESOURCE_COUNT;
    }
}