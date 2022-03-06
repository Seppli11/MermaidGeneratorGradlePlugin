package ch.zhaw.catan.interaction;

import java.awt.Point;
import ch.zhaw.catan.SiedlerGame;

/**
 * Provides the functionality to build a city.
 */
public class CityAction extends BuildingAction {
    /**
     * Initializes a new instance of the {@link CityAction} class.
     *
     * @param game The game this action belongs to.
     */
    public CityAction(SiedlerGame game) {
        super(game);
    }

    @Override
    protected boolean buildStructure(Point coordinate) {
        return getGame().buildCity(coordinate);
    }

    @Override
    protected String getHeading() {
        return "Building a city";
    }
}
