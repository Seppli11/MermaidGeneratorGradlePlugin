package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Land;
import ch.zhaw.catan.model.Player;
import ch.zhaw.catan.model.SiedlerField;
import ch.zhaw.catan.model.SiedlerSettlement;
import ch.zhaw.catan.model.SiedlerStreet;
import ch.zhaw.hexboard.HexBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SiedlerBoard extends HexBoard<SiedlerField, SiedlerSettlement, SiedlerStreet, Void> {
    /**
     * Verifies if the selected location is a valid location for a street.
     *
     * @return true, if the location of the street is valid.
     */
    public boolean isValidStreetLocation(Point startPoint, Point endPoint, Faction owner) {
        return (hasAdjacentSettlement(startPoint, endPoint, owner)
                || hasAdjacentStreet(startPoint, endPoint, owner)) && hasEdge(startPoint, endPoint)
                && getEdge(startPoint, endPoint) == null && isCornerTouchingLand(startPoint)
                && isCornerTouchingLand(endPoint);
    }

    /**
     * Verifies if the selected location is a valid location for a settlement.
     *
     * @return true, if the location of the settlement is valid.
     */
    public boolean isValidSettlementLocation(Point position, Faction owner) {
        return isValidInitialSettlementLocation(position, owner) && hasAdjacentStreet(position, owner);
    }

    /**
     * Verifies if the selected location is a valid location for an initial street.
     *
     * @return true, if the location of the street is valid.
     */
    public boolean isValidInitialStreetLocation(Point startPoint, Point endPoint, Faction owner) {
        return isValidStreetLocation(startPoint, endPoint, owner)
                && ((hasSettlement(startPoint, owner) && getAdjacentEdges(startPoint).isEmpty())
                        || (hasSettlement(endPoint, owner) && getAdjacentEdges(endPoint).isEmpty()));
    }

    /**
     * Verifies if the selected location is a valid location for an initial
     * settlement.
     *
     * @return true, if the location of the settlement is valid.
     */
    public boolean isValidInitialSettlementLocation(Point position, Faction owner) {
        return isCornerTouchingLand(position) && getNeighboursOfCorner(position).isEmpty() && hasCorner(position)
                && getCorner(position) == null;
    }

    /**
     * Checks if the corner touches a field that isn't water
     * 
     * @param cornerPosition the position of the corner
     * @return if the corner touches land
     * @throws IllegalArgumentException if the given position isn't valid
     */
    private boolean isCornerTouchingLand(Point cornerPosition) {
        boolean foundLand = false;
        for (SiedlerField field : getFields(cornerPosition)) {
            if (field != null && field.getFieldType() != Land.WATER) {
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
    public boolean hasAdjacentStreet(Point startPoint, Point endPoint, Faction owner) {
        for (Point point : new Point[] { startPoint, endPoint }) {
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
    public boolean hasAdjacentStreet(Point position, Faction owner) {
        for (SiedlerStreet street : getAdjacentEdges(position)) {
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
    public boolean hasAdjacentSettlement(Point startPoint, Point endPoint, Faction owner) {
        for (Point point : new Point[] { startPoint, endPoint }) {
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
     * @param location The location to check.
     * @param owner    The expected owner.
     * @return A value indicating whether the corner at the specified
     *         {@code location} has a settlement
     *         which belongs to the specified {@code owner}.
     */
    public boolean hasSettlement(Point location, Faction owner) {
        return hasCorner(location) && getCorner(location) != null
                && getCorner(location).getOwner().getFaction() == owner;
    }

    /**
     * Returns the fields associated with the specified dice value.
     *
     * @param dice the dice value
     * @return the fields associated with the dice value
     */
    public List<Point> getFieldsForDiceValue(int dice) {
        List<Point> result = new ArrayList<>();

        for (Point coordinate : getFields()) {
            if (getField(coordinate).getDiceValue() == dice) {
                result.add(coordinate);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the {@link Land}s adjacent to the specified corner.
     *
     * @param corner the corner
     * @return the list with the adjacent {@link Land}s
     */
    public List<Land> getLandsForCorner(Point corner) {
        List<Land> result = new ArrayList<>();

        for (SiedlerField field : getFields(corner)) {
            result.add(field.getFieldType());
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Returns a list of players around the given field
     * 
     * @param field the position of the field. It has to be a valid field position
     * @return the list of players
     * @throws IllegalArgumentException if the given position isn't valid
     */
    public List<Player> getPlayersAroundField(Point field) {
        List<SiedlerSettlement> settlementsAroundField = getCornersOfField(field);
        Set<Player> playersAroundField = new HashSet<>();
        for (SiedlerSettlement settlement : settlementsAroundField) {
            playersAroundField.add(settlement.getOwner());
        }
        return new ArrayList<>(playersAroundField);
    }

    /**
     * Creates a new game board with predetermined fields and dice values.
     * 
     * @return A predetermined {@link GameBoard}.
     */
    public static SiedlerBoard createStandartSiedlerBoard() {
        SiedlerBoard board = new SiedlerBoard();
        Map<Point, Integer> standardDiceNumbers = Config.getStandardDiceNumberPlacement();
        for (Entry<Point, Land> standardLandEntry : Config.getStandardLandPlacement().entrySet()) {
            board.addField(standardLandEntry.getKey(), new SiedlerField(standardLandEntry.getValue(),
                    standardDiceNumbers.getOrDefault(standardLandEntry.getKey(), -1)));
        }
        return board;
    }

}
