package ch.zhaw.catan.interaction;

import java.awt.Point;

import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build a building.
 */
public abstract class BuildingAction extends StructureAction<Point> {
    /**
     * Initializes a new instance of the {@link BuildingAction} class.
     *
     * @param game The game this action belongs to.
     */
    protected BuildingAction(SiedlerGame game) {
        super(game);
    }

    /**
     * Gets the coordinate to place the building on.
     */
    @Override
    protected Point getStructureInfo() {
        return fetchCoordinate();
    }

    /**
     * Builds the specified structure.
     *
     * @param coordinate The coordinate to place the building on.
     *
     * @return A value indicating whether the action was successful.
     */
    protected abstract boolean buildStructure(Point coordinate);
}
