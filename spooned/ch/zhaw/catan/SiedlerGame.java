package ch.zhaw.catan;
import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.model.Bank;
import ch.zhaw.catan.model.Player;
/**
 * This class performs all actions related to modifying the game state.
 *
 * @author Jonas Costa
 */
public class SiedlerGame {
    /**
     * Specifies how many resource cards the bank requires for a trade.
     */
    private static final int FOUR_TO_ONE_TRADE_OFFER = 4;

    /**
     * Specifies how many resource cards the bank gives when trading.
     */
    private static final int FOUR_TO_ONE_TRADE_WANT = 1;

    /**
     * Specifies the number of victory points that are required for a victory.
     */
    private int winPoints;

    /**
     * Specifies the current amount of players in the game.
     */
    private int numberOfPlayers;

    /**
     * Specifies which player's turn it is.
     */
    private int currentPlayer;

    /**
     * An array of current {@link Player} objects in the game.
     */
    private ch.zhaw.catan.model.Player[] players;

    /**
     * Stores a {@link Bank} object.
     */
    private ch.zhaw.catan.model.Bank bank;

    /**
     * Where the thief is currently located
     */
    private java.awt.Point thiefPoint = new java.awt.Point(ch.zhaw.catan.Config.INITIAL_THIEF_POSITION);

    /**
     * Creates and subsequently stores a predetermined {@link SiedlerBoard}.
     */
    private ch.zhaw.catan.SiedlerBoard board = ch.zhaw.catan.SiedlerBoard.createStandartSiedlerBoard();

    /**
     * The random instance used to generate random numbers
     */
    private java.util.Random random = new java.util.Random();

