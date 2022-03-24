package ch.zhaw.catan;
import org.beryx.textio.TextTerminal;
/**
 * This is a helper class for siedler game.
 */
public class InventoryPrinter {
    /**
     * the siedler game instance
     */
    private ch.zhaw.catan.SiedlerGame game;

    /**
     * only contains static methods, thus a constructor is not necessary
     */
    public InventoryPrinter(ch.zhaw.catan.SiedlerGame game) {
        this.game = game;
    }

    /**
     * Returns the resources in a map of the current player
     *
     * @param game
     * 		the game
     * @return the resources in a map
     */
    private java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> getResourceOfCurrentPlayer() {
        java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> playerInventory = new java.util.EnumMap<>(ch.zhaw.catan.Config.Resource.class);
        for (ch.zhaw.catan.Config.Resource resource : ch.zhaw.catan.Config.Resource.values()) {
            playerInventory.put(resource, game.getCurrentPlayerResourceStock(resource));
        }
        return playerInventory;
    }

    /**
     * Prints the inventory of the current player
     *
     * @param textTerminal
     * 		the text terminal
     */
    public void printPlayerInventory(org.beryx.textio.TextTerminal<?> textTerminal) {
        java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> playerInventory = getResourceOfCurrentPlayer();
        if (playerInventory.isEmpty()) {
            textTerminal.println("In your inventory is empty!");
        } else {
            textTerminal.println("Your inventory contains:");
            for (java.util.Map.Entry<ch.zhaw.catan.Config.Resource, java.lang.Integer> resourceEntry : playerInventory.entrySet()) {
                textTerminal.printf("\t- %s: %d\n", resourceEntry.getKey().name(), resourceEntry.getValue());
            }
        }
        textTerminal.println();
    }
}