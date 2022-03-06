package ninja.seppli.umlgenerator.scanner.model;

public record DiagramRelation(DiagramType from, RelationType type, DiagramType to) {

    public enum RelationType {
        INHERITANCE,
        COMPOSITION,
        AGGREGATION,
        ASSOCIATION,
        REALIZATION;
    }
}
