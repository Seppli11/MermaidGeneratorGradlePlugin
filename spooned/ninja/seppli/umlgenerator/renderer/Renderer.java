package ninja.seppli.umlgenerator.renderer;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
/**
 * Renders a given {@link DiagramModel} to a string
 */
public interface Renderer {
    /**
     * Renders the given model to a string
     *
     * @param model
     * 		the model to render
     * @return the string representation
     */
    java.lang.String render(ninja.seppli.umlgenerator.scanner.model.DiagramModel model);
}