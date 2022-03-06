package ch.zhaw.catan.model;

/**
 * This enum contains a list of allowed console input keywords.
 *
 * Opposed to the naming convention we usually use when coding projects,
 * the values of this enum are named in PascalCase in order to make them look better in enum input prompts.
 * 
 * @author Jonas Costa
 */
public enum ActionType {
    /**
     * The player wants to trade with the bank (trading with other players or ports
     * isn't supported)
     */
    Trade,
    /**
     * The player wants to build a settlement or upgrade it to a city
     */
    Build,
    /**
     * The player wants to end his turn
     */
    End,
    /**
     * The player wants to quit the game
     */
    Quit;
}
