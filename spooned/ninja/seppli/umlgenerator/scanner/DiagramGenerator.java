/* This Java source file was generated by the Gradle 'init' task. */
package ninja.seppli.umlgenerator.scanner;
import java.nio.file.InvalidPathException;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
/**
 * Generates a {@link DiagramModel} from java source files
 */
public class DiagramGenerator {
    /**
     * logger
     */
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    /**
     * which packages are ignored by default
     */
    private final ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList DEFAULT_PACKAGES_TO_IGNORE = ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList.createFromPackagePaths("java.lang");

    /**
     * the paths which should be scanned
     */
    private java.util.Set<java.nio.file.Path> pathList = new java.util.HashSet<>();

    /**
     * Constructor
     */
    public DiagramGenerator() {
    }

    /**
     * The given files will be scanned when {@link #scan()} is called.
     * If a folder is given, all files in the folder and in its subfolders are
     * scanned.
     *
     * @param paths
     * 		the paths to scan
     * @returns this instance for a fluent like api
     * @see #addFiles(Path...)
     * @throws InvalidPathException
     * 		if the given pahts couldn't be converted to a
     * 		valid path
     */
    public ninja.seppli.umlgenerator.scanner.DiagramGenerator addFiles(java.lang.String... paths) {
        java.nio.file.Path[] pathObjs = java.util.Arrays.stream(paths).map(java.nio.file.Paths::get).toArray(java.nio.file.Path[]::new);
        addFiles(pathObjs);
        return this;
    }

    /**
     * The given files will be scanned when {@link #scan()} is called.
     * If a folder is given, all files in the folder and in its subfolders are
     * scanned.
     *
     * @param paths
     * 		the paths to scan
     * @returns this instance for a fluent like api
     * @see #addFiles(String...)
     */
    public ninja.seppli.umlgenerator.scanner.DiagramGenerator addFiles(java.nio.file.Path... paths) {
        pathList.addAll(java.util.Arrays.asList(paths));
        return this;
    }

    /**
     * Scanns the folder and files added by {@link #addFiles(Path...)},
     * {@link #addFiles(String...)} and {@link #addFolder(String)}.
     *
     * @returns the built {@link DiagramModel}
     * @see #addFiles(Path...)
     * @see #addFiles(String...)
     * @see #addFolder(String)
     */
    public ninja.seppli.umlgenerator.scanner.model.DiagramModel scan() {
        spoon.Launcher launcher = new spoon.Launcher();
        pathList.stream().map(java.nio.file.Path::toAbsolutePath).filter(this::fileExistsFilter).map(java.nio.file.Path::toString).forEach(launcher::addInputResource);
        launcher.run();
        launcher.process();
        spoon.reflect.factory.Factory factory = launcher.getFactory();
        spoon.processing.ProcessingManager processingManager = new spoon.support.QueueProcessingManager(factory);
        processingManager.process(factory.Module().getAllModules());
        spoon.reflect.CtModel ctModel = launcher.getModel();
        ninja.seppli.umlgenerator.scanner.SpoonClassProcessor classProcessor = new ninja.seppli.umlgenerator.scanner.SpoonClassProcessor();
        classProcessor.processModel(ctModel);
        return classProcessor.getMermaidModel();
    }

    /**
     * Checks if the given path acually exists. This is used as a predicate.
     *
     * @param path
     * 		the path to check
     * @return if the path exists or not
     */
    private boolean fileExistsFilter(java.nio.file.Path path) {
        if (!path.toFile().exists()) {
            logger.warn("File \"{}\" doesn\'t exist", path);
            return false;
        }
        return true;
    }

    public static void main(java.lang.String[] args) {
        new ninja.seppli.umlgenerator.scanner.DiagramGenerator().addFiles("library/").scan();
    }
}