    /**
     * Constructs a SiedlerGame game state object.
     *
     * @param winPoints
     * 		the number of points required to win the game
     * @param numberOfPlayers
     * 		the number of players
     * @throws IllegalArgumentException
     * 		if winPoints is lower than three or players
     * 		is not between two and four
     */
    public SiedlerGame(int winPoints, int numberOfPlayers) {
        // Check if input is out of bounds
        if (((winPoints < 3) || (numberOfPlayers < 2)) || (numberOfPlayers > 4)) {
            throw new java.lang.IllegalArgumentException("Invalid amount of players or win points.");
        }
        // Populate fields
        this.winPoints = winPoints;
        this.numberOfPlayers = numberOfPlayers;
        bank = new ch.zhaw.catan.model.Bank(ch.zhaw.catan.Config.INITIAL_RESOURCE_CARDS_BANK);
        players = new ch.zhaw.catan.model.Player[numberOfPlayers];
        // Player creation
        ch.zhaw.catan.Config.Faction[] factions = ch.zhaw.catan.Config.Faction.values();
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new ch.zhaw.catan.model.Player(factions[i]);
        }
    }

    /**
     * Switches to the next player in the defined sequence of players.
     */
    public void switchToNextPlayer() {
        currentPlayer++;
        if (currentPlayer >= numberOfPlayers) {
            currentPlayer = 0;
        }
    }

    /**
     * Switches to the previous player in the defined sequence of players.
     */
    public void switchToPreviousPlayer() {
        currentPlayer--;
        if (currentPlayer < 0) {
            currentPlayer = numberOfPlayers - 1;
        }
    }

    /**
     * Returns the {@link Faction}s of the active players.
     *
     * <p>
     * The order of the player's factions in the list must correspond to the oder in
     * which they play. Hence, the player that sets the first settlement must be at
     * position 0 in the list etc.
     *
     * <strong>Important note:</strong> The list must contain the factions of active
     * players only.
     * </p>
     *
     * @return the list with player's factions
     */
    public java.util.List<ch.zhaw.catan.Config.Faction> getPlayerFactions() {
        java.util.List<ch.zhaw.catan.Config.Faction> factions = new java.util.ArrayList<>();
        for (ch.zhaw.catan.model.Player player : players) {
            factions.add(player.getFaction());
        }
        return factions;
    }

    /**
     * Returns the game board.
     *
     * @return the game board
     */
    public ch.zhaw.catan.SiedlerBoard getBoard() {
        return board;
    }

    /**
     * Returns the {@link Faction} of the current player.
     *
     * @return the faction of the current player
     */
    public ch.zhaw.catan.Config.Faction getCurrentPlayerFaction() {
        return getCurrentPlayer().getFaction();
    }

    /**
     * Returns the current player
     *
     * @return the current player
     */
    protected ch.zhaw.catan.model.Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    /**
     * Returns how many resource cards of the specified type the current player
     * owns.
     *
     * @param resource
     * 		the resource type
     * @return the number of resource cards of this type
     */
    public int getCurrentPlayerResourceStock(ch.zhaw.catan.Config.Resource resource) {
        return getCurrentPlayer().getResourceCount(resource);
    }

    /**
     * Places a settlement in the founder's phase (phase II) of the game.
     *
     * <p>
     * The placement does not cost any resource cards. If payout is set to true, for
     * each adjacent resource-producing field, a resource card of the type of the
     * resource produced by the field is taken from the bank (if available) and
     * added to the players' stock of resource cards.
     * </p>
     *
     * @param position
     * 		the position of the settlement
     * @param payout
     * 		if true, the player gets one resource card per adjacent
     * 		resource-producing field
     * @return true, if the placement was successful
     */
    public boolean placeInitialSettlement(java.awt.Point position, boolean payout) {
        ch.zhaw.catan.model.Player player = getCurrentPlayer();
        ch.zhaw.catan.Config.Faction faction = player.getFaction();
        if (board.isValidInitialSettlementLocation(position, faction)) {
            ch.zhaw.catan.model.SiedlerSettlement settlement = new ch.zhaw.catan.model.SiedlerSettlement(player);
            board.setCorner(position, settlement);
            if (payout) {
                payoutInitialSettlement(position);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pays out the initial settlement at the given position to the current player
     *
     * @param position
     * 		the position, where the initial settlement was built
     */
    private void payoutInitialSettlement(java.awt.Point position) {
        for (ch.zhaw.catan.Config.Land land : board.getLandsForCorner(position)) {
            ch.zhaw.catan.Config.Resource resource = land.getResource();
            if (resource != null) {
                bank.decrementResource(resource, 1);
                getCurrentPlayer().incrementResource(resource, 1);
            }
        }
    }

    /**
     * Places a road in the founder's phase (phase II) of the game. The placement
     * does not cost any resource cards.
     *
     * @param roadStart
     * 		position of the start of the road
     * @param roadEnd
     * 		position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean placeInitialRoad(java.awt.Point roadStart, java.awt.Point roadEnd) {
        ch.zhaw.catan.model.Player player = getCurrentPlayer();
        ch.zhaw.catan.Config.Faction faction = player.getFaction();
        if (board.isValidInitialStreetLocation(roadStart, roadEnd, faction)) {
            ch.zhaw.catan.model.SiedlerStreet street = new ch.zhaw.catan.model.SiedlerStreet(player);
            board.setEdge(roadStart, roadEnd, street);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method takes care of actions depending on the dice throw result.
     *
     * A key action is the payout of the resource cards to the players according to
     * the payout rules of the game. This includes the "negative payout" in case a 7
     * is thrown and a player has more than {@link Config#MAX_CARDS_IN_HAND_NO_DROP}
     * resource cards.
     *
     * If a player does not get resource cards, the list for this players'
     * {@link Faction} is <b>an empty list (not null)</b>!.
     *
     * <p>
     * The payout rules of the game take into account factors such as, the number of
     * resource cards currently available in the bank, settlement types (settlement
     * or city), and the number of players that should get resource cards of a
     * certain type (relevant if there are not enough left in the bank).
     * </p>
     *
     * @param diceThrow
     * 		the resource cards that have been distributed to the players
     * @return the resource cards added to the stock of the different players
     */
    public java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> throwDice(int diceThrow) {
        if (diceThrow != 7) {
            java.util.List<java.awt.Point> fields = new java.util.ArrayList<>(board.getFieldsForDiceValue(diceThrow));
            fields.remove(thiefPoint);
            return new ch.zhaw.catan.SiedlerGame.PaymentPerformer(fields).performPayout();
        } else {
            handleSeven();
            return getEmptyResourceMap();
        }
    }

    /**
     * Handles the scenario when a {@code 7} has been rolled.
     */
    private void handleSeven() {
        for (ch.zhaw.catan.model.Player player : players) {
            if (player.getAllResourcesCount() > ch.zhaw.catan.Config.MAX_CARDS_IN_HAND_NO_DROP) {
                for (int i = player.getAllResourcesCount() / 2; i > 0; i--) {
                    ch.zhaw.catan.Config.Resource[] resources = player.getResourceArray();
                    player.decrementResource(resources[random.nextInt(resources.length)], 1);
                }
            }
        }
    }

    /**
     * Gets a map containing an empty resource-list for each player.
     *
     * @return A map containing an empty resource-list for each player.
     */
    private java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> getEmptyResourceMap() {
        java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> result = new java.util.EnumMap<>(ch.zhaw.catan.Config.Faction.class);
        for (ch.zhaw.catan.model.Player player : players) {
            result.put(player.getFaction(), new java.util.ArrayList<>());
        }
        return result;
    }

    /**
     * Builds a settlement at the specified position on the board.
     *
     * <p>
     * The settlement can be built if:
     * <ul>
     * <li>the player possesses the required resource cards</li>
     * <li>a settlement to place on the board</li>
     * <li>the specified position meets the build rules for settlements</li>
     * </ul>
     *
     * @param position
     * 		the position of the settlement
     * @return true, if the placement was successful
     */
    public boolean buildSettlement(java.awt.Point position) {
        ch.zhaw.catan.model.Player player = getCurrentPlayer();
        ch.zhaw.catan.Config.Faction faction = player.getFaction();
        if (board.isValidSettlementLocation(position, faction)) {
            ch.zhaw.catan.model.SiedlerSettlement settlement = new ch.zhaw.catan.model.SiedlerSettlement(player);
            java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> cost = settlement.getStructure().getCostsAsMap();
            if (player.hasResources(cost)) {
                player.decrementResources(cost);
                bank.incrementResources(cost);
                board.setCorner(position, settlement);
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a city at the specified position on the board.
     *
     * <p>
     * The city can be built if:
     * <ul>
     * <li>the player possesses the required resource cards</li>
     * <li>a city to place on the board</li>
     * <li>the specified position meets the build rules for cities</li>
     * </ul>
     *
     * @param position
     * 		the position of the city
     * @return true, if the placement was successful
     */
    public boolean buildCity(java.awt.Point position) {
        ch.zhaw.catan.model.Player player = getCurrentPlayer();
        ch.zhaw.catan.Config.Faction faction = player.getFaction();
        if (board.hasSettlement(position, faction)) {
            ch.zhaw.catan.model.SiedlerCity city = new ch.zhaw.catan.model.SiedlerCity(player);
            java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> cost = city.getStructure().getCostsAsMap();
            if (player.hasResources(cost)) {
                player.decrementResources(cost);
                bank.incrementResources(cost);
                board.setCorner(position, city);
                return true;
            }
        }
        return false;
    }

    /**
     * Builds a road at the specified position on the board.
     *
     * <p>
     * The road can be built if:
     * <ul>
     * <li>the player possesses the required resource cards</li>
     * <li>a road to place on the board</li>
     * <li>the specified position meets the build rules for roads</li>
     * </ul>
     *
     * @param roadStart
     * 		the position of the start of the road
     * @param roadEnd
     * 		the position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean buildRoad(java.awt.Point roadStart, java.awt.Point roadEnd) {
        ch.zhaw.catan.model.Player player = getCurrentPlayer();
        ch.zhaw.catan.Config.Faction faction = player.getFaction();
        if (board.isValidStreetLocation(roadStart, roadEnd, faction)) {
            ch.zhaw.catan.model.SiedlerStreet street = new ch.zhaw.catan.model.SiedlerStreet(player);
            java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> cost = street.getStructure().getCostsAsMap();
            if (player.hasResources(cost)) {
                player.decrementResources(cost);
                bank.incrementResources(cost);
                board.setEdge(roadStart, roadEnd, street);
                return true;
            }
        }
        return false;
    }

    /**
     * Trades in {@link #FOUR_TO_ONE_TRADE_OFFER} resource cards of the offered type
     * for {@link #FOUR_TO_ONE_TRADE_WANT} resource cards of the wanted type.
     *
     * The trade only works when bank and player possess the resource cards for the
     * trade before the trade is executed.
     *
     * @param offer
     * 		offered type
     * @param want
     * 		wanted type
     * @return true, if the trade was successful
     */
    public boolean tradeWithBankFourToOne(ch.zhaw.catan.Config.Resource offer, ch.zhaw.catan.Config.Resource want) {
        if ((bank.getResourceCount(want) < ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_WANT) || (getCurrentPlayer().getResourceCount(offer) < ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_OFFER)) {
            return false;
        }
        bank.decrementResource(want, ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_WANT);
        getCurrentPlayer().incrementResource(want, ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_WANT);
        bank.incrementResource(offer, ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_OFFER);
        getCurrentPlayer().decrementResource(offer, ch.zhaw.catan.SiedlerGame.FOUR_TO_ONE_TRADE_OFFER);
        return true;
    }

    /**
     * Returns the winner of the game, if any.
     *
     * @return the winner of the game or null, if there is no winner (yet)
     */
    public ch.zhaw.catan.Config.Faction getWinner() {
        return getScore(getCurrentPlayerFaction()) >= winPoints ? getCurrentPlayerFaction() : null;
    }

    /**
     *
     *
     * @return the current position of the thief. The returned position is a copy.
     */
    public java.awt.Point getThiefPosition() {
        return new java.awt.Point(thiefPoint);
    }

    /**
     * Places the thief on the specified field and steals a random resource card (if
     * the player has such cards) from a random player with a settlement at that
     * field (if there is a settlement) and adds it to the resource cards of the
     * current player.
     *
     * @param fieldPosition
     * 		the field on which to place the thief
     * @return false, if the specified field is not a field or the thief cannot be
    placed there (e.g., on water)
     */
    public boolean placeThiefAndStealCard(java.awt.Point fieldPosition) {
        ch.zhaw.catan.model.SiedlerField siedlerField = getSiedlerFieldOrNull(fieldPosition);
        if ((siedlerField == null) || (siedlerField.getFieldType() == ch.zhaw.catan.Config.Land.WATER))
            return false;

        thiefPoint.setLocation(fieldPosition);
        stealRandomResource(fieldPosition);
        return true;
    }

    /**
     * Returns a siedler field or null if the given coordinate isn't valid.
     * This is a helper method.
     *
     * @param fieldPosition
     * 		the field position
     * @return the siedler field or null
     */
    private ch.zhaw.catan.model.SiedlerField getSiedlerFieldOrNull(java.awt.Point fieldPosition) {
        try {
            return board.getField(fieldPosition);
        } catch (java.lang.IllegalArgumentException e) {
            // here a logger should log this exception, but this seems outside of the scope
            // of this game
            return null;
        }
    }

    /**
     * Steals a random resource from a random player that has a settlement (or
     *
     * city) next to the given field
     *
     * @param field
     * 		the field where the thief was placed on. Only player with a
     * 		settlement (or city) next to the field
     * 		are targets
     */
    private void stealRandomResource(java.awt.Point field) {
        ch.zhaw.catan.model.Player player = getRandomPlayerWithResources(field);
        if (player == null) {
            return;// all possible players have an empty inventory

        }
        ch.zhaw.catan.Config.Resource[] playerResources = player.getResourceArray();
        ch.zhaw.catan.Config.Resource resourceToSteal = playerResources[random.nextInt(playerResources.length)];
        player.decrementResource(resourceToSteal, 1);
        getCurrentPlayer().incrementResource(resourceToSteal, 1);
    }

    /**
     * Finds a random player with resources or returns null around the given file.
     * If no player has resources
     *
     * @param field
     * 		the field around which players are searched
     * @return the player or null
     */
    private ch.zhaw.catan.model.Player getRandomPlayerWithResources(java.awt.Point field) {
        // we only want each player once, so list to map to list
        java.util.List<ch.zhaw.catan.model.Player> possiblePlayers = new java.util.ArrayList<>(new java.util.HashSet<>(board.getPlayersAroundField(field)));
        while (!possiblePlayers.isEmpty()) {
            ch.zhaw.catan.model.Player nextPlayer = possiblePlayers.remove(random.nextInt(possiblePlayers.size()));
            if (nextPlayer.getAllResourcesCount() > 0) {
                return nextPlayer;
            }
        } 
        return null;
    }

    /**
     * Gets the score of the specified {@code faction}.
     *
     * @param faction
     * 		The faction to get the score for.
     * @return The score of the specified {@code faction}.
     */
    private int getScore(ch.zhaw.catan.Config.Faction faction) {
        int result = 0;
        for (ch.zhaw.catan.model.SiedlerSettlement settlement : getBoard().getCorners()) {
            if (settlement.getOwner().getFaction() == faction) {
                result += settlement.getVictoryPoints();
            }
        }
        return result;
    }

    /**
     * A class for calculating the payment to players after a dice roll.
     */
    private class PaymentPerformer {
        /**
         * the result map
         */
        private java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> result = getEmptyResourceMap();

        /**
         * the total amount
         */
        private int totalAmount = 0;

        /**
         * the fields to check
         */
        private java.util.List<java.awt.Point> fields = new java.util.ArrayList<>();

        /**
         * Constructor
         *
         * @param fields
         * 		fields to check for resources to pay out
         */
        public PaymentPerformer(java.util.List<java.awt.Point> fields) {
            this.fields.addAll(fields);
        }

        /**
         * Performs the payout to player and returns a map with what has been done (for
         * the "gui"
         * to display it).
         *
         * @param diceValue
         * 		The value of the dices that were thrown.
         * @return The resources that were paid out.
         */
        private java.util.Map<ch.zhaw.catan.Config.Faction, java.util.List<ch.zhaw.catan.Config.Resource>> performPayout() {
            for (ch.zhaw.catan.Config.Resource resource : ch.zhaw.catan.Config.Resource.values()) {
                java.util.Map<ch.zhaw.catan.model.Player, java.lang.Integer> payoutAmount = calculatePaymentAmount(resource);
                if ((totalAmount <= bank.getResourceCount(resource)) || (payoutAmount.keySet().size() <= 1)) {
                    payoutResourceToPlayers(resource, payoutAmount);
                }
            }
            return result;
        }

        /**
         * Calculates how much of the given resource is payed out to each player. All
         * {@link #fields} are
         * checked.
         *
         * @param resource
         * 		the resource for which the amount is calculated
         * @return a map from player two the amount each player gets
         */
        private java.util.Map<ch.zhaw.catan.model.Player, java.lang.Integer> calculatePaymentAmount(ch.zhaw.catan.Config.Resource resource) {
            java.util.Map<ch.zhaw.catan.model.Player, java.lang.Integer> payoutAmount = new java.util.HashMap<>();
            for (java.awt.Point coordinate : fields) {
                if (board.getField(coordinate).getFieldType().getResource() == resource) {
                    for (ch.zhaw.catan.model.SiedlerSettlement settlement : board.getCornersOfField(coordinate)) {
                        ch.zhaw.catan.model.Player player = settlement.getOwner();
                        totalAmount += settlement.getResourceCount();
                        payoutAmount.put(player, payoutAmount.getOrDefault(player, 0) + settlement.getResourceCount());
                    }
                }
            }
            return payoutAmount;
        }

        /**
         * Pays out the given resource to the players. When the player gets an amount
         * of resource
         * the same amount is removed from the bank.
         *
         * @param resource
         * 		the resource to payout
         * @param payoutAmount
         * 		the amount to payout for each player
         */
        private void payoutResourceToPlayers(ch.zhaw.catan.Config.Resource resource, java.util.Map<ch.zhaw.catan.model.Player, java.lang.Integer> payoutAmount) {
            for (java.util.Map.Entry<ch.zhaw.catan.model.Player, java.lang.Integer> entry : payoutAmount.entrySet()) {
                int count = java.lang.Math.min(entry.getValue(), bank.getResourceCount(resource));
                bank.decrementResource(resource, count);
                entry.getKey().incrementResource(resource, count);
                for (int i = count; i > 0; i--) {
                    result.get(entry.getKey().getFaction()).add(resource);
                }
            }
        }
    }
}