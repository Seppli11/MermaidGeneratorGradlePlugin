package ninja.seppli.umlgenerator.gradle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import ninja.seppli.umlgenerator.renderer.Renderer;
import ninja.seppli.umlgenerator.scanner.DiagramGenerator;
import ninja.seppli.umlgenerator.scanner.DiagramWriter;
import spoon.reflect.CtModel;

public abstract class UmlGeneratorTask extends DefaultTask {

    @TaskAction
    public void generateMermaidClassDiagramm() {
        UmlGeneratorExtension extension = getProject().getExtensions().getByType(UmlGeneratorExtension.class);
        Renderer renderer = extension.getRendererType().createRenderer();
        FileCollection inputPaths = extension.getFilesToScan();
        File outputFile = extension.getOutputFile();
        generateDiagram(renderer, inputPaths, outputFile);
    }

    private void generateDiagram(Renderer renderer, FileCollection inputPaths, File outputFile) {
        try {
            Path[] inputFiles = inputPaths.getFiles().stream().map(File::toPath)
                    .toArray(Path[]::new);
            getLogger().info("scanning directories and files {}", Arrays.toString(inputFiles));
            CtModel model = new DiagramGenerator()
                    .addFiles(inputFiles)
                    .scan();

            new DiagramWriter(model, renderer).writeFile(outputFile.toPath());
            getLogger().lifecycle("Generated model to \"{}\"", outputFile);
        } catch (IOException e) {
            getLogger().error("Couldn't write model to \"{}\"", outputFile, e);
        }
    }
}