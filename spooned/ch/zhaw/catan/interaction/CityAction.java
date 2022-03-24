package ch.zhaw.catan.interaction;
/**
 * Provides the functionality to build a city.
 */
public class CityAction extends ch.zhaw.catan.interaction.BuildingAction {
    /**
     * Initializes a new instance of the {@link CityAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public CityAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    protected boolean buildStructure(java.awt.Point coordinate) {
        return getGame().buildCity(coordinate);
    }

    @java.lang.Override
    protected java.lang.String getHeading() {
        return "Building a city";
    }
}