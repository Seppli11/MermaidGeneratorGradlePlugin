package ninja.seppli.umlgenerator.scanner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.martiansoftware.jsap.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ninja.seppli.umlgenerator.scanner.model.DiagramClass;
import ninja.seppli.umlgenerator.scanner.model.DiagramField;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod;
import ninja.seppli.umlgenerator.scanner.model.DiagramModel;
import ninja.seppli.umlgenerator.scanner.model.DiagramPackage;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation;
import ninja.seppli.umlgenerator.scanner.model.DiagramType;
import ninja.seppli.umlgenerator.scanner.model.DiagramUnkownType;
import ninja.seppli.umlgenerator.scanner.model.DiagramVisibility;
import ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType;
import ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter;
import ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType;
import ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList;
import spoon.processing.AbstractProcessor;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

public class SpoonClassProcessor {
    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    private DiagramPackage rootPackage = new DiagramPackage("");
    private Set<DiagramRelation> relations = new HashSet<>();
    private PackageList ignorePackageList = new PackageList();

    public void processModel(CtModel ctModel) {
        rootPackage = processPackage(ctModel.getRootPackage());
    }

    public DiagramPackage processPackage(CtPackage ctPackage) {
        String packagePath = ctPackage.getQualifiedName();
        DiagramPackage packageObj = new DiagramPackage(packagePath);
        for (CtType<?> ctType : ctPackage.getTypes()) {
            if (ctType instanceof CtClass) {
                CtClass<?> ctClass = (CtClass<?>) ctType;
                DiagramClass diagramClass = processClass(ctClass);
                packageObj.classes().add(diagramClass);
            } else {
                logger.warn("Unkown subclass of CtType: \"{}\"", ctType.getClass().getName());
            }
        }
        for (CtPackage subCtPackage : ctPackage.getPackages()) {
            String subPackagePath = subCtPackage.getQualifiedName();
            if (!ignorePackageList.containsPackageRef(subPackagePath)) {
                var subPackageObj = processPackage(subCtPackage);
                packageObj.packages().add(subPackageObj);
            } else {
                logger.debug("Ignoring class \"{}\" because it is on the ignore list", subPackagePath);

            }
        }

        return packageObj;
    }

    private DiagramClass processClass(CtClass<?> element) {
        String classPackage = element.getPackage().getQualifiedName();
        if (ignorePackageList.containsPackageRef(classPackage)) {
            return null;
        }
        Set<DiagramClass> foundClasses = new HashSet<>();
        DiagramClass diagramClass = createClass(element);
        if (hasElidgableSuperClass(element)) {
            relations.add(createSuperRelation(element.getSuperclass(), diagramClass));
        }
        element.getSuperInterfaces().stream()
                .map(ctInterface -> createImplementInterfaceRelation(ctInterface, diagramClass))
                .forEach(relations::add);
        return diagramClass;
    }

    private boolean hasElidgableSuperClass(CtClass<?> element) {
        return element.getSuperclass() != null && !element.isEnum();
    }

    private DiagramRelation createSuperRelation(CtTypeReference<?> superType, DiagramClass elementClass) {
        DiagramType mermaidSuperType = createType(superType);
        return new DiagramRelation(elementClass, RelationType.INHERITANCE, mermaidSuperType);
    }

    private DiagramRelation createImplementInterfaceRelation(CtTypeReference<?> element, DiagramClass elementClass) {
        DiagramType superType = createType(element);
        return new DiagramRelation(elementClass, RelationType.REALIZATION, superType);
    }

    public DiagramModel getMermaidModel() {
        return new DiagramModel(rootPackage, relations);
    }

    private DiagramClass createClass(CtClass<?> from) {
        DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        DiagramClassType classType = createClassType(from);
        List<DiagramField> fields = from.getFields().stream().map(this::createField).toList();
        Stream<DiagramMethod> constructorStream = from.getConstructors().stream()
                .map(constructor -> createConstructor(from, constructor));
        Stream<DiagramMethod> methodStream = from.getMethods().stream()
                .map(this::createMethod);
        List<DiagramMethod> methods = Stream.concat(constructorStream, methodStream).toList();
        return new DiagramClass(classType, visibility, from.getSimpleName(), fields, methods);
    }

    private DiagramClassType createClassType(CtClass<?> from) {
        if (from.isInterface())
            return DiagramClassType.INTERFACE;
        if (from.isEnum())
            return DiagramClassType.ENUM;
        if (from.isClass() && from.isAbstract())
            return DiagramClassType.ABSTRACT_CLASS;
        if (from.isClass() && !from.isAbstract())
            return DiagramClassType.CLASS;
        logger.warn("Unkown type of class \"{}\"", from);
        return DiagramClassType.CLASS;
    }

    private DiagramField createField(CtField<?> from) {
        return new DiagramField(from.isStatic(), from.isFinal(), from.getSimpleName(), createType(from.getType()),
                fromModifierKind(from.getVisibility()));
    }

    private DiagramType createType(CtTypeReference<?> from) {
        return new DiagramUnkownType(from.getSimpleName());
    }

    private DiagramMethod createConstructor(CtClass<?> clazz, CtConstructor<?> from) {
        DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        List<DiagramMethodParameter> parameters = createMermaidParameters(from.getParameters());
        return new DiagramMethod(clazz.getSimpleName(), false, false, visibility, DiagramUnkownType.CONSTRUCTOR_RETURN,
                parameters);
    }

    private DiagramMethod createMethod(CtMethod<?> from) {
        String name = from.getSimpleName();
        boolean isStatic = from.hasModifier(ModifierKind.STATIC);
        boolean isAbstract = from.hasModifier(ModifierKind.ABSTRACT);
        DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        DiagramType returnType = createType(from.getType());
        List<DiagramMethodParameter> parameters = createMermaidParameters(from.getParameters());
        return new DiagramMethod(name, isStatic, isAbstract, visibility, returnType,
                parameters);
    }

    private List<DiagramMethodParameter> createMermaidParameters(List<CtParameter<?>> parameters) {
        return parameters.stream()
                .map(p -> new DiagramMethodParameter(p.getSimpleName(), createType(p.getType()))).toList();
    }

    private DiagramVisibility fromModifierKind(ModifierKind visiblity) {
        if (visiblity == null)
            return DiagramVisibility.PACKAGE_PRIVATE;
        return switch (visiblity) {
            case PRIVATE -> DiagramVisibility.PRIVATE;
            case PROTECTED -> DiagramVisibility.PROTECTED;
            case PUBLIC -> DiagramVisibility.PUBLIC;
            default -> DiagramVisibility.PACKAGE_PRIVATE;
        };
    }

    public static void main(String[] args) {
        System.out.println("test");
    }
}
