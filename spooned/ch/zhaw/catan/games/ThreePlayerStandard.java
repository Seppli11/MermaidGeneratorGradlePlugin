package ch.zhaw.catan.games;
import ch.zhaw.catan.SiedlerGame;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * This class can be used to prepare some predefined siedler game situations and, for some
 * of the situations, it provides information about the expected game state,
 * for example the number of resource cards in each player's stock or the expected resource
 * card payout when the dices are thrown (for each dice value).
 * <br>
 * The basic game situations upon which all other situations that can be retrieved are based is
 * the following:
 * <pre>
 *                                 (  )            (  )            (  )            (  )
 *                              //      \\      //      \\      //      \\      //      \\
 *                         (  )            (  )            (  )            (  )            (  )
 *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
 *                          ||              ||              ||              ||              ||
 *                         (  )            (  )            (  )            (  )            (  )
 *                      //      \\      //      \\      //      \\      //      \\      //      \\
 *                 (  )            (  )            (  )            (bb)            (  )            (  )
 *                  ||      ~~      ||      LU      ||      WL      bb      WL      ||      ~~      ||
 *                  ||              ||      06      ||      03      bb      08      ||              ||
 *                 (  )            (  )            (  )            (  )            (  )            (  )
 *              //      \\      //      \\      rr      \\      //      \\      //      \\      //      \\
 *         (  )            (  )            (rr)            (  )            (  )            (  )            (  )
 *          ||      ~~      ||      GR      ||      OR      ||      GR      ||      LU      ||      ~~      ||
 *          ||              ||      02      ||      04      ||      05      ||      10      ||              ||
 *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
 *      //      \\      //      \\      //      \\      //      \\      //      \\      //      \\      //      \\
 * (  )            (  )            (  )            (  )            (  )            (  )            (  )            (  )
 *  ||      ~~      gg      LU      ||      BR      ||      --      ||      OR      ||      GR      ||      ~~      ||
 *  ||              gg      05      ||      09      ||      07      ||      06      ||      09      ||              ||
 * (  )            (gg)            (  )            (  )            (  )            (  )            (  )            (  )
 *      \\      //      \\      //      \\      //      \\      //      \\      //      \\      bb      \\      //
 *         (  )            (  )            (  )            (  )            (  )            (bb)            (  )
 *          ||      ~~      ||      GR      ||      OR      ||      LU      ||      WL      ||      ~~      ||
 *          ||              ||      10      ||      11      ||      03      ||      12      ||              ||
 *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
 *              \\      //      \\      //      \\      //      \\      //      rr      //      \\      //
 *                 (  )            (  )            (  )            (  )            (rr)            (  )
 *                  ||      ~~      ||      WL      ||      BR      ||      BR      ||      ~~      ||
 *                  ||              ||      08      ||      04      ||      11      ||              ||
 *                 (  )            (  )            (  )            (  )            (  )            (  )
 *                      \\      //      \\      //      \\      gg      \\      //      \\      //
 *                         (  )            (  )            (gg)            (  )            (  )
 *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
 *                          ||              ||              ||              ||              ||
 *                         (  )            (  )            (  )            (  )            (  )
 *                              \\      //      \\      //      \\      //      \\      //
 *                                 (  )            (  )            (  )            (  )
 * </pre>
 * Resource cards after the setup phase:
 *  <ul>
 *  <li>Player 1: WOOL BRICK</li>
 *  <li>Player 2: WOOL WOOL</li>
 *  <li>Player 3: BRICK</li>
 *  </ul>
 * <p>The main ideas for this setup were the following:</p>
 * <ul>
 * <li>Player one has access to all resource types from the start so that any resource card can be acquired by
 * throwing the corresponding dice value.</li>
 * <li>The settlements are positioned in a way that for each dice value, there is only one resource card paid
 * to one player, except for the dice values 4 and 12.</li>
 * <li>There is a settlement next to water and the owner has access to resource types required to build roads</li>
 * <li>The initial resource card stock of each player does not allow to build anything without getting
 * additional resources first</li>
 * </ul>
 *
 * @author tebe
 */
public class ThreePlayerStandard {
    public static final int NUMBER_OF_PLAYERS = 3;

