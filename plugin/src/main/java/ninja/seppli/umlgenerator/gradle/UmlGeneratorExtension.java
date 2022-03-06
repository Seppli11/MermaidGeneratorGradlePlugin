package ninja.seppli.umlgenerator.gradle;

import java.io.File;
import java.util.Set;
import java.util.function.Supplier;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import ninja.seppli.umlgenerator.renderer.MermaidRenderer;
import ninja.seppli.umlgenerator.renderer.PlantumlRenderer;
import ninja.seppli.umlgenerator.renderer.Renderer;

/**
 * MermaidGeneratorExtension
 */
public class UmlGeneratorExtension {
    private Project project;

    private FileCollection filesToScan;
    private File outputFile;
    private RendererType rendererType = RendererType.PLANTUML;

    public UmlGeneratorExtension(Project project) {
        this.project = project;
        setupDefaultValues();
    }

    private void setupDefaultValues() {
        // filesToScan
        SourceSetContainer sourceSets = project.getExtensions().findByType(SourceSetContainer.class);
        if (sourceSets != null) {
            File[] sourceFiles = sourceSets.getAsMap().values().stream().map(SourceSet::getAllSource)
                    .map(SourceDirectorySet::getSrcDirs).flatMap(Set::stream).toArray(File[]::new);
            filesToScan = project.files((Object) sourceFiles);
        }
    }

    /**
     * @return the filesToScan
     */
    public FileCollection getFilesToScan() {
        return filesToScan;
    }

    /**
     * @param filesToScan the filesToScan to set
     */
    public void setFilesToScan(FileCollection filesToScan) {
        this.filesToScan = filesToScan;
    }

    /**
     * @param filesToScan the filesToScan to set
     */
    public void setFilesToScan(File filesToScan) {
        this.filesToScan = project.files(filesToScan);
    }

    /**
     * @return the outputFile
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * @param outputFile the outputFile to set
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public RendererType getRendererType() {
        return rendererType;
    }

    public void setRendererType(RendererType rendererType) {
        this.rendererType = rendererType;
    }

    public enum RendererType {
        MERMAID(() -> new MermaidRenderer()),
        PLANTUML(() -> new PlantumlRenderer());

        private Supplier<Renderer> supplier;

        private RendererType(Supplier<Renderer> supplier) {
            this.supplier = supplier;
        }

        public Renderer createRenderer() {
            return supplier.get();
        }
    }

}