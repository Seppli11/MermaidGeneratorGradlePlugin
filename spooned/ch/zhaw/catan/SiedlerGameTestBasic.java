package ch.zhaw.catan;
import java.util.List;
import java.util.Map;
import java.awt.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
// TODO revert this class
/**
 * This class contains some basic tests for the {@link SiedlerGame} class
 * <p>
 * </p>
 * <p>
 * DO NOT MODIFY THIS CLASS
 * </p>
 *
 * @author tebe
 */
public class SiedlerGameTestBasic {
    private static final int DEFAULT_WINPOINTS = 5;

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 3;

    /**
     * Tests whether the functionality for switching to the next/previous player
     * works as expected for different numbers of players.
     *
     * @param numberOfPlayers
     * 		the number of players
     */
    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(ints = { 2, 3, 4 })
    public void requirementPlayerSwitching(int numberOfPlayers) {
        ch.zhaw.catan.SiedlerGame model = new ch.zhaw.catan.SiedlerGame(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS, numberOfPlayers);
        assertTrue(numberOfPlayers == model.getPlayerFactions().size(), "Wrong number of players returned by getPlayers()");
        // Switching forward
        for (int i = 0; i < numberOfPlayers; i++) {
            assertEquals(ch.zhaw.catan.Config.Faction.values()[i], model.getCurrentPlayerFaction(), "Player order does not match order of Faction.values()");
            model.switchToNextPlayer();
        }
        assertEquals(ch.zhaw.catan.Config.Faction.values()[0], model.getCurrentPlayerFaction(), "Player wrap-around from last player to first player did not work.");
        // Switching backward
        for (int i = numberOfPlayers - 1; i >= 0; i--) {
            model.switchToPreviousPlayer();
            assertEquals(ch.zhaw.catan.Config.Faction.values()[i], model.getCurrentPlayerFaction(), "Switching players in reverse order does not work as expected.");
        }
    }

    /**
     * Tests whether the game board meets the required layout/land placement.
     */
    @org.junit.jupiter.api.Test
    public void requirementLandPlacementTest() {
        ch.zhaw.catan.SiedlerGame model = new ch.zhaw.catan.SiedlerGame(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS, ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_NUMBER_OF_PLAYERS);
        assertTrue(ch.zhaw.catan.Config.getStandardLandPlacement().size() == model.getBoard().getFields().size(), "Check if explicit init must be done (violates spec): modify initializeSiedlerGame accordingly.");
        for (java.util.Map.Entry<java.awt.Point, ch.zhaw.catan.Config.Land> e : ch.zhaw.catan.Config.getStandardLandPlacement().entrySet()) {
            // Changed the original test in order to handle `SiedlerField` classes instead of `Land`s.
            assertEquals(e.getValue(), model.getBoard().getField(e.getKey()).getFieldType(), "Land placement does not match default placement.");
        }
    }

