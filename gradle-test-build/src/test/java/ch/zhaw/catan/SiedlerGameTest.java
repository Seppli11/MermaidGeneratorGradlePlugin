package ch.zhaw.catan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.games.ThreePlayerStandard;
import ch.zhaw.catan.model.Player;

/***
 * Note: Have a look at {@link ch.zhaw.catan.games.ThreePlayerStandard}. It can
 * be used to get several different game states.
 *
 */
public class SiedlerGameTest {

    /**
     * Tests whether placing the thief is successful for valid fields and is
     * unsuccessful for coordinates which aren't fields or are water
     */
    @Test
    void testPlacingThief() {
        SiedlerGame model = new SiedlerGame(3, 4);
        assertEquals(Config.INITIAL_THIEF_POSITION, model.getThiefPosition());

        // tries to move the thief out of bounds
        assertFalse(model.placeThiefAndStealCard(new Point(-1, -1)));
        assertEquals(Config.INITIAL_THIEF_POSITION, model.getThiefPosition());

        // tries to move the thief out of bounds
        assertFalse(model.placeThiefAndStealCard(new Point(20, 20)));
        assertEquals(Config.INITIAL_THIEF_POSITION, model.getThiefPosition());

        // tries to move the thief to an invalid position
        assertFalse(model.placeThiefAndStealCard(new Point(2, 0)));
        assertEquals(Config.INITIAL_THIEF_POSITION, model.getThiefPosition());

        // tries to move the thief to a water field
        assertFalse(model.placeThiefAndStealCard(new Point(4, 2)));
        assertEquals(Config.INITIAL_THIEF_POSITION, model.getThiefPosition());

        // tries to move the thief to a valid field
        assertTrue(model.placeThiefAndStealCard(new Point(5, 5)));
        assertEquals(new Point(5, 5), model.getThiefPosition());
    }

    /**
     * Tests if a random card from one player is stolen when the thief is placed.
     * To accomplish this, the inventory of all players is cleared and red's
     * inventory has only one brick in it.
     * This ensures that this brick is stolen from red player.
     */
    @Test
    void testStealRandomCardWhenPlacingThief() {
        SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(4);
        assertEquals(Faction.RED, model.getCurrentPlayerFaction());
        // make sure, that red only has brick, so that the thief has to steal brick
        clearAllInventories(model);
        Player redPlayer = model.getCurrentPlayer();
        redPlayer.setResource(Resource.BRICK, 1);
        // switch players so red doesn't steal from himself
        model.switchToNextPlayer();
        // do the actual stealing
        assertTrue(model.placeThiefAndStealCard(new Point(5, 5)));
        // the thief should steal one brick as this is
        checkAllInventoriesEmptyExcept(model, model.getCurrentPlayer());
        assertEquals(1, model.getCurrentPlayerResourceStock(Resource.BRICK));
    }

    /**
     * Tests whether no error would occur if non of the player had anything in
     * their inventory
     */
    @Test
    void testStealRandomCardWhenEmptyInventory() {
        SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(4);
        clearAllInventories(model);

        // switch players so red doesn't steel from himself
        model.switchToNextPlayer();
        // do the actual stealing
        assertTrue(model.placeThiefAndStealCard(new Point(5, 5)));
        // the thief should steal one brick as this is
        checkAllInventoriesEmptyExcept(model, null);
    }

    /**
     * Tests, that the field on which the thief is standing, isn't payed out.
     * For this all inventories are cleared to make the checking easier
     */
    @Test
    void testPaymentOfFieldWithThief() {
        SiedlerGame model = ThreePlayerStandard.getAfterSetupPhase(4);
        clearAllInventories(model);
        // place thief on 5,5
        assertTrue(model.placeThiefAndStealCard(new Point(5, 5)));
        // try to payout the field on which the thief is standing
        int diceValue = model.getBoard().getField(model.getThiefPosition()).getDiceValue();
        model.throwDice(diceValue);
        // All inventories should still be empty, as the thief prevents the payout
        checkAllInventoriesEmptyExcept(model, null);
    }

    /**
     * Helper method, which clears the inventories of all players
     * 
     * @param model the siedler game
     */
    private void clearAllInventories(SiedlerGame model) {
        Player startPlayer = model.getCurrentPlayer();
        do {
            for (Resource resource : Resource.values()) {
                model.getCurrentPlayer().setResource(resource, 0);
            }
            model.switchToNextPlayer();
            // switch to next player until start player is reached again
        } while (startPlayer != model.getCurrentPlayer());
    }

    /**
     * A helper method, which checks, whether all inventories are still empty except
     * one player.
     * 
     * @param model          the siedler game
     * @param nonEmptyPlayer the player with a non empty inventory or null if no
     *                       player should have an empty inventory
     */
    private void checkAllInventoriesEmptyExcept(SiedlerGame model, Player nonEmptyPlayer) {
        Player startPlayer = model.getCurrentPlayer();
        do {
            if (!model.getCurrentPlayer().equals(nonEmptyPlayer)) {
                assertEquals(0, model.getCurrentPlayer().getResourceArray().length,
                        "Player \"" + model.getCurrentPlayerFaction() + "\" has a non empty inventory");
            }
            model.switchToNextPlayer();
            // switch to next player until start player is reached again
        } while (startPlayer != model.getCurrentPlayer());

    }

}