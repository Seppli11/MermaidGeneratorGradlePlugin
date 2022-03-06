package ch.zhaw.catan.interaction;

import java.awt.Point;
import org.beryx.textio.IntInputReader;
import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to ask a user for building a structure.
 */
public abstract class StructureAction<T> extends Action {
    /**
     * Initializes a new instance of the {@link StructureAction} class.
     *
     * @param game The game this action belongs to.
     */
    protected StructureAction(SiedlerGame game) {
        super(game);
    }

    @Override
    public boolean execute() {
        getIO().getTextTerminal().println(getHeading());
        T info = getStructureInfo();

        if (buildStructure(info)) {
            getIO().getTextTerminal().println("The action was successful");
            return true;
        } else {
            getIO().getTextTerminal().println("An error occurred! Please review your input.");
            return false;
        }
    }

    /**
     * Gets the maximal inclusive X coordinate.
     *
     * @return The maximal inclusive X coordinate.
     */
    protected int getMaxXCoordinate() {
        return 14;
    }

    /**
     * Gets the maximal inclusive Y coordinate.
     *
     * @return The maximal inclusive Y coordinate.
     */
    protected int getMaxYCoordinate() {
        return 22;
    }

    /**
     * Gets the heading to print when executing the action.
     *
     * @return The heading to print when executing the action.
     */
    protected abstract String getHeading();

    /**
     * Gets the prompt for asking for the X coordinate.
     *
     * @returns The prompt for asking for the X coordinate.
     */
    protected String getXCoordinatePrompt() {
        return "Please enter the X coordinate";
    }

    /**
     * Gets the prompt for asking for the Y coordinate.
     *
     * @returns The prompt for asking for the Y coordinate.
     */
    protected String getYCoordinatePrompt() {
        return "Please enter the Y coordinate";
    }

    /**
     * Fetches a coordinate from the user input.
     *
     * @return The coordinate that was fetched.
     */
    protected Point fetchCoordinate() {
        IntInputReader reader = getIO().newIntInputReader().withMinVal(0);

        return new Point(
                reader.withMaxVal(getMaxXCoordinate()).read(getXCoordinatePrompt()),
                reader.withMaxVal(getMaxYCoordinate()).read(getYCoordinatePrompt()));
    }

    /**
     * Fetches the information for performing the action from the user input.
     *
     * @return The coordinate that was fetched.
     */
    protected abstract T getStructureInfo();

    /**
     * Builds the specified structure.
     *
     * @param info The information for building the structure.
     *
     * @return A value indicating whether the action was successful.
     */
    protected abstract boolean buildStructure(T info);
}
