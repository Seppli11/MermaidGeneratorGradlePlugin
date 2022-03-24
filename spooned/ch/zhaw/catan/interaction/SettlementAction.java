package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to build a settlement.
 */
public class SettlementAction extends ch.zhaw.catan.interaction.BuildingAction {
    /**
     * Initializes a new instance of the {@link SettlementAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public SettlementAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    protected java.lang.String getHeading() {
        return "Building a settlement";
    }

    @java.lang.Override
    protected boolean buildStructure(java.awt.Point coordinate) {
        return getGame().buildSettlement(coordinate);
    }
}