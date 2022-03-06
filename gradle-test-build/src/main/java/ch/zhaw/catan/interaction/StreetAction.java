package ch.zhaw.catan.interaction;

import java.awt.Point;
import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build a street.
 */
public class StreetAction extends StructureAction<StreetAction.StreetInfo> {
    /**
     * Provides information about a street.
     */
    public static class StreetInfo {
        /**
         * The coordinate of the starting point of the street.
         */
        Point start;

        /**
         * The coordinate of the ending point of the street.
         */
        Point end;

        /**
         * Initializes a new instance of the {@link StreetInfo} class.
         *
         * @param start The coordinate of the starting point of the street.
         * @param end   The coordinate of the ending point of the street.
         */
        public StreetInfo(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Gets the coordinate of the starting point of the street.
         *
         * @return The coordinate of the starting point of the street.
         */
        public Point getStart() {
            return start;
        }

        /**
         * Gets the coordinate of the ending point of the street.
         *
         * @return The coordinate of the ending point of the street.
         */
        public Point getEnd() {
            return end;
        }
    }

    /**
     * Initializes a new instance of the {@link StreetAction} class.
     *
     * @param game The game this action belongs to.
     */
    public StreetAction(SiedlerGame game) {
        super(game);
    }

    @Override
    protected String getXCoordinatePrompt() {
        return "X";
    }

    @Override
    protected String getYCoordinatePrompt() {
        return "Y";
    }

    @Override
    protected String getHeading() {
        return "Building a road";
    }

    @Override
    protected StreetInfo getStructureInfo() {
        getIO().getTextTerminal().println("Please enter the coordinate of the starting point.");
        Point start = fetchCoordinate();
        getIO().getTextTerminal().println("Please enter the coordinate of the ending point.");
        Point end = fetchCoordinate();
        return new StreetInfo(start, end);
    }

    @Override
    protected boolean buildStructure(StreetInfo info) {
        return getGame().buildRoad(info.getStart(), info.getEnd());
    }
}
