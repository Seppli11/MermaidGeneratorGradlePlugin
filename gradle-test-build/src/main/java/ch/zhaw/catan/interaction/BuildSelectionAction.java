package ch.zhaw.catan.interaction;

import ch.zhaw.catan.SiedlerGame;
import ch.zhaw.catan.Config.Structure;

/**
 * Provides the functionality to prompt the user to choose from a structure to
 * build.
 */
public class BuildSelectionAction extends Action {
    /**
     * Initializes a new instance of the {@link BuildSelectionAction} class.
     * 
     * @param game The game this action belongs to.
     */
    public BuildSelectionAction(SiedlerGame game) {
        super(game);
    }

    @Override
    public boolean execute() {
        Structure structureKind = getIO().newEnumInputReader(Structure.class)
                .read("What kind of building do you want to buy?");

        return switch (structureKind) {
            case SETTLEMENT -> new SettlementAction(getGame()).execute();
            case ROAD -> new StreetAction(getGame()).execute();
            case CITY -> new CityAction(getGame()).execute();
            default -> false;
        };
    }
}
