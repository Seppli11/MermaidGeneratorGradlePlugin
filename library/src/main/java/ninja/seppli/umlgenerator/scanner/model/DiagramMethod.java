package ninja.seppli.umlgenerator.scanner.model;

import java.util.List;

public record DiagramMethod(String name, boolean isStatic, boolean isAbstract, DiagramVisibility visibility,
        DiagramType returnType, List<DiagramMethodParameter> parameters) {

    public static record DiagramMethodParameter(String name, DiagramType type) {
    }

}
