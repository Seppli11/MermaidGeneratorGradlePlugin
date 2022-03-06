package ninja.seppli.umlgenerator.scanner;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ninja.seppli.umlgenerator.renderer.Renderer;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;

/**
 * This is a helper class to write a {@link DiagramModel} to a file or convert
 * it to a string
 */
public class DiagramWriter {
    private DiagramModel model;
    private Renderer renderer;

    /**
     * Constructs an MermaidWriter instance
     * 
     * @param model    the model which will be written
     * @param renderer the renderer which renders the model
     */
    public DiagramWriter(DiagramModel model, Renderer renderer) {
        this.model = model;
        this.renderer = renderer;
    }

    /**
     * Writes the mermaid model to the given file
     * 
     * @param file the file to write to
     * @throws IOException
     */
    public void writeFile(Path file) throws IOException {
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writeFile(writer);
        }
    }

    /**
     * Writes the mermaid model to the given file
     * 
     * @param file the file to write to
     * @throws IOException
     */
    public void writeFile(String outputPath) throws IOException {
        writeFile(Paths.get(outputPath));
    }

    /**
     * Writes the mermaid model to the given file writer
     * 
     * @param fileWriter the file writer to write to
     * @throws IOException
     */
    public void writeFile(FileWriter fileWriter) throws IOException {
        fileWriter.write(renderer.render(model));
    }

}
