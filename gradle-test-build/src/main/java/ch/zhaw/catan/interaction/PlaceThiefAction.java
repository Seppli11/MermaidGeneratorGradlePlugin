package ch.zhaw.catan.interaction;

import ch.zhaw.catan.SiedlerGame;
import java.awt.Point;

/**
 * Asks the user for a coordinate where to place the thief. If valid, the thief
 * is placed and a random card from a random players with settlements or cities
 * around
 * the field is stolen.
 */
public class PlaceThiefAction extends BuildingAction {

    /**
     * Constructor
     * 
     * @param game the game
     */
    public PlaceThiefAction(SiedlerGame game) {
        super(game);
    }

    @Override
    protected boolean buildStructure(Point info) {
        try {
            boolean result = getGame().placeThiefAndStealCard(info);

            if (result) {
                getIO().getTextTerminal().printf("The thief was moved to %d/%d\n", getGame().getThiefPosition().getX(),
                        getGame().getThiefPosition().getY());
            } else {
                getIO().getTextTerminal().printf("Could not move the thief to %d/%d\n",
                        getGame().getThiefPosition().getX(),
                        getGame().getThiefPosition().getY());
            }

            return result;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    protected String getHeading() {
        return "A seven was rolled. Choose carefully where the mighty thief should move next!";
    }

}
