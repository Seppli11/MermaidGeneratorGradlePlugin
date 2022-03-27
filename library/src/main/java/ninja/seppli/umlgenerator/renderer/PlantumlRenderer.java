package ninja.seppli.umlgenerator.renderer;

import java.util.List;
import java.util.stream.Collectors;

import ninja.seppli.umlgenerator.options.UmlOptions;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtEnumValue;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtFormalTypeDeclarer;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtRecord;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.reference.CtTypeReference;

public class PlantumlRenderer extends AbstractRenderer {
    @Override
    public String render(CtModel model, UmlOptions options) {
        return """
                @startuml
                %s
                @enduml
                """.formatted(super.render(model, options));
    }

    @Override
    protected String getClassString(CtClass<?> classObj) {
        String body = indent(getAllFieldsString(classObj), getAllConstructorsString(classObj),
                getAllMethodsString(classObj));
        String abstractStr = classObj.isAbstract() ? "abstract " : "";
        return """
                %sclass %s%s {
                %s
                }
                """.formatted(abstractStr, classObj.getSimpleName(), getAllDeclaredGenericsString(classObj), body);
    }

    @Override
    protected String getInterfaceString(CtInterface<?> interfaceObj) {
        String body = indent(getAllFieldsString(interfaceObj), getAllMethodsString(interfaceObj));
        return """
                interface %s%s {
                %s
                }
                """.formatted(interfaceObj.getSimpleName(), getAllDeclaredGenericsString(interfaceObj), body);
    }

    @Override
    protected String getEnumString(CtEnum<?> enumObj) {
        String body = indent(getAllFieldsString(enumObj), getAllConstructorsString(enumObj),
                getAllMethodsString(enumObj));
        return """
                enum %s%s {
                %s
                }
                """.formatted(enumObj.getSimpleName(), getAllDeclaredGenericsString(enumObj), body);
    }

    @Override
    protected String getAnnotationString(CtAnnotationType<?> annotationObj) {
        return """
                annotation %s%s {
                %s
                %s
                }
                """.formatted(annotationObj.getSimpleName(), getAllDeclaredGenericsString(annotationObj),
                getAllFieldsString(annotationObj),
                getAllMethodsString(annotationObj));
    }

    @Override
    protected String getRecordString(CtRecord recordObj) {
        String body = indent(getAllFieldsString(recordObj), getAllConstructorsString(recordObj),
                getAllMethodsString(recordObj));
        return """
                class %s%s <<record>> {
                %s
                }
                """.formatted(recordObj.getSimpleName(), getAllDeclaredGenericsString(recordObj), body);
    }

    @Override
    protected String getPackages(CtPackage packageObj, List<String> classStrings) {
        if (classStrings.isEmpty())
            return "";
        String joinedClassString = indent(classStrings.stream().collect(Collectors.joining("\n")));
        return """
                package %s {
                %s
                }
                """.formatted(packageObj.getQualifiedName(), joinedClassString);
    }

    @Override
    protected String getExtendsString(CtType<?> type) {
        if (type.getSuperclass() == null || type.getSuperclass().getQualifiedName().equals("java.lang.Object"))
            return "";
        return "%s --|> %s".formatted(type.getSimpleName(), type.getSuperclass().getSimpleName());
    }

    @Override
    protected String getImplementsString(CtType<?> type) {
        return type.getSuperInterfaces().stream().map(interfaceObj -> getImplementString(type, interfaceObj))
                .collect(Collectors.joining("\n"));
    }

    private String getImplementString(CtType<?> type, CtTypeReference<?> interfaceObj) {
        return "%s ..|> %s".formatted(type.getSimpleName(), interfaceObj.getSimpleName());
    }

    private String getAllDeclaredGenericsString(CtFormalTypeDeclarer genericType) {
        if (genericType == null)
            return "";
        List<CtTypeParameter> parameters = genericType.getFormalCtTypeParameters();
        if (parameters.isEmpty())
            return "";
        return "<" + parameters.stream().map(this::getDeclaredGenericString).collect(Collectors.joining(", ")) + ">";
    }

    private String getDeclaredGenericString(CtTypeParameter parameter) {
        return parameter.getSimpleName();
    }

    private String getAllEnumStrings(CtEnum<?> enumObj) {
        return enumObj.getEnumValues().stream().map(CtEnumValue::getSimpleName).collect(Collectors.joining("\n"));
    }

    private String getAllConstructorsString(CtClass<?> classObj) {
        return classObj.getConstructors().stream().filter(this::isMethodVisible).map(this::getConstructorString)
                .collect(Collectors.joining("\n"));
    }

    private String getConstructorString(CtConstructor<?> constructor) {
        return getVisibilityString(constructor) + constructor.getType().getSimpleName()
                + getAllParametersString(constructor);
    }

    private String getAllMethodsString(CtType<?> typeObj) {
        return typeObj.getMethods().stream().filter(this::isMethodVisible).map(this::getMethodString)
                .collect(Collectors.joining("\n"));
    }

    private String getMethodString(CtMethod<?> method) {
        return getVisibilityString(method)
                + concat(getStaticAbstractString(method, method.getDeclaringType().isInterface()),
                        getTypeString(method), method.getSimpleName())
                + getAllParametersString(method);
    }

    private String getAllParametersString(CtExecutable<?> executable) {
        return "("
                + executable.getParameters().stream().map(this::getParamreterString).collect(Collectors.joining(", "))
                + ")";
    }

    private String getParamreterString(CtParameter<?> parameter) {
        return concat(getTypeString(parameter), parameter.getSimpleName());
    }

    private String getAllFieldsString(CtType<?> classObj) {
        return classObj.getFields().stream().filter(this::isFieldVisible).map(this::getFieldString)
                .collect(Collectors.joining("\n"));
    }

    private String getFieldString(CtField<?> field) {
        return getVisibilityString(field) + getTypeString(field) + " " + field.getSimpleName();
    }

    private String getTypeString(CtTypedElement<?> typedElement) {
        return typedElement.getType().getSimpleName() + getGenericsString(typedElement.getType());
    }

    private String getGenericsString(CtTypeReference<?> reference) {
        if (reference.getActualTypeArguments().isEmpty())
            return "";
        return "<" + reference.getActualTypeArguments().stream().map(CtTypeReference::getSimpleName)
                .collect(Collectors.joining(", ")) + ">";
    }

    private String getVisibilityString(CtModifiable modifiable) {
        if (modifiable == null || modifiable.getVisibility() == null)
            return "~";
        return switch (modifiable.getVisibility()) {
            case PRIVATE -> "-";
            case PROTECTED -> "#";
            case PUBLIC -> "+";
            default -> "~"; // if no visibility modifier is present, assume it is package private
        };
    }

    private String getStaticAbstractString(CtModifiable modifiable, boolean isInterfaceField) {
        StringBuilder builder = new StringBuilder();
        if (modifiable.isAbstract() && !isInterfaceField) {
            builder.append("{abstract}");
        }
        if (modifiable.isStatic()) {
            builder.append("{static}");
        }
        return builder.toString();
    }
}
