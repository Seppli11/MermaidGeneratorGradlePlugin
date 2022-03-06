package ninja.seppli.umlgenerator.scanner.model;

import java.util.List;

public record DiagramClass(DiagramClassType type, DiagramVisibility visibility, String name, List<DiagramField> fields,
        List<DiagramMethod> methods) implements DiagramType {

    /**
     * @returns if this diagram class contains any methods or fields
     */
    public boolean isEmpty() {
        return fields.isEmpty() && methods.isEmpty();
    }

    public enum DiagramClassType {
        CLASS, ENUM, INTERFACE, ABSTRACT_CLASS, RECORD
    }

}
