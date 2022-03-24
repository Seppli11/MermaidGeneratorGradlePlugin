package ch.zhaw.catan;
import ch.zhaw.catan.Config.Land;
import java.awt.*;
public class SiedlerBoard extends ch.zhaw.hexboard.HexBoard<ch.zhaw.catan.model.SiedlerField, ch.zhaw.catan.model.SiedlerSettlement, ch.zhaw.catan.model.SiedlerStreet, java.lang.Void> {
    /**
     * Verifies if the selected location is a valid location for a street.
     *
     * @return true, if the location of the street is valid.
     */
    public boolean isValidStreetLocation(java.awt.Point startPoint, java.awt.Point endPoint, ch.zhaw.catan.Config.Faction owner) {
        return ((((hasAdjacentSettlement(startPoint, endPoint, owner) || hasAdjacentStreet(startPoint, endPoint, owner)) && hasEdge(startPoint, endPoint)) && (getEdge(startPoint, endPoint) == null)) && isCornerTouchingLand(startPoint)) && isCornerTouchingLand(endPoint);
    }

    /**
     * Verifies if the selected location is a valid location for a settlement.
     *
     * @return true, if the location of the settlement is valid.
     */
    public boolean isValidSettlementLocation(java.awt.Point position, ch.zhaw.catan.Config.Faction owner) {
        return isValidInitialSettlementLocation(position, owner) && hasAdjacentStreet(position, owner);
    }

    /**
     * Verifies if the selected location is a valid location for an initial street.
     *
     * @return true, if the location of the street is valid.
     */
    public boolean isValidInitialStreetLocation(java.awt.Point startPoint, java.awt.Point endPoint, ch.zhaw.catan.Config.Faction owner) {
        return isValidStreetLocation(startPoint, endPoint, owner) && ((hasSettlement(startPoint, owner) && getAdjacentEdges(startPoint).isEmpty()) || (hasSettlement(endPoint, owner) && getAdjacentEdges(endPoint).isEmpty()));
    }

    /**
     * Verifies if the selected location is a valid location for an initial
     * settlement.
     *
     * @return true, if the location of the settlement is valid.
     */
    public boolean isValidInitialSettlementLocation(java.awt.Point position, ch.zhaw.catan.Config.Faction owner) {
        return ((isCornerTouchingLand(position) && getNeighboursOfCorner(position).isEmpty()) && hasCorner(position)) && (getCorner(position) == null);
    }

    /**
     * Checks if the corner touches a field that isn't water
     *
     * @param cornerPosition
     * 		the position of the corner
     * @return if the corner touches land
     * @throws IllegalArgumentException
     * 		if the given position isn't valid
     */
    private boolean isCornerTouchingLand(java.awt.Point cornerPosition) {
        boolean foundLand = false;
        for (ch.zhaw.catan.model.SiedlerField field : getFields(cornerPosition)) {
            if ((field != null) && (field.getFieldType() != ch.zhaw.catan.Config.Land.WATER)) {
                foundLand = true;
            }
        }
        return foundLand;
    }

    /**
     * Verifies if the street has an adjacent street that belongs to
     * the same player.
     *
     * @return true, if there is an adjacent street.
     */
    public boolean hasAdjacentStreet(java.awt.Point startPoint, java.awt.Point endPoint, ch.zhaw.catan.Config.Faction owner) {
        for (java.awt.Point point : new java.awt.Point[]{ startPoint, endPoint }) {
            if (hasAdjacentStreet(point, owner)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the settlement has an adjacent street that belongs to
     * the same player.
     *
     * @return true, if there is an adjacent street.
     */
    public boolean hasAdjacentStreet(java.awt.Point position, ch.zhaw.catan.Config.Faction owner) {
        for (ch.zhaw.catan.model.SiedlerStreet street : getAdjacentEdges(position)) {
            if (street.getOwner().getFaction() == owner) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the street has an adjacent settlement that belongs to the same
     * player.
     *
     * @return true, if there is an adjacent street.
     */
    public boolean hasAdjacentSettlement(java.awt.Point startPoint, java.awt.Point endPoint, ch.zhaw.catan.Config.Faction owner) {
        for (java.awt.Point point : new java.awt.Point[]{ startPoint, endPoint }) {
            if (hasSettlement(point, owner)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the corner at the specified {@code location} has a settlement
     * which belongs to the specified {@code owner}.
     *
     * @param location
     * 		The location to check.
     * @param owner
     * 		The expected owner.
     * @return A value indicating whether the corner at the specified
    {@code location} has a settlement
    which belongs to the specified {@code owner}.
     */
    public boolean hasSettlement(java.awt.Point location, ch.zhaw.catan.Config.Faction owner) {
        return (hasCorner(location) && (getCorner(location) != null)) && (getCorner(location).getOwner().getFaction() == owner);
    }

    /**
     * Returns the fields associated with the specified dice value.
     *
     * @param dice
     * 		the dice value
     * @return the fields associated with the dice value
     */
    public java.util.List<java.awt.Point> getFieldsForDiceValue(int dice) {
        java.util.List<java.awt.Point> result = new java.util.ArrayList<>();
        for (java.awt.Point coordinate : getFields()) {
            if (getField(coordinate).getDiceValue() == dice) {
                result.add(coordinate);
            }
        }
        return java.util.Collections.unmodifiableList(result);
    }

    /**
     * Returns the {@link Land}s adjacent to the specified corner.
     *
     * @param corner
     * 		the corner
     * @return the list with the adjacent {@link Land}s
     */
    public java.util.List<ch.zhaw.catan.Config.Land> getLandsForCorner(java.awt.Point corner) {
        java.util.List<ch.zhaw.catan.Config.Land> result = new java.util.ArrayList<>();
        for (ch.zhaw.catan.model.SiedlerField field : getFields(corner)) {
            result.add(field.getFieldType());
        }
        return java.util.Collections.unmodifiableList(result);
    }

    /**
     * Returns a list of players around the given field
     *
     * @param field
     * 		the position of the field. It has to be a valid field position
     * @return the list of players
     * @throws IllegalArgumentException
     * 		if the given position isn't valid
     */
    public java.util.List<ch.zhaw.catan.model.Player> getPlayersAroundField(java.awt.Point field) {
        java.util.List<ch.zhaw.catan.model.SiedlerSettlement> settlementsAroundField = getCornersOfField(field);
        java.util.Set<ch.zhaw.catan.model.Player> playersAroundField = new java.util.HashSet<>();
        for (ch.zhaw.catan.model.SiedlerSettlement settlement : settlementsAroundField) {
            playersAroundField.add(settlement.getOwner());
        }
        return new java.util.ArrayList<>(playersAroundField);
    }

    /**
     * Creates a new game board with predetermined fields and dice values.
     *
     * @return A predetermined {@link GameBoard}.
     */
    public static ch.zhaw.catan.SiedlerBoard createStandartSiedlerBoard() {
        ch.zhaw.catan.SiedlerBoard board = new ch.zhaw.catan.SiedlerBoard();
        java.util.Map<java.awt.Point, java.lang.Integer> standardDiceNumbers = ch.zhaw.catan.Config.getStandardDiceNumberPlacement();
        for (java.util.Map.Entry<java.awt.Point, ch.zhaw.catan.Config.Land> standardLandEntry : ch.zhaw.catan.Config.getStandardLandPlacement().entrySet()) {
            board.addField(standardLandEntry.getKey(), new ch.zhaw.catan.model.SiedlerField(standardLandEntry.getValue(), standardDiceNumbers.getOrDefault(standardLandEntry.getKey(), -1)));
        }
        return board;
    }
}