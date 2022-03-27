package ninja.seppli.umlgenerator.gradle;

import java.io.File;
import java.util.Set;
import java.util.function.Supplier;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.util.internal.ConfigureUtil;

import groovy.lang.Closure;
import ninja.seppli.umlgenerator.options.GeneralUmlOptions;
import ninja.seppli.umlgenerator.options.PlantumlOptions;
import ninja.seppli.umlgenerator.options.UmlOptions;
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

    private UmlOptions umlOptions = new UmlOptions(GeneralUmlOptions.DEFAULT_OPTIONS, new PlantumlOptions());

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
            sourceFiles = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getAllSource().getSrcDirs()
                    .toArray(File[]::new);
            filesToScan = project.files((Object) sourceFiles);
        }
    }

    public void umlOptions(Closure<Void> clsoure) {
        umlOptions(ConfigureUtil.configureUsing(clsoure));
    }

    public void umlOptions(Action<GeneralUmlOptions> action) {
        action.execute(umlOptions.getGeneralUmlOptions());
    }

    public void plantumlOptions(Closure<Void> closure) {
        umlOptions(ConfigureUtil.configureUsing(closure));
    }

    public void plantumlOptions(Action<PlantumlOptions> action) {
        action.execute(umlOptions.getPlantumlOptions());
    }

    /**
     * @return the options
     */
    public UmlOptions getUmlOptions() {
        return umlOptions;
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
        // MERMAID(() -> new MermaidRenderer()),
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