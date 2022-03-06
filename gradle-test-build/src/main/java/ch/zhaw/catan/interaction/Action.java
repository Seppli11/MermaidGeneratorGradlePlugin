package ch.zhaw.catan.interaction;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import ch.zhaw.catan.SiedlerGame;

/**
 * Represents an action.
 */
public abstract class Action {
    /**
     * The game this action belongs to.
     */
    private SiedlerGame game;

    /**
     * A component for reading and writing data from and to the console.
     */
    private TextIO io;

    /**
     * Initializes a new instance of the {@link Action} class.
     *
     * @param game The game this action belongs to.
     */
    protected Action(SiedlerGame game) {
        this.game = game;
        io = TextIoFactory.getTextIO();
    }

    /**
     * Gets the game this action belongs to.
     *
     * @return The game this action belongs to.
     */
    protected SiedlerGame getGame() {
        return game;
    }

    /**
     * Gets a component for reading and writing data from and to the console.
     *
     * @return A component for reading and writing data from and to the console.
     */
    protected TextIO getIO() {
        return io;
    }

    /**
     * Executes the action.
     *
     * @return A value indicating whether the action was successful.
     */
    public abstract boolean execute();

    /**
     * Executes {@link #execute()} until it returns true
     */
    public void executeUntilSuccessful() {
        while (!execute()) {
        }
    }
}
