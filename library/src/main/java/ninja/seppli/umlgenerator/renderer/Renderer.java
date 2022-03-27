package ninja.seppli.umlgenerator.renderer;

import ninja.seppli.umlgenerator.options.UmlOptions;
import spoon.reflect.CtModel;

/**
 * Renders a given {@link DiagramModel} to a string
 */
public interface Renderer {
    /**
     * Renders the given model to a string
     * 
     * @param model   the model to render
     * @param options the options
     * @return the string representation
     */
    String render(CtModel model, UmlOptions options);
}
