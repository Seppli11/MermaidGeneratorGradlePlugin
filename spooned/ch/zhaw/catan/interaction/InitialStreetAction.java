package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to build the initial street.
 */
public class InitialStreetAction extends ch.zhaw.catan.interaction.StreetAction {
    /**
     * Initializes a new instance of the {@link InitialStreetAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public InitialStreetAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    protected boolean buildStructure(ch.zhaw.catan.interaction.StreetAction.StreetInfo info) {
        return getGame().placeInitialRoad(info.getStart(), info.getEnd());
    }
}