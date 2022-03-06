package ninja.seppli.umlgenerator.renderer;

import java.util.stream.Collectors;

import ninja.seppli.umlgenerator.scanner.model.DiagramClass;
import ninja.seppli.umlgenerator.scanner.model.DiagramField;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation;
import ninja.seppli.umlgenerator.scanner.model.DiagramType;
import ninja.seppli.umlgenerator.scanner.model.DiagramVisibility;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType;

public class MermaidRenderer implements Renderer {

    @Override
    public String render(DiagramModel model) {
        StringBuilder builder = new StringBuilder();
        for (DiagramClass clazz : model.classes()) {
            builder.append(getClassString(clazz));
        }
        for (DiagramRelation relation : model.relations()) {
            builder.append(getRelationString(relation));
        }
        return builder.toString();
    }

    private String getClassString(DiagramClass clazz) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("class %s {%n", clazz.name()));
        builder.append("\t" + getClassAnnotations(clazz) + "\n");
        for (DiagramField field : clazz.fields()) {
            builder.append("\t" + getFieldString(field) + "\n");
        }
        for (DiagramMethod method : clazz.methods()) {
            builder.append("\t" + getMethodString(method) + "\n");
        }
        if (clazz.isEmpty()) {
            builder.append("\t\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String getFieldString(DiagramField field) {
        return getVisibility(field.visibility()) + " " + field.type().name() + " " + field.name();
    }

    private String getMethodString(DiagramMethod method) {
        String parameterStr = method.parameters().stream().map(this::getMethodParameterString)
                .collect(Collectors.joining(", "));
        return getVisibility(method.visibility()) + " " + method.name() + "(" + parameterStr + ") :"
                + method.returnType().name();
    }

    private String getMethodParameterString(DiagramMethodParameter parameter) {
        return parameter.type().name() + " " + parameter.name();
    }

    private String getVisibility(DiagramVisibility visibility) {
        return switch (visibility) {
            case PUBLIC -> "+";
            case PROTECTED -> "#";
            case PACKAGE_PRIVATE -> "~";
            case PRIVATE -> "-";
        };
    }

    private String getRelationString(DiagramRelation relation) {
        return relation.from() + getRelationType(relation.type()) + relation.to();
    }

    private String getRelationType(RelationType type) {
        return switch (type) {
            case INHERITANCE -> "--|>";
            case COMPOSITION -> "--*";
            case AGGREGATION -> "--0";
            case ASSOCIATION -> "-->";
            case REALIZATION -> "..|>";
        };
    }

    private String getClassAnnotations(DiagramClass clazz) {
        return switch (clazz.type()) {
            case ENUM -> "<<enumeration>>";
            case INTERFACE -> "<<interface>>";
            case ABSTRACT_CLASS -> "<<abstract>>";
            case CLASS -> "";
            case RECORD -> "<<record>>";
        };
    }

}
