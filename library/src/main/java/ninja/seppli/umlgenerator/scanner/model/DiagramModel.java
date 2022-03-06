package ninja.seppli.umlgenerator.scanner.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record DiagramModel(DiagramPackage rootPackage, Set<DiagramRelation> relations) {

    public Set<DiagramClass> classes() {
        return rootPackage.streamSubClasses().collect(Collectors.toSet());
    }

    @Override
    public Set<DiagramRelation> relations() {
        return new HashSet<>(relations);
    }

}
