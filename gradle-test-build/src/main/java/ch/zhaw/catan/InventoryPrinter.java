package ch.zhaw.catan;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Resource;

/**
 * This is a helper class for siedler game.
 */
public class InventoryPrinter {
    /**
     * the siedler game instance
     */
    private SiedlerGame game;

    /**
     * only contains static methods, thus a constructor is not necessary
     */
    public InventoryPrinter(SiedlerGame game) {
        this.game = game;
    }

    /**
     * Returns the resources in a map of the current player
     * 
     * @param game the game
     * @return the resources in a map
     */
    private Map<Resource, Integer> getResourceOfCurrentPlayer() {
        Map<Resource, Integer> playerInventory = new EnumMap<>(Resource.class);

        for (Resource resource : Resource.values()) {
            playerInventory.put(resource, game.getCurrentPlayerResourceStock(resource));
        }

        return playerInventory;
    }

    /**
     * Prints the inventory of the current player
     * 
     * @param textTerminal the text terminal
     */
    public void printPlayerInventory(TextTerminal<?> textTerminal) {
        Map<Resource, Integer> playerInventory = getResourceOfCurrentPlayer();

        if (playerInventory.isEmpty()) {
            textTerminal.println("In your inventory is empty!");
        } else {
            textTerminal.println("Your inventory contains:");

            for (Entry<Resource, Integer> resourceEntry : playerInventory.entrySet()) {
                textTerminal.printf("\t- %s: %d\n", resourceEntry.getKey().name(),
                        resourceEntry.getValue());
            }
        }
        textTerminal.println();
    }
}
