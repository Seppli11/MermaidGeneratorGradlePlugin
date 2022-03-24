package ch.zhaw.catan.interaction;
/**
 * Asks the user for a coordinate where to place the thief. If valid, the thief
 * is placed and a random card from a random players with settlements or cities
 * around
 * the field is stolen.
 */
public class PlaceThiefAction extends ch.zhaw.catan.interaction.BuildingAction {
    /**
     * Constructor
     *
     * @param game
     * 		the game
     */
    public PlaceThiefAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    protected boolean buildStructure(java.awt.Point info) {
        try {
            boolean result = getGame().placeThiefAndStealCard(info);
            if (result) {
                getIO().getTextTerminal().printf("The thief was moved to %d/%d\n", getGame().getThiefPosition().getX(), getGame().getThiefPosition().getY());
            } else {
                getIO().getTextTerminal().printf("Could not move the thief to %d/%d\n", getGame().getThiefPosition().getX(), getGame().getThiefPosition().getY());
            }
            return result;
        } catch (java.lang.IllegalArgumentException e) {
            return false;
        }
    }

    @java.lang.Override
    protected java.lang.String getHeading() {
        return "A seven was rolled. Choose carefully where the mighty thief should move next!";
    }
}