package ch.zhaw.catan.interaction;

import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build the initial street.
 */
public class InitialStreetAction extends StreetAction {
    /**
     * Initializes a new instance of the {@link InitialStreetAction} class.
     *
     * @param game The game this action belongs to.
     */
    public InitialStreetAction(SiedlerGame game) {
        super(game);
    }

    @Override
    protected boolean buildStructure(StreetInfo info) {
        return getGame().placeInitialRoad(info.getStart(), info.getEnd());
    }
}
