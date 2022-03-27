package ninja.seppli.umlgenerator.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ninja.seppli.umlgenerator.options.UmlOptions;
import ninja.seppli.umlgenerator.options.VisibilityLevel;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtRecord;
import spoon.reflect.declaration.CtType;

public abstract class AbstractRenderer implements Renderer {
    /**
     * the options
     */
    private UmlOptions options;

    /**
     * @return the options
     */
    public UmlOptions getOptions() {
        return options;
    }

    @Override
    public String render(CtModel model, UmlOptions options) {
        this.options = options;
        List<String> importsExtendsList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (CtPackage packageObj : model.getAllPackages()) {
            List<String> classStrings = getClassStringsOfPackage(packageObj.getTypes());
            builder.append(getPackages(packageObj, classStrings));
            importsExtendsList.addAll(getExtendsAndImplementsConnections(packageObj));
        }
        builder.append(importsExtendsList.stream().filter(s -> !s.isBlank()).collect(Collectors.joining("\n")));
        return builder.toString();
    }

    private List<String> getClassStringsOfPackage(Collection<CtType<?>> types) {
        List<String> typeStringList = new ArrayList<>();
        for (CtType<?> typeObj : types) {
            typeStringList.add(getClassString(typeObj));
            typeStringList.addAll(getClassStringsOfPackage(typeObj.getNestedTypes()));
        }
        return typeStringList;
    }

    private String getClassString(CtType<?> typeObj) {
        if (typeObj instanceof CtEnum<?> enumObj) {
            return getEnumString(enumObj);
        } else if (typeObj instanceof CtRecord recordObj) {
            return getRecordString(recordObj);
        } else if (typeObj instanceof CtClass<?> classObj) {
            return getClassString(classObj);
        } else if (typeObj instanceof CtInterface<?> interfaceObj) {
            return getInterfaceString(interfaceObj);
        } else if (typeObj instanceof CtAnnotationType<?> annotationTypeObj) {
            return getAnnotationString(annotationTypeObj);
        } else {
            return getDefaultTypeString(typeObj);
        }

    }

    private List<String> getExtendsAndImplementsConnections(CtPackage packageObj) {
        List<String> typeStringList = new ArrayList<>();
        for (CtType<?> typeObj : packageObj.getTypes()) {
            typeStringList.add(getExtendsString(typeObj));
            typeStringList.add(getImplementsString(typeObj));
        }
        return typeStringList;
    }

    protected abstract String getClassString(CtClass<?> classObj);

    protected abstract String getInterfaceString(CtInterface<?> classObj);

    protected abstract String getEnumString(CtEnum<?> classObj);

    protected abstract String getAnnotationString(CtAnnotationType<?> classObj);

    protected abstract String getRecordString(CtRecord classObj);

    protected String getDefaultTypeString(CtType<?> classObj) {
        return "";
    }

    protected abstract String getPackages(CtPackage packageObj, List<String> classStrings);

    protected abstract String getExtendsString(CtType<?> type);

    protected abstract String getImplementsString(CtType<?> type);

    protected String indent(String... str) {
        return Arrays.stream(str).flatMap(String::lines).map(line -> "\t" + line).collect(Collectors.joining("\n"));
    }

    protected String concat(String... strs) {
        return Arrays.stream(strs).filter(s -> !s.isBlank()).collect(Collectors.joining(" "));
    }

    protected boolean isMethodVisible(CtModifiable modifiable) {
        VisibilityLevel modifiableLevel = VisibilityLevel.fromModifierKind(modifiable.getVisibility());
        return modifiableLevel.ordinal() > options.getGeneralUmlOptions().getMethodLevel().ordinal();
    }

    protected boolean isFieldVisible(CtModifiable modifiable) {
        VisibilityLevel modifiableLevel = VisibilityLevel.fromModifierKind(modifiable.getVisibility());
        return modifiableLevel.ordinal() > options.getGeneralUmlOptions().getFieldLevel().ordinal();
    }

}
