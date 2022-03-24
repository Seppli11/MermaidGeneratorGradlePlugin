package ch.zhaw.catan.interaction;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
/**
 * Represents an action.
 */
public abstract class Action {
    /**
     * The game this action belongs to.
     */
    private ch.zhaw.catan.SiedlerGame game;

    /**
     * A component for reading and writing data from and to the console.
     */
    private org.beryx.textio.TextIO io;

    /**
     * Initializes a new instance of the {@link Action} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    protected Action(ch.zhaw.catan.SiedlerGame game) {
        this.game = game;
        io = org.beryx.textio.TextIoFactory.getTextIO();
    }

    /**
     * Gets the game this action belongs to.
     *
     * @return The game this action belongs to.
     */
    protected ch.zhaw.catan.SiedlerGame getGame() {
        return game;
    }

    /**
     * Gets a component for reading and writing data from and to the console.
     *
     * @return A component for reading and writing data from and to the console.
     */
    protected org.beryx.textio.TextIO getIO() {
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