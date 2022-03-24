package ninja.seppli.umlgenerator.scanner.model;

public record GenericType(String name) {
    public enum Generic {
        SUPER,
        IMPLEMENTS
    }
}
