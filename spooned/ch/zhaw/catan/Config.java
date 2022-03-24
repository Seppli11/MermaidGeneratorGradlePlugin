package ch.zhaw.catan;
/**
 * This class specifies the most important and basic parameters of the game
 * Catan.
 * <p>
 * The class provides definitions such as for the type and number of resource
 * cards or the number of available road elements per player. Furthermore, it
 * provides a dice number to field and a field to land type mapping for the
 * standard setup detailed <a href=
 * "https://www.catan.de/files/downloads/4002051693602_catan_-_das_spiel_0.pdf">here</a>
 * </p>
 *
 * @author tebe
 */
public class Config {
    // Minimum number of players
    // Note: The max. number is equal to the number of factions (see Faction enum)
    public static final int MIN_NUMBER_OF_PLAYERS = 2;

    // Initial thief position (on the desert field)
    public static final java.awt.Point INITIAL_THIEF_POSITION = new java.awt.Point(7, 11);

    // Available factions
    public enum Faction {

        RED("rr"),
        BLUE("bb"),
        GREEN("gg"),
        YELLOW("yy");
        private java.lang.String name;

        private Faction(java.lang.String name) {
            this.name = name;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return name;
        }
    }

    // RESOURCE CARD DECK
    public static final java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> INITIAL_RESOURCE_CARDS_BANK = java.util.Map.of(ch.zhaw.catan.Config.Resource.LUMBER, 19, ch.zhaw.catan.Config.Resource.BRICK, 19, ch.zhaw.catan.Config.Resource.WOOL, 19, ch.zhaw.catan.Config.Resource.GRAIN, 19, ch.zhaw.catan.Config.Resource.ORE, 19);

    // SPECIFICATION OF AVAILABLE RESOURCE TYPES
    /**
     * This {@link Enum} specifies the available resource types in the game.
     *
     * @author tebe
     */
    public enum Resource {

        GRAIN("GR"),
        WOOL("WL"),
        LUMBER("LU"),
        ORE("OR"),
        BRICK("BR");
        private java.lang.String name;

        private Resource(java.lang.String name) {
            this.name = name;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return name;
        }
    }

    // SPECIFICATION OF AVAILABLE LAND TYPES
    /**
     * This {@link Enum} specifies the available lands in the game. Some land types
     * produce resources (e.g., {@link Land#FOREST}, others do not (e.g.,
     * {@link Land#WATER}.
     *
     * @author tebe
     */
    public enum Land {

        FOREST(ch.zhaw.catan.Config.Resource.LUMBER),
        PASTURE(ch.zhaw.catan.Config.Resource.WOOL),
        FIELDS(ch.zhaw.catan.Config.Resource.GRAIN),
        MOUNTAIN(ch.zhaw.catan.Config.Resource.ORE),
        HILLS(ch.zhaw.catan.Config.Resource.BRICK),
        WATER("~~"),
        DESERT("--");
        private ch.zhaw.catan.Config.Resource resource = null;

        private java.lang.String name;

        private Land(ch.zhaw.catan.Config.Resource resource) {
            this(resource.toString());
            this.resource = resource;
        }

        private Land(java.lang.String name) {
            this.name = name;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return this.name;
        }

        /**
         * Returns the {@link Resource} that this land provides or null,
         * if it does not provide any.
         *
         * @return the {@link Resource} or null
         */
        public ch.zhaw.catan.Config.Resource getResource() {
            return resource;
        }
    }

    // STRUCTURES (with costs)
    private static final int NUMBER_OF_ROADS_PER_PLAYER = 15;

    private static final int NUMBER_OF_SETTLEMENTS_PER_PLAYER = 5;

    private static final int NUMBER_OF_CITIES_PER_PLAYER = 4;

    public static final int MAX_CARDS_IN_HAND_NO_DROP = 7;

