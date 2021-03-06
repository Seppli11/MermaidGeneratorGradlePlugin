package ch.zhaw.catan;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.interaction.BuildSelectionAction;
import ch.zhaw.catan.interaction.InitialSettlementAction;
import ch.zhaw.catan.interaction.InitialStreetAction;
import ch.zhaw.catan.interaction.PlaceThiefAction;
import ch.zhaw.catan.interaction.TradeAction;
import ch.zhaw.catan.model.ActionType;

/**
 * Provides the functionality to run a Catan game.
 */
public class Controller {
    /**
     * Represents a direction.
     */
    private enum Direction {
        NATURAL,
        REVERSE
    }

    /**
     * The number of points required for winning a game.
     */
    private static final int WIN_POINTS = 7;

    /**
     * A component for reading and writing data from and to the console.
     */
    private TextIO io;

    /**
     * A random instance used to roll the dies
     */
    private Random random = new Random();

    /**
     * Initializes a new instance of the {@link Controller} class.
     */
    public Controller() {
        io = TextIoFactory.getTextIO();
    }

    /**
     * Runs a Catan game.
     */
    public void run() {
        int playerCount = io.newIntInputReader().withMinVal(2).withMaxVal(4)
                .read("What's the number of players you'd like to play with?");

        SiedlerGame game = new SiedlerGame(WIN_POINTS, playerCount);
        SiedlerBoardTextView textView = new SiedlerBoardTextView(game.getBoard());
        runInitialPhase(playerCount, game, textView);
        runMainPhase(game, textView);

        if (game.getWinner() != null) {
            String faction = game.getWinner().name();
            getTextTerminal().getProperties().setPromptColor(faction);
            getTextTerminal().println(String.format("Team %s is the winner!", faction));
        } else {
            getTextTerminal().println("Goodbye!");
        }
    }

    /**
     * Runs the initial phase.
     *
     * @param playerCount The number of players to play with.
     * @param game        The actual game.
     * @param textView    A component for printing the game board.
     */
    private void runInitialPhase(int playerCount, SiedlerGame game, SiedlerBoardTextView textView) {
        for (Direction direction : Direction.values()) {
            for (int i = 0; i < playerCount; i++) {
                String faction = game.getCurrentPlayerFaction().name();
                getTextTerminal().getProperties().setPromptColor("");
                getTextTerminal().print(textView.toString());
                getTextTerminal().getProperties().setPromptColor(faction);

                getTextTerminal()
                        .println(String.format("Team %s, please place a settlement and a road.", faction));

                // Prompt user to build initial settlement and street until it succeeds
                new InitialSettlementAction(game, direction == Direction.REVERSE).executeUntilSuccessful();

                new InitialStreetAction(game).executeUntilSuccessful();

                if (i < playerCount - 1) {
                    if (direction == Direction.NATURAL) {
                        game.switchToNextPlayer();
                    } else {
                        game.switchToPreviousPlayer();
                    }
                }
            }
        }
    }

    /**
     * Runs the main phase of the game. This method exits, when the game is either
     * finished or a player quit
     * 
     * @param game     the game
     * @param textView the textview
     */
    private void runMainPhase(SiedlerGame game, SiedlerBoardTextView textView) {
        boolean quit;

        do {
            printCurrentGameState(game, textView);
            getTextTerminal().println("Throwing the dices...");
            int diceValue = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);
            getTextTerminal().println(String.format("Alea iacta est! %d!", diceValue));
            Map<Faction, List<Resource>> newResources = game.throwDice(diceValue);
            printReceivedResources(newResources);
            getTextTerminal().getProperties().setPromptColor(game.getCurrentPlayerFaction().name());
            new InventoryPrinter(game).printPlayerInventory(getTextTerminal());
            if (diceValue == 7) {
                new PlaceThiefAction(game).executeUntilSuccessful();
            }
            quit = runTurn(game, textView);
            switchIfNoWinner(game);
        } while (game.getWinner() == null && !quit);
    }

    /**
     * Prints the current state of the map and thief
     * 
     * @param game     the game
     * @param textView the text view to print on
     */
    private void printCurrentGameState(SiedlerGame game, SiedlerBoardTextView textView) {
        String faction = game.getCurrentPlayerFaction().name();
        getTextTerminal().getProperties().setPromptColor("");
        getTextTerminal().print(textView.toString());
        getTextTerminal().printf("The thief is at %.0f/%.0f\n", game.getThiefPosition().getX(),
                game.getThiefPosition().getY());
        getTextTerminal().getProperties().setPromptColor(faction);
        getTextTerminal().println(String.format("It's team %s's turn!", faction));
    }

    /**
     * Runs one turn of a player. It exists only when the player choses Quit or End
     * 
     * @param game the game
     * @return true, if the game should quit
     */
    private boolean runTurn(SiedlerGame game, SiedlerBoardTextView textView) {
        ActionType actionType = null;
        do {
            actionType = promptNextAction();
            switch (actionType) {
                case Build:
                    if (new BuildSelectionAction(game).execute()) {
                        getTextTerminal().print(textView.toString());
                        new InventoryPrinter(game).printPlayerInventory(getTextTerminal());
                    }
                    break;
                case Trade:
                    new TradeAction(game).execute();
                    break;
                case Quit:
                    return true;
                default:
                    break;
            }
        } while (actionType != ActionType.End && game.getWinner() == null);

        return false;
    }

    /**
     * Prints the specified {@code newResources} to the console.
     *
     * @param newResources The newly received resources.
     */
    private void printReceivedResources(Map<Faction, List<Resource>> newResources) {
        for (Map.Entry<Faction, List<Resource>> entry : newResources.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                getTextTerminal().getProperties().setPromptColor(entry.getKey().name());
                getTextTerminal().println(String
                        .format("Team %s got following resources from their settlements and cities:", entry.getKey()));

                for (Resource resource : entry.getValue()) {
                    getTextTerminal().println(String.format("(%s) %s", resource, resource.name()));
                }
                getTextTerminal().println();
            }
        }
        getTextTerminal().println();
    }

    /**
     * Gets a component for reading data from the console.
     *
     * @return A component for reading data from the console.
     */
    private TextIO getIO() {
        return io;
    }

    /**
     * Gets a component for writing to the console.
     *
     * @return A component for writing to the console.
     */
    private TextTerminal<?> getTextTerminal() {
        return getIO().getTextTerminal();
    }

    /**
     * Switches to the next player if no winner has been determined so far.
     *
     * @param game The current game.
     */
    private void switchIfNoWinner(SiedlerGame game) {
        if (game.getWinner() == null) {
            game.switchToNextPlayer();
        }
    }

    /**
     * Asks the user for the next action to execute.
     *
     * @return The next action to execute.
     */
    private ActionType promptNextAction() {
        return getIO().newEnumInputReader(ActionType.class).read("Please select your next action");
    }
}
