package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to build a building.
 */
public abstract class BuildingAction extends ch.zhaw.catan.interaction.StructureAction<java.awt.Point> {
    /**
     * Initializes a new instance of the {@link BuildingAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    protected BuildingAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    /**
     * Gets the coordinate to place the building on.
     */
    @java.lang.Override
    protected java.awt.Point getStructureInfo() {
        return fetchCoordinate();
    }

    /**
     * Builds the specified structure.
     *
     * @param coordinate
     * 		The coordinate to place the building on.
     * @return A value indicating whether the action was successful.
     */
    protected abstract boolean buildStructure(java.awt.Point coordinate);
}