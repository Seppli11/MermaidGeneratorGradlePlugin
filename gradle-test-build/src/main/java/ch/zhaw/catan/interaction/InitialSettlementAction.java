package ch.zhaw.catan.interaction;

import java.awt.Point;
import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build the initial settlement.
 */
public class InitialSettlementAction extends SettlementAction {
    /**
     * A value indicating whether resources should be paid out after executing the
     * action.
     */
    private boolean payout;


    /**
     * Initializes a new instance of the {@link InitialSettlementAction} class.
     * 
     * @param game   The game this action belongs to.
     * @param payout A value indicating whether resources should be paid out after
     *               executing the action.
     */
    public InitialSettlementAction(SiedlerGame game, boolean payout) {
        super(game);
        this.payout = payout;
    }

    /**
     * Gets a value indicating whether resources should be paid out after executing
     * the
     * action.
     *
     * @return A value indicating whether resources should be paid out after
     *         executing the
     *         action.
     */
    public boolean getPayout() {
        return payout;
    }

    @Override
    protected boolean buildStructure(Point coordinate) {
        return getGame().placeInitialSettlement(coordinate, getPayout());
    }
}
