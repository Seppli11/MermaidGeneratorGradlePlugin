package ninja.seppli.umlgenerator.renderer;

import java.util.stream.Collectors;

import ninja.seppli.umlgenerator.scanner.model.DiagramClass;
import ninja.seppli.umlgenerator.scanner.model.DiagramField;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
import ninja.seppli.umlgenerator.scanner.model.DiagramPackage;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation;
import ninja.seppli.umlgenerator.scanner.model.DiagramVisibility;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType;

public class PlantumlRenderer implements Renderer {

    @Override
    public String render(DiagramModel model) {
        StringBuilder builder = new StringBuilder();
        builder.append("@startuml\n");
        for (DiagramPackage packageObj : model.rootPackage().packages()) {
            builder.append(getPackageString(packageObj, ""));
        }
        for (DiagramRelation relation : model.relations()) {
            builder.append(getRelationString(relation));
        }
        builder.append("@enduml\n");
        return builder.toString();
    }

    private String getPackageString(DiagramPackage packageObj, String parentPath) {
        StringBuilder builder = new StringBuilder();
        if (packageObj.containsOnlyOnePackage()) {
            builder.append(getPackageString(packageObj.packages().get(0), packageObj.name()));
        } else {
            builder.append("package " + packageObj.path() + "{ \n");
            for (DiagramClass clazz : packageObj.classes()) {
                builder.append(getClassString(clazz));
            }
            for (DiagramPackage subPackageObj : packageObj.packages()) {
                builder.append(getPackageString(subPackageObj, ""));
            }
            builder.append("}\n");
        }
        return builder.toString();
    }

    private String getClassString(DiagramClass clazz) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s %s {%n", getClassKeyword(clazz), clazz.name()));
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
        return (field.isStatic() ? "{static}" : "") + getVisibility(field.visibility()) + " " + field.type().name()
                + " " + field.name();
    }

    private String getMethodString(DiagramMethod method) {
        String staticAbstract = (method.isAbstract() ? "{abstract}" : "") + (method.isStatic() ? "{static}" : "");
        String parameterStr = method.parameters().stream().map(this::getMethodParameterString)
                .collect(Collectors.joining(", "));
        return staticAbstract + getVisibility(method.visibility()) + " " + method.name() + "(" + parameterStr + ") : "
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
        return relation.from().name() + " " + getRelationType(relation.type()) + " " + relation.to().name() + "\n";
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

    private String getClassKeyword(DiagramClass clazz) {
        return switch (clazz.type()) {
            case ENUM -> "enum";
            case INTERFACE -> "interface";
            case ABSTRACT_CLASS -> "abstract class";
            case CLASS -> "class";
            case RECORD -> "class";
        };
    }

}
