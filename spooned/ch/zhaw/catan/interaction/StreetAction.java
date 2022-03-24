package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to build a street.
 */
public class StreetAction extends ch.zhaw.catan.interaction.StructureAction<ch.zhaw.catan.interaction.StreetAction.StreetInfo> {
    /**
     * Provides information about a street.
     */
    public static class StreetInfo {
        /**
         * The coordinate of the starting point of the street.
         */
        java.awt.Point start;

        /**
         * The coordinate of the ending point of the street.
         */
        java.awt.Point end;

        /**
         * Initializes a new instance of the {@link StreetInfo} class.
         *
         * @param start
         * 		The coordinate of the starting point of the street.
         * @param end
         * 		The coordinate of the ending point of the street.
         */
        public StreetInfo(java.awt.Point start, java.awt.Point end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Gets the coordinate of the starting point of the street.
         *
         * @return The coordinate of the starting point of the street.
         */
        public java.awt.Point getStart() {
            return start;
        }

        /**
         * Gets the coordinate of the ending point of the street.
         *
         * @return The coordinate of the ending point of the street.
         */
        public java.awt.Point getEnd() {
            return end;
        }
    }

    /**
     * Initializes a new instance of the {@link StreetAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public StreetAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    protected java.lang.String getXCoordinatePrompt() {
        return "X";
    }

    @java.lang.Override
    protected java.lang.String getYCoordinatePrompt() {
        return "Y";
    }

    @java.lang.Override
    protected java.lang.String getHeading() {
        return "Building a road";
    }

    @java.lang.Override
    protected ch.zhaw.catan.interaction.StreetAction.StreetInfo getStructureInfo() {
        getIO().getTextTerminal().println("Please enter the coordinate of the starting point.");
        java.awt.Point start = fetchCoordinate();
        getIO().getTextTerminal().println("Please enter the coordinate of the ending point.");
        java.awt.Point end = fetchCoordinate();
        return new ch.zhaw.catan.interaction.StreetAction.StreetInfo(start, end);
    }

    @java.lang.Override
    protected boolean buildStructure(ch.zhaw.catan.interaction.StreetAction.StreetInfo info) {
        return getGame().buildRoad(info.getStart(), info.getEnd());
    }
}