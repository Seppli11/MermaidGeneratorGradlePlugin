package ninja.seppli.umlgenerator.scanner;
import java.io.IOException;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
/**
 * This is a helper class to write a {@link DiagramModel} to a file or convert
 * it to a string
 */
public class DiagramWriter {
    private ninja.seppli.umlgenerator.scanner.model.DiagramModel model;

    private ninja.seppli.umlgenerator.renderer.Renderer renderer;

    /**
     * Constructs an MermaidWriter instance
     *
     * @param model
     * 		the model which will be written
     * @param renderer
     * 		the renderer which renders the model
     */
    public DiagramWriter(ninja.seppli.umlgenerator.scanner.model.DiagramModel model, ninja.seppli.umlgenerator.renderer.Renderer renderer) {
        this.model = model;
        this.renderer = renderer;
    }

    /**
     * Writes the mermaid model to the given file
     *
     * @param file
     * 		the file to write to
     * @throws IOException
     * 		
     */
    public void writeFile(java.nio.file.Path file) throws java.io.IOException {
        try (java.io.FileWriter writer = new java.io.FileWriter(file.toFile())) {
            writeFile(writer);
        }
    }

    /**
     * Writes the mermaid model to the given file
     *
     * @param file
     * 		the file to write to
     * @throws IOException
     * 		
     */
    public void writeFile(java.lang.String outputPath) throws java.io.IOException {
        writeFile(java.nio.file.Paths.get(outputPath));
    }

    /**
     * Writes the mermaid model to the given file writer
     *
     * @param fileWriter
     * 		the file writer to write to
     * @throws IOException
     * 		
     */
    public void writeFile(java.io.FileWriter fileWriter) throws java.io.IOException {
        fileWriter.write(renderer.render(model));
    }
}