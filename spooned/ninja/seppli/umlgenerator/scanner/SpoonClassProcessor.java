package ninja.seppli.umlgenerator.scanner;
public class SpoonClassProcessor {
    /**
     * logger
     */
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private ninja.seppli.umlgenerator.scanner.model.DiagramPackage rootPackage = new ninja.seppli.umlgenerator.scanner.model.DiagramPackage("");

    private java.util.Set<ninja.seppli.umlgenerator.scanner.model.DiagramRelation> relations = new java.util.HashSet<>();

    private ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList ignorePackageList = new ninja.seppli.umlgenerator.scanner.model.packagelist.PackageList();

    public void processModel(spoon.reflect.CtModel ctModel) {
        rootPackage = processPackage(ctModel.getRootPackage());
    }

    public ninja.seppli.umlgenerator.scanner.model.DiagramPackage processPackage(spoon.reflect.declaration.CtPackage ctPackage) {
        java.lang.String packagePath = ctPackage.getQualifiedName();
        ninja.seppli.umlgenerator.scanner.model.DiagramPackage packageObj = new ninja.seppli.umlgenerator.scanner.model.DiagramPackage(packagePath);
        for (spoon.reflect.declaration.CtType<?> ctType : ctPackage.getTypes()) {
            if (ctType instanceof spoon.reflect.declaration.CtClass) {
                spoon.reflect.declaration.CtClass<?> ctClass = ((spoon.reflect.declaration.CtClass<?>) (ctType));
                ninja.seppli.umlgenerator.scanner.model.DiagramClass diagramClass = processClass(ctClass);
                packageObj.classes().add(diagramClass);
            } else {
                logger.warn("Unkown subclass of CtType: \"{}\"", ctType.getClass().getName());
            }
        }
        for (spoon.reflect.declaration.CtPackage subCtPackage : ctPackage.getPackages()) {
            java.lang.String subPackagePath = subCtPackage.getQualifiedName();
            if (!ignorePackageList.containsPackageRef(subPackagePath)) {
                ninja.seppli.umlgenerator.scanner.var subPackageObj = processPackage(subCtPackage);
                packageObj.packages().add(subPackageObj);
            } else {
                logger.debug("Ignoring class \"{}\" because it is on the ignore list", subPackagePath);
            }
        }
        return packageObj;
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramClass processClass(spoon.reflect.declaration.CtClass<?> element) {
        java.lang.String classPackage = element.getPackage().getQualifiedName();
        if (ignorePackageList.containsPackageRef(classPackage)) {
            return null;
        }
        java.util.Set<ninja.seppli.umlgenerator.scanner.model.DiagramClass> foundClasses = new java.util.HashSet<>();
        ninja.seppli.umlgenerator.scanner.model.DiagramClass diagramClass = createClass(element);
        if (hasElidgableSuperClass(element)) {
            relations.add(createSuperRelation(element.getSuperclass(), diagramClass));
        }
        element.getSuperInterfaces().stream().map(( ctInterface) -> createImplementInterfaceRelation(ctInterface, diagramClass)).forEach(relations::add);
        return diagramClass;
    }

    private boolean hasElidgableSuperClass(spoon.reflect.declaration.CtClass<?> element) {
        return (element.getSuperclass() != null) && (!element.isEnum());
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramRelation createSuperRelation(spoon.reflect.reference.CtTypeReference<?> superType, ninja.seppli.umlgenerator.scanner.model.DiagramClass elementClass) {
        ninja.seppli.umlgenerator.scanner.model.DiagramType mermaidSuperType = createType(superType);
        return new ninja.seppli.umlgenerator.scanner.model.DiagramRelation(elementClass, ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType.INHERITANCE, mermaidSuperType);
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramRelation createImplementInterfaceRelation(spoon.reflect.reference.CtTypeReference<?> element, ninja.seppli.umlgenerator.scanner.model.DiagramClass elementClass) {
        ninja.seppli.umlgenerator.scanner.model.DiagramType superType = createType(element);
        return new ninja.seppli.umlgenerator.scanner.model.DiagramRelation(elementClass, ninja.seppli.umlgenerator.scanner.model.DiagramRelation.RelationType.REALIZATION, superType);
    }

    public ninja.seppli.umlgenerator.scanner.model.DiagramModel getMermaidModel() {
        return new ninja.seppli.umlgenerator.scanner.model.DiagramModel(rootPackage, relations);
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramClass createClass(spoon.reflect.declaration.CtClass<?> from) {
        ninja.seppli.umlgenerator.scanner.model.DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType classType = createClassType(from);
        java.util.List<ninja.seppli.umlgenerator.scanner.model.DiagramField> fields = from.getFields().stream().map(this::createField).toList();
        java.util.stream.Stream<ninja.seppli.umlgenerator.scanner.model.DiagramMethod> constructorStream = from.getConstructors().stream().map(( constructor) -> createConstructor(from, constructor));
        java.util.stream.Stream<ninja.seppli.umlgenerator.scanner.model.DiagramMethod> methodStream = from.getMethods().stream().map(this::createMethod);
        java.util.List<ninja.seppli.umlgenerator.scanner.model.DiagramMethod> methods = java.util.stream.Stream.concat(constructorStream, methodStream).toList();
        return new ninja.seppli.umlgenerator.scanner.model.DiagramClass(classType, visibility, from.getSimpleName(), fields, methods);
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType createClassType(spoon.reflect.declaration.CtClass<?> from) {
        if (from.isInterface())
            return ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType.INTERFACE;

        if (from.isEnum())
            return ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType.ENUM;

        if (from.isClass() && from.isAbstract())
            return ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType.ABSTRACT_CLASS;

        if (from.isClass() && (!from.isAbstract()))
            return ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType.CLASS;

        logger.warn("Unkown type of class \"{}\"", from);
        return ninja.seppli.umlgenerator.scanner.model.DiagramClass.DiagramClassType.CLASS;
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramField createField(spoon.reflect.declaration.CtField<?> from) {
        return new ninja.seppli.umlgenerator.scanner.model.DiagramField(from.isStatic(), from.isFinal(), from.getSimpleName(), createType(from.getType()), fromModifierKind(from.getVisibility()));
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramType createType(spoon.reflect.reference.CtTypeReference<?> from) {
        return new ninja.seppli.umlgenerator.scanner.model.DiagramUnkownType(from.getSimpleName());
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramMethod createConstructor(spoon.reflect.declaration.CtClass<?> clazz, spoon.reflect.declaration.CtConstructor<?> from) {
        ninja.seppli.umlgenerator.scanner.model.DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        java.util.List<ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter> parameters = createMermaidParameters(from.getParameters());
        return new ninja.seppli.umlgenerator.scanner.model.DiagramMethod(clazz.getSimpleName(), false, false, visibility, ninja.seppli.umlgenerator.scanner.model.DiagramUnkownType.CONSTRUCTOR_RETURN, parameters);
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramMethod createMethod(spoon.reflect.declaration.CtMethod<?> from) {
        java.lang.String name = from.getSimpleName();
        boolean isStatic = from.hasModifier(spoon.reflect.declaration.ModifierKind.STATIC);
        boolean isAbstract = from.hasModifier(spoon.reflect.declaration.ModifierKind.ABSTRACT);
        ninja.seppli.umlgenerator.scanner.model.DiagramVisibility visibility = fromModifierKind(from.getVisibility());
        ninja.seppli.umlgenerator.scanner.model.DiagramType returnType = createType(from.getType());
        java.util.List<ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter> parameters = createMermaidParameters(from.getParameters());
        return new ninja.seppli.umlgenerator.scanner.model.DiagramMethod(name, isStatic, isAbstract, visibility, returnType, parameters);
    }

    private java.util.List<ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter> createMermaidParameters(java.util.List<spoon.reflect.declaration.CtParameter<?>> parameters) {
        return parameters.stream().map(( p) -> new ninja.seppli.umlgenerator.scanner.model.DiagramMethod.DiagramMethodParameter(p.getSimpleName(), createType(p.getType()))).toList();
    }

    private ninja.seppli.umlgenerator.scanner.model.DiagramVisibility fromModifierKind(spoon.reflect.declaration.ModifierKind visiblity) {
        if (visiblity == null)
            return ninja.seppli.umlgenerator.scanner.model.DiagramVisibility.PACKAGE_PRIVATE;

        return switch (visiblity) {
            case PRIVATE ->
                ninja.seppli.umlgenerator.scanner.model.DiagramVisibility.PRIVATE;
            case PROTECTED ->
                ninja.seppli.umlgenerator.scanner.model.DiagramVisibility.PROTECTED;
            case PUBLIC ->
                ninja.seppli.umlgenerator.scanner.model.DiagramVisibility.PUBLIC;
            default ->
                ninja.seppli.umlgenerator.scanner.model.DiagramVisibility.PACKAGE_PRIVATE;
        };
    }
}