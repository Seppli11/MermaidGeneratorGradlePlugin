package ninja.seppli.umlgenerator.renderer;
public class PlantumlRenderer implements ninja.seppli.umlgenerator.renderer.Renderer {
    @java.lang.Override
    public java.lang.String render(ninja.seppli.umlgenerator.scanner.model.DiagramModel model) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("@startuml\n");
        for (ninja.seppli.umlgenerator.scanner.model.DiagramPackage packageObj : model.rootPackage().packages()) {
            builder.append(getPackageString(packageObj, ""));
        }
        for (ninja.seppli.umlgenerator.scanner.model.DiagramRelation relation : model.relations()) {
            builder.append(getRelationString(relation));
        }
        builder.append("@enduml\n");
        return builder.toString();
    }

    private java.lang.String getPackageString(ninja.seppli.umlgenerator.scanner.model.DiagramPackage packageObj, java.lang.String parentPath) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (packageObj.containsOnlyOnePackage()) {
            builder.append(getPackageString(packageObj.packages().get(0), packageObj.name()));
        } else {
            builder.append(("package " + packageObj.path()) + "{ \n");
            for (ninja.seppli.umlgenerator.scanner.model.DiagramClass clazz : packageObj.classes()) {
                builder.append(getClassString(clazz));
            }
            for (ninja.seppli.umlgenerator.scanner.model.DiagramPackage subPackageObj : packageObj.packages()) {
                builder.append(getPackageString(subPackageObj, ""));
            }
            builder.append("}\n");
        }
        return builder.toString();
    }

    private java.lang.String getClassString(ninja.seppli.umlgenerator.scanner.model.DiagramClass clazz) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(java.lang.String.format("%s %s {%n", getClassKeyword(clazz), clazz.name()));
        for (ninja.seppli.umlgenerator.scanner.model.DiagramField field : clazz.fields()) {
            builder.append(("\t" + getFieldString(field)) + "\n");
        }
        for (ninja.seppli.umlgenerator.scanner.model.DiagramMethod method : clazz.methods()) {
            builder.append(("\t" + getMethodString(method)) + "\n");
        }
        if (clazz.isEmpty()) {
            builder.append("\t\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private java.lang.String getFieldString(ninja.seppli.umlgenerator.scanner.model.DiagramField field) {
        return (((((field.isStatic() ? "{static}" : "") + getVisibility(field.visibility())) + " ") + field.type().name()) + " ") + field.name();
    }

    private java.lang.String getMethodString(ninja.seppli.umlgenerator.scanner.model.DiagramMethod method) {
        java.lang.String staticAbstract = (method.isAbstract() ? "{abstract}" : "") + (method.isStatic() ? "{static}" : "");
        java.lang.String parameterStr = method.parameters().stream().map(this::getMethodParameterString).collect(java.util.stream.Collectors.joining(", "));
        return ((((((staticAbstract + getVisibility(method.visibility())) + " ") + method.name()) + "(") + parameterStr) + ") : ") + method.returnType().name();
    }

    private java.lang.String getMethodParameterString(ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter parameter) {
        return (parameter.type().name() + " ") + parameter.name();
    }

    private java.lang.String getVisibility(ninja.seppli.umlgenerator.scanner.model.DiagramVisibility visibility) {
        return switch (visibility) {
            case PUBLIC ->
                "+";
            case PROTECTED ->
                "#";
            case PACKAGE_PRIVATE ->
                "~";
            case PRIVATE ->
                "-";
        };
    }

    private java.lang.String getRelationString(ninja.seppli.umlgenerator.scanner.model.DiagramRelation relation) {
        return ((((relation.from().name() + " ") + getRelationType(relation.type())) + " ") + relation.to().name()) + "\n";
    }

    private java.lang.String getRelationType(ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType type) {
        return switch (type) {
            case INHERITANCE ->
                "--|>";
            case COMPOSITION ->
                "--*";
            case AGGREGATION ->
                "--0";
            case ASSOCIATION ->
                "-->";
            case REALIZATION ->
                "..|>";
        };
    }

    private java.lang.String getClassKeyword(ninja.seppli.umlgenerator.scanner.model.DiagramClass clazz) {
        return switch (clazz.type()) {
            case ENUM ->
                "enum";
            case INTERFACE ->
                "interface";
            case ABSTRACT_CLASS ->
                "abstract class";
            case CLASS ->
                "class";
            case RECORD ->
                "class";
        };
    }
}