    public static final java.util.Map<ch.zhaw.catan.Config.Faction, ch.zhaw.catan.Tuple<java.awt.Point, java.awt.Point>> INITIAL_SETTLEMENT_POSITIONS = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], new ch.zhaw.catan.Tuple<>(new java.awt.Point(5, 7), new java.awt.Point(10, 16)), ch.zhaw.catan.Config.Faction.values()[1], new ch.zhaw.catan.Tuple<>(new java.awt.Point(11, 13), new java.awt.Point(8, 4)), ch.zhaw.catan.Config.Faction.values()[2], new ch.zhaw.catan.Tuple<>(new java.awt.Point(2, 12), new java.awt.Point(7, 19)));

    public static final java.util.Map<ch.zhaw.catan.Config.Faction, ch.zhaw.catan.Tuple<java.awt.Point, java.awt.Point>> INITIAL_ROAD_ENDPOINTS = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], new ch.zhaw.catan.Tuple<>(new java.awt.Point(6, 6), new java.awt.Point(9, 15)), ch.zhaw.catan.Config.Faction.values()[1], new ch.zhaw.catan.Tuple<>(new java.awt.Point(12, 12), new java.awt.Point(8, 6)), ch.zhaw.catan.Config.Faction.values()[2], new ch.zhaw.catan.Tuple<>(new java.awt.Point(2, 10), new java.awt.Point(8, 18)));

    public static final java.util.Map<ch.zhaw.catan.Config.Faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer>> INITIAL_PLAYER_CARD_STOCK = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 1, ch.zhaw.catan.Config.Resource.BRICK, 1, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0), ch.zhaw.catan.Config.Faction.values()[1], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 2, ch.zhaw.catan.Config.Resource.BRICK, 0, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0), ch.zhaw.catan.Config.Faction.values()[2], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 0, ch.zhaw.catan.Config.Resource.BRICK, 1, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0));

    public static final java.util.Map<ch.zhaw.catan.Config.Faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer>> BANK_ALMOST_EMPTY_RESOURCE_CARD_STOCK = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 8, ch.zhaw.catan.Config.Resource.WOOL, 9, ch.zhaw.catan.Config.Resource.BRICK, 9, ch.zhaw.catan.Config.Resource.ORE, 7, ch.zhaw.catan.Config.Resource.LUMBER, 9), ch.zhaw.catan.Config.Faction.values()[1], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 8, ch.zhaw.catan.Config.Resource.WOOL, 10, ch.zhaw.catan.Config.Resource.BRICK, 0, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0), ch.zhaw.catan.Config.Faction.values()[2], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 0, ch.zhaw.catan.Config.Resource.BRICK, 8, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 9));

    public static final java.util.Map<ch.zhaw.catan.Config.Faction, java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer>> PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_RESOURCE_CARD_STOCK = java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 2, ch.zhaw.catan.Config.Resource.WOOL, 2, ch.zhaw.catan.Config.Resource.BRICK, 3, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 3), ch.zhaw.catan.Config.Faction.values()[1], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 5, ch.zhaw.catan.Config.Resource.BRICK, 0, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0), ch.zhaw.catan.Config.Faction.values()[2], java.util.Map.of(ch.zhaw.catan.Config.Resource.GRAIN, 0, ch.zhaw.catan.Config.Resource.WOOL, 0, ch.zhaw.catan.Config.Resource.BRICK, 1, ch.zhaw.catan.Config.Resource.ORE, 0, ch.zhaw.catan.Config.Resource.LUMBER, 0));

    public static final java.util.Map<java.lang.Integer, java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>>> INITIAL_DICE_THROW_PAYOUT = java.util.Map.of(2, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(ch.zhaw.catan.Config.Resource.GRAIN), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 3, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(ch.zhaw.catan.Config.Resource.WOOL), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 4, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(ch.zhaw.catan.Config.Resource.ORE), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of(ch.zhaw.catan.Config.Resource.BRICK)), 5, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of(ch.zhaw.catan.Config.Resource.LUMBER)), 6, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(ch.zhaw.catan.Config.Resource.LUMBER), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 8, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(ch.zhaw.catan.Config.Resource.WOOL), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 9, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(ch.zhaw.catan.Config.Resource.GRAIN), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 10, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 11, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(ch.zhaw.catan.Config.Resource.BRICK), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()), 12, java.util.Map.of(ch.zhaw.catan.Config.Faction.values()[0], java.util.List.of(ch.zhaw.catan.Config.Resource.WOOL), ch.zhaw.catan.Config.Faction.values()[1], java.util.List.of(ch.zhaw.catan.Config.Resource.WOOL), ch.zhaw.catan.Config.Faction.values()[2], java.util.List.of()));

    public static final java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> RESOURCE_CARDS_IN_BANK_AFTER_STARTUP_PHASE = java.util.Map.of(ch.zhaw.catan.Config.Resource.LUMBER, 19, ch.zhaw.catan.Config.Resource.BRICK, 17, ch.zhaw.catan.Config.Resource.WOOL, 16, ch.zhaw.catan.Config.Resource.GRAIN, 19, ch.zhaw.catan.Config.Resource.ORE, 19);

    public static final java.awt.Point PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_FIFTH_SETTLEMENT_POSITION = new java.awt.Point(9, 13);

    public static final java.util.List<java.awt.Point> playerOneReadyToBuildFifthSettlementAllSettlementPositions = java.util.List.of(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(ch.zhaw.catan.Config.Faction.values()[0]).first, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(ch.zhaw.catan.Config.Faction.values()[0]).second, new java.awt.Point(7, 7), new java.awt.Point(6, 4), ch.zhaw.catan.games.ThreePlayerStandard.PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_FIFTH_SETTLEMENT_POSITION);

    /**
     * Returns a siedler game after the setup phase in the setup
     * and with the initial resource card setup as described
     * in {@link ThreePlayerStandard}.
     *
     * @param winpoints
     * 		the number of points required to win the game
     * @return the siedler game
     */
    public static ch.zhaw.catan.SiedlerGame getAfterSetupPhase(int winpoints) {
        ch.zhaw.catan.SiedlerGame model = new ch.zhaw.catan.SiedlerGame(winpoints, ch.zhaw.catan.games.ThreePlayerStandard.NUMBER_OF_PLAYERS);
        for (int i = 0; i < model.getPlayerFactions().size(); i++) {
            ch.zhaw.catan.Config.Faction f = model.getCurrentPlayerFaction();
            assertTrue(model.placeInitialSettlement(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).first, false));
            assertTrue(model.placeInitialRoad(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).first, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_ROAD_ENDPOINTS.get(f).first));
            model.switchToNextPlayer();
        }
        for (int i = 0; i < model.getPlayerFactions().size(); i++) {
            model.switchToPreviousPlayer();
            ch.zhaw.catan.Config.Faction f = model.getCurrentPlayerFaction();
            assertTrue(model.placeInitialSettlement(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).second, true));
            assertTrue(model.placeInitialRoad(ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_SETTLEMENT_POSITIONS.get(f).second, ch.zhaw.catan.games.ThreePlayerStandard.INITIAL_ROAD_ENDPOINTS.get(f).second));
        }
        return model;
    }

    /**
     * Returns a siedler game after the setup phase in the setup
     * described in {@link ThreePlayerStandard} and with the bank almost empty.
     *
     * The following resource cards should be in the stock of the bank:
     * <ul>
     * <li>LUMBER: 1</li>
     * <li>BRICK: 2</li>
     * <li>GRAIN: 3</li>
     * <li>ORE: 13</li>
     * <li>WOOL: 0</li>
     * </ul>
     *
     * The stocks of the players should contain:
     * <br>
     * Player 1:
     * <ul>
     * <li>LUMBER: 9</li>
     * <li>BRICK: 9</li>
     * <li>GRAIN: 8</li>
     * <li>ORE: 7</li>
     * <li>WOOL: 9</li>
     * </ul>
     * Player 2:
     * <ul>
     * <li>LUMBER: 0</li>
     * <li>BRICK: 0</li>
     * <li>GRAIN: 8</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 10</li>
     * </ul>
     * Player 3:
     * <ul>
     * <li>LUMBER: 9</li>
     * <li>BRICK: 8</li>
     * <li>GRAIN: 0</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 0</li>
     * </ul>
     *
     * @param winpoints
     * 		the number of points required to win the game
     * @return the siedler game
     */
    public static ch.zhaw.catan.SiedlerGame getAfterSetupPhaseAlmostEmptyBank(int winpoints) {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(winpoints);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 6, 9);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 11, 8);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 2, 8);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 4, 7);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 12, 8);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 5, 9);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 9, 8);
        return model;
    }

    /**
     * Returns a {@link SiedlerGame} with several roads added but none longer than
     * 4 elements. Hence, no player meets the longest road criteria yet. Furthermore,
     * players one and three have enough resource cards to build additional roads and settlements.
     *
     * <p></p>
     * <p>The game board should look as follows:
     * <pre>
     *                                 (  )            (  )            (  )            (  )
     *                              //      \\      //      \\      //      \\      //      \\
     *                         (  )            (  )            (  )            (  )            (  )
     *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
     *                          ||              ||              ||              ||              ||
     *                         (  )            (  )            (  )            (  )            (  )
     *                      //      \\      //      \\      //      \\      //      \\      //      \\
     *                 (  )            (  )            (  )            (bb)            (  )            (  )
     *                  ||      ~~      ||      LU      ||      WL      bb      WL      ||      ~~      ||
     *                  ||              ||      06      ||      03      bb      08      ||              ||
     *                 (  )            (  )            (  )            (  )            (  )            (  )
     *              //      \\      //      \\      rr      \\      //      \\      //      \\      //      \\
     *         (  )            (  )            (rr)            (  )            (  )            (  )            (  )
     *          ||      ~~      gg      GR      rr      OR      ||      GR      ||      LU      ||      ~~      ||
     *          ||              gg      02      rr      04      ||      05      ||      10      ||              ||
     *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *      //      \\      gg      rr      rr      \\      //      \\      //      \\      //      \\      //      \\
     * (  )            (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *  ||      ~~      gg      LU      ||      BR      ||      --      ||      OR      ||      GR      ||      ~~      ||
     *  ||              gg      05      ||      09      ||      07      ||      06      ||      09      ||              ||
     * (  )            (gg)            (  )            (  )            (  )            (  )            (  )            (  )
     *      \\      //      gg      //      \\      //      \\      //      \\      rr      \\      bb      \\      //
     *         (  )            (  )            (  )            (  )            (  )            (bb)            (  )
     *          ||      ~~      ||      GR      ||      OR      ||      LU      rr      WL      ||      ~~      ||
     *          ||              ||      10      ||      11      ||      03      rr      12      ||              ||
     *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *              \\      //      \\      //      \\      //      \\      //      rr      rr      \\      //
     *                 (  )            (  )            (  )            (  )            (rr)            (  )
     *                  ||      ~~      ||      WL      gg      BR      gg      BR      rr      ~~      ||
     *                  ||              ||      08      gg      04      gg      11      rr              ||
     *                 (  )            (  )            (  )            (  )            (  )            (  )
     *                      \\      //      \\      //      gg      gg      \\      //      \\      //
     *                         (  )            (  )            (gg)            (  )            (  )
     *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
     *                          ||              ||              ||              ||              ||
     *                         (  )            (  )            (  )            (  )            (  )
     *                              \\      //      \\      //      \\      //      \\      //
     *                                 (  )            (  )            (  )            (  )
     * </pre>
     * <p>
     * And the player resource card stocks:
     * <br>
     * Player 1:
     * <ul>
     * <li>LUMBER: 6</li>
     * <li>BRICK: 6</li>
     * <li>GRAIN: 1</li>
     * <li>ORE: 11</li>
     * <li>WOOL: 1</li>
     * </ul>
     * Player 2:
     * <ul>
     * <li>LUMBER: 0</li>
     * <li>BRICK: 0</li>
     * <li>GRAIN: 0</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 2</li>
     * </ul>
     * Player 3:
     * <ul>
     * <li>LUMBER: 6</li>
     * <li>BRICK: 6</li>
     * <li>GRAIN: 1</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 1</li>
     * </ul>
     *
     * @param winpoints
     * 		the number of points required to win the game
     * @return the siedler game
     */
    public static ch.zhaw.catan.SiedlerGame getAfterSetupPhaseSomeRoads(int winpoints) {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(winpoints);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 6, 7);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 11, 6);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 4, 5);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 5, 6);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 2, 1);
        model.switchToNextPlayer();
        model.switchToNextPlayer();
        model.buildRoad(new java.awt.Point(2, 12), new java.awt.Point(3, 13));
        ch.zhaw.catan.games.ThreePlayerStandard.buildRoad(model, java.util.List.of(new java.awt.Point(2, 10), new java.awt.Point(3, 9), new java.awt.Point(3, 7)));
        model.buildRoad(new java.awt.Point(8, 18), new java.awt.Point(8, 16));
        ch.zhaw.catan.games.ThreePlayerStandard.buildRoad(model, java.util.List.of(new java.awt.Point(7, 19), new java.awt.Point(6, 18), new java.awt.Point(6, 16)));
        model.switchToNextPlayer();
        model.buildRoad(new java.awt.Point(10, 16), new java.awt.Point(11, 15));
        model.buildRoad(new java.awt.Point(10, 16), new java.awt.Point(10, 18));
        ch.zhaw.catan.games.ThreePlayerStandard.buildRoad(model, java.util.List.of(new java.awt.Point(9, 15), new java.awt.Point(9, 13), new java.awt.Point(10, 12)));
        ch.zhaw.catan.games.ThreePlayerStandard.buildRoad(model, java.util.List.of(new java.awt.Point(5, 7), new java.awt.Point(5, 9), new java.awt.Point(4, 10), new java.awt.Point(3, 9)));
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 6, 6);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 11, 6);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 4, 6);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 5, 6);
        model.switchToNextPlayer();
        model.switchToNextPlayer();
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 5, 4);
        model.tradeWithBankFourToOne(ch.zhaw.catan.Config.Resource.LUMBER, ch.zhaw.catan.Config.Resource.GRAIN);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 5, 4);
        model.tradeWithBankFourToOne(ch.zhaw.catan.Config.Resource.LUMBER, ch.zhaw.catan.Config.Resource.WOOL);
        model.switchToNextPlayer();
        return model;
    }

    private static ch.zhaw.catan.SiedlerGame throwDiceMultipleTimes(ch.zhaw.catan.SiedlerGame model, int diceValue, int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            model.throwDice(diceValue);
        }
        return model;
    }

    /**
     * Returns a siedler game after building four additional roads and two
     * settlements after the setup phase with the resource cards and roads
     * for player one ready to build a fifth settlement at {@link #PLAYER_ONE_READY_TO_BUILD_FIFTH_SETTLEMENT_FIFTH_SETTLEMENT_POSITION}
     * <p></p>
     * <p>The game board should look as follows:
     * <pre>
     *                                 (  )            (  )            (  )            (  )
     *                              //      \\      //      \\      //      \\      //      \\
     *                         (  )            (  )            (  )            (  )            (  )
     *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
     *                          ||              ||              ||              ||              ||
     *                         (  )            (  )            (  )            (  )            (  )
     *                      //      \\      //      \\      //      \\      //      \\      //      \\
     *                 (  )            (  )            (rr)            (bb)            (  )            (  )
     *                  ||      ~~      ||      LU      rr      WL      bb      WL      ||      ~~      ||
     *                  ||              ||      06      rr      03      bb      08      ||              ||
     *                 (  )            (  )            (  )            (  )            (  )            (  )
     *              //      \\      //      \\      rr      rr      //      \\      //      \\      //      \\
     *         (  )            (  )            (rr)            (rr)            (  )            (  )            (  )
     *          ||      ~~      ||      GR      ||      OR      ||      GR      ||      LU      ||      ~~      ||
     *          ||              ||      02      ||      04      ||      05      ||      10      ||              ||
     *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *      //      \\      //      \\      //      \\      //      \\      //      \\      //      \\      //      \\
     * (  )            (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *  ||      ~~      gg      LU      ||      BR      ||      --      ||      OR      ||      GR      ||      ~~      ||
     *  ||              gg      05      ||      09      ||      07      ||      06      ||      09      ||              ||
     * (  )            (gg)            (  )            (  )            (  )            (  )            (  )            (  )
     *      \\      //      \\      //      \\      //      \\      //      \\      //      \\      bb      \\      //
     *         (  )            (  )            (  )            (  )            (  )            (bb)            (  )
     *          ||      ~~      ||      GR      ||      OR      ||      LU      rr      WL      ||      ~~      ||
     *          ||              ||      10      ||      11      ||      03      rr      12      ||              ||
     *         (  )            (  )            (  )            (  )            (  )            (  )            (  )
     *              \\      //      \\      //      \\      //      \\      //      rr      //      \\      //
     *                 (  )            (  )            (  )            (  )            (rr)            (  )
     *                  ||      ~~      ||      WL      ||      BR      ||      BR      ||      ~~      ||
     *                  ||              ||      08      ||      04      ||      11      ||              ||
     *                 (  )            (  )            (  )            (  )            (  )            (  )
     *                      \\      //      \\      //      \\      gg      \\      //      \\      //
     *                         (  )            (  )            (gg)            (  )            (  )
     *                          ||      ~~      ||      ~~      ||      ~~      ||      ~~      ||
     *                          ||              ||              ||              ||              ||
     *                         (  )            (  )            (  )            (  )            (  )
     *                              \\      //      \\      //      \\      //      \\      //
     *                                 (  )            (  )            (  )            (  )
     *
     * </pre>
     * <br>
     * <p>And the player resource card stocks:</p>
     * <br>
     * Player 1:
     * <ul>
     * <li>LUMBER: 3</li>
     * <li>BRICK: 3</li>
     * <li>GRAIN: 2</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 2</li>
     * </ul>
     * Player 2:
     * <ul>
     * <li>LUMBER: 0</li>
     * <li>BRICK: 0</li>
     * <li>GRAIN: 0</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 5</li>
     * </ul>
     * Player 3:
     * <ul>
     * <li>LUMBER: 0</li>
     * <li>BRICK: 1</li>
     * <li>GRAIN: 0</li>
     * <li>ORE: 0</li>
     * <li>WOOL: 0</li>
     * </ul>
     *
     * @param winpoints
     * 		the number of points required to win the game
     * @return the siedler game
     */
    public static ch.zhaw.catan.SiedlerGame getPlayerOneReadyToBuildFifthSettlement(int winpoints) {
        ch.zhaw.catan.SiedlerGame model = ch.zhaw.catan.games.ThreePlayerStandard.getAfterSetupPhase(winpoints);
        // generate resources to build four roads and four settlements.
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 6, 8);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 11, 7);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 2, 4);
        ch.zhaw.catan.games.ThreePlayerStandard.throwDiceMultipleTimes(model, 12, 3);
        model.buildRoad(new java.awt.Point(6, 6), new java.awt.Point(7, 7));
        model.buildRoad(new java.awt.Point(6, 6), new java.awt.Point(6, 4));
        model.buildRoad(new java.awt.Point(9, 15), new java.awt.Point(9, 13));
        model.buildSettlement(ch.zhaw.catan.games.ThreePlayerStandard.playerOneReadyToBuildFifthSettlementAllSettlementPositions.get(2));
        model.buildSettlement(ch.zhaw.catan.games.ThreePlayerStandard.playerOneReadyToBuildFifthSettlementAllSettlementPositions.get(3));
        return model;
    }

    private static void buildSettlement(ch.zhaw.catan.SiedlerGame model, java.awt.Point position, java.util.List<java.awt.Point> roads) {
        ch.zhaw.catan.games.ThreePlayerStandard.buildRoad(model, roads);
        assertTrue(model.buildSettlement(position));
    }

    private static void buildRoad(ch.zhaw.catan.SiedlerGame model, java.util.List<java.awt.Point> roads) {
        for (int i = 0; i < (roads.size() - 1); i++) {
            assertTrue(model.buildRoad(roads.get(i), roads.get(i + 1)));
        }
    }
}