package ninja.seppli.umlgenerator.scanner.model;

public record DiagramField(boolean isStatic, boolean isFinal, String name, DiagramType type,
        DiagramVisibility visibility) {

}