    /**
     * This enum models the different structures that can be built.
     * <p>
     * The enum provides information about the cost of a structure and how many of
     * these structures are available per player.
     * </p>
     */
    public enum Structure {

        SETTLEMENT(java.util.List.of(ch.zhaw.catan.Config.Resource.LUMBER, ch.zhaw.catan.Config.Resource.BRICK, ch.zhaw.catan.Config.Resource.WOOL, ch.zhaw.catan.Config.Resource.GRAIN), ch.zhaw.catan.Config.NUMBER_OF_SETTLEMENTS_PER_PLAYER),
        CITY(java.util.List.of(ch.zhaw.catan.Config.Resource.ORE, ch.zhaw.catan.Config.Resource.ORE, ch.zhaw.catan.Config.Resource.ORE, ch.zhaw.catan.Config.Resource.GRAIN, ch.zhaw.catan.Config.Resource.GRAIN), ch.zhaw.catan.Config.NUMBER_OF_CITIES_PER_PLAYER),
        ROAD(java.util.List.of(ch.zhaw.catan.Config.Resource.LUMBER, ch.zhaw.catan.Config.Resource.BRICK), ch.zhaw.catan.Config.NUMBER_OF_ROADS_PER_PLAYER);
        private java.util.List<ch.zhaw.catan.Config.Resource> costs;

        private int stockPerPlayer;

        private Structure(java.util.List<ch.zhaw.catan.Config.Resource> costs, int stockPerPlayer) {
            this.costs = costs;
            this.stockPerPlayer = stockPerPlayer;
        }

        /**
         * Returns the build costs of this structure.
         * <p>
         * Each list entry represents a resource card. The value of an entry (e.g., {@link Resource#LUMBER})
         * identifies the resource type of the card.
         * </p>
         *
         * @return the build costs
         */
        public java.util.List<ch.zhaw.catan.Config.Resource> getCosts() {
            return costs;
        }

        /**
         * Returns the build costs of this structure.
         *
         * @return the build costs in terms of the number of resource cards per resource type
         */
        public java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> getCostsAsMap() {
            return costs.stream().collect(java.util.stream.Collectors.groupingBy(java.util.function.Function.identity(), java.util.stream.Collectors.counting()));
        }

        /**
         * Returns the number of pieces that are available of a certain structure (per
         * player). For example, there are {@link Config#NUMBER_OF_ROADS_PER_PLAYER}
         * pieces of the structure {@link Structure#ROAD} per player.
         *
         * @return the stock per player
         */
        public int getStockPerPlayer() {
            return stockPerPlayer;
        }
    }

    // STANDARD FIXED DICE NUMBER TO FIELD SETUP
    /**
     * Returns a mapping of the dice values per field.
     *
     * @return the dice values per field
     */
    public static final java.util.Map<java.awt.Point, java.lang.Integer> getStandardDiceNumberPlacement() {
        java.util.Map<java.awt.Point, java.lang.Integer> assignment = new java.util.HashMap<>();
        assignment.put(new java.awt.Point(4, 8), 2);
        assignment.put(new java.awt.Point(7, 5), 3);
        assignment.put(new java.awt.Point(8, 14), 3);
        assignment.put(new java.awt.Point(6, 8), 4);
        assignment.put(new java.awt.Point(7, 17), 4);
        assignment.put(new java.awt.Point(3, 11), 5);
        assignment.put(new java.awt.Point(8, 8), 5);
        assignment.put(new java.awt.Point(5, 5), 6);
        assignment.put(new java.awt.Point(9, 11), 6);
        assignment.put(new java.awt.Point(7, 11), 7);
        assignment.put(new java.awt.Point(9, 5), 8);
        assignment.put(new java.awt.Point(5, 17), 8);
        assignment.put(new java.awt.Point(5, 11), 9);
        assignment.put(new java.awt.Point(11, 11), 9);
        assignment.put(new java.awt.Point(4, 14), 10);
        assignment.put(new java.awt.Point(10, 8), 10);
        assignment.put(new java.awt.Point(6, 14), 11);
        assignment.put(new java.awt.Point(9, 17), 11);
        assignment.put(new java.awt.Point(10, 14), 12);
        return java.util.Collections.unmodifiableMap(assignment);
    }

