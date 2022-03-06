package ch.zhaw.catan.interaction;

import java.awt.Point;
import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build a settlement.
 */
public class SettlementAction extends BuildingAction {
    /**
     * Initializes a new instance of the {@link SettlementAction} class.
     *
     * @param game The game this action belongs to.
     */
    public SettlementAction(SiedlerGame game) {
        super(game);
    }

    @Override
    protected String getHeading() {
        return "Building a settlement";
    }

    @Override
    protected boolean buildStructure(Point coordinate) {
        return getGame().buildSettlement(coordinate);
    }
}