    /**
     * Tests whether the {@link ThreePlayerStandard#getAfterSetupPhase(int)}} game
     * board is not empty (returns an object) at positions where settlements and
     * roads have been placed.
     */
    @org.junit.jupiter.api.Test
    public void requirementSettlementAndRoadPositionsOccupiedThreePlayerStandard() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertEquals(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_NUMBER_OF_PLAYERS, model.getPlayerFactions().size());
        for (ch.zhaw.catan.Config.Faction f : model.getPlayerFactions()) {
            assertTrue(model.getBoard().getCorner(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).first) != null);
            assertTrue(model.getBoard().getCorner(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).second) != null);
            assertTrue(model.getBoard().getEdge(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).first, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_ROAD_ENDPOINTS.get(f).first) != null);
            assertTrue(model.getBoard().getEdge(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).second, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_ROAD_ENDPOINTS.get(f).second) != null);
        }
    }

    /**
     * Checks that the resource card payout for different dice values matches the
     * expected payout for the game state
     * {@link ThreePlayerStandard#getAfterSetupPhase(int)}}.
     *
     * Note, that for the test to work, the {@link Map} returned by
     * {@link SiedlerGame#throwDice(int)} must contain a {@link List} with resource
     * cards (empty {@link List}, if the player gets none) for each of the players.
     *
     * @param diceValue
     * 		the dice value
     */
    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(ints = { 2, 3, 4, 5, 6, 8, 9, 10, 11, 12 })
    public void requirementDiceThrowResourcePayoutThreePlayerStandardTest(int diceValue) {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> expectd = ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_DICE_THROW_PAYOUT.get(diceValue);
        java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> actual = model.throwDice(diceValue);
        assertEquals(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_DICE_THROW_PAYOUT.get(diceValue), model.throwDice(diceValue));
    }

    /**
     * Tests whether the resource card stock of the players matches the expected
     * stock for the game state
     * {@link ThreePlayerStandard#getAfterSetupPhase(int)}}.
     */
    @org.junit.jupiter.api.Test
    public void requirementPlayerResourceCardStockAfterSetupPhase() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertPlayerResourceCardStockEquals(model, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_PLAYER_CARD_STOCK);
    }

    /**
     * Tests whether the resource card stock of the players matches the expected
     * stock for the game state
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}}.
     */
    @org.junit.jupiter.api.Test
    public void requirementPlayerResourceCardStockAfterSetupPhaseAlmostEmptyBank() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertPlayerResourceCardStockEquals(model, ch.zhaw.catan.games.ThreePlayerStandard.BANK_ALMOST_EMPTY_RESOURCE_CARD_STOCK);
    }

    /**
     * Tests whether the resource card stock of the players matches the expected
     * stock for the game state
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}}.
     */
    @org.junit.jupiter.api.Test
    public void requirementPlayerResourceCardStockPlayerOneReadyToBuildFifthSettlement() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getPlayerOneReadyToBuildFifthSettlement(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertPlayerResourceCardStockEquals(model, ch.zhaw.catan.games.ThreePlayerStandard.PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_RESOURCE_CARD_STOCK);
    }

    /**
     * Throws each dice value except 7 once and tests whether the resource card
     * stock of the players matches the expected stock.
     */
    @org.junit.jupiter.api.Test
    public void requirementDiceThrowPlayerResourceCardStockUpdateTest() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        for (int i : java.util.List.of(2, 3, 4, 5, 6, 8, 9, 10, 11, 12)) {
            model.throwDice(i);
        }
        java.util.Map<ch.zhaw.catan.Config.Faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer>> expected = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 1, ch.zhaw.catan.Config.Resource.WOOL, 2, ch.zhaw.catan.Config.Resource.BRICK, 2, ch.zhaw.catan.Config.Resource.ORE, 1, ch.zhaw.catan.Config.Resource.LUMBER, 1), ch.zhaw.catan.Config.Faction.values()[1], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 1, ch.zhaw.catan.Config.Resource.WOOL, 5, ch.zhaw.catan.Config.Resource.BRICK, 0, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0), ch.zhaw.catan.Config.Faction.values()[2], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 0, ch.zhaw.catan.Config.Resource.BRICK, 2, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 1));
        assertPlayerResourceCardStockEquals(model, expected);
    }

    private void assertPlayerResourceCardStockEquals(ch.zhaw.catan.SiedlerGame model, java.util.Map<ch.zhaw.catan.Config.Faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer>> expected) {
        for (int i = 0; i < expected.keySet().size(); i++) {
            ch.zhaw.catan.Config.Faction f = model.getCurrentPlayerFaction();
            for (ch.zhaw.catan.Config.Resource r : ch.zhaw.catan.Config.Resource.values()) {
                assertEquals(expected.get(f).get(r), model.getCurrentPlayerResourceStock(r), ((((("Resource card stock of player " + i) + " [faction ") + f) + "] for resource type ") + r) + " does not match.");
            }
            model.switchToNextPlayer();
        }
    }

    /**
     * Tests whether player one can build two roads starting in game state
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}.
     */
    @org.junit.jupiter.api.Test
    public void requirementBuildRoad() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertTrue(model.buildRoad(new java.awt.Point(6, 6), new java.awt.Point(6, 4)));
        assertTrue(model.buildRoad(new java.awt.Point(6, 4), new java.awt.Point(7, 3)));
    }

    /**
     * Tests whether player one can build a road and a settlement starting in game
     * state {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}.
     */
    @org.junit.jupiter.api.Test
    public void requirementBuildSettlement() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertTrue(model.buildRoad(new java.awt.Point(9, 15), new java.awt.Point(9, 13)));
        assertTrue(model.buildSettlement(new java.awt.Point(9, 13)));
    }

    /**
     * Tests whether payout with multiple settlements of the same player at one
     * field works
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}.
     */
    @org.junit.jupiter.api.Test
    public void requirementTwoSettlementsSamePlayerSameFieldResourceCardPayout() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        for (int diceValue : java.util.List.of(2, 6, 6, 11)) {
            model.throwDice(diceValue);
        }
        assertTrue(model.buildRoad(new java.awt.Point(6, 6), new java.awt.Point(7, 7)));
        assertTrue(model.buildSettlement(new java.awt.Point(7, 7)));
        assertEquals(java.util.List.of(ch.zhaw.catan.Config.Resource.ORE, ch.zhaw.catan.Config.Resource.ORE), model.throwDice(4).get(model.getCurrentPlayerFaction()));
    }

    /**
     * Tests whether player one can build a city starting in game state
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}.
     */
    @org.junit.jupiter.api.Test
    public void requirementBuildCity() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        assertTrue(model.buildCity(new java.awt.Point(10, 16)));
    }

    /**
     * Tests whether player two can trade in resources with the bank and has the
     * correct number of resource cards afterwards. The test starts from game state
     * {@link ThreePlayerStandard#getAfterSetupPhaseAlmostEmptyBank(int)}.
     */
    @org.junit.jupiter.api.Test
    public void requirementCanTradeFourToOneWithBank() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhaseAlmostEmptyBank(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        model.switchToNextPlayer();
        java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> expectedResourceCards = ch.zhaw.catan.games.ThreePlayerStandard.BANK_ALMOST_EMPTY_RESOURCE_CARD_STOCK.get(model.getCurrentPlayerFaction());
        assertEquals(expectedResourceCards.get(ch.zhaw.catan.Config.Resource.WOOL), model.getCurrentPlayerResourceStock(ch.zhaw.catan.Config.Resource.WOOL));
        assertEquals(expectedResourceCards.get(ch.zhaw.catan.Config.Resource.LUMBER), model.getCurrentPlayerResourceStock(ch.zhaw.catan.Config.Resource.LUMBER));
        model.tradeWithBankFourToOne(ch.zhaw.catan.Config.Resource.WOOL, ch.zhaw.catan.Config.Resource.LUMBER);
        int cardsOffered = 4;
        int cardsReceived = 1;
        assertEquals(expectedResourceCards.get(ch.zhaw.catan.Config.Resource.WOOL) - cardsOffered, model.getCurrentPlayerResourceStock(ch.zhaw.catan.Config.Resource.WOOL));
        assertEquals(expectedResourceCards.get(ch.zhaw.catan.Config.Resource.LUMBER) + cardsReceived, model.getCurrentPlayerResourceStock(ch.zhaw.catan.Config.Resource.LUMBER));
    }

    /**
     * *
     * This test is not actually a test and should be removed. However, we leave it
     * in for you to have a quick and easy way to look at the game board produced by
     * {@link ThreePlayerStandard#getAfterSetupPhase(int)}, augmented by
     * annotations, which you won't need since we do not ask for more advanced
     * trading functionality using harbours.
     */
    @org.junit.jupiter.api.Test
    public void print() {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(ch.zhaw.catan.SiedlerGameTestBasic.DEFAULT_WINPOINTS);
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(6, 6), "N ");
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(5, 7), "NE");
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(5, 9), "SE");
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(6, 10), "S ");
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(7, 7), "NW");
        // model.getBoard().addFieldAnnotation(new Point(6, 8),new Point(7, 9), "SW");
        java.lang.System.out.println(new ch.zhaw.catan.SiedlerBoardTextView(model.getBoard()).toString());
    }
}