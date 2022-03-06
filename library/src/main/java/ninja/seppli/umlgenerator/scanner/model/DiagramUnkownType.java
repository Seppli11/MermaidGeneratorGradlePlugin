package ninja.seppli.umlgenerator.scanner.model;

import java.util.Collections;
import java.util.List;

public record DiagramUnkownType(String name, List<DiagramType> genericTypes) implements DiagramType {

    public final static DiagramType VOID = new DiagramUnkownType("void");
    public final static DiagramType CONSTRUCTOR_RETURN = new DiagramUnkownType("");

    public DiagramUnkownType(String name) {
        this(name, Collections.emptyList());
    }
}
