package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to prompt the user to choose from a structure to
 * build.
 */
public class BuildSelectionAction extends ch.zhaw.catan.interaction.Action {
    /**
     * Initializes a new instance of the {@link BuildSelectionAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public BuildSelectionAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    public boolean execute() {
        ch.zhaw.catan.Config.Structure structureKind = getIO().newEnumInputReader(ch.zhaw.catan.Config.Structure.class).read("What kind of building do you want to buy?");
        return switch (structureKind) {
            case SETTLEMENT ->
                new ch.zhaw.catan.interaction.SettlementAction(getGame()).execute();
            case ROAD ->
                new ch.zhaw.catan.interaction.StreetAction(getGame()).execute();
            case CITY ->
                new ch.zhaw.catan.interaction.CityAction(getGame()).execute();
            default ->
                false;
        };
    }
}