    // STANDARD FIXED LAND SETUP
    /**
     * Returns the field (coordinate) to {@link Land} mapping for the <a href=
     * "https://www.catan.de/files/downloads/4002051693602_catan_-_das_spiel_0.pdf">standard
     * setup</a> of the game Catan..
     *
     * @return the field to {@link Land} mapping for the standard setup
     */
    public static final java.util.Map<java.awt.Point, ch.zhaw.catan.Config.Land> getStandardLandPlacement() {
        java.util.Map<java.awt.Point, ch.zhaw.catan.Config.Land> assignment = new java.util.HashMap<>();
        java.awt.Point[] water = new java.awt.Point[]{ new java.awt.Point(4, 2), new java.awt.Point(6, 2), new java.awt.Point(8, 2), new java.awt.Point(10, 2), new java.awt.Point(3, 5), new java.awt.Point(11, 5), new java.awt.Point(2, 8), new java.awt.Point(12, 8), new java.awt.Point(1, 11), new java.awt.Point(13, 11), new java.awt.Point(2, 14), new java.awt.Point(12, 14), new java.awt.Point(3, 17), new java.awt.Point(11, 17), new java.awt.Point(4, 20), new java.awt.Point(6, 20), new java.awt.Point(8, 20), new java.awt.Point(10, 20) };
        for (java.awt.Point p : water) {
            assignment.put(p, ch.zhaw.catan.Config.Land.WATER);
        }
        assignment.put(new java.awt.Point(5, 5), ch.zhaw.catan.Config.Land.FOREST);
        assignment.put(new java.awt.Point(7, 5), ch.zhaw.catan.Config.Land.PASTURE);
        assignment.put(new java.awt.Point(9, 5), ch.zhaw.catan.Config.Land.PASTURE);
        assignment.put(new java.awt.Point(4, 8), ch.zhaw.catan.Config.Land.FIELDS);
        assignment.put(new java.awt.Point(6, 8), ch.zhaw.catan.Config.Land.MOUNTAIN);
        assignment.put(new java.awt.Point(8, 8), ch.zhaw.catan.Config.Land.FIELDS);
        assignment.put(new java.awt.Point(10, 8), ch.zhaw.catan.Config.Land.FOREST);
        assignment.put(new java.awt.Point(3, 11), ch.zhaw.catan.Config.Land.FOREST);
        assignment.put(new java.awt.Point(5, 11), ch.zhaw.catan.Config.Land.HILLS);
        assignment.put(new java.awt.Point(7, 11), ch.zhaw.catan.Config.Land.DESERT);
        assignment.put(new java.awt.Point(9, 11), ch.zhaw.catan.Config.Land.MOUNTAIN);
        assignment.put(new java.awt.Point(11, 11), ch.zhaw.catan.Config.Land.FIELDS);
        assignment.put(new java.awt.Point(4, 14), ch.zhaw.catan.Config.Land.FIELDS);
        assignment.put(new java.awt.Point(6, 14), ch.zhaw.catan.Config.Land.MOUNTAIN);
        assignment.put(new java.awt.Point(8, 14), ch.zhaw.catan.Config.Land.FOREST);
        assignment.put(new java.awt.Point(10, 14), ch.zhaw.catan.Config.Land.PASTURE);
        assignment.put(new java.awt.Point(5, 17), ch.zhaw.catan.Config.Land.PASTURE);
        assignment.put(new java.awt.Point(7, 17), ch.zhaw.catan.Config.Land.HILLS);
        assignment.put(new java.awt.Point(9, 17), ch.zhaw.catan.Config.Land.HILLS);
        return java.util.Collections.unmodifiableMap(assignment);
    }
}