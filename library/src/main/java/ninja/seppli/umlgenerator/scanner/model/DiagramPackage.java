package ninja.seppli.umlgenerator.scanner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record DiagramPackage(String path, List<DiagramPackage> packages, List<DiagramClass> classes) {

    public final static DiagramPackage DEFAULT_PACKAGE = new DiagramPackage("default");

    public DiagramPackage(String path) {
        this(path, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * @return if this package only contains one package and no classes. If this is
     *         the case then it can be combined with the one package
     */
    public boolean containsOnlyOnePackage() {
        return packages.size() == 1 && classes.isEmpty();
    }

    /**
     * @return the individual parts of the path
     */
    public List<String> pathParts() {
        return Arrays.asList(path.split("\\."));
    }

    /**
     * @return the name of the package which is the last element of the path
     */
    public String name() {
        // always contains one element because the list is created with split
        return pathParts().get(pathParts().size() - 1);
    }

    /**
     * @return if this instance is the default package
     */
    public boolean isDefaultPackage() {
        return this == DEFAULT_PACKAGE;
    }

    /**
     * @return a stream with all classes in this package and its subpackages
     */
    public Stream<DiagramClass> streamSubClasses() {
        Stream<DiagramClass> localClassStream = classes().stream();
        Stream<DiagramClass> packageClassStream = packages.stream().flatMap(DiagramPackage::streamSubClasses);
        return Stream.concat(localClassStream, packageClassStream);
    }